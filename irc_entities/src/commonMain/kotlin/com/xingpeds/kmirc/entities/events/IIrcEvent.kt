/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IIrcMessage
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * IIrcEvent is a sealed interface that represents various IRC events. Including Client only events, such as INIT.
 * If the name of the sub class is all caps, it is mostly likely directly generated by a message.
 * Otherwise, it is a client domain specific event
 */
sealed interface IIrcEvent {

    /**
     * Represents a timestamp of an IRC event.
     *
     * @property timestamp The instant when the event occurred.
     */
    val timestamp: Instant

}

data class TOPIC(val channel: String, val topic: String, override val timestamp: Instant) : IIrcEvent {
    @Throws(IllegalIRCMessage::class)
    constructor(ircMessage: IIrcMessage) : this(
        channel = ircMessage.params.list.getOrNull(0) ?: throw IllegalIRCMessage(
            "topic message missing change param",
            ircMessage
        ),
        topic = ircMessage.params.longParam ?: throw IllegalIRCMessage("topic message missing the topic", ircMessage),
        timestamp = ircMessage.timestamp
    )
}

data object EndOfMOTD : IIrcEvent {
    override val timestamp: Instant
        get() = Clock.System.now()
}

/**
 * IllegalIRCMessage class for handling illegal message situations.
 * This class extends Exception and is used to indicate that an IRC message did not conform
 * to the required format or was missing necessary information.
 */
class IllegalIRCMessage(override val message: String?, val ircMessage: IIrcMessage) : Exception()