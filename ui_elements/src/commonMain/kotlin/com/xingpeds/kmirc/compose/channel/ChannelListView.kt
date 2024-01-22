/*
 * Copyright (c) Kyle McBurnett 2024.
 */
package com.xingpeds.kmirc.compose.channel

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@Composable
fun ChannelListView(
    channelList: List<IChannelViewInfo>,
    selected: Int,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
): Unit {
    LazyColumn(modifier = modifier) {
        items(channelList.size) { index ->
            val channel = channelList[index]
            ChannelView(channel, selected = selected == index) { onClick(index) }
        }
    }
}

@Preview
@Composable
private fun ChannlListViewPreview() {
    var selected by remember { mutableStateOf(0) }
    ChannelListView(
        listOf(

        ),
        selected = selected,
        modifier = Modifier,
        onClick = {
            selected = it
        }
    )
}

@OptIn(ExperimentalResourceApi::class)
private fun main(): Unit = application {
    Window(
        state = WindowState(size = DpSize(350.dp, 500.dp)),
        onCloseRequest = ::exitApplication,
        title = "KMIRC",
        icon = painterResource("kmirc_icon.png")
    ) {
        ChannlListViewPreview()
    }
}