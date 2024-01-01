/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities

import com.xingpeds.kmirc.entities.events.IIrcEvent

/**
 * [MessageProcessor]s will be used in the engine before the message is broadcast as an event.
 * This is mostly for updating the state before firing an event.
 * They will be placed in a set therefore the required hashCode implementation.
 * Should just set it the hashcode of the name of the class.
 */
interface MessageProcessor {

    suspend fun process(message: IIrcMessage, broadcast: (IIrcEvent) -> Unit)

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int

}