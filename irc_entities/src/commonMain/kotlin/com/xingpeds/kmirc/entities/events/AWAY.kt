/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IrcMessage
import kotlinx.datetime.Instant

/**
 *
 * The `AWAY` class is used to represent an IRC event indicating that a user has set an away message.
 * It implements the `IIrcEvent` interface.
 *
 * @param awayMessage The away message set by the user. If it is null or empty the user is no longer away
 * @param timestamp The timestamp of the IRC event.
 *
 * @throws IllegalIRCMessage If the IRC message provided does not conform to the required format or is missing necessary information.
 */
class AWAY(val awayMessage: String?, override val timestamp: Instant) : IIrcEvent {
    /**
     * The `AWAY` class is used to represent an IRC event indicating that a user has set an away message.
     * It implements the `IIrcEvent` interface.
     *
     * @property awayMessage The away message set by the user.
     * @property timestamp The timestamp of the IRC event.
     *
     * @throws IllegalIRCMessage If the IRC message provided does not conform to the required format or is missing necessary information.
     */
    @Throws(IllegalIRCMessage::class)
    constructor(ircMessage: IrcMessage) : this(
        awayMessage = ircMessage.params.longParam,
        timestamp = ircMessage.timestamp
    )
}