/*
 * Copyright 2024 Kyle McBurnett
 */
package com.xingpeds.kmirc.desktop.views

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun ChannelListView(channelList: List<ChannelState>, selected: Int, onClick: (Int) -> Unit): Unit {
    LazyColumn() {
        items(channelList.size) { index ->
            val channelState = channelList[index]
            val channelName = remember { channelState.name }
            val colors =
                if (index == selected) ListItemDefaults.colors(containerColor = Color.LightGray) else ListItemDefaults.colors()

            Column {
                if (index != 0) {
                    Divider()
                }
                ListItem(
                    modifier = Modifier.clickable { onClick(index) },
                    colors = colors,
                    headlineContent = { Text(channelName) },
                    supportingContent = { Text(channelState.topic.collectAsState().value ?: "") },
//                trailingContent = { Text("meta") },
//                    leadingContent = {
//                        Icon(
//                            Icons.Filled.Favorite,
//                            contentDescription = "Localized description",
//                        )
//                    }
                )
            }

        }
    }
}

@Preview
@Composable
private fun ChannlListViewPreview() {
    var selected by remember { mutableStateOf(0) }
    ChannelListView(
        listOf(
            MutableChannelState("hello").apply {
                this.mTopic.value = "welcome chanel"
            },
            MutableChannelState("TestChannel1").apply {
                this.mTopic.value = "Test channel 1's topic"
            },
            MutableChannelState("TestChannel2").apply {
                this.mTopic.value = "Test channel 2's topic"
            }
        ),
        selected = selected,
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