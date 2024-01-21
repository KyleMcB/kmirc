/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.desktop.views

import LeftBorder
import NoBorder
import ResizableBorder
import RightBorder
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.xingpeds.kmirc.desktop.views.channel.ChannelListView
import com.xingpeds.kmirc.desktop.views.channel.IChannelViewInfo
import com.xingpeds.kmirc.state.ChannelState
import com.xingpeds.kmirc.state.MutableChannelState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChannelOutputView(
    channels: List<ChannelState>,
    selected: Int,
    onChannelClick: (Int) -> Unit,
    channelMessages: List<IChannelMessage>,
    nicks: List<IChannelNick>
): Unit {

    ResizableBorder(
        RightBorder { ChannelListView(channels.toChannelViewInfo(), selected, onClick = onChannelClick) },
        NoBorder { ChannelMessageView(Modifier, channelMessages) },
        LeftBorder { NickList(Modifier, nicks) }
    )
}

private fun List<ChannelState>.toChannelViewInfo(): List<IChannelViewInfo> = map { state ->
    object : IChannelViewInfo {
        override val name: String
            get() = state.name
        override val topic: Flow<String?>
            get() = state.topic
        override val memberCount: Flow<Int>
            get() = state.members.map { it.size }

    }
}

@Preview
@Composable
private fun ChannelOutputViewPreview() {
    val channels = listOf<ChannelState>(MutableChannelState("channel1"))
    val selected = 0
    val onChannelClick: (Int) -> Unit = {}
    val channelMessages = listOf<IChannelMessage>(ChannelMessage("euclid", "Hello there"))
    val nicks = listOf<IChannelNick>(ChannelNick("Euclid"), ChannelNick("ben"))
    ChannelOutputView(channels, selected, onChannelClick, channelMessages, nicks)
}

@OptIn(ExperimentalResourceApi::class)
private fun main(): Unit = application {
    Window(
        state = WindowState(size = DpSize(1000.dp, 500.dp)),
        onCloseRequest = ::exitApplication,
        title = "KMIRC",
        icon = painterResource("kmirc_icon.png")
    ) {
        ChannelOutputViewPreview()
    }
}