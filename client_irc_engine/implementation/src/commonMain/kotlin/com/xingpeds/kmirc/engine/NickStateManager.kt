/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import LogTag
import Logged
import StartableJob
import com.xingpeds.kmirc.entities.*
import com.xingpeds.kmirc.entities.events.IIrcEvent
import com.xingpeds.kmirc.entities.events.PickNewNick
import com.xingpeds.kmirc.entities.events.TCPConnected
import com.xingpeds.kmirc.state.MutableNickState
import com.xingpeds.kmirc.state.NickStateMachine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import v

/**
 * manages the state machine to get a nick/user/realname registered with the irc server
 * @param wantedNick the initial wanted nick/user/realname
 */
class NickStateManager(
    val wantedNick: IIrcUser,
    private val send: suspend (IIrcMessage) -> Unit,
    private val broadcast: suspend (IIrcEvent) -> Unit,
    private val events: Flow<IIrcEvent>,
    private val messages: Flow<IIrcMessage>,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    private val mState: MutableStateFlow<NickStateMachine> = MutableNickState.selfNick,
    private val autoStart: Boolean = true
) : StartableJob, Logged by LogTag("NickStateManager") {
    private var attemptedNick: String = wantedNick.nick
    private var nickRetryCounter: Int = 0

    init {
        if (autoStart) {
            scope.launch {
                start()
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
                mState.emit(NickStateMachine.Refused("In use", nickRetryCounter))
                broadcast(PickNewNick)
            }

            else -> {
                mState.emit(NickStateMachine.Accept(attemptedNick))
            }
        }
    }

    private suspend fun updateNickState(message: IIrcMessage) {
        when (mState.value) {
            is NickStateMachine.Accept -> Unit
            NickStateMachine.NickLess -> handleNickMessage(message)
            is NickStateMachine.Refused -> handleNickMessage(message)
        }
    }

    private suspend fun sendNickAndUserRequest() {
        send(IrcMessage(command = IrcCommand.NICK, params = IrcParams(wantedNick.nick)))
        send(
            IrcMessage(
                command = IrcCommand.USER,
                params = IrcParams(
                    wantedNick.username ?: "empty username",
                    wantedNick.hostname ?: "*",
                    "*"/* server name */,
                    longParam = wantedNick.realName ?: ""
                )
            )
        )
    }

    override fun start(): Job = scope.launch {
        scope.launch {
            events.filterIsInstance<TCPConnected>().collect {
                v("init caught, sending wanted nickname")
                sendNickAndUserRequest()
            }
        }
        scope.launch {
            messages.collect { message ->
                updateNickState(message)
            }
        }
    }

}