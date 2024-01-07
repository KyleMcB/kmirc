/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.events

import com.xingpeds.kmirc.entities.events.IIrcEvent
import kotlinx.coroutines.flow.Flow

/**
 * This is an eventBus of irc events
 */
@Suppress("KDocMissingDocumentation") // pretty self explanatory
interface EventList {
    val onPING: Flow<IIrcEvent.PING>
    val onNOTICE: Flow<IIrcEvent.Notice>
    val onINIT: Flow<IIrcEvent.INIT>
    val onPRIVMSG: Flow<IIrcEvent.PRIVMSG>
    val onJOIN: Flow<IIrcEvent.JOIN>
    val onPART: Flow<IIrcEvent.PART>

}