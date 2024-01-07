/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import Logged
import StartableJob
import co.touchlab.kermit.Logger.Companion.e
import co.touchlab.kermit.Logger.Companion.i
import co.touchlab.kermit.Logger.Companion.v
import com.xingpeds.kmirc.entities.events.IIrcEvent
import com.xingpeds.kmirc.events.EventList
import com.xingpeds.kmirc.state.MutableNickState.isNickMe
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import withErrorLogging
import kotlin.time.Duration.Companion.seconds

/**
 * Singleton event processor to update the clients state
 */
@FlowPreview
object StateEventProcessor : StartableJob, Logged {
    private val events: EventList = EventList()
    internal var scope: CoroutineScope = CoroutineScope(Dispatchers.Default)


    override fun start(): Job = scope.launch {
        i("StateEventProcessor") {
            "start called"
        }
        events.onNOTICE.onEach(::handleNotice).launchIn(scope)
        events.onPRIVMSG.onEach(::handlePrivmsg).launchIn(scope)
        events.onJOIN.onEach(::handleJoin).launchIn(scope)
    }

    private suspend fun handlePrivmsg(event: IIrcEvent.PRIVMSG) {
        MutableClientState.mPrivmsgs.update { it + event }
    }

    private suspend fun handleNotice(event: IIrcEvent.Notice) {
        MutableClientState.mNotices.update { it + event }
    }

    private suspend fun handleJoin(event: IIrcEvent.JOIN) = withErrorLogging {
        v(tag) {
            "handling join event"
        }
        //when join
        //could be a self join or another nick join
        if (isNickMe(event.nick)) {
            v(tag) { "self join event" }
            //self join
            // add a new channel to state
            val newChannel = MutableChannelState(event.channel)
            MutableClientState.mChannels.update { channelMap: Map<ChannelName, MutableChannelState> ->
                channelMap + (event.channel to newChannel)
            }
        } else {
            v(tag) { "other join event" }
            //other join
            // add a nick to a channel's state
            // This could be tricky because channel state might be in flight.

            val channelState: MutableChannelState? =
                MutableClientState.mChannels
                    .filter { map ->
                        map.containsKey(event.channel)
                    }
                    .timeout(4.seconds)
                    .catch { e ->
                        if (e is TimeoutCancellationException) {
                            e(tag) {
                                "looking for ${event.channel} timed out while trying to add ${event.nick} to channel roster"
                            }
                        }
                    }.firstOrNull()?.get(event.channel)
            if (channelState == null) {
                e(tag) {
                    "recevied join event for another nick. ${event.channel}. In channel. ${event.channel}. However, state for that chanel can not be found"
                }
            }

            channelState?.mMembers?.update { members: Set<String> ->
                members + event.nick
            }
        }

    }

    override val tag: String
        get() = "StateEventProvessor"
}