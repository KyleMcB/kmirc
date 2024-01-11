package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IrcParams
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * PING data class for handling the case of a PING event.
 * @param ircParams the parameters that were on the incoming ping message
 */
data class PING(val ircParams: IrcParams, override val timestamp: Instant = Clock.System.now()) : IIrcEvent