package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IrcFrom
import com.xingpeds.kmirc.entities.IrcTarget
import com.xingpeds.kmirc.entities.isChannel
import kotlinx.datetime.Instant

/**
 * PRIVMSG data class for holding information about private messages in IRC.
 * @throws IllegalIRCMessage if the Privmsg message is not properly formed.
 *
 * @param target channel/user that the notice is addressed to
 * @param from the server/user that notice was created by
 * @param message the content of the notice
 */
data class PRIVMSG(val from: IrcFrom, val target: IrcTarget, val message: String, override val timestamp: Instant) :
    IIrcEvent {
    @Throws(IllegalIRCMessage::class)
    constructor(ircMessage: IIrcMessage) : this(
        from = if (ircMessage.prefix?.host == null && ircMessage.prefix?.user == null) {
            IrcFrom.Server(
                ircMessage.prefix?.nickOrServer ?: throw IllegalIRCMessage("privmsg missing server prefix", ircMessage)
            )
        } else {
            IrcFrom.User(ircMessage.prefix?.nickOrServer ?: throw IllegalIRCMessage("privmsg missing nick", ircMessage))
        },
        target = if (isChannel(ircMessage.params.list[0])) {
            IrcTarget.Channel(ircMessage.params.list[0])
        } else {
            IrcTarget.User(ircMessage.params.list[0])
        },
        message = ircMessage.params.longParam ?: throw IllegalIRCMessage("privmsg missing longparam", ircMessage),
        timestamp = ircMessage.timestamp
    )
}