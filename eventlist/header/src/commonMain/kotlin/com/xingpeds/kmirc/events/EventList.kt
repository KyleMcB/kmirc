/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.events

import com.xingpeds.kmirc.entities.events.IIrcEvent
import kotlinx.coroutines.flow.SharedFlow

interface EventList {
    val onPING: SharedFlow<IIrcEvent.PING>
    val onNOTICE: SharedFlow<IIrcEvent.Notice>
    val onINIT: SharedFlow<IIrcEvent.INIT>
    val onPRIVMSG: SharedFlow<IIrcEvent.PRIVMSG>
}