/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.*
import com.xingpeds.kmirc.state.ClientState
import com.xingpeds.kmirc.state.MutableClientState
import com.xingpeds.kmirc.state.NickStateMachine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class IrcEngine(
    val wantedNick: IIrcUser,
    val send: suspend (IIrcMessage) -> Unit,
    input: Flow<IIrcMessage>,
    private val engineScope: CoroutineScope,
    private val mState: MutableClientState = MutableClientState()
) : IClientIrcEngine {
    private var attemptedNick: String = wantedNick.nick
    private var nickRetryCounter: Int = 0
    private val mEvents =
        MutableSharedFlow<IIrcEvent>(replay = 100, extraBufferCapacity = 100, onBufferOverflow = BufferOverflow.SUSPEND)

    private val inputFlow = input.shareIn(engineScope, SharingStarted.Eagerly, replay = 100)
    override fun stop() {

    }


    val eventList: EventList = EventListImpl
    override val events: SharedFlow<IIrcEvent>
        get() = mEvents
    override val state: ClientState
        get() = mState
    override val self: StateFlow<NickStateMachine>
        get() = mState.mNickSate

    private var retryCount = 0

    init {

        inputFlow.onEach {
            val event = try {
                messageToEvent(it)
            } catch (e: Throwable) {
                println("[engine] Can not turn ${it.command} into event\n${e.message}")
                null
            }
            if (event == null) {
                println("[engine] command ${it.command} has no event implemented yet")
            }
            event?.let { it1 -> mEvents.emit(it1) }
        }.launchIn(engineScope)
        startEventBroadcaster()
        engineScope.launch {

            mEvents.emit(IIrcEvent.INIT)
        }
    }

    internal fun startEventBroadcaster() = engineScope.launch {
        try {
            events.collect { event ->
                println("[engine] collected event: $event")


                when (event) {
                    is IIrcEvent.ChannelMessage -> Unit
                    is IIrcEvent.ChannelModeChange -> Unit
                    is IIrcEvent.ChannelNotice -> Unit
                    IIrcEvent.INIT -> EventListImpl.mINIT.emit(event as IIrcEvent.INIT) //sendNickAndUserRequest()
                    is IIrcEvent.JOIN -> Unit
                    is IIrcEvent.NickChange -> Unit
                    is IIrcEvent.PART -> Unit
                    is IIrcEvent.PING -> EventListImpl.mPing.emit(event) //send(IrcMessage(command = IrcCommand.PONG, params = event.ircParams))
                    is IIrcEvent.PrivateMessage -> Unit
                    is IIrcEvent.Quit -> Unit
                    is IIrcEvent.TopicChange -> Unit
                    is IIrcEvent.UserModeChange -> Unit
                    IIrcEvent.Notice -> Unit
                }
            }
        } catch (e: Throwable) {
            println(e)
        }

    }

    private suspend fun updateNickState(message: IIrcMessage) {
        when (val nickState: NickStateMachine = state.nickState.value) {
            is NickStateMachine.Accept -> Unit
            NickStateMachine.NickLess -> {
                handleNickMessage(message)
            }

            is NickStateMachine.Refused -> {
                handleNickMessage(message)
            }
        }
    }

    private suspend fun handleNickMessage(message: IIrcMessage) {
        when (message.command) {
            IrcCommand.ERR_NONICKNAMEGIVEN -> {
                throw Exception("No nickname given. Should probably be impossible")
            }

            IrcCommand.ERR_ERRONEUSNICKNAME -> {
                nickRetryCounter++
                mState.mNickSate.emit(NickStateMachine.Refused("In use", nickRetryCounter))
            }

            else -> {
                mState.mNickSate.emit(NickStateMachine.Accept(attemptedNick))
            }
        }
    }

    private suspend fun sendNickAndUserRequest() {
        send(IrcMessage(command = IrcCommand.NICK, params = IrcParams(wantedNick.nick)))
        send(
            IrcMessage(
                command = IrcCommand.USER,
                params = IrcParams(
                    wantedNick.username,
                    wantedNick.hostname,
                    "*"/* server name */,
                    longParam = wantedNick.realName
                )
            )
        )
    }


    private fun updateState(message: IIrcMessage): IIrcMessage {
        TODO("Not yet implemented")
    }

    init {
        eventList.onPING.onEach { ping: IIrcEvent.PING ->
            send(IrcMessage(command = IrcCommand.PONG, params = ping.ircParams))
        }.launchIn(engineScope)
        eventList.onINIT.onEach { sendNickAndUserRequest() }.launchIn(engineScope)
    }

}