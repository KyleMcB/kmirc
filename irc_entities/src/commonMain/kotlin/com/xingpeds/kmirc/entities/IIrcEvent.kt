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

fun prefixToFrom(prefix: IrcPrefix): IrcFrom? {
    return if (prefix.host == null) {
        IrcFrom.Server(prefix.nick)
    } else {
        IrcFrom.User(prefix.nick)
    }
}

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
            from = if (message.prefix?.host == null) {
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