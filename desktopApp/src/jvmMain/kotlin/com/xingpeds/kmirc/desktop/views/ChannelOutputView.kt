/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.desktop.views

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.xingpeds.kmirc.state.ChannelState
import com.xingpeds.kmirc.state.MutableChannelState
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
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        ChannelListView(channels, selected, Modifier.weight(0.2f), onChannelClick)
        ChannelMessageView(Modifier.weight(1f), channelMessages)
        NickList(Modifier.weight(0.2f), nicks)
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
        state = WindowState(size = DpSize(350.dp, 500.dp)),
        onCloseRequest = ::exitApplication,
        title = "KMIRC",
        icon = painterResource("kmirc_icon.png")
    ) {
        ChannelOutputViewPreview()
    }
}