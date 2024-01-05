/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import StartableJob
import com.xingpeds.kmirc.entities.events.IIrcEvent
import com.xingpeds.kmirc.events.EventList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object StateEventProcessor : StartableJob {
    val events: EventList = EventList()
    internal var scope: CoroutineScope = CoroutineScope(Dispatchers.Default)


    override fun start() = scope.launch {

        events.onNOTICE.onEach(::handleNotice).launchIn(scope)
    }

    suspend fun handleNotice(event: IIrcEvent.Notice): Unit {
        MutableClientState.mNotices.update { it + event }
    }
}