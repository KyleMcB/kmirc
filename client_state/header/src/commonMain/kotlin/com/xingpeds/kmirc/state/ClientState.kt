/*
 * Copyright 2024 Kyle McBurnett
 */


package com.xingpeds.kmirc.state

import com.xingpeds.kmirc.entities.IIrcEvent
import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.MessageProcessor
import com.xingpeds.kmirc.entities.messageToEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * ClientState interface defines the primary state elements of an IRC client.
 */
interface ClientState {
    /**
     * Channel list represented as a StateFlow.
     */
    val channels: StateFlow<List<ChannelState>>

    /**
     * Message list represented as a StateFlow.
     */
//    val messages: StateFlow<List<IIrcEvent>>

    val notices: StateFlow<List<IIrcEvent.Notice>>
    val privmsgs: StateFlow<List<IIrcEvent.PRIVMSG>>

    /**
     * Client's nickname state represented as a StateFlow.
     */
    val nickState: StateFlow<NickStateMachine>
}

/**
 * ISelfNickState interface defines the nickname state of the client.
 */
interface ISelfNickState {
    /**
     * Client's nickname state represented as a StateFlow.
     */
    val selfNickState: StateFlow<NickStateMachine>
}

/**
 * SelfNickState class represents a singleton implementation of the ISelfNickState interface.
 */
object SelfNickState : ISelfNickState {
    val selfNick = MutableStateFlow<NickStateMachine>(NickStateMachine.NickLess)
    override val selfNickState: StateFlow<NickStateMachine>
        get() = selfNick
}

/**
 * MutableClientState class represents a singleton implementation of the ClientState interface.
 * It provides mutable state flows.
 * It is the source of truth of the client's state
 */
object MutableClientState : ClientState {

    val mChannels: MutableStateFlow<List<MutableChannelState>> = MutableStateFlow(emptyList())

    //    private val mMessages = MutableStateFlow<List<IIrcMessage>>(emptyList())
    override val channels: StateFlow<List<ChannelState>>
        get() = mChannels

    val mNotices = MutableStateFlow<List<IIrcEvent.Notice>>(emptyList())
    val mPrivmsgs = MutableStateFlow<List<IIrcEvent.PRIVMSG>>(emptyList())
    override val notices: StateFlow<List<IIrcEvent.Notice>>
        get() = mNotices
    override val privmsgs: StateFlow<List<IIrcEvent.PRIVMSG>>
        get() = mPrivmsgs

    //    override val messages: StateFlow<List<IIrcMessage>>
//        get() = mMessages
    override val nickState: StateFlow<NickStateMachine>
        get() = SelfNickState.selfNick
}

//todo move this to a impl module
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
}