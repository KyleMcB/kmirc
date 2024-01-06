/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.events

import com.xingpeds.kmirc.entities.events.IIrcEvent
import kotlinx.coroutines.flow.Flow

interface EventList {
    val onPING: Flow<IIrcEvent.PING>
    val onNOTICE: Flow<IIrcEvent.Notice>
    val onINIT: Flow<IIrcEvent.INIT>
    val onPRIVMSG: Flow<IIrcEvent.PRIVMSG>
    val onJOIN: Flow<IIrcEvent.JOIN>

}