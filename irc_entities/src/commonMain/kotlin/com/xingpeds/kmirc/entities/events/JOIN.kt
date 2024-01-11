package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IIrcMessage
import kotlinx.datetime.Instant

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