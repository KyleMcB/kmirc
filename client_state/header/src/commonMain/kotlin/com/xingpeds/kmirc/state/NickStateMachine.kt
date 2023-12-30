/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

sealed interface NickStateMachine : CharSequence {
    /**
     * akin to loading state
     */
    object NickLess : NickStateMachine, CharSequence by empty

    /**
     * nick attempted, and refused. Need ot try again
     */
    data class Refused(val message: String, val attempt: Int) : NickStateMachine, CharSequence by empty

    /**
     * Server accepted nick. State should get updated.
     */
    data class Accept(val nick: String) : NickStateMachine, CharSequence by nick
    companion object {
        const val empty = ""
    }
}
