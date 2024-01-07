/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import assert
import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcUser
import com.xingpeds.kmirc.entities.events.IIrcEvent
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import runWaitingTest
import kotlin.test.Test

class NickStateManagerTest {
    private val username = "TestUser"
    private val hostname = "testHostname"
    private val realName = "test realname"
    private val nickName = "TestNick"
    private val ircUser = IrcUser(
        nickName, username = username, hostname = hostname, realName = realName
    )

    @Test
    fun userCommand(): Unit = runWaitingTest { complete ->
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
            events = flowOf(IIrcEvent.INIT),
            broadcast = {},
            messages = emptyFlow()
        ).start().join()
    }

    @Test
    fun nickCommand(): Unit = runWaitingTest { complete ->
        NickStateManager(
            wantedNick = ircUser,
            send = {
                if (it.command == IrcCommand.NICK) {
                    it.params.list[0].assert(nickName)
                    complete()
                }

            },
            scope = backgroundScope,
            events = flowOf(IIrcEvent.INIT),
            broadcast = {},
            messages = emptyFlow()
        ).start().join()
    }
}