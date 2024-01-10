/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.*

/**
 * IIrcEvent is a sealed interface that represents various IRC events. Including Client only events, such as INIT.
 */
sealed interface IIrcEvent {

//    data class TOPIC(val channel: String, val topic: String) : IIrcEvent {
//        constructor(ircMessage: IrcMessage) : this(
//            channel = ircMessage.params.list.getOrNull(0)
//                ?: throw IllegalIRCMessage("topic message is missing target channel"),
//            topic = ircMessage.params.longParam
//        )
//    }

    /**
     * This class represents an IRC mode/mode change event.
     *
     * @property from The source of the event, either a user or a server.
     * @property target The target of the mode change, either a user or a channel.
     * @property modes The modes that have been added.
     * @property modesRemoved The modes that have been removed.
     */
    data class MODE(val from: IrcFrom, val target: IrcTarget, val modes: Set<Char>, val modesRemoved: Set<Char>) :
        IIrcEvent {
        constructor(ircMessage: IrcMessage) : this(
            from = if (ircMessage.prefix?.user == null || ircMessage.prefix.host == null) {
                IrcFrom.Server(
                    ircMessage.prefix?.nickOrServer ?: throw IllegalIRCMessage("mode message missing prefix")
                )
            } else {
                IrcFrom.User(user = ircMessage.prefix.nickOrServer)
            },
            target = if (ircMessage.params.list[0].startsWith("#")) {
                IrcTarget.Channel(ircMessage.params.list[0])
            } else {
                IrcTarget.User(ircMessage.params.list[0])
            },
            modes = ircMessage.params.list.getOrNull(1).let { modes ->
                if (modes == null) throw IllegalIRCMessage("modes message missing second parameter. The list of mode changes")
                val plusIndex: Int = modes.indexOf('+')
                if (plusIndex == -1) {
                    emptySet<Char>()
                } else {
                    val end: Int =
                        modes.indexOf('-', startIndex = plusIndex + 1).takeUnless { it == -1 } ?: modes.lastIndex
                    modes.substring((plusIndex..end)).toSet()
                }
            },
            modesRemoved = ircMessage.params.list.getOrNull(1).let { modes ->
                if (modes == null) throw IllegalIRCMessage("modes message missing second parameter. The list of mode changes")
                val minusIndex = modes.indexOf('-')
                if (minusIndex == -1) {
                    emptySet<Char>()
                } else {
                    val end: Int =
                        modes.indexOf('+', startIndex = minusIndex + 1).takeUnless { it == -1 } ?: modes.lastIndex
                    modes.substring((minusIndex..end)).toSet()
                }
            }
        )
    }

    /**
     * The `UserQuit` class represents an IRC quit event where a user quits the server.
     *
     * @property nick The nickname of the user who quit.
     * @property quitMessage The quit message provided by the user.
     * @constructor Creates a `UserQuit` instance with the given `nick` and `quitMessage`.
     * @throws IllegalIRCMessage if the `nick` or `quitMessage` is missing or null.
     *
     * @param ircMessage The IRC message from which to extract the quit event information.
     */
    data class UserQuit(val nick: String, val quitMessage: String) : IIrcEvent {
        constructor(ircMessage: IrcMessage) : this(
            nick = ircMessage.prefix?.nickOrServer ?: throw IllegalIRCMessage("quit message missing nick prefix"),
            quitMessage = ircMessage.params.longParam ?: throw IllegalIRCMessage("quite message missing reason")
        )
    }

    /**
     * The `NickChange` class represents a change in nickname in an IRC event.
     * It implements the `IIrcEvent` interface.
     *
     * @property previousNick The previous nickname before the change.
     * @property newNick The new nickname after the change.
     * @throws IllegalIRCMessage if the necessary information is missing in the IRC message.
     */
    data class NickChange(val previousNick: String, val newNick: String) : IIrcEvent {
        constructor(ircMessage: IrcMessage) : this(
            previousNick = ircMessage.prefix?.nickOrServer
                ?: throw IllegalIRCMessage("Nick change message missing previous nick"),
            newNick = ircMessage.params.list.getOrNull(0)
                ?: throw IllegalIRCMessage("nick change message missing new nick")
        )
    }

    /**
     * a single line from the MOTD from the server
     * @property line the message
     */
    data class MOTDLINE(val line: String) : IIrcEvent {
        constructor(ircMessage: IrcMessage) : this(
            line = ircMessage.params.longParam ?: throw IllegalIRCMessage("motd message missing the test parameter")
        )
    }

    /**
     * stub for messages that have no event yet
     */
    data object NotImplYet : IIrcEvent

    /**
     * server welcome message
     * @property rawIrcLine message from the server
     * @property welcomeMessage just the text of the message
     */
    data class WELCOME(val rawIrcLine: IIrcMessage) : IIrcEvent {
        public val welcomeMessage: String? = rawIrcLine.params.longParam
    }

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