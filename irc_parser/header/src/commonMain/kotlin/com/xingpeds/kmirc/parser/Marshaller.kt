/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.parser

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Marshalls IRC packets from the input Flow of strings.
 *
 * @param input the Flow of strings representing IRC packets
 * @return a Flow of strings representing marshalled IRC packets
 */
fun marshallIrcPackets(input: Flow<String>): Flow<String> = flow {
    val buffer = StringBuilder(1024)
    input.collect { packet ->
        println("[marshaller] raw $packet")
        buffer.append(packet)
        println("[marchaller] buffer=${buffer.toString()}")

        while (buffer.contains("\r\n")) {
            val index = buffer.indexOf("\r\n")
            val packetToSend = buffer.substring(0, index)
            buffer.delete(0, index + 2)
            emit(packetToSend)
        }
    }
}