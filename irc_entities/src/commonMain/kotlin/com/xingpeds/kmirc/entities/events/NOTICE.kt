package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.*
import kotlinx.datetime.Instant

/**
 * Notice data class for holding information about IRC notices.
 * @throws IllegalIRCMessage if the Notice message is not properly formed.
 *
 * @param target channel/user that the notice is addressed to
 * @param from the server/user that notice was created by
 * @param message the content of the notice
 */
data class NOTICE(val target: IrcTarget, val from: IrcFrom, val message: String, override val timestamp: Instant) :
    IIrcEvent {
    @Throws(IllegalIRCMessage::class)
    constructor(message: IIrcMessage) : this(
        from = prefixToFrom(
            message.prefix ?: throw IllegalIRCMessage(
                "prefix is missing for Notice message",
                ircMessage = message
            )
        ),
        target = if (isChannel(message.params.list[0])) {
            IrcTarget.Channel(message.params.list[0])
        } else {
            IrcTarget.User(message.params.list[0])
        },
        message = message.params.longParam ?: throw IllegalIRCMessage("notice missing longparam", message),
        timestamp = message.timestamp
    )
}