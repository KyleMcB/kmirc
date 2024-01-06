/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

/**
 * SelfNickState class represents a singleton implementation of the ISelfNickState interface.
 */
object MutableNickState : ISelfNickState {
    /**
     * Nick state machine
     */
    val selfNick: MutableStateFlow<NickStateMachine> = MutableStateFlow(NickStateMachine.NickLess)
    override val selfNickState: StateFlow<NickStateMachine>
        get() = selfNick

    override suspend fun isNickMe(nick: String): Boolean {
        return when (val currentState = selfNick.first()) {
            is NickStateMachine.Accept -> {
                currentState.nick == nick
            }

            NickStateMachine.NickLess -> false
            is NickStateMachine.Refused -> false
        }
    }
}