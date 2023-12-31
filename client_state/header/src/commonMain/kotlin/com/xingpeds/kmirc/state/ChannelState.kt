/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import com.xingpeds.kmirc.entities.IIrcChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ChannelState : IIrcChannel {
    val nicks: StateFlow<List<String>>
}

data class MutableChannelState(override val name: String) : ChannelState {
    val mNicks = MutableStateFlow<List<String>>(emptyList())
    override val nicks: StateFlow<List<String>>
        get() = mNicks
}