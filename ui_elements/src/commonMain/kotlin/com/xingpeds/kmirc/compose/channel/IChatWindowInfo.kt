/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.compose.channel

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

sealed interface IChatWindowInfo {
    val name: String
    val mostRecentActivity: Flow<Instant>
}

interface IChannelViewInfo : IChatWindowInfo {
    val memberCount: Flow<Int>
    val topic: Flow<String?>

}

interface IOneOnOneViewInfo : IChatWindowInfo

