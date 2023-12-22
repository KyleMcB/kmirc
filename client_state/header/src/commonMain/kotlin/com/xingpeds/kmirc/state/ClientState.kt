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
    val self: UserState
}

data class MutableClientState(
    val mChannels: MutableStateFlow<List<MutableChannelState>>,
    val mMessages: MutableStateFlow<List<IIrcMessage>>,
    val mNickSate: MutableStateFlow<NickStateMachine>,
    val mSelf: MutableUserState
) : ClientState {
    override val channels: StateFlow<List<ChannelState>>
        get() = mChannels
    override val messages: StateFlow<List<IIrcMessage>>
        get() = mMessages
    override val nickState: StateFlow<NickStateMachine>
        get() = TODO("Not yet implemented")
    override val self: UserState
        get() = mSelf

}