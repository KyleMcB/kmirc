package com.xingpeds.kmirc.parser

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MarshallerTest {
    @Test
    fun multipacketline() = runTest {
        val packets = flowOf(":NickName!~UserName@host.com NOTI", "CE #channel :This is a notice message\r\n")
        val marshaller = IrcPacketMarshaller()
        val lines = marshaller(packets).toList()
        assertEquals(actual = lines.size, expected = 1, message = "should only have one output line")
    }

    @Test
    fun multiMessageSinglePacket() = runTest {
        val packet =
            flowOf(":NickName!~UserName@host.com NOTICE #channel :This is a notice message\r\n:NickName!~UserName@host.com JOIN #channel\r\n")
        val marshaller = IrcPacketMarshaller()
        val lines = marshaller(packet).toList()
        assertEquals(actual = lines.size, expected = 2, message = "should only have two output lines")
    }
}