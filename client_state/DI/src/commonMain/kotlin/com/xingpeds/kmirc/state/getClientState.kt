/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import com.xingpeds.kmirc.container.DIContainer

fun getClientState(): ClientState = MutableClientState

/**
 *
 */
fun DIContainer.getClientState(): ClientState = MutableClientState