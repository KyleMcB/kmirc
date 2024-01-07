/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.*

/**
 * IIrcEvent is a sealed interface that represents various IRC events. Including Client only events, such as INIT.
 */
sealed interface IIrcEvent {

    /**
     * JOIN event
     * @param [channel] the name of the channel someone joined
     * @param [nick] the name of the nick that joined the channel
     */
    data class JOIN(val channel: String, val nick: String) : IIrcEvent {
        @Throws(IllegalIRCMessage::class)
        constructor(message: IIrcMessage) : this(
            channel = message.params.list[0],
            nick = message.prefix?.nickOrServer ?: throw IllegalIRCMessage("JOIN message missing nick")
        )
    }

    /**
     * Part event. When a user leaves a channel
     * @param channel the channel someone just left
     * @param nick the user the just left
     */
    data class PART(val channel: String, val nick: String) : IIrcEvent {
        @Throws(IllegalIRCMessage::class)
        constructor(message: IIrcMessage) : this(
            channel = message.params.list[0],
            nick = message.prefix?.nickOrServer ?: throw IllegalIRCMessage("Part message missing prefix")
        )
    }

    /**
     * PickNewNick object for handling the case of NickName picking.
     */
    data object PickNewNick : IIrcEvent

    /**
     * Notice data class for holding information about IRC notices.
     * @param target channel/user that the notice is addressed to
     * @param from the server/user that notice was created by
     * @param message the content of the notice
     */
    data class Notice(val target: IrcTarget, val from: IrcFrom, val message: String) : IIrcEvent {
        @Throws(IllegalIRCMessage::class)
        constructor(message: IIrcMessage) : this(
            from = prefixToFrom(message.prefix ?: throw IllegalIRCMessage("prefix is missing for Notice message")),
            target = if (isChannel(message.params.list[0])) {
                IrcTarget.Channel(message.params.list[0])
            } else {
                IrcTarget.User(message.params.list[0])
            },
            message = message.params.longParam ?: throw IllegalIRCMessage("notice missing longparam")
        )
    }

    /**
     * INIT object for handling the case of TCP connection initiation.
     */
    data object INIT : IIrcEvent

    /**
     * PING data class for handling the case of a PING event.
     * @param ircParams the parameters that were on the incoming ping message
     */
    data class PING(val ircParams: IrcParams) : IIrcEvent

    /**
     * PRIVMSG data class for holding information about private messages in IRC.
     * @param target channel/user that the notice is addressed to
     * @param from the server/user that notice was created by
     * @param message the content of the notice
     */
    data class PRIVMSG(val from: IrcFrom, val target: IrcTarget, val message: String) : IIrcEvent {
        @Throws(IllegalIRCMessage::class)
        constructor(message: IIrcMessage) : this(
            from = if (message.prefix?.host == null && message.prefix?.user == null) {
                IrcFrom.Server(message.prefix?.nickOrServer ?: throw IllegalIRCMessage("privmsg missing server prefix"))
            } else {
                IrcFrom.User(message.prefix?.nickOrServer ?: throw IllegalIRCMessage("privmsg missing nick"))
            }, target = if (isChannel(message.params.list[0])) {
                IrcTarget.Channel(message.params.list[0])
            } else {
                IrcTarget.User(message.params.list[0])
            }, message = message.params.longParam ?: throw IllegalIRCMessage("privmsg missing longparam")
        )
    }
}

/**
 * IllegalIRCMessage class for handling illegal message situations.
 */
class IllegalIRCMessage(override val message: String?) : Exception()