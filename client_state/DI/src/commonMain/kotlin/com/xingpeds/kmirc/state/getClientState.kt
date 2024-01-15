/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import com.xingpeds.kmirc.container.DIContainer
import com.xingpeds.kmirc.state.processing.StateEventProcessor
import kotlinx.coroutines.Job

fun ClientState(): ClientState = MutableClientState

fun startEventProcessing(): Job = StateEventProcessor.start()

/**
 *
 */
fun DIContainer.getClientState(): ClientState = MutableClientState