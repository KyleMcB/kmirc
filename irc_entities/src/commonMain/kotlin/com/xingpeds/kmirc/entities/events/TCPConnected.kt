package com.xingpeds.kmirc.entities.events

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * INIT object for handling the case of TCP connection initiation.
 */
data object TCPConnected : IIrcEvent {
    override val timestamp: Instant = Clock.System.now()
}