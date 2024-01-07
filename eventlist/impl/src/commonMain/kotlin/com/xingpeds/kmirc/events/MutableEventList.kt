/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.events

import com.xingpeds.kmirc.entities.events.IIrcEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * the mutable singleton eventbus system for the irc engine
 */
object MutableEventList : EventList {

    val mPing: MutableSharedFlow<IIrcEvent.PING> = MutableSharedFlow()
    override val onPING: SharedFlow<IIrcEvent.PING>
        get() = mPing

    val mNotice: MutableSharedFlow<IIrcEvent.Notice> = MutableSharedFlow()
    override val onNOTICE: SharedFlow<IIrcEvent.Notice>
        get() = mNotice

    val mInit: MutableSharedFlow<IIrcEvent.INIT> = MutableSharedFlow(1)
    override val onINIT: SharedFlow<IIrcEvent.INIT>
        get() = mInit

    val mPrivmsg: MutableSharedFlow<IIrcEvent.PRIVMSG> = MutableSharedFlow()
    override val onPRIVMSG: SharedFlow<IIrcEvent.PRIVMSG>
        get() = mPrivmsg
    val mJoin: MutableSharedFlow<IIrcEvent.JOIN> = MutableSharedFlow()
    override val onJOIN: Flow<IIrcEvent.JOIN>
        get() = mJoin
    val mPart: MutableSharedFlow<IIrcEvent.PART> = MutableSharedFlow()
    override val onPART: Flow<IIrcEvent.PART>
        get() = mPart
}