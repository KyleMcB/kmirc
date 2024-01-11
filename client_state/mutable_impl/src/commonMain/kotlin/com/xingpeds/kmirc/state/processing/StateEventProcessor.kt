/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.state.processing

import LogTag
import Logged
import StartableJob
import com.xingpeds.kmirc.entities.events.NOTICE
import com.xingpeds.kmirc.entities.events.PART
import com.xingpeds.kmirc.entities.events.PRIVMSG
import com.xingpeds.kmirc.entities.events.TOPIC
import com.xingpeds.kmirc.events.EventList
import com.xingpeds.kmirc.state.MutableClientState
import com.xingpeds.kmirc.state.lookupChannel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import logError
import v

/**
 * Singleton event processor to update the clients state
 */
@FlowPreview
object StateEventProcessor : StartableJob, Logged by LogTag("StateEventProvessor") {
    private val events: EventList = EventList()
    internal var scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
        set(value) {
            field.cancel()
            field = value
        }


    override fun start(): Job = scope.launch {
        v("start called")
        events.onNOTICE.onEach(::handleNotice).launchIn(scope)
        events.onPRIVMSG.onEach(::handlePrivmsg).launchIn(scope)
        events.onJOIN.onEach(::processJoinEvent).launchIn(scope)
        events.onPART.onEach(::handlePart).launchIn(scope)
        events.onTOPIC.onEach(::handleTopic).launchIn(scope)

    }

    private suspend fun handleTopic(event: TOPIC) {
        //if we just joined the channel, channel state might be inflight
        val channel = MutableClientState.mChannels.lookupChannel(event.channel)
        if (channel == null) {
            logError {
                "TOPIC event for ${event.channel}, channel not found"
            }
            return
        }
        channel.mTopic.emit(event.topic)


    }

    private suspend fun handlePrivmsg(event: PRIVMSG) {
        MutableClientState.mPrivmsgs.update { it + event }
    }

    private suspend fun handleNotice(event: NOTICE) {
        MutableClientState.mNotices.update { it + event }
    }

    private suspend fun handlePart(event: PART) {
        // I need to remove this nick from the channel
        MutableClientState.mChannels.first().get(event.channel)?.mMembers?.update { it.minus(event.nick) }
    }

}