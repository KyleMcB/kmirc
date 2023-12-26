/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities

sealed class IrcTarget : CharSequence {
    data class User(val user: String) : IrcTarget(), CharSequence by user
    data class Channel(val channel: String) : IrcTarget(), CharSequence by channel
}

sealed class IrcFrom : CharSequence {
    data class User(val user: String) : IrcFrom(), CharSequence by user
    data class Server(val server: String) : IrcFrom(), CharSequence by server
}

sealed interface IIrcEvent {
    data class Notice(val target: IrcTarget, val from: IrcFrom, val message: String) : IIrcEvent
    data object INIT : IIrcEvent //this is for a tcp connection
    data class PING(val ircParams: IrcParams) : IIrcEvent
    data class JOIN(val user: IIrcUser, val channels: List<IIrcChannel>) : IIrcEvent
    data class PART(val user: IIrcUser, val channels: List<IIrcChannel>) : IIrcEvent
    data class ChannelMessage(val user: IIrcUser, val channel: IIrcChannel, val message: String) : IIrcEvent
    data class PrivateMessage(val user: IIrcUser, val message: String) : IIrcEvent
    data class Quit(val user: IIrcUser, val reason: String) : IIrcEvent
    data class NickChange(val user: IIrcUser, val newNick: String) : IIrcEvent
    data class UserModeChange(val user: IIrcUser, val channel: IIrcChannel, val mode: String) : IIrcEvent
    data class ChannelModeChange(val channel: IIrcChannel, val mode: String) : IIrcEvent
    data class TopicChange(val user: IIrcUser, val channel: IIrcChannel, val topic: String) : IIrcEvent
    data class ChannelNotice(val user: IIrcUser, val channel: IIrcChannel, val message: String) : IIrcEvent
}