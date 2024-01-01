/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.*

sealed interface IIrcEvent {
    data class JOIN(val channel: String, val nick: String) : IIrcEvent {
        constructor(message: IIrcMessage) : this(
            channel = message.params.list[0],
            nick = message.prefix?.nick ?: throw Exception("JOIN message missing nick")
        )
    }

    data object PickNewNick : IIrcEvent
    data class Notice(val target: IrcTarget, val from: IrcFrom, val message: String) : IIrcEvent {
        constructor(message: IIrcMessage) : this( //todo
            from = prefixToFrom(message.prefix ?: throw Exception("prefix is missing for Notice message"))
                ?: throw Exception("can not convert prefix to IrcFrom"),
            target = if (isChannel(message.params.list[0])) {
                IrcTarget.Channel(message.params.list[0])
            } else {
                IrcTarget.User(message.params.list[0])
            },
            message = message.params.longParam ?: throw Exception("notice missing longparam")
        )
    }

    data object INIT : IIrcEvent //this is for a tcp connection
    data class PING(val ircParams: IrcParams) : IIrcEvent
    data class PRIVMSG(val from: IrcFrom, val target: IrcTarget, val message: String) : IIrcEvent {
        constructor(message: IIrcMessage) : this(
            from = if (message.prefix?.host == null && message.prefix?.user == null) {
                IrcFrom.Server(message.prefix?.nick ?: throw Exception("privmsg missing server prefix"))
            } else {
                IrcFrom.User(message.prefix?.nick ?: throw Exception("privmsg missing nick"))
            },
            target = if (isChannel(message.params.list[0])) {
                IrcTarget.Channel(message.params.list[0])
            } else {
                IrcTarget.User(message.params.list[0])
            },
            message = message.params.longParam ?: throw Exception("privmsg missing longparam")
        )
    }
}