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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration.Companion.minutes


@Composable
fun ChannelListView(
    channelList: List<IChatWindowInfo>,
    selected: Int,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
): Unit {
    LazyColumn(modifier = modifier) {
        items(channelList.size) { index ->
            when (val channel: IChatWindowInfo = channelList[index]) {
                is IChannelViewInfo -> ChannelTabView(
                    channel,
                    selected = selected == index
                ) {
                    onClick(selected)
                }

                is IOneOnOneViewInfo -> PrivateChatTabView(
                    channel,
                    selected = selected == index
                ) {
                    onClick(selected)
                }
            }


        }
    }
}

@Preview
@Composable
private fun ChannlListViewPreview() {
    var selected by remember { mutableStateOf(0) }
    val list = List<IChatWindowInfo>(10) {
        if ((0..9).random() > 3) {
            object : IOneOnOneViewInfo {
                override val name: String
                    get() = listOf("#KMIRC", "#help", "#try2hack").random()
                override val mostRecentActivity: Flow<Instant>
                    get() = flow {
                        emit(System.now() - ((1..9999).random().minutes))
                    }
            }
        } else {
            object : IChannelViewInfo {
                override val memberCount: Flow<Int>
                    get() = flow {
                        emit(
                            (1..99999).random()
                        )
                    }
                override val topic: Flow<String?>
                    get() = flow {
                        emit("ugh")
                    }
                override val name: String
                    get() = "Euclid"
                override val mostRecentActivity: Flow<Instant>
                    get() = flowOf(System.now())

            }
        }
    }
    ChannelListView(
        list,
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