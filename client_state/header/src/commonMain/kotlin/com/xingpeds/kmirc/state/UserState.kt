/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import kotlinx.coroutines.flow.StateFlow

interface UserState {
    val nick: StateFlow<String>
    val userName: StateFlow<String?>
    val hostName: StateFlow<String?>
}

data class MutableUserState(
    val mNick: StateFlow<String>,
    val mUserName: StateFlow<String?>,
    val mHostName: StateFlow<String?>
) : UserState {
    override val nick: StateFlow<String>
        get() = mNick
    override val userName: StateFlow<String?>
        get() = mUserName
    override val hostName: StateFlow<String?>
        get() = mHostName
}