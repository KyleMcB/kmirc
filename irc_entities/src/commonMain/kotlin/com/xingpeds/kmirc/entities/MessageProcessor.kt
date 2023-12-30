/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities


interface MessageProcessor {

    suspend fun process(message: IIrcMessage, broadcast: (IIrcEvent) -> Unit)

}