/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import com.xingpeds.kmirc.entities.IIrcChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ChannelState : IIrcChannel {
    val topic: StateFlow<String>

}

data class MutableChannelState(override val name: String, val mTopic: MutableStateFlow<String>) : ChannelState {
    override val topic: StateFlow<String>
        get() = mTopic
}