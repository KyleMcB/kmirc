/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcMessage
import com.xingpeds.kmirc.entities.IrcParams
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.domain
import io.kotest.property.arbitrary.string
import io.kotest.property.arbs.name
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import nickPrefixArb
import org.junit.Test
import serverPrefixArb
import kotlin.test.assertFailsWith

class IrcEventTest {
    @Test
    fun functionlessDataObjectEvents() {
        // this is for line coverage.
        val list = listOf<IIrcEvent>(
            PickNewNick,
            TCPConnected
        )
    }

    @Test
    fun part(): Unit = runTest {
        checkAll(nickPrefixArb, Arb.name()) { prefix, channelName ->
            PART(
                IrcMessage(
                    prefix = prefix,
                    command = IrcCommand.PART,
                    params = IrcParams(channelName.toString())
                )
            )
        }
    }

    @Test
    fun ping(): Unit = runTest {
        checkAll(Arb.domain()) { domain ->
            PING(IrcParams(longParam = domain))
        }
    }

    @Test
    fun join(): Unit = runTest {
        //:WiZ JOIN #Twilight_zone        ; JOIN message from WiZ
        checkAll(nickPrefixArb, Arb.name()) { prefix, channelName ->
            val message: IIrcMessage =
                IrcMessage(prefix, command = IrcCommand.JOIN, params = IrcParams(channelName.toString()))
            JOIN(message)
        }
    }

    @Test
    fun notice(): Unit = runTest {
        checkAll(nickPrefixArb, Arb.boolean(), Arb.string(), Arb.name()) { prefix, targetChannel, message, targetName ->
            val line: IrcMessage = IrcMessage(
                prefix, IrcCommand.NOTICE, IrcParams(
                    if (targetChannel) {
                        "#$targetName"
                    } else {
                        targetName.toString()
                    }, longParam = message
                )
            )
            NOTICE(line)
        }
    }

    @Test
    fun privmsgFromNick(): Unit = runTest {
//        :Angel PRIVMSG Wiz :Hello are you receiving this message ?
        checkAll(nickPrefixArb, Arb.boolean(), Arb.string(), Arb.name()) { prefix, targetChannel, message, targetName ->
            val line = IrcMessage(
                prefix, IrcCommand.PRIVMSG, IrcParams(
                    if (targetChannel) {
                        "#$targetName"
                    } else {
                        targetName.toString()
                    }, longParam = message
                )
            )
            PRIVMSG(line)
        }
    }

    @Test
    fun privmsgFromServer(): Unit = runTest {
//        :Angel PRIVMSG Wiz :Hello are you receiving this message ?
        checkAll(
            serverPrefixArb,
            Arb.boolean(),
            Arb.string(),
            Arb.name()
        ) { prefix, targetChannel, message, targetName ->
            val line: IrcMessage = IrcMessage(
                prefix, IrcCommand.PRIVMSG, IrcParams(
                    if (targetChannel) {
                        "#$targetName"
                    } else {
                        targetName.toString()
                    }, longParam = message
                )
            )
            PRIVMSG(line)
        }
    }

    @Test
    fun joinMissingNick() {
        val message: IIrcMessage =
            IrcMessage(null, command = IrcCommand.JOIN, params = IrcParams("#hello"))
        assertFailsWith<IllegalIRCMessage> {
            JOIN(message)

        }
    }
}