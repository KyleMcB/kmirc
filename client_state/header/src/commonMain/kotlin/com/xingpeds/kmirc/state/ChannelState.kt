/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.state

import com.xingpeds.kmirc.entities.IIrcChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ChannelState : IIrcChannel {
    val members: StateFlow<Set<String>>
    val topic: StateFlow<String?>
}

data class MutableChannelState(override val name: String) : ChannelState {
    val mTopic: MutableStateFlow<String?> = MutableStateFlow(null)
    override val topic: StateFlow<String?>
        get() = mTopic
    val mMembers = MutableStateFlow<Set<String>>(emptySet())
    override val members: StateFlow<Set<String>>
        get() = mMembers

}