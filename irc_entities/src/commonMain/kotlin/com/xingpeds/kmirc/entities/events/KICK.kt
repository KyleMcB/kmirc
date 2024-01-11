package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IIrcMessage
import kotlinx.datetime.Instant

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