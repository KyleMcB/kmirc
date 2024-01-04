/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import com.xingpeds.kmirc.entities.IIrcChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ChannelState : IIrcChannel {
    val members: StateFlow<Set<String>>
}

data class MutableChannelState(override val name: String) : ChannelState {
    val mMembers = MutableStateFlow<Set<String>>(emptySet())
    override val members: StateFlow<Set<String>>
        get() = mMembers

}