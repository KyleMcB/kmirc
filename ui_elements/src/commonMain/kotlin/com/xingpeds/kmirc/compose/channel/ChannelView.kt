/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.compose.channel

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.*
import kotlinx.datetime.Clock.System
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.random.Random

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ChannelTabView(channel: IChannelViewInfo, selected: Boolean, onClick: () -> Unit) {
    val topic by channel.topic.collectAsState(null)
    val memberCount: Int by channel.memberCount.collectAsState(1)
    ListItem(
        colors = if (selected)
            ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        else
            ListItemDefaults.colors(),
        modifier = Modifier.selectable(selected = selected, onClick = onClick),
        headlineContent = { Text(channel.name.removePrefix("#")) },
        supportingContent = topic?.let {
            { Text(it) }
        },
        trailingContent = { Text(memberCount.toString()) },
        leadingContent = {
            Box(
                modifier = Modifier.background(color = Color.LightGray, shape = RoundedCornerShape(100)).padding(6.dp)
            ) {
//                Icon(Icons.Default._3p, "one on one chat")
                Icon(Icons.Sharp.Tag, "one on one chat")
            }
        }
    )
}

private data class ChannelView(
    override val name: String,
    override val mostRecentActivity: Flow<Instant>,
    override val memberCount: Flow<Int>,
    override val topic: Flow<String?>
) : IChannelViewInfo


@Preview
@Composable
fun ChannelViewPreview() {
    var selected: Int by remember { mutableStateOf(0) }
    val channel = ChannelView(
        name = "#KMIRC",
        mostRecentActivity = flowOf(generateTimestamp()),
        memberCount = flowOf(3),
        topic = flowOf("welcome to the channel")
    )
//    val channel1 = ChannelView("#KMIRC", flowOf("making my own irc client"), flowOf(2))
//    val channel2 = ChannelView("#NoTopic", flowOf(null), flowOf(5))
    Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.selectableGroup()) {
        ChannelTabView(channel, selected = selected == 0, { selected = 0 })
//        ChannelView(channel2, selected = selected == 1, { selected = 1 })
    }
}

private fun generateTimestamp(): Instant {
    val now = System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val month = Random.nextInt(0, 2)
    val day = Random.nextInt(0, 30)
    val hour = Random.nextInt(0, 23)
    val minute = Random.nextInt(0, 59)
    val timestamp = LocalDateTime(now.year, now.monthNumber - month, now.dayOfMonth - day, hour, minute).toInstant(
        TimeZone.UTC
    )
    return timestamp
}

private fun main() = singleWindowApplication {
    ChannelViewPreview()
}