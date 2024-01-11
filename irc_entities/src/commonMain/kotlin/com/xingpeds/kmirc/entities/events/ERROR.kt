package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IrcMessage
import kotlinx.datetime.Instant
import kotlin.jvm.Throws

/**
 * The `ERROR` class represents an IRC error event.
 * Sent right before the server disconnects from the client.
 *
 * @property message The error message.
 * @property timestamp The timestamp of the error event.
 *
 * @constructor Creates a new `ERROR` instance with the specified message and timestamp.
 * @param message The error message.
 * @param timestamp The timestamp of the error event.
 *
 * @throws IllegalIRCMessage if the error message is missing a reason.
 *
 * @constructor Creates a new `ERROR` instance from an `IrcMessage` object.
 * @param ircMessage The IRC message from which to create the error event.
 *
 * @throws IllegalIRCMessage if the error message is missing a reason.
 */
class ERROR(val message:String, override val timestamp: Instant) : IIrcEvent {
    @Throws(IllegalIRCMessage::class)
    constructor(ircMessage: IrcMessage) : this (
        message = ircMessage.params.longParam ?: throw IllegalIRCMessage("error message is missing reason", ircMessage),
        timestamp = ircMessage.timestamp
    )

}