package com.xingpeds.kmirc.entities.events

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * Stub for messages that have no event yet
 */
data object NotImplYet : IIrcEvent {
    override val timestamp: Instant = Clock.System.now()
}