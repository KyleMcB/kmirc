/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import assert
import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcUser
import com.xingpeds.kmirc.entities.events.TCPConnected
import io.kotest.mpp.log
import io.kotest.property.forAll
import ircUserArb
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import runWaitingTest
import kotlin.test.Test

class
NickStateManagerTest {
    private val username = "TestUser"
    private val hostname = "testHostname"
    private val realName = "test realname"
    private val nickName = "TestNick"
    private val ircUser = IrcUser(
        nickName, username = username, hostname = hostname, realName = realName
    )

    @Test
    fun alwaysSendsValidRequest(): Unit = runTest {
//        USER guest 0 * :Ronnie Reagan
//        ; No ident server
//        ; User gets registered with username
//        "~guest" and real name "Ronnie Reagan"
//
//        USER guest 0 * :Ronnie Reagan
//        ; Ident server gets contacted and
//        returns the name "danp"
//        ; User gets registered with username
//        "danp" and real name "Ronnie Reagan"
        forAll(ircUserArb) { ircUser: IrcUser ->
            val completed = CompletableDeferred<Boolean>()
            NickStateManager(
                wantedNick = ircUser,
                scope = backgroundScope,
                events = flowOf(TCPConnected),
                broadcast = {},
                messages = emptyFlow(),
                autoStart = false,
                send = { message ->
                    if (message.command == IrcCommand.USER) {
                        if (message.params.list.size != 3) {
                            log { "incorrect param size" }
                            completed.complete(false)
                        }
                        if (message.params.longParam == null) {
                            log { "missing realname" }
                            completed.complete(false)
                        }
                        completed.complete(true)
                    }
                }
            ).start().join()
            completed.await()
        }
    }

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
            events = flowOf(TCPConnected),
            broadcast = {},
            messages = emptyFlow(),
            autoStart = false
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
            events = flowOf(TCPConnected),
            broadcast = {},
            messages = emptyFlow(),
            autoStart = false
        ).start().join()
    }
}