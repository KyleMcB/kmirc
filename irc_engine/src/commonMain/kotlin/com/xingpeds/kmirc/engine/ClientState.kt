package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.IrcMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface IrcClientState {
    val nick: StateFlow<String>
    val user: StateFlow<String>
    val hostname: StateFlow<String>
    val serverInfo: StateFlow<String>
    val ircCommands: StateFlow<List<IrcMessage>>
    val channels: StateFlow<List<ChannelState>>
    val connectionStatus: StateFlow<ConnectionStatus>
    // Additional fields as needed
}

data class MutableIrcClientState(
    override val nick: MutableStateFlow<String> = MutableStateFlow(""),
    override val user: MutableStateFlow<String> = MutableStateFlow(""),
    override val hostname: MutableStateFlow<String> = MutableStateFlow(""),
    override val serverInfo: MutableStateFlow<String> = MutableStateFlow(""),
    override val ircCommands: MutableStateFlow<List<IrcMessage>> = MutableStateFlow(emptyList()),
    override val channels: MutableStateFlow<List<ChannelState>> = MutableStateFlow(emptyList()),
    override val connectionStatus: MutableStateFlow<ConnectionStatus> = MutableStateFlow(ConnectionStatus.connecting),
    // Additional fields as needed
) : IrcClientState