package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcFrom
import com.xingpeds.kmirc.entities.IrcMessage
import kotlinx.datetime.Instant

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