/*
 * Copyright 2024 Kyle McBurnett
 */

import com.xingpeds.kmirc.entities.IIrcEvent
import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.MessageProcessor
import com.xingpeds.kmirc.entities.messageToEvent
import com.xingpeds.kmirc.state.MutableChannelState
import com.xingpeds.kmirc.state.MutableClientState
import com.xingpeds.kmirc.state.SelfNickState
import kotlinx.coroutines.flow.update

/**
 * StateMessageProcessor object implements the MessageProcessor, and it's responsible for
 * processing messages and broadcasting IRC events.
 */
object StateMessageProcessor : MessageProcessor {
    override suspend fun process(message: IIrcMessage, broadcast: (IIrcEvent) -> Unit) {
        when (val event = messageToEvent(message)) {
            IIrcEvent.INIT -> Unit // no state change on init
            is IIrcEvent.Notice -> MutableClientState.mNotices.update { it + event }
            is IIrcEvent.PING -> Unit // no state change on PING
            is IIrcEvent.PRIVMSG -> MutableClientState.mPrivmsgs.update { it + event }
            IIrcEvent.PickNewNick -> Unit // is this where I want to handle the nick state?
            is IIrcEvent.JOIN -> {
                //is it me?
                val nick = event.nick
                if (nick == SelfNickState.selfNickState.value.toString()) {
                    MutableClientState.mChannels.update { it + MutableChannelState(event.channel) }
                } else {
                    MutableClientState.mChannels.value.first { it.name == event.channel }.mNicks.update {
                        it + event.nick
                    }
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean = hashCode() == other.hashCode()

    override fun hashCode(): Int = "StateMessageProcessor".hashCode()
}