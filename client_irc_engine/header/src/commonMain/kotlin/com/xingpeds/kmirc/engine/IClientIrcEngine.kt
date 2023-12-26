/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.IIrcEvent
import com.xingpeds.kmirc.state.ClientState
import com.xingpeds.kmirc.state.NickStateMachine
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * The engine listens to incoming irc lines and updates the state and publishes
 * events
 */
interface IClientIrcEngine {
    fun stop()

    val events: SharedFlow<IIrcEvent>
    val state: ClientState
    val self: StateFlow<NickStateMachine>
}