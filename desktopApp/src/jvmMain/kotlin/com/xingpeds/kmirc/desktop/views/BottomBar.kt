/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.desktop.views

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*

@Composable
fun ColumnScope.BottomBar(hintText: String, onSend: (String) -> Unit) {
    var userInput by remember { mutableStateOf("") }
    Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
        TextField(
            placeholder = { Text(hintText) },
            modifier = Modifier.weight(1f).onPreviewKeyEvent { keyEvent ->
                if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyUp) {
                    onSend(userInput)
                    userInput = ""
                    true // Event is consumed
                } else {
                    false // Event is not consumed
                }
            },
            value = userInput,
            onValueChange = { userInput = it })
        IconButton(onClick = {
            onSend(userInput)
            userInput = ""
        }) {
            Icon(Icons.Default.Send, contentDescription = "send message")
        }
    }
}

@Preview
@Composable
private fun BottomBarPreview() {
    Column {

        BottomBar("send to #kmirc", {})
    }
}