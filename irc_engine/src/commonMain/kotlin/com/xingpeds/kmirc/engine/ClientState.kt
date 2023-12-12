package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.IrcMessage
import kotlinx.coroutines.flow.MutableStateFlow

data class IrcClientState(
    val nick: MutableStateFlow<String> = MutableStateFlow(""),
    val user: MutableStateFlow<String> = MutableStateFlow(""),
    val hostname: MutableStateFlow<String> = MutableStateFlow(""),
    val serverInfo: MutableStateFlow<String> = MutableStateFlow(""),
    val ircCommands: MutableStateFlow<List<IrcMessage>> = MutableStateFlow(emptyList()),
    val channels: MutableStateFlow<List<ChannelState>> = MutableStateFlow(emptyList()),
//    val connectionStatus: MutableStateFlow<ConnectionStatus>,
    // Additional fields as needed
)

