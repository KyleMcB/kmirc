/*
 * Copyright 2024 Kyle McBurnett
 */


package com.xingpeds.kmirc.state

import com.xingpeds.kmirc.entities.events.IIrcEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ClientState interface defines the primary state elements of an IRC client.
 */
interface ClientState {
    /**
     * Channel list represented as a StateFlow.
     */
    val channels: StateFlow<List<ChannelState>>

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
}

/**
 * SelfNickState class represents a singleton implementation of the ISelfNickState interface.
 */
object SelfNickState : ISelfNickState {
    val selfNick = MutableStateFlow<NickStateMachine>(NickStateMachine.NickLess)
    override val selfNickState: StateFlow<NickStateMachine>
        get() = selfNick
}