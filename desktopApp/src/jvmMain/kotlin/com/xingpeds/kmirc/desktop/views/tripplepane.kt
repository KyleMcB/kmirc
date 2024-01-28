/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.desktop.views

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.singleWindowApplication
import com.xingpeds.kmirc.state.ChannelState
import com.xingpeds.kmirc.state.MutableChannelState
import kotlinx.coroutines.launch

@Composable
fun TripplePane(
    channels: List<ChannelState>, selected: Int, channelMessages: List<IChannelMessage>,
    nicks: List<IChannelNick>, onChannelClick: (Int) -> Unit, onSend: suspend (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {

            ChannelOutputView(
                channels = channels,
                selected = selected,
                onChannelClick = onChannelClick,
                channelMessages = channelMessages,
                nicks = nicks,
                privateChats = emptyList()
            )
        }
        if (selected > 0) {
            val channelName = channels[selected].name
            BottomBar("send to $channelName", onSend = {
                scope.launch {
                    onSend(it)
                }
            })
        }
    }
}

@Preview
@Composable
fun TripplePanePreview() {
    TripplePane(
        channels = listOf(MutableChannelState("servername"), MutableChannelState("#kmirc")),
        selected = 1,
        channelMessages = listOf(object : IChannelMessage {
            override val from: String
                get() = "Euclid"
            override val message: String
                get() = "hellp"
        }),
        nicks = emptyList(),
        onChannelClick = {},
        onSend = {}
    )
}

private fun main() = singleWindowApplication {
    TripplePanePreview()
}