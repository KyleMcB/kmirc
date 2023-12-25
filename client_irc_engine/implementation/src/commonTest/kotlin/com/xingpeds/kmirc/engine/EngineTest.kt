/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import assert
import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcUser
import kotlinx.coroutines.flow.emptyFlow
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
                println(it)
                println(it.toIRCString())
            }, inputFlow = emptyFlow(), this
        )
    }

    @Test
    fun userCommand() = runTest {
        IrcEngine(
            wantedNick = ircUser,
            send = { message: IIrcMessage ->
                //assert
                when (message.command) {
                    IrcCommand.USER -> {
                        //assert correctness
//                           Command: USER
//                           Parameters: <username> <hostname> <servername> <realname>
//                           USER guest tolmoon tolsun :Ronnie Reagan
                        val output = message.toIRCString()
                        output.assert("USER $username $hostname * :$realName\r\n")
                    }

                    IrcCommand.NICK -> Unit //ignore for now
                    else -> fail("engine should send any other messages at startup")
                }
                //end test here
            },
            inputFlow = emptyFlow(),
            engineScope = this
        )
    }

}
