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
//    val self: UserState
}

data class MutableClientState(
    val mChannels: MutableStateFlow<List<MutableChannelState>> = MutableStateFlow(emptyList()),
    val mMessages: MutableStateFlow<List<IIrcMessage>> = MutableStateFlow(emptyList()),
    val mNickSate: MutableStateFlow<NickStateMachine> = MutableStateFlow(NickStateMachine.NickLess),
//    val mSelf: MutableUserState = MutableUserState()
) : ClientState {
    override val channels: StateFlow<List<ChannelState>>
        get() = mChannels
    override val messages: StateFlow<List<IIrcMessage>>
        get() = mMessages
    override val nickState: StateFlow<NickStateMachine>
        get() = mNickSate
//    override val self: UserState
//        get() = mSelf

}