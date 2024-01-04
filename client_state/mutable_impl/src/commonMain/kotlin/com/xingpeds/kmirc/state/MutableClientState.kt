/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.events.IIrcEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * MutableClientState class represents a singleton implementation of the ClientState interface.
 * It provides mutable state flows.
 * It is the source of truth of the client's state
 */
object MutableClientState : ClientState {

    val mChannels: MutableStateFlow<Map<ChannelName, MutableChannelState>> = MutableStateFlow(emptyMap())
    val mMessages = MutableStateFlow<List<IIrcMessage>>(emptyList())
    override val channels: StateFlow<Map<ChannelName, ChannelState>>
        get() = mChannels

    val mNotices = MutableStateFlow<List<IIrcEvent.Notice>>(emptyList())
    val mPrivmsgs = MutableStateFlow<List<IIrcEvent.PRIVMSG>>(emptyList())
    override val notices: StateFlow<List<IIrcEvent.Notice>>
        get() = mNotices
    override val privmsgs: StateFlow<List<IIrcEvent.PRIVMSG>>
        get() = mPrivmsgs

    //    override val messages: StateFlow<List<IIrcMessage>>
//        get() = mMessages
    override val nickState: StateFlow<NickStateMachine>
        get() = SelfNickState.selfNick
}
