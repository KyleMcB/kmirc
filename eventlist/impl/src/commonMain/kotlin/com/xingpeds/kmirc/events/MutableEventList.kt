/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.events

import com.xingpeds.kmirc.entities.events.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * the mutable singleton eventbus system for the irc engine
 */
@Suppress("KDocMissingDocumentation") //self documenting
object MutableEventList : EventList {

    val mNAMES: MutableSharedFlow<NAMES> = MutableSharedFlow()
    override val onNAMES: SharedFlow<NAMES>
        get() = mNAMES
    val mEndOfMOTD: MutableSharedFlow<EndOfMOTD> = MutableSharedFlow(1)
    val mERROR: MutableSharedFlow<ERROR> = MutableSharedFlow()
    val mAWAY: MutableSharedFlow<AWAY> = MutableSharedFlow()
    val mKICK: MutableSharedFlow<KICK> = MutableSharedFlow()
    val mINVITE: MutableSharedFlow<INVITE> = MutableSharedFlow()
    val mTOPIC: MutableSharedFlow<TOPIC> = MutableSharedFlow()
    override val onTOPIC: SharedFlow<TOPIC>
        get() = mTOPIC
    override val onEndOfMOTD: SharedFlow<EndOfMOTD>
        get() = mEndOfMOTD
    override val onERROR: SharedFlow<ERROR>
        get() = mERROR
    override val onAWAY: SharedFlow<AWAY>
        get() = mAWAY
    override val onKICK: SharedFlow<KICK>
        get() = mKICK
    override val onINVITE: SharedFlow<INVITE>
        get() = mINVITE

    val mPickNewNick: MutableSharedFlow<PickNewNick> = MutableSharedFlow()
    override val onPickNewNick: SharedFlow<PickNewNick>
        get() = mPickNewNick

    val mUserQuit: MutableSharedFlow<UserQuit> = MutableSharedFlow()
    override val onUserQuit: SharedFlow<UserQuit>
        get() = mUserQuit

    val mNickChange: MutableSharedFlow<NickChange> = MutableSharedFlow()
    override val onNickChange: SharedFlow<NickChange>
        get() = mNickChange

    val mMOTDLINE: MutableSharedFlow<MOTDLINE> = MutableSharedFlow()
    override val onMOTDLINE: SharedFlow<MOTDLINE>
        get() = mMOTDLINE

    val mPART: MutableSharedFlow<PART> = MutableSharedFlow()
    override val onPart: SharedFlow<PART>
        get() = mPART

    val mMODE: MutableSharedFlow<MODE> = MutableSharedFlow()
    override val onMODE: SharedFlow<MODE>
        get() = mMODE
    val mWELCOME: MutableSharedFlow<WELCOME> = MutableSharedFlow(1)
    override val onWELCOME: SharedFlow<WELCOME>
        get() = mWELCOME

    val mPing: MutableSharedFlow<PING> = MutableSharedFlow()
    override val onPING: SharedFlow<PING>
        get() = mPing

    val mNotice: MutableSharedFlow<NOTICE> = MutableSharedFlow()
    override val onNOTICE: SharedFlow<NOTICE>
        get() = mNotice

    val mInit: MutableSharedFlow<TCPConnected> = MutableSharedFlow(1)
    override val onINIT: SharedFlow<TCPConnected>
        get() = mInit

    val mPrivmsg: MutableSharedFlow<PRIVMSG> = MutableSharedFlow()
    override val onPRIVMSG: SharedFlow<PRIVMSG>
        get() = mPrivmsg
    val mJoin: MutableSharedFlow<JOIN> = MutableSharedFlow()
    override val onJOIN: SharedFlow<JOIN>
        get() = mJoin
    val mPart: MutableSharedFlow<PART> = MutableSharedFlow()
    override val onPART: SharedFlow<PART>
        get() = mPart
}