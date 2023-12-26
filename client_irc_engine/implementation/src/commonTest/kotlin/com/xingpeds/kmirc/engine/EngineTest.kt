/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import assert
import com.xingpeds.kmirc.entities.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test

class EngineTest {
    val username = "TestUser"
    val hostname = "testHostname"
    val realName = "test realname"
    private val ircUser = IrcUser(
        "TestNick", username = username, hostname = hostname, realName = realName
    )


    fun runWaitingTest(block: TestScope.(testComplete: () -> Unit) -> Unit) = runTest {
        val completed = CompletableDeferred<Boolean>()
        block({ completed.complete(true) })
        completed.await()
    }

    @Test
    fun userCommand() = runWaitingTest { testCompleted ->
        val engine: IClientIrcEngine = IrcEngine(
            wantedNick = ircUser,
            send = { message: IIrcMessage ->
                println("[engineTest] $message sent")
                if (message.command == IrcCommand.USER) {
                    val output = message.toIRCString()
                    output.assert("USER $username $hostname * :$realName\r\n")
                    testCompleted()
                }
            },
            input = emptyFlow(),
            engineScope = this.backgroundScope
        )
    }

    @Test
    fun pongCommand() = runWaitingTest { complete ->
        val longParam = "iW|dHYrFO^"
        val engine: IClientIrcEngine = IrcEngine(
            wantedNick = ircUser,
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
    fun nickCommand() = runWaitingTest { complete ->
        val engine: IClientIrcEngine = IrcEngine(
            wantedNick = ircUser,
            send = { message: IIrcMessage ->
                if (message.command == IrcCommand.NICK) {
                    val output = message.toIRCString()
                    output.assert("NICK ${ircUser.nick}\r\n")
                    complete()
                }
            },
            input = emptyFlow(),
            engineScope = backgroundScope
        )
    }
}