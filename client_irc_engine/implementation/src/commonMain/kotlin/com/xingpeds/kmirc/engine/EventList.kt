/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.events.IIrcEvent
import com.xingpeds.kmirc.events.EventList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow


internal object EventListImpl : EventList {
    val mPing = MutableSharedFlow<IIrcEvent.PING>(replay = 2)
    override val onPING: SharedFlow<IIrcEvent.PING>
        get() = mPing

    val mNotice = MutableSharedFlow<IIrcEvent.Notice>(replay = 10)
    override val onNOTICE: SharedFlow<IIrcEvent.Notice>
        get() = mNotice

    val mPRIVMSG = MutableSharedFlow<IIrcEvent.PRIVMSG>(replay = 10)
    override val onPRIVMSG: SharedFlow<IIrcEvent.PRIVMSG>
        get() = mPRIVMSG

    val mINIT = MutableSharedFlow<IIrcEvent.INIT>(1)
    override val onINIT: SharedFlow<IIrcEvent.INIT>
        get() = mINIT
}