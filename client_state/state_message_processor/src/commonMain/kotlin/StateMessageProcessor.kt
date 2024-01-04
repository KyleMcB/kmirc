/*
 * Copyright 2024 Kyle McBurnett
 */
package com.xingpeds.kmirc.state

import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.MessageProcessor
import com.xingpeds.kmirc.entities.events.IIrcEvent
import com.xingpeds.kmirc.entities.messageToEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

/**
 * StateMessageProcessor object implements the MessageProcessor, and it's responsible for
 * processing messages and broadcasting IRC events.
 */
object StateMessageProcessor : MessageProcessor {
    override suspend fun process(message: IIrcMessage, broadcast: (IIrcEvent) -> Unit) {
        MutableClientState.mMessages.update { it.take(99) + message }
        when (val event = messageToEvent(message)) {
            IIrcEvent.INIT -> Unit // no state change on init
            is IIrcEvent.Notice -> MutableClientState.mNotices.update { it + event }
            is IIrcEvent.PING -> Unit // no state change on PING
            IIrcEvent.PickNewNick -> Unit // is this where I want to handle the nick state?
            is IIrcEvent.JOIN -> {
                println(event)
                val nick = event.nick
                if (nick == SelfNickState.selfNickState.value.toString()) {
                    println("selfjoin")
                    MutableClientState.mChannels.update {
                        it + mapOf(event.channel to MutableChannelState(event.channel))
                    }
                } else {
                    println("add nick to channel")
                    val members: MutableStateFlow<Set<String>> =
                        MutableClientState.mChannels.first()[event.channel]?.mMembers
                            ?: throw Exception("channel [${event.channel}] not found\nchannels: ${MutableClientState.mChannels.first()}")
                    members.update {
                        it + event.nick
                    }
                }
            }

            is IIrcEvent.PRIVMSG -> MutableClientState.mPrivmsgs.update { it + event }
        }
    }

    override fun equals(other: Any?): Boolean = hashCode() == other.hashCode()

    override fun hashCode(): Int = "StateMessageProcessor".hashCode()
}