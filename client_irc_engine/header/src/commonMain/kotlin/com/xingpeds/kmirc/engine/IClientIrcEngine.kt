/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.IIrcEvent
import com.xingpeds.kmirc.state.ClientState
import kotlinx.coroutines.flow.SharedFlow

/**
 * The engine listens to incoming irc lines and updates the state and publishes
 * events
 */
interface IClientIrcEngine {
    val events: SharedFlow<IIrcEvent>
    val state: ClientState

}