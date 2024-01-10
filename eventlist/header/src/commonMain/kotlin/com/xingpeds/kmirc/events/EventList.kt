/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.events

import com.xingpeds.kmirc.entities.events.IIrcEvent
import kotlinx.coroutines.flow.SharedFlow

/**
 * This is an eventBus of irc events
 */
@Suppress("KDocMissingDocumentation") // pretty self explanatory
interface EventList {
    val onPickNewNick: SharedFlow<IIrcEvent.PickNewNick>
    val onUserQuit: SharedFlow<IIrcEvent.UserQuit>
    val onNickChange: SharedFlow<IIrcEvent.NickChange>
    val onMOTDLINE: SharedFlow<IIrcEvent.MOTDLINE>
    val onPart: SharedFlow<IIrcEvent.PART>
    val onMODE: SharedFlow<IIrcEvent.MODE>
    val onWELCOME: SharedFlow<IIrcEvent.WELCOME>
    val onPING: SharedFlow<IIrcEvent.PING>
    val onNOTICE: SharedFlow<IIrcEvent.Notice>
    val onINIT: SharedFlow<IIrcEvent.INIT>
    val onPRIVMSG: SharedFlow<IIrcEvent.PRIVMSG>
    val onJOIN: SharedFlow<IIrcEvent.JOIN>
    val onPART: SharedFlow<IIrcEvent.PART>

}