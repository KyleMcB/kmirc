/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import StartableJob
import co.touchlab.kermit.Logger.Companion.i
import com.xingpeds.kmirc.entities.events.IIrcEvent
import com.xingpeds.kmirc.events.EventList
import com.xingpeds.kmirc.state.MutableNickState.isNickMe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * Singleton event processor to update the clients state
 */
object StateEventProcessor : StartableJob {
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

    private suspend fun handleJoin(event: IIrcEvent.JOIN) {
        //when join
        //could be a self join or another nick join
        if (isNickMe(event.nick)) {
            //self join
            // add a new channel to state
            val newChannel = MutableChannelState(event.channel)
            MutableClientState.mChannels.update { channelMap: Map<ChannelName, MutableChannelState> ->
                channelMap + (event.channel to newChannel)
            }
        } else {
            //other join
            // add a nick to a channel's state
            // This could be tricky because channel state might be in flight.
            val channelState: MutableChannelState? = MutableClientState.mChannels.timeout(2.seconds)
                .first { map ->
                    map.containsKey(event.channel)
                }[event.channel]

            channelState?.mMembers?.update { members: Set<String> ->
                members + event.nick
            }
        }

    }
}