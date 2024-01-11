package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IrcMessage
import kotlinx.datetime.Instant

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