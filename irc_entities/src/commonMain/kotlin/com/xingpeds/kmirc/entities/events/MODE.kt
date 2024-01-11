package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IrcFrom
import com.xingpeds.kmirc.entities.IrcMessage
import com.xingpeds.kmirc.entities.IrcTarget
import kotlinx.datetime.Instant

/**
 * This class represents an IRC mode/mode change event.
 * @throws IllegalIRCMessage if the Mode message is not properly formed.
 *
 * @property from The source of the event, either a user or a server.
 * @property target The target of the mode change, either a user or a channel.
 * @property modes The modes that have been added.
 * @property modesRemoved The modes that have been removed.
 */
data class MODE(
    val from: IrcFrom,
    val target: IrcTarget,
    val modes: Set<Char>,
    val modesRemoved: Set<Char>,
    override val timestamp: Instant
) : IIrcEvent {
    @Throws(IllegalIRCMessage::class)
    constructor(ircMessage: IrcMessage) : this(from = if (ircMessage.prefix?.user == null || ircMessage.prefix.host == null) {
        IrcFrom.Server(
            ircMessage.prefix?.nickOrServer ?: throw IllegalIRCMessage(
                "mode message missing prefix",
                ircMessage
            )
        )
    } else {
        IrcFrom.User(user = ircMessage.prefix.nickOrServer)
    }, target = if (ircMessage.params.list[0].startsWith("#")) {
        IrcTarget.Channel(ircMessage.params.list[0])
    } else {
        IrcTarget.User(ircMessage.params.list[0])
    }, modes = ircMessage.params.list.getOrNull(1).let { modes ->
        if (modes == null) throw IllegalIRCMessage(
            "modes message missing second parameter. The list of mode changes",
            ircMessage
        )
        val plusIndex: Int = modes.indexOf('+')
        if (plusIndex == -1) {
            emptySet<Char>()
        } else {
            val end: Int = modes.indexOf('-', startIndex = plusIndex + 1).takeUnless { it == -1 } ?: modes.lastIndex
            modes.substring((plusIndex..end)).toSet()
        }
    }, modesRemoved = ircMessage.params.list.getOrNull(1).let { modes ->
        if (modes == null) throw IllegalIRCMessage(
            "modes message missing second parameter. The list of mode changes",
            ircMessage
        )
        val minusIndex = modes.indexOf('-')
        if (minusIndex == -1) {
            emptySet<Char>()
        } else {
            val end: Int =
                modes.indexOf('+', startIndex = minusIndex + 1).takeUnless { it == -1 } ?: modes.lastIndex
            modes.substring((minusIndex..end)).toSet()
        }
    }, timestamp = ircMessage.timestamp
    )
}