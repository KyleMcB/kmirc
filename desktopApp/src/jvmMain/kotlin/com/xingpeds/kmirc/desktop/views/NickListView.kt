/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.desktop.views

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface IChannelNick {
    val nick: String
}

data class ChannelNick(override val nick: String) : IChannelNick {

}

@Composable
fun NickList(modifier: Modifier, nicks: List<IChannelNick>): Unit {
    LazyColumn(modifier) {
        items(nicks.size) { index ->
            val nick = nicks[index]
            ListItem(
                headlineContent = { Text(nick.nick) }
            )
        }
    }
}

@Preview
@Composable
private fun NickListPreview() {
    val list = List<IChannelNick>(10) {
        object : IChannelNick {
            override val nick: String
                get() = "nick$it"

        }
    }
    NickList(Modifier, list)
}