package com.xingpeds.kmirc.parser

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test


class IrcParserImplTest {
    @Test
    fun privmsg() = runTest {
        val line = ":NickName!~UserName@host.com PRIVMSG #channel :Hello how are you?\r\n"
        val parser = IrcParser()
        val lines = parser(line).toList()
    }
}