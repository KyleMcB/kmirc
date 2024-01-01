/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcMessage
import com.xingpeds.kmirc.entities.MessageProcessor
import com.xingpeds.kmirc.entities.events.IIrcEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class IrcEngine(
    val send: suspend (IIrcMessage) -> Unit,
    input: Flow<IIrcMessage>,
    private val processors: Set<MessageProcessor>,
    private val engineScope: CoroutineScope,
) : IClientIrcEngine {
    private val mEvents =
        MutableSharedFlow<IIrcEvent>(replay = 100, extraBufferCapacity = 100, onBufferOverflow = BufferOverflow.SUSPEND)

    val eventList: EventList = EventListImpl

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


            when (event) {
                IIrcEvent.INIT -> {
                    EventListImpl.mINIT.emit(event as IIrcEvent.INIT)
                }

                is IIrcEvent.PING -> {
                    EventListImpl.mPing.emit(event)
                }

                is IIrcEvent.Notice -> {
                    EventListImpl.mNotice.emit(event)
                }

                is IIrcEvent.PRIVMSG -> {
                    EventListImpl.mPRIVMSG.emit(event)
                }

                else -> TODO("add single event broadcaster for $event")
            }
        }
    }

    init {
        eventList.onPING.onEach { ping: IIrcEvent.PING ->
            send(IrcMessage(command = IrcCommand.PONG, params = ping.ircParams))
        }.launchIn(engineScope)

        eventList.onPRIVMSG.onEach { msg: IIrcEvent.PRIVMSG ->
            println("Caught a PRIVMSG event: ${msg.toString()}") // Debug statement
        }.launchIn(engineScope)

    }
}