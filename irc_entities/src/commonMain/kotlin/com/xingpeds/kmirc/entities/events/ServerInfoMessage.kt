/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IIrcMessage
import kotlinx.datetime.Instant

class ServerInfoMessage(val message: String, override val timestamp: Instant) : IIrcEvent {
    constructor(ircMessage: IIrcMessage) : this(
        message = ircMessage.params.longParam ?: throw IllegalIRCMessage(
            "generic server message missing longParam",
            ircMessage
        ),
        timestamp = ircMessage.timestamp
    )
}