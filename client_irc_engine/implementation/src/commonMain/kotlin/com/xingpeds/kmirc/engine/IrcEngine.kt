/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.*
import com.xingpeds.kmirc.state.ClientState
import com.xingpeds.kmirc.state.MutableClientState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch


class IrcEngine(
    val wantedNick: IIrcUser,
    val send: suspend (IIrcMessage) -> Unit,
    val mState: MutableClientState,
    val inputFlow: Flow<IIrcMessage>,
    private val engineScope: CoroutineScope
) : IClientIrcEngine {
    private val mEvents = MutableSharedFlow<IIrcEvent>()
    override val events: SharedFlow<IIrcEvent>
        get() = mEvents
    override val state: ClientState
        get() = mState


    init {
        //I need to get a nick and user
        var nickacquired = false
        engineScope.launch {
            send(IrcMessage(command = IrcCommand.NICK, params = IrcParams(wantedNick.nick)))
//            Command: USER
//            Parameters: <username> <hostname> <servername> <realname>
            send(
                IrcMessage(
                    command = IrcCommand.USER,
                    params = IrcParams(wantedNick.username, wantedNick.hostname, longParam = wantedNick.realName)
                )
            )
            inputFlow.takeWhile { nickacquired == false }.collect {
                nickacquired = true
            }
        }
        engineScope.launch {

            inputFlow.collect { message ->
                //
                val event = messageToEvent(message)
            }
        }
    }


    private fun updateState(message: IIrcMessage): IIrcMessage {
        TODO("Not yet implemented")
    }

}