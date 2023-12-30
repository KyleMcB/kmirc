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
    object PICK_NEW_NICK : IIrcEvent
    data class Notice(val target: IrcTarget, val from: IrcFrom, val message: String) : IIrcEvent
    data object INIT : IIrcEvent //this is for a tcp connection
    data class PING(val ircParams: IrcParams) : IIrcEvent
    data class PRIVMSG(val from: IrcFrom, val target: IrcTarget, val message: String) : IIrcEvent
}