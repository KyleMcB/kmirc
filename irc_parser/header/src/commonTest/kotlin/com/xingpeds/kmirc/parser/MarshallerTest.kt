package com.xingpeds.kmirc.parser

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

suspend fun flowNeverEmits(flow: Flow<*>) {
    var hasValues = false
    flow.collect {
        hasValues = true
    }

    assert(!hasValues) { "Flow emitted some values" }
}

fun Any?.assert(expected: Any?) {
    assertEquals(actual = this, expected = expected)
}

class MarshallerTest {
    @Test
    fun noIRCPacket() = runTest {
        val internetPackets = flowOf("hello", "world", "never has irc terminator")
        val output = marshallIrcPackets(internetPackets)
        flowNeverEmits(output)
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