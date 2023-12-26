/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import assert
import com.xingpeds.kmirc.entities.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.fail

class EngineTest {
    val username = "TestUser"
    val hostname = "testHostname"
    val realName = "test realname"
    private val ircUser = IrcUser(
        "TestNick", username = username, hostname = hostname, realName = realName
    )

    @Test
    fun prototyping() = runTest {
        val subject = IrcEngine(
            wantedNick = ircUser,
            send = {
            }, input = emptyFlow(), this.backgroundScope
        )
    }

    @Test
    fun userCommand() = runTest {
        val longParam = "iW|dHYrFO^"
        val userAssertDone = CompletableDeferred<Boolean>()
        val pongAssertDone = CompletableDeferred<Boolean>()
        val engine: IClientIrcEngine = IrcEngine(
            wantedNick = ircUser,
            send = { message: IIrcMessage ->
                println("[enginetest engine sent: $message")
                when (message.command) {
                    IrcCommand.USER -> {
                        //assert correctness
                        //                           Command: USER
                        //                           Parameters: <username> <hostname> <servername> <realname>
                        //                           USER guest tolmoon tolsun :Ronnie Reagan
                        val output = message.toIRCString()
                        output.assert("USER $username $hostname * :$realName\r\n")
                        userAssertDone.complete(true)
                        //signal test is done
                    }

                    IrcCommand.NICK -> Unit //ignore for now
                    IrcCommand.PONG -> {
                        val output = message.toIRCString()
                        output.assert("PONG :iW|dHYrFO^\r\n")
                        pongAssertDone.complete(true)
                    }

                    else -> fail("engine should send any other messages at startup")
                }
            },
            input = flowOf(
                IrcMessage(
                    command = IrcCommand.PING,
                    params = IrcParams(longParam = longParam)
                )
            ),
            engineScope = this.backgroundScope
        )
        pongAssertDone.await()
        userAssertDone.await()
        backgroundScope.cancel()
        //wait for done signal
    }
}