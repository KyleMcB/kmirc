/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

sealed interface NickStateMachine {
    /**
     * akin to loading state
     */
    object NickLess : NickStateMachine

    /**
     * nick attempted, and refused. Need ot try again
     */
    data class Refused(val message: String) : NickStateMachine

    /**
     * Server accepted nick. State should get updated.
     */
    data class Accept(val nick: String) : NickStateMachine
}
