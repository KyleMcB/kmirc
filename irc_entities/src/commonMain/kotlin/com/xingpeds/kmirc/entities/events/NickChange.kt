package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IrcMessage
import kotlinx.datetime.Instant

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