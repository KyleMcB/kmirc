package com.xingpeds.kmirc.entities.events

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * PickNewNick object for handling the case of NickName picking.
 * @property timestamp has no meaning for this event. If fired, client needs to send a new nickname
 */
data object PickNewNick : IIrcEvent {
    override val timestamp: Instant = Clock.System.now()
}