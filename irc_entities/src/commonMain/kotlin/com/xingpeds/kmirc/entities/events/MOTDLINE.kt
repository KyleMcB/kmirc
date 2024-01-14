/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IIrcMessage
import kotlinx.datetime.Instant

/**
 * A single line from the MOTD from the server
 * @throws IllegalIRCMessage if the MotdLine message is not properly formed.
 *
 * @property line the message
 */
data class MOTDLINE(val line: String, override val timestamp: Instant) : IIrcEvent {
    @Throws(IllegalIRCMessage::class)
    constructor(ircMessage: IIrcMessage) : this(
        line = ircMessage.params.longParam ?: throw IllegalIRCMessage(
            "motd message missing the text parameter",
            ircMessage
        ), timestamp = ircMessage.timestamp
    )
}