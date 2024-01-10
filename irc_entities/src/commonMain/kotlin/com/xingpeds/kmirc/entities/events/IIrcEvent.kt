/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * IIrcEvent is a sealed interface that represents various IRC events. Including Client only events, such as INIT.
 */
sealed interface IIrcEvent {

    /**
     * Represents a kick event in an IRC channel.
     *
     * @property nick The nickname of the user who was kicked.
     * @property channel The channel from which the user was kicked.
     * @property reason The reason for the kick.
     * @property kickedByNick The nickname of the user who performed the kick.
     * @property timestamp The timestamp of the kick event.
     * @constructor Creates a KICK object with the specified properties.
     */
    data class KICK(
        val nick: String,
        val channel: String,
        val reason: String,
        val kickedByNick: String,
        override val timestamp: Instant
    ) : IIrcEvent {
        constructor(ircMessage: IIrcMessage) : this(
            nick = ircMessage.params.list.getOrNull(1)
                ?: throw IllegalIRCMessage("kick message missing the kicked nick", ircMessage),
            channel = ircMessage.params.list.getOrNull(0) ?: throw IllegalIRCMessage(
                "kick message missing the channel",
                ircMessage
            ),
            reason = ircMessage.params.longParam ?: "", //todo not sure if a reason is required
            kickedByNick = ircMessage.prefix?.nickOrServer ?: throw IllegalIRCMessage(
                "kick message missing prefix",
                ircMessage
            ),
            timestamp = ircMessage.timestamp
        )
    }

    /**
     * Represents a timestamp of an IRC event.
     *
     * @property timestamp The instant when the event occurred.
     */
    val timestamp: Instant

    /**
     * Represents an INVITE IRC event.
     * @throws IllegalIRCMessage if the Invite message is not properly formed.
     *
     * @property ircFrom The source of the invite message.
     * @property channel The target channel that the user was invited to.
     * @constructor Creates an INVITE object with the given IRC from and channel.
     */
    data class INVITE(val ircFrom: IrcFrom, val channel: String, override val timestamp: Instant) : IIrcEvent {
        @Throws(IllegalIRCMessage::class)
        constructor(ircMessage: IrcMessage) : this(
            ircFrom = IrcFrom.fromPrefix(
                ircMessage.prefix ?: throw IllegalIRCMessage(
                    "Invite message missing prefix",
                    ircMessage
                )
            ),
            channel = ircMessage.params.list.getOrNull(0)
                ?: throw IllegalIRCMessage("target channel missing on invite message", ircMessage),
            timestamp = ircMessage.timestamp
        ) {
            if (ircMessage.command != IrcCommand.INVITE) throw IllegalIRCMessage(
                "creating Invite event from ${ircMessage.command} message",
                ircMessage
            )
        }
    }

    /**
     * This class represents an IRC mode/mode change event.
     * @throws IllegalIRCMessage if the Mode message is not properly formed.
     *
     * @property from The source of the event, either a user or a server.
     * @property target The target of the mode change, either a user or a channel.
     * @property modes The modes that have been added.
     * @property modesRemoved The modes that have been removed.
     */
    data class MODE(
        val from: IrcFrom,
        val target: IrcTarget,
        val modes: Set<Char>,
        val modesRemoved: Set<Char>,
        override val timestamp: Instant
    ) : IIrcEvent {
        @Throws(IllegalIRCMessage::class)
        constructor(ircMessage: IrcMessage) : this(from = if (ircMessage.prefix?.user == null || ircMessage.prefix.host == null) {
            IrcFrom.Server(
                ircMessage.prefix?.nickOrServer ?: throw IllegalIRCMessage(
                    "mode message missing prefix",
                    ircMessage
                )
            )
        } else {
            IrcFrom.User(user = ircMessage.prefix.nickOrServer)
        }, target = if (ircMessage.params.list[0].startsWith("#")) {
            IrcTarget.Channel(ircMessage.params.list[0])
        } else {
            IrcTarget.User(ircMessage.params.list[0])
        }, modes = ircMessage.params.list.getOrNull(1).let { modes ->
            if (modes == null) throw IllegalIRCMessage(
                "modes message missing second parameter. The list of mode changes",
                ircMessage
            )
            val plusIndex: Int = modes.indexOf('+')
            if (plusIndex == -1) {
                emptySet<Char>()
            } else {
                val end: Int = modes.indexOf('-', startIndex = plusIndex + 1).takeUnless { it == -1 } ?: modes.lastIndex
                modes.substring((plusIndex..end)).toSet()
            }
        }, modesRemoved = ircMessage.params.list.getOrNull(1).let { modes ->
            if (modes == null) throw IllegalIRCMessage(
                "modes message missing second parameter. The list of mode changes",
                ircMessage
            )
            val minusIndex = modes.indexOf('-')
            if (minusIndex == -1) {
                emptySet<Char>()
            } else {
                val end: Int =
                    modes.indexOf('+', startIndex = minusIndex + 1).takeUnless { it == -1 } ?: modes.lastIndex
                modes.substring((minusIndex..end)).toSet()
            }
        }, timestamp = ircMessage.timestamp
        )
    }

    /**
     * The `UserQuit` class represents an IRC quit event where a user quits the server.
     * @throws IllegalIRCMessage if the Quit message is not properly formed.
     *
     * @property nick The nickname of the user who quit.
     * @property quitMessage The quit message provided by the user.
     * @constructor Creates a `UserQuit` instance with the given `nick` and `quitMessage`.
     * @throws IllegalIRCMessage if the `nick` or `quitMessage` is missing or null.
     *
     * @param ircMessage The IRC message from which to extract the quit event information.
     */
    data class UserQuit(val nick: String, val quitMessage: String, override val timestamp: Instant) : IIrcEvent {
        @Throws(IllegalIRCMessage::class)
        constructor(ircMessage: IrcMessage) : this(
            nick = ircMessage.prefix?.nickOrServer ?: throw IllegalIRCMessage(
                "quit message missing nick prefix",
                ircMessage
            ),
            quitMessage = ircMessage.params.longParam ?: throw IllegalIRCMessage(
                "quit message missing reason",
                ircMessage
            ),
            timestamp = ircMessage.timestamp
        )
    }

    /**
     * The `NickChange` class represents a change in nickname in an IRC event.
     * @throws IllegalIRCMessage if the NickChange message is not properly formed.
     *
     * @property previousNick The previous nickname before the change.
     * @property newNick The new nickname after the change.
     * @throws IllegalIRCMessage if the necessary information is missing in the IRC message.
     */
    data class NickChange(val previousNick: String, val newNick: String, override val timestamp: Instant) : IIrcEvent {
        @Throws(IllegalIRCMessage::class)
        constructor(ircMessage: IrcMessage) : this(
            previousNick = ircMessage.prefix?.nickOrServer
                ?: throw IllegalIRCMessage("Nick change message missing previous nick", ircMessage),
            newNick = ircMessage.params.list.getOrNull(0)
                ?: throw IllegalIRCMessage("nick change message missing new nick", ircMessage),
            timestamp = ircMessage.timestamp
        )
    }

    /**
     * A single line from the MOTD from the server
     * @throws IllegalIRCMessage if the MotdLine message is not properly formed.
     *
     * @property line the message
     */
    data class MOTDLINE(val line: String, override val timestamp: Instant) : IIrcEvent {
        @Throws(IllegalIRCMessage::class)
        constructor(ircMessage: IrcMessage) : this(
            line = ircMessage.params.longParam ?: throw IllegalIRCMessage(
                "motd message missing the text parameter",
                ircMessage
            ), timestamp = ircMessage.timestamp
        )
    }

    /**
     * Stub for messages that have no event yet
     */
    data object NotImplYet : IIrcEvent {
        override val timestamp: Instant = Clock.System.now()
    }

    /**
     * Server welcome message
     * @throws IllegalIRCMessage if the Welcome message is not properly formed.
     *
     * @property welcomeMessage just the text of the message
     */
    data class WELCOME(val welcomeMessage: String, override val timestamp: Instant) : IIrcEvent {
        @Throws(IllegalIRCMessage::class)
        constructor(ircMessage: IrcMessage) : this(
            welcomeMessage = ircMessage.params.longParam
                ?: throw IllegalIRCMessage("server welcome message missing long parameter", ircMessage),
            timestamp = ircMessage.timestamp
        )
    }

    /**
     * JOIN event
     * @throws IllegalIRCMessage if the Join message is not properly formed.
     *
     * @param [channel] the name of the channel someone joined
     * @param [nick] the name of the nick that joined the channel
     */
    data class JOIN(val channel: String, val nick: String, override val timestamp: Instant) : IIrcEvent {
        @Throws(IllegalIRCMessage::class)
        constructor(ircMessage: IIrcMessage) : this(
            channel = ircMessage.params.list[0],
            nick = ircMessage.prefix?.nickOrServer ?: throw IllegalIRCMessage("JOIN message missing nick", ircMessage),
            timestamp = ircMessage.timestamp
        )
    }

    /**
     * Part event. When a user leaves a channel
     * @throws IllegalIRCMessage if the Part message is not properly formed.
     *
     * @param channel the channel someone just left
     * @param nick the user the just left
     */
    data class PART(val channel: String, val nick: String, override val timestamp: Instant) : IIrcEvent {
        @Throws(IllegalIRCMessage::class)
        constructor(ircMessage: IrcMessage) : this(
            channel = ircMessage.params.list[0],
            nick = ircMessage.prefix?.nickOrServer ?: throw IllegalIRCMessage("Part message missing prefix", ircMessage),
            timestamp = ircMessage.timestamp
        )
    }

    /**
     * PickNewNick object for handling the case of NickName picking.
     */
    data object PickNewNick : IIrcEvent {
        override val timestamp: Instant = Clock.System.now()
    }

    /**
     * Notice data class for holding information about IRC notices.
     * @throws IllegalIRCMessage if the Notice message is not properly formed.
     *
     * @param target channel/user that the notice is addressed to
     * @param from the server/user that notice was created by
     * @param message the content of the notice
     */
    data class Notice(val target: IrcTarget, val from: IrcFrom, val message: String, override val timestamp: Instant) :
        IIrcEvent {
        @Throws(IllegalIRCMessage::class)
        constructor(message: IIrcMessage) : this(
            from = prefixToFrom(message.prefix ?: throw IllegalIRCMessage("prefix is missing for Notice message", ircMessage = message)),
            target = if (isChannel(message.params.list[0])) {
                IrcTarget.Channel(message.params.list[0])
            } else {
                IrcTarget.User(message.params.list[0])
            },
            message = message.params.longParam ?: throw IllegalIRCMessage("notice missing longparam", message),
            timestamp = message.timestamp
        )
    }

    /**
     * INIT object for handling the case of TCP connection initiation.
     */
    data object INIT : IIrcEvent {
        override val timestamp: Instant = Clock.System.now()
    }

    /**
     * PING data class for handling the case of a PING event.
     * @param ircParams the parameters that were on the incoming ping message
     */
    data class PING(val ircParams: IrcParams, override val timestamp: Instant = Clock.System.now()) : IIrcEvent

    /**
     * PRIVMSG data class for holding information about private messages in IRC.
     * @throws IllegalIRCMessage if the Privmsg message is not properly formed.
     *
     * @param target channel/user that the notice is addressed to
     * @param from the server/user that notice was created by
     * @param message the content of the notice
     */
    data class PRIVMSG(val from: IrcFrom, val target: IrcTarget, val message: String, override val timestamp: Instant) :
        IIrcEvent {
        @Throws(IllegalIRCMessage::class)
        constructor(ircMessage: IIrcMessage) : this(
            from = if (ircMessage.prefix?.host == null && ircMessage.prefix?.user == null) {
                IrcFrom.Server(
                    ircMessage.prefix?.nickOrServer ?: throw IllegalIRCMessage("privmsg missing server prefix", ircMessage)
                )
            } else {
                IrcFrom.User(ircMessage.prefix?.nickOrServer ?: throw IllegalIRCMessage("privmsg missing nick", ircMessage))
            },
            target = if (isChannel(ircMessage.params.list[0])) {
                IrcTarget.Channel(ircMessage.params.list[0])
            } else {
                IrcTarget.User(ircMessage.params.list[0])
            },
            message = ircMessage.params.longParam ?: throw IllegalIRCMessage("privmsg missing longparam", ircMessage),
            timestamp = ircMessage.timestamp
        )
    }
}

/**
 * IllegalIRCMessage class for handling illegal message situations.
 * This class extends Exception and is used to indicate that an IRC message did not conform
 * to the required format or was missing necessary information.
 */
class IllegalIRCMessage(override val message: String?, val ircMessage: IIrcMessage) : Exception()