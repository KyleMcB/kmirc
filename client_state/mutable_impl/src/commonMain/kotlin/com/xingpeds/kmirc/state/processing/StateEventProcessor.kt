/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state.processing

import LogTag
import Logged
import StartableJob
import com.xingpeds.kmirc.entities.events.*
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
object StateEventProcessor : StartableJob, Logged by LogTag("StateEventProcessor") {
    private val events: EventList = EventList()
    internal var scope: CoroutineScope? = null
        set(value) {
            field?.cancel()
            field = value
        }

    override fun start(): Job {
        val _scope = scope ?: CoroutineScope(Dispatchers.Default)
        return _scope.launch {
            v("start called")
            events.onNOTICE.onEach(::handleNotice).launchIn(_scope)
            events.onPRIVMSG.onEach(::handlePrivmsg).launchIn(_scope)
            events.onJOIN.onEach(::processJoinEvent).launchIn(_scope)
            events.onPART.onEach(::handlePart).launchIn(_scope)
            events.onTOPIC.onEach(::handleTopic).launchIn(_scope)
            events.onNAMES.onEach(::handleNames).launchIn(_scope)
        }
    }

    private suspend fun handleNames(event: NAMES) {
        MutableClientState.mChannels.lookupChannel(event.channel)?.mMembers?.update { members: Set<String> ->
            members + event.nicks
            //todo This leaves in mod symbol in the nick string
        }
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