/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.*
import com.xingpeds.kmirc.state.ClientState
import com.xingpeds.kmirc.state.MutableClientState
import com.xingpeds.kmirc.state.NickStateMachine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IrcEngine(
    val wantedNick: IIrcUser,
    val send: suspend (IIrcMessage) -> Unit,
    val mState: MutableClientState,
    val inputFlow: Flow<IIrcMessage>,
    private val engineScope: CoroutineScope
) : IClientIrcEngine {
    private var attemptedNick: String = wantedNick.nick
    private var nickRetryCounter: Int = 0
    private val mEvents = MutableSharedFlow<IIrcEvent>()
    override val events: SharedFlow<IIrcEvent>
        get() = mEvents
    override val state: ClientState
        get() = mState
    override val self: StateFlow<NickStateMachine>
        get() = mState.mNickSate

    private var retryCount = 0

    init {
        engineScope.launch {
            sendNickAndUserRequest()
            inputFlow.collect { message ->
                when (message.command) {
                    IrcCommand.ERR_NICKNAMEINUSE, IrcCommand.ERR_NICKCOLLISION -> {
                        if (retryCount < 3) {
                            //ask user for a new nick
                            sendNickAndUserRequest()
                            delay(2000)
                            retryCount++
                        } else {
                            // handle max retry exceed
                            // ...
                        }
                    }

                    else -> TODO()

                }
            }
        }
        engineScope.launch {
            inputFlow.collect { message ->
                val event = messageToEvent(message)
            }
        }
    }

    //I would prefer to have the nick statemachine inside of the event system
    //but since any message means the nick is accepted it needs to be a seperate state machine
    private suspend fun updateNickState(message: IIrcMessage) {
        //I need a currently attempted nickname
        when (val nickState: NickStateMachine = state.nickState.value) {
            is NickStateMachine.Accept -> Unit
            NickStateMachine.NickLess -> {
                //what does it mean for a message to come in while nickless?
                //It could be an error and should move sate machine to refused
                // it could be anything else indicating the nick was accepted
                handleNickMessage(message)
            }

            is NickStateMachine.Refused -> {
                handleNickMessage(message)
            }
        }

    }

    /**
     * helper to [updateNickState] for readablitiy
     */
    private suspend fun handleNickMessage(message: IIrcMessage) {
        when (message.command) {
            IrcCommand.ERR_NONICKNAMEGIVEN -> {
                //this feels impossible...
                throw Exception("No nickname given. Should probably be impossible")
            }

            IrcCommand.ERR_ERRONEUSNICKNAME -> {
                //inc the fail counter and try a new nickname
                nickRetryCounter++
                mState.mNickSate.emit(NickStateMachine.Refused("In use", nickRetryCounter))
                // the client is going to have to deal with this. If it is a user client, then ask for
                // a nickname. If a bot, use a backup nick, or new nickname generating algo.
                // so emitting a blocked Refused state is all the engine can do here.
            }

            else -> {
                //nick accepted
                mState.mNickSate.emit(NickStateMachine.Accept(attemptedNick))
            }
        }
    }

    private suspend fun sendNickAndUserRequest() {
        send(IrcMessage(command = IrcCommand.NICK, params = IrcParams(wantedNick.nick)))
        send(
            IrcMessage(
                command = IrcCommand.USER,
                params = IrcParams(wantedNick.username, wantedNick.hostname, longParam = wantedNick.realName)
            )
        )
    }

    private fun updateState(message: IIrcMessage): IIrcMessage {
        TODO("Not yet implemented")
    }

}