/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import co.touchlab.kermit.Logger.Companion.i
import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcMessage
import com.xingpeds.kmirc.entities.MessageProcessor
import com.xingpeds.kmirc.entities.events.IIrcEvent
import com.xingpeds.kmirc.events.EventList
import com.xingpeds.kmirc.events.MutableEventList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EventBroadcaster(
    val send: suspend (IIrcMessage) -> Unit,
    input: Flow<IIrcMessage>,
    private val processors: Set<MessageProcessor>,
    private val engineScope: CoroutineScope,
) : IClientIrcEngine {
    private val mEvents =
        MutableSharedFlow<IIrcEvent>(replay = 100, extraBufferCapacity = 100, onBufferOverflow = BufferOverflow.SUSPEND)

    val eventList: EventList = MutableEventList

    init {
        eventList.onPING.onEach { ping: IIrcEvent.PING ->
            send(IrcMessage(command = IrcCommand.PONG, params = ping.ircParams))
        }.launchIn(engineScope)

    }

    override val events: SharedFlow<IIrcEvent>
        get() = mEvents


    init {
        input.onEach { message ->
            val event = try {
                messageToEvent(message)
            } catch (e: Throwable) {
                null
            }
            if (event != null) {
                processors.forEach { processor ->
                    processor.process(message) { event ->
                        engineScope.launch {
                            mEvents.emit(event)
                        }
                    }
                }
                mEvents.emit(event)
            }
        }.launchIn(engineScope)

        startEventBroadcaster()

        engineScope.launch {
            mEvents.emit(IIrcEvent.INIT)
        }
    }

    internal fun startEventBroadcaster() = engineScope.launch {
        events.collect { event ->
            i("engine") {
                "event $event"
            }

            when (event) {
                IIrcEvent.INIT -> MutableEventList.mInit.emit(event as IIrcEvent.INIT)

                is IIrcEvent.PING -> MutableEventList.mPing.emit(event)

                is IIrcEvent.Notice -> MutableEventList.mNotice.emit(event)

                is IIrcEvent.PRIVMSG -> MutableEventList.mPrivmsg.emit(event)

                is IIrcEvent.JOIN -> MutableEventList.mJoin.emit(event)
                IIrcEvent.PickNewNick -> TODO()
            }
        }
    }


}