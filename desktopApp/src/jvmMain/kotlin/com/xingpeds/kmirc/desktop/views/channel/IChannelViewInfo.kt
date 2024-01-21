/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.desktop.views.channel

import kotlinx.coroutines.flow.Flow

interface IChannelViewInfo {
    val name: String
    val topic: Flow<String?>
    val memberCount: Flow<Int>
}