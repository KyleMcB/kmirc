/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import com.xingpeds.kmirc.entities.events.NOTICE
import com.xingpeds.kmirc.entities.events.PRIVMSG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * MutableClientState class represents a singleton implementation of the ClientState interface.
 * It provides mutable state flows.
 * It is the source of truth of the client's state
 */
object MutableClientState : ClientState {

    /**
     * map of channel name to channel state
     */
    val mChannels: MutableStateFlow<Map<ChannelName, MutableChannelState>> = MutableStateFlow(emptyMap())
    override val channels: StateFlow<Map<ChannelName, ChannelState>>
        get() = mChannels

    /**
     * All notices the client has seen
     */
    val mNotices: MutableStateFlow<List<NOTICE>> = MutableStateFlow(emptyList())
    override val notices: StateFlow<List<NOTICE>>
        get() = mNotices

    /**
     * All privmsgs the client has seen
     */
    val mPrivmsgs: MutableStateFlow<List<PRIVMSG>> = MutableStateFlow(emptyList())
    override val privmsgs: StateFlow<List<PRIVMSG>>
        get() = mPrivmsgs

    override val nickState: StateFlow<NickStateMachine>
        get() = MutableNickState.selfNick
}