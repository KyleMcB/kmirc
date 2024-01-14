/*
 * Copyright 2024 Kyle McBurnett
 */

import androidx.compose.material.Text
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMIRC",
        icon = painterResource("kmirc_icon.png")
    ) {
        Text("hello!")
    }
}