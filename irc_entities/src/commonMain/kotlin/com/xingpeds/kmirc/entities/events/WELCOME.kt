/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IIrcMessage
import kotlinx.datetime.Instant

/**
 * Server welcome message
 * @throws IllegalIRCMessage if the Welcome message is not properly formed.
 *
 * @property welcomeMessage just the text of the message
 */
data class WELCOME(val welcomeMessage: String, override val timestamp: Instant) : IIrcEvent {
    @Throws(IllegalIRCMessage::class)
    constructor(ircMessage: IIrcMessage) : this(
        welcomeMessage = ircMessage.params.longParam
            ?: throw IllegalIRCMessage("server welcome message missing long parameter", ircMessage),
        timestamp = ircMessage.timestamp
    )
}