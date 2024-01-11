/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IIrcMessage
import kotlinx.datetime.Instant

/**
 * Part event. When a user leaves a channel
 * @throws IllegalIRCMessage if the Part message is not properly formed.
 *
 * @param channel the channel someone just left
 * @param nick the user the just left
 */
data class PART(val channel: String, val nick: String, override val timestamp: Instant) : IIrcEvent {
    @Throws(IllegalIRCMessage::class)
    constructor(ircMessage: IIrcMessage) : this(
        channel = ircMessage.params.list[0],
        nick = ircMessage.prefix?.nickOrServer ?: throw IllegalIRCMessage("Part message missing prefix", ircMessage),
        timestamp = ircMessage.timestamp
    )
}