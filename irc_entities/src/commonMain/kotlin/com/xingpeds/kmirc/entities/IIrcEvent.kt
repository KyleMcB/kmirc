/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities

sealed interface IIrcEvent {
    data object Notice : IIrcEvent
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