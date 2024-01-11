/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.*
import kotlinx.datetime.Instant

/**
 * IIrcEvent is a sealed interface that represents various IRC events. Including Client only events, such as INIT.
 */
sealed interface IIrcEvent {

    /**
     * Represents a timestamp of an IRC event.
     *
     * @property timestamp The instant when the event occurred.
     */
    val timestamp: Instant

}

/**
 * IllegalIRCMessage class for handling illegal message situations.
 * This class extends Exception and is used to indicate that an IRC message did not conform
 * to the required format or was missing necessary information.
 */
class IllegalIRCMessage(override val message: String?, val ircMessage: IIrcMessage) : Exception()