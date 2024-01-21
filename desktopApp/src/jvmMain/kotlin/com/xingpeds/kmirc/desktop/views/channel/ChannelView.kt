/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.desktop.views.channel

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun ChannelView(channel: IChannelViewInfo, selected: Boolean, onClick: () -> Unit) {
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
                Text("#")
            }
        }
    )
}

/**
 * this is for previews
 */
private data class ChannelView(
    override val name: String,
    override val topic: Flow<String?>,
    override val memberCount: Flow<Int>
) :
    IChannelViewInfo {

}

@Preview
@Composable
fun ChannelViewPreview() {
    var selected: Int by remember { mutableStateOf(0) }
    val channel1 = ChannelView("#KMIRC", flowOf("making my own irc client"), flowOf(2))
    val channel2 = ChannelView("#NoTopic", flowOf(null), flowOf(5))
    Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.selectableGroup()) {
        ChannelView(channel1, selected = selected == 0, { selected = 0 })
        ChannelView(channel2, selected = selected == 1, { selected = 1 })
    }
}

private fun main() = singleWindowApplication {
    ChannelViewPreview()
}