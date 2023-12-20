/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.parser

import assert
import assertFlowNeverEmits
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test


class MarshallerTest {
    @Test
    fun noIRCPacket() = runTest {
        val internetPackets = flowOf("hello", "world", "never has irc terminator")
        val output = marshallIrcPackets(internetPackets)
        assertFlowNeverEmits(output)
    }

    @Test
    fun oneIrcPacket() = runTest {
        val internetPackets = flowOf("hello\r\n")
        val output = marshallIrcPackets(internetPackets)
        output.toList().size.assert(1)
    }

    @Test
    fun twoNetPacketsOneIrcPacket() = runTest {
        val internetPackets = flowOf("hello", "world\r\n")
        val output = marshallIrcPackets(internetPackets)
        output.toList().size.assert(1)
    }

    @Test
    fun oneNetPacketTwoIrcPackets() = runTest {
        val internetPackets = flowOf("hello\r\n world\r\n")
        val output = marshallIrcPackets(internetPackets)
        output.toList().size.assert(2)
    }
}