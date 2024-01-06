/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.events

import com.xingpeds.kmirc.entities.events.IIrcEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object MutableEventList : EventList {

    val mPing = MutableSharedFlow<IIrcEvent.PING>()
    override val onPING: SharedFlow<IIrcEvent.PING>
        get() = mPing

    val mNotice = MutableSharedFlow<IIrcEvent.Notice>()
    override val onNOTICE: SharedFlow<IIrcEvent.Notice>
        get() = mNotice

    val mInit = MutableSharedFlow<IIrcEvent.INIT>(1)
    override val onINIT: SharedFlow<IIrcEvent.INIT>
        get() = mInit

    val mPrivmsg = MutableSharedFlow<IIrcEvent.PRIVMSG>()
    override val onPRIVMSG: SharedFlow<IIrcEvent.PRIVMSG>
        get() = mPrivmsg
    val mJoin = MutableSharedFlow<IIrcEvent.JOIN>()
    override val onJOIN: Flow<IIrcEvent.JOIN>
        get() = mJoin
}