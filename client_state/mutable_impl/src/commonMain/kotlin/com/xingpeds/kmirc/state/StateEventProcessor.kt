/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import com.xingpeds.kmirc.events.EventList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object StateEventProcessor {
    val events: EventList = EventList()
    internal var scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    fun startProcessingEvents() = scope.launch {
        // lots of subscribers here
    }
}