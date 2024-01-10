/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import LogTag
import Logged
import com.xingpeds.kmirc.engine.converter.messageToEvent
import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcMessage
import com.xingpeds.kmirc.entities.events.IIrcEvent
import com.xingpeds.kmirc.events.MutableEventList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import v
import withErrorLogging

/**
 * Takes in general irc messages and fires individual event bus events
 * @param send send an Irc message over the network
 */
class EventBroadcaster(
    val send: suspend (IIrcMessage) -> Unit,
    input: SharedFlow<IIrcMessage>,
    private val engineScope: CoroutineScope,
) : IBroadcaster, Logged by LogTag("EventBroadcaster") {
    private val mEvents =
        MutableSharedFlow<IIrcEvent>(replay = 100, extraBufferCapacity = 100, onBufferOverflow = BufferOverflow.SUSPEND)

    init {
        MutableEventList.onPING.onEach { ping: IIrcEvent.PING ->
            send(IrcMessage(command = IrcCommand.PONG, params = ping.ircParams))
        }.launchIn(engineScope)

    }

    suspend fun sendEvent(event: IIrcEvent): Unit = withErrorLogging {
        mEvents.emit(event)
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
                mEvents.emit(event)
            }
        }.launchIn(engineScope)

        startEventBroadcaster()

        engineScope.launch {
            mEvents.emit(IIrcEvent.INIT)
        }
    }

    internal fun startEventBroadcaster() = engineScope.launch {
        v("starting event broadcaster")
        events.collect { event ->
            if (event !is IIrcEvent.NotImplYet) v("event: $event")
            when (event) {
                IIrcEvent.INIT -> MutableEventList.mInit.emit(event as IIrcEvent.INIT)

                is IIrcEvent.PING -> MutableEventList.mPing.emit(event)

                is IIrcEvent.Notice -> MutableEventList.mNotice.emit(event)

                is IIrcEvent.PRIVMSG -> MutableEventList.mPrivmsg.emit(event)

                is IIrcEvent.JOIN -> MutableEventList.mJoin.emit(event)
                IIrcEvent.PickNewNick -> Unit
                is IIrcEvent.PART -> TODO()
                IIrcEvent.NotImplYet -> Unit
            }
        }
    }


}