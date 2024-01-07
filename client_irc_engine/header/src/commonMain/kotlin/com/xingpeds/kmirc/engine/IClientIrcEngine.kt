/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.events.IIrcEvent
import kotlinx.coroutines.flow.SharedFlow

/**
 * The engine listens to incoming irc lines and updates the state and publishes
 * events
 */
interface IClientIrcEngine {

    /**
     * all irc events
     */
    val events: SharedFlow<IIrcEvent>
}



