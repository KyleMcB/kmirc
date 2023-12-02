package com.xingpeds.kmirc.parser

import com.xingpeds.kmirc.entities.IrcCommand
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.asserter


class IrcParserImplTest {
    @Test
    fun privmsg() = runTest {
        val line = ":NickName!~UserName@host.com PRIVMSG #channel :Hello how are you?\r\n"
        val parser = IrcParser()
        val lines = parser(line).toList()
        asserter.assertEquals(actual = lines.size, expected = 1, message = "should only have one output line")
        assertEquals(actual = lines[0].command, expected = IrcCommand.PRIVMSG, message = "expecting a privmsg")
        assertEquals(
            actual = lines[0].params.params[0],
            expected = "#channel",
            message = "first param should be the destination"
        )
    }
}