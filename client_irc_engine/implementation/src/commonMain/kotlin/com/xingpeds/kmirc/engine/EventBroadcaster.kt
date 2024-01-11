/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.engine

import LogTag
import Logged
import com.xingpeds.kmirc.engine.Converter.messageToEvent
import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcMessage
import com.xingpeds.kmirc.entities.events.*
import com.xingpeds.kmirc.events.MutableEventList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        MutableEventList.onPING.onEach { ping: PING ->
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
            mEvents.emit(TCPConnected)
        }
    }

    internal fun startEventBroadcaster() = engineScope.launch {
        v("starting event broadcaster")
        events.collect { event ->
            when (event) {
                TCPConnected -> MutableEventList.mInit.emit(event as TCPConnected)

                is PING -> MutableEventList.mPing.emit(event)

                is NOTICE -> MutableEventList.mNotice.emit(event)

                is PRIVMSG -> MutableEventList.mPrivmsg.emit(event)

                is JOIN -> MutableEventList.mJoin.emit(event)
                PickNewNick -> MutableEventList.mPickNewNick.emit(event as PickNewNick)
                is PART -> MutableEventList.mPART.emit(event)
                NotImplYet -> Unit
                is WELCOME -> MutableEventList.mWELCOME.emit(event)
                is MODE -> MutableEventList.mMODE.emit(event)
                is MOTDLINE -> MutableEventList.mMOTDLINE.emit(event)
                is NickChange -> MutableEventList.mNickChange.emit(event)
                is UserQuit -> MutableEventList.mUserQuit.emit(event)
                is INVITE -> MutableEventList.mINVITE.emit(event)
                is KICK -> MutableEventList.mKICK.emit(event)
                is AWAY -> MutableEventList.mAWAY.emit(event)
                is ERROR -> MutableEventList.mERROR.emit(event)
                EndOfMOTD -> MutableEventList.mEndOfMOTD.emit(event as EndOfMOTD)
                is TOPIC -> MutableEventList.mTOPIC.emit(event)
            }
        }
    }


}