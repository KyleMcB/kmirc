/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.IrcMessage
import com.xingpeds.kmirc.entities.events.IIrcEvent
import com.xingpeds.kmirc.events.EventList
import com.xingpeds.kmirc.state.ClientState
import kotlinx.coroutines.flow.SharedFlow

/**
 * The engine listens to incoming irc lines and updates the state and publishes
 * events
 */
interface IBroadcaster {

    /**
     * all irc events
     */
    val events: SharedFlow<IIrcEvent>

}

/**
 *
 */
interface IIrcClientEngine {
    val broadcaster: IBroadcaster
    val serverHostName: String
    val eventList: EventList
    val state: ClientState
    suspend fun send(ircMessage: IrcMessage): Unit
}



