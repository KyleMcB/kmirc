/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import assert
import co.touchlab.kermit.Logger.Companion.i
import com.xingpeds.kmirc.entities.*
import com.xingpeds.kmirc.events.MutableEventList
import kotlinx.coroutines.flow.flowOf
import launchNow
import org.junit.Test
import runWaitingTest

class EngineTest {


    @Test
    fun pongCommand() = runWaitingTest { complete ->
        val longParam = "iW|dHYrFO^"
        val engine: IClientIrcEngine = IrcEngine(
            send = { message: IIrcMessage ->
                i("engineTest") {
                    message.toString()
                }
                if (message.command == IrcCommand.PONG) {
                    val output = message.toIRCString()
                    output.assert("PONG :iW|dHYrFO^\r\n")
                    complete()
                }
            }, input = flowOf(
                IrcMessage(
                    command = IrcCommand.PING, params = IrcParams(longParam = longParam)
                )
            ), engineScope = backgroundScope, processors = emptySet()
        )
    }


    @Test
    fun noticeCommand() = runWaitingTest { complete ->
        val longParam = "*** Found your hostname (c-24-17-115-100.hsd1.wa.comcast.net)"
        //        :*.freenode.net NOTICE hellobotlongname :*** Found your hostname (c-24-17-115-100.hsd1.wa.comcast.net)
        backgroundScope.launchNow {
            MutableEventList.onNOTICE.collect { notice ->
                println("[engine test] got $notice")
                notice.message.assert(longParam)
                complete()
            }
        }
        val subject = IrcEngine(
            send = {}, input = flowOf(
                IrcMessage(
                    command = IrcCommand.NOTICE,
                    prefix = IrcPrefix("*.freenode.net"),
                    params = IrcParams("hellobotlongname", longParam = longParam)
                )
            ), engineScope = backgroundScope, processors = emptySet()
        )
    }

    @Test
    fun privmsg() = runWaitingTest { testComplete ->
//        :Harambe!~harambe@freenode/service/Harambe PRIVMSG hellobotlongname :VERSION
        backgroundScope.launchNow {
            MutableEventList.onPRIVMSG.collect {
                println("[engine test] got $it")
                it.message.assert("VERSION")
                testComplete()
            }

        }
        val engine = IrcEngine(
            send = {}, input = flowOf(
                IrcMessage(
                    prefix = IrcPrefix(nick = "Harambe", user = "harambe", host = "freenode/service/Harambe"),
                    command = IrcCommand.PRIVMSG,
                    params = IrcParams("hellobotlongname", longParam = "VERSION")
                )
            ), processors = emptySet(), engineScope = backgroundScope
        )
    }
}
