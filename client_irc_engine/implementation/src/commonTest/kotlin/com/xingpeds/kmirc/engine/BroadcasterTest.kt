/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import LogTag
import Logged
import assert
import com.xingpeds.kmirc.entities.*
import com.xingpeds.kmirc.events.MutableEventList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn
import launchNow
import org.junit.Test
import runWaitingTest
import v

class BroadcasterTest : Logged by LogTag("BroadcasterTest") {

    @Test
    fun pongCommand(): Unit = runWaitingTest { complete ->
        val longParam = "iW|dHYrFO^"
        EventBroadcaster(
            send = { message: IIrcMessage ->
                if (message.command == IrcCommand.PONG) {
                    val output = message.toIRCString()
                    output.assert("PONG :iW|dHYrFO^\r\n")
                    complete()
                }
            }, input = flowOf(
                IrcMessage(
                    command = IrcCommand.PING, params = IrcParams(longParam = longParam)
                )
            ).shareIn(this, SharingStarted.Lazily), engineScope = backgroundScope
        )
    }


    @Test
    fun noticeCommand(): Unit = runWaitingTest { complete ->
        val longParam = "*** Found your hostname (c-24-17-115-100.hsd1.wa.comcast.net)"
        //        :*.freenode.net NOTICE hellobotlongname :*** Found your hostname (c-24-17-115-100.hsd1.wa.comcast.net)
        backgroundScope.launchNow {
            MutableEventList.onNOTICE.collect { notice ->
                v("[engine test] got $notice")
                notice.message.assert(longParam)
                complete()
            }
        }
        EventBroadcaster(
            send = {}, input = flowOf(
                IrcMessage(
                    command = IrcCommand.NOTICE,
                    prefix = IrcPrefix("*.freenode.net"),
                    params = IrcParams("hellobotlongname", longParam = longParam)
                )
            ).shareIn(this, SharingStarted.Lazily),
            engineScope = backgroundScope
        )
    }

    @Test
    fun privmsg(): Unit = runWaitingTest { testComplete ->
//        :Harambe!~harambe@freenode/service/Harambe PRIVMSG hellobotlongname :VERSION
        backgroundScope.launchNow {
            MutableEventList.onPRIVMSG.collect {
                v("[engine test] got $it")
                it.message.assert("VERSION")
                testComplete()
            }

        }
        EventBroadcaster(
            send = {}, input = flowOf(
                IrcMessage(
                    prefix = IrcPrefix(nickOrServer = "Harambe", user = "harambe", host = "freenode/service/Harambe"),
                    command = IrcCommand.PRIVMSG,
                    params = IrcParams("hellobotlongname", longParam = "VERSION")
                )
            ).shareIn(this, SharingStarted.Lazily),
            engineScope = backgroundScope
        )
    }
}
