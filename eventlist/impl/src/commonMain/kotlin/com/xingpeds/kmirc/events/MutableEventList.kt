/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.events

import com.xingpeds.kmirc.entities.events.IIrcEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * the mutable singleton eventbus system for the irc engine
 */
@Suppress("KDocMissingDocumentation") //self documenting
object MutableEventList : EventList {

    val mINVITE: MutableSharedFlow<IIrcEvent.INVITE> = MutableSharedFlow()
    override val onINVITE: SharedFlow<IIrcEvent.INVITE>
        get() = mINVITE

    val mPickNewNick: MutableSharedFlow<IIrcEvent.PickNewNick> = MutableSharedFlow()
    override val onPickNewNick: SharedFlow<IIrcEvent.PickNewNick>
        get() = mPickNewNick

    val mUserQuit: MutableSharedFlow<IIrcEvent.UserQuit> = MutableSharedFlow()
    override val onUserQuit: SharedFlow<IIrcEvent.UserQuit>
        get() = mUserQuit

    val mNickChange: MutableSharedFlow<IIrcEvent.NickChange> = MutableSharedFlow()
    override val onNickChange: SharedFlow<IIrcEvent.NickChange>
        get() = mNickChange

    val mMOTDLINE: MutableSharedFlow<IIrcEvent.MOTDLINE> = MutableSharedFlow()
    override val onMOTDLINE: SharedFlow<IIrcEvent.MOTDLINE>
        get() = mMOTDLINE

    val mPART: MutableSharedFlow<IIrcEvent.PART> = MutableSharedFlow()
    override val onPart: SharedFlow<IIrcEvent.PART>
        get() = mPART

    val mMODE: MutableSharedFlow<IIrcEvent.MODE> = MutableSharedFlow()
    override val onMODE: SharedFlow<IIrcEvent.MODE>
        get() = mMODE
    val mWELCOME: MutableSharedFlow<IIrcEvent.WELCOME> = MutableSharedFlow(1)
    override val onWELCOME: SharedFlow<IIrcEvent.WELCOME>
        get() = mWELCOME

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
    override val onJOIN: SharedFlow<IIrcEvent.JOIN>
        get() = mJoin
    val mPart: MutableSharedFlow<IIrcEvent.PART> = MutableSharedFlow()
    override val onPART: SharedFlow<IIrcEvent.PART>
        get() = mPart
}