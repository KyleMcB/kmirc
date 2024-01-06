/*
 * Copyright 2024 Kyle McBurnett
 */


package com.xingpeds.kmirc.state

import com.xingpeds.kmirc.entities.events.IIrcEvent
import kotlinx.coroutines.flow.StateFlow

/**
 * Name of a channel type, Might include validation later TODO add validation
 */
typealias ChannelName = String

/**
 * ClientState interface defines the primary state elements of an IRC client.
 */
interface ClientState {
    /**
     * Channel list represented as a StateFlow.
     */
    val channels: StateFlow<Map<ChannelName, ChannelState>>

    /**
     * Message list represented as a StateFlow.
     */
    val notices: StateFlow<List<IIrcEvent.Notice>>
    val privmsgs: StateFlow<List<IIrcEvent.PRIVMSG>>

    /**
     * Client's nickname state represented as a StateFlow.
     */
    val nickState: StateFlow<NickStateMachine>
}

/**
 * ISelfNickState interface defines the nickname state of the client.
 */
interface ISelfNickState {
    /**
     * Client's nickname state represented as a StateFlow.
     */
    val selfNickState: StateFlow<NickStateMachine>

    /**
     * is nick the client's nick
     */
    suspend fun isNickMe(nick: String): Boolean
}

