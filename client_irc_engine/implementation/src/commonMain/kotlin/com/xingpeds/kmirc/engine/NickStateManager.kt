/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.*
import com.xingpeds.kmirc.state.NickStateMachine
import com.xingpeds.kmirc.state.SelfNickState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

class NickStateManager(
    val wantedNick: IIrcUser,
    private val mState: MutableStateFlow<NickStateMachine> = SelfNickState.selfNick, //todo I might want to move this class into its module so the rest of the engine can not see the mutable state
    private val send: suspend (IIrcMessage) -> Unit,
    private val scope: CoroutineScope,
    private val events: Flow<IIrcEvent>
) : MessageProcessor {
    private var attemptedNick: String = wantedNick.nick
    private var nickRetryCounter: Int = 0

    private suspend fun handleNickMessage(message: IIrcMessage, broadcast: (IIrcEvent) -> Unit) {
        when (message.command) {
            IrcCommand.ERR_NONICKNAMEGIVEN -> {
                throw Exception("No nickname given. Should probably be impossible")
            }

            IrcCommand.ERR_ERRONEUSNICKNAME -> {
                nickRetryCounter++
                mState.emit(NickStateMachine.Refused("In use", nickRetryCounter))
                broadcast(IIrcEvent.PickNewNick)
            }

            else -> {
                mState.emit(NickStateMachine.Accept(attemptedNick))
            }
        }
    }

    private suspend fun updateNickState(message: IIrcMessage, broadcast: (IIrcEvent) -> Unit) {
        when (mState.value) {
            is NickStateMachine.Accept -> Unit
            NickStateMachine.NickLess -> handleNickMessage(message, broadcast)
            is NickStateMachine.Refused -> handleNickMessage(message, broadcast)
        }
    }

    init {
        scope.launch {
            events.filterIsInstance<IIrcEvent.INIT>().collect {
                sendNickAndUserRequest()
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

    override suspend fun process(message: IIrcMessage, broadcast: (IIrcEvent) -> Unit) =
        updateNickState(message, broadcast)
}