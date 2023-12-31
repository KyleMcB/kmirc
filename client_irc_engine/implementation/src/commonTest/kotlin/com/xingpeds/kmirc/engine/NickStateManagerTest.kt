package com.xingpeds.kmirc.engine

import assert
import com.xingpeds.kmirc.entities.IIrcEvent
import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import runWaitingTest
import kotlin.test.Test

class NickStateManagerTest {
    val username = "TestUser"
    val hostname = "testHostname"
    val realName = "test realname"
    val nickName = "TestNick"
    private val ircUser = IrcUser(
        nickName, username = username, hostname = hostname, realName = realName
    )

        @Test
    fun userCommand() = runWaitingTest { complete ->
            NickStateManager(
                wantedNick = ircUser,
                send = {
                    if (it.command == IrcCommand.USER) {
                        it.params.list[0].assert(username)
                        it.params.list[1].assert(hostname)
                        it.params.longParam.assert(realName)
                        complete()
                    }

                },
                scope = backgroundScope,
                events = flowOf(IIrcEvent.INIT)
            )
    }
    @Test
    fun nickCommand() = runWaitingTest { complete ->
        NickStateManager(
            wantedNick = ircUser,
            send = {
                if (it.command == IrcCommand.NICK) {
                    it.params.list[0].assert(nickName)
                    complete()
                }

            },
            scope = backgroundScope,
            events = flowOf(IIrcEvent.INIT)
        )
    }
}