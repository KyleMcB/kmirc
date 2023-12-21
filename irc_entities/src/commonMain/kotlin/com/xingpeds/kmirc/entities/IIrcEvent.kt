/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities

sealed interface IIrcEvent {
    data class JOIN(val user: IIrcUser, val channels: List<IIrcChannel>)
    data class PART(val user: IIrcUser, val channels: List<IIrcChannel>)
    data class ChannelMessage(val user: IIrcUser, val channel: IIrcChannel, val message: String)
    data class PrivateMessage(val user: IIrcUser, val message: String)
    data class Quit(val user: IIrcUser, val reason: String)
    data class NickChange(val user: IIrcUser, val newNick: String)
    data class ModeChange(val user: IIrcUser, val channel: IIrcChannel, val mode: String)
    data class TopicChange(val user: IIrcUser, val channel: IIrcChannel, val topic: String)
    data class ChannelNotice(val user: IIrcUser, val channel: IIrcChannel, val message: String)
}