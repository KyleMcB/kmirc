/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities.events

import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcMessage
import com.xingpeds.kmirc.entities.IrcParams
import io.kotest.property.Arb
import io.kotest.property.arbitrary.domain
import io.kotest.property.arbs.name
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import nickPrefixArb
import org.junit.Test

class IrcEventTest {
    @Test
    fun functionlessDataObjectEvents() {
        // this is for line coverage.
        val list = listOf<IIrcEvent>(
            IIrcEvent.PickNewNick,
            IIrcEvent.INIT
        )
    }

    @Test
    fun ping() = runTest {
        checkAll(Arb.domain()) { domain ->
            IIrcEvent.PING(IrcParams(longParam = domain))
        }
    }

    @Test
    fun join() = runTest {
        //:WiZ JOIN #Twilight_zone        ; JOIN message from WiZ
        checkAll(nickPrefixArb, Arb.name()) { prefix, channelName ->
            val message: IIrcMessage =
                IrcMessage(prefix, command = IrcCommand.JOIN, params = IrcParams(channelName.toString()))
            IIrcEvent.JOIN(message)
        }

    }
}