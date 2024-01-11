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
import com.xingpeds.kmirc.events.EventList
import com.xingpeds.kmirc.state.MutableClientState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
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