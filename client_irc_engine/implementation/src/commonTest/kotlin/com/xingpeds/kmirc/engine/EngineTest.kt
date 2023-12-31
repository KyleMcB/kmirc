/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import assert
import com.xingpeds.kmirc.entities.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.junit.Test
import runWaitingTest

class EngineTest {
    val username = "TestUser"
    val hostname = "testHostname"
    val realName = "test realname"
    private val ircUser = IrcUser(
        "TestNick", username = username, hostname = hostname, realName = realName
    )

    @Test
    fun pongCommand() = runWaitingTest { complete ->
        val longParam = "iW|dHYrFO^"
        val engine: IClientIrcEngine = IrcEngine(
            send = { message: IIrcMessage ->
                if (message.command == IrcCommand.PONG) {
                    val output = message.toIRCString()
                    output.assert("PONG :iW|dHYrFO^\r\n")
                    complete()
                }
            },
            input = flowOf(
                IrcMessage(
                    command = IrcCommand.PING,
                    params = IrcParams(longParam = longParam)
                )
            ),
            engineScope = backgroundScope
        )
    }


    @Test
    fun noticeCommand() = runWaitingTest { complete ->
        val longParam = "*** Found your hostname (c-24-17-115-100.hsd1.wa.comcast.net)"
        //        :*.freenode.net NOTICE hellobotlongname :*** Found your hostname (c-24-17-115-100.hsd1.wa.comcast.net)
        val subject = IrcEngine(
            send = {},
            input = flowOf(
                IrcMessage(
                    command = IrcCommand.NOTICE,
                    prefix = IrcPrefix("*.freenode.net"),
                    params = IrcParams("hellobotlongname", longParam = longParam)
                )
            ),
            engineScope = backgroundScope,
        )
        backgroundScope.launch {
            subject.eventList.onNOTICE.collect { notice ->
                println("[engine test] got $notice")
                notice.message.assert(longParam)
                complete()
            }
        }
    }

    @Test
    fun privmsg() = runWaitingTest { testComplete ->
//        :Harambe!~harambe@freenode/service/Harambe PRIVMSG hellobotlongname :VERSION
        val engine = IrcEngine(
            send = {},
            input = flowOf(
                IrcMessage(
                    prefix = IrcPrefix(nick = "Harambe", user = "harambe", host = "freenode/service/Harambe"),
                    command = IrcCommand.PRIVMSG,
                    params = IrcParams("hellobotlongname", longParam = "VERSION")
                )
            ),
            processors = emptyList(),
            engineScope = backgroundScope
        )
        backgroundScope.launch {
            engine.eventList.onPRIVMSG.collect {
                println("[engine test] got $it")
                it.message.assert("VERSION")
                testComplete()
            }

        }
    }
}
