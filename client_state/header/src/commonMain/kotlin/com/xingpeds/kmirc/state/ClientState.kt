/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import com.xingpeds.kmirc.entities.IIrcMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ClientState {
    val channels: StateFlow<List<ChannelState>>
    val messages: StateFlow<List<IIrcMessage>>
    val nickState: StateFlow<NickStateMachine>
//    val selfNick: StateFlow<CharSequence>
}

interface ISelfNickState {
    val selfNickState: StateFlow<NickStateMachine>
}

object SelfNickState : ISelfNickState {
    val selfNick = MutableStateFlow<NickStateMachine>(NickStateMachine.NickLess)
    override val selfNickState: StateFlow<NickStateMachine>
        get() = selfNick
}

object MutableClientState : ClientState {

    private val mChannels: MutableStateFlow<List<ChannelState>> = MutableStateFlow(emptyList())
    private val mMessages = MutableStateFlow<List<IIrcMessage>>(emptyList())
    override val channels: StateFlow<List<ChannelState>>
        get() = mChannels
    override val messages: StateFlow<List<IIrcMessage>>
        get() = mMessages
    override val nickState: StateFlow<NickStateMachine>
        get() = SelfNickState.selfNick
//    override val selfNick: StateFlow<CharSequence>
//        get() = SelfNickState.selfNick.filterIsInstance<NickStateMachine.Accept>()

}