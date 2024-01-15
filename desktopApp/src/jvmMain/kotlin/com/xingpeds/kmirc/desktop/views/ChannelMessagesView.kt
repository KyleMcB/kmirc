/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.desktop.views

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

interface IChannelMessage {
    val from: String
    val message: String
}

@Composable
fun ChannelMessageView(list: List<IChannelMessage>): Unit {

    LazyColumn {
        items(list.size) { index ->
            val channelMessage = list[index]
            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                Text("[${channelMessage.from}]", modifier = Modifier.padding(end = 2.dp))
                Text(channelMessage.message)
            }
        }
    }
}

@Preview
@Composable
private fun ChannelMessageViewPreview() {
    val contentList = listOf(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        "Vivamus feugiat, arcu nec pellentesque porta, dolor leo dapibus dolor, eu tincidunt diam augue at ex.",
        "Vestibulum aliquam pellentesque nisl, sit amet bibendum lorem luctus ac.",
        "Donec vitae est pretium, mollis tortor id, pharetra sem.",
        "Sed lacinia quam non nunc congue euismod.",
        "Morbi fermentum eros ac augue facilisis, sed tincidunt dui aliquam.",
        "Cras pretium turpis vitae risus porta, in semper magna congue.",
        "Fusce vel mauris nec neque porttitor tincidunt a non eros.",
        "Etiam at neque ullamcorper, mollis ante non, cursus turpis.",
        "Nulla facilisi. Donec ac convallis quam, ac vehicula augue."
    )
    val nickList = listOf("euclid", "pear", "zhan")
    ChannelMessageView(contentList.map { message ->
        object : IChannelMessage {
            override val from: String = nickList.random()
            override val message: String = message
        }
    })
}