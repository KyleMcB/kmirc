/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.events

import com.xingpeds.kmirc.entities.events.*
import kotlinx.coroutines.flow.SharedFlow

/**
 * This is an eventBus of irc events
 */
@Suppress("KDocMissingDocumentation") // pretty self explanatory
interface EventList {
    val onEndOfMOTD: SharedFlow<EndOfMOTD>
    val onERROR: SharedFlow<ERROR>
    val onAWAY: SharedFlow<AWAY>
    val onKICK: SharedFlow<KICK>
    val onINVITE: SharedFlow<INVITE>
    val onPickNewNick: SharedFlow<PickNewNick>
    val onUserQuit: SharedFlow<UserQuit>
    val onNickChange: SharedFlow<NickChange>
    val onMOTDLINE: SharedFlow<MOTDLINE>
    val onPart: SharedFlow<PART>
    val onMODE: SharedFlow<MODE>
    val onWELCOME: SharedFlow<WELCOME>
    val onPING: SharedFlow<PING>
    val onNOTICE: SharedFlow<NOTICE>
    val onINIT: SharedFlow<TCPConnected>
    val onPRIVMSG: SharedFlow<PRIVMSG>
    val onJOIN: SharedFlow<JOIN>
    val onPART: SharedFlow<PART>

}