/*
 * Copyright (c) Kyle McBurnett 2024.
 */

@file:OptIn(FlowPreview::class)

package com.xingpeds.kmirc.state

import LogTag
import Logged
import assert
import com.xingpeds.kmirc.entities.IrcFrom
import com.xingpeds.kmirc.entities.IrcTarget
import com.xingpeds.kmirc.entities.events.JOIN
import com.xingpeds.kmirc.entities.events.NOTICE
import com.xingpeds.kmirc.events.MutableEventList
import com.xingpeds.kmirc.state.processing.StateEventProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Test
import kotlin.test.fail
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class StateEventProcessorTests : Logged by LogTag("StateEventProcessorTests") {

    private fun test(timeout: Duration = 10.seconds, testBlock: suspend CoroutineScope.() -> Unit): Unit =
        runTest(timeout = timeout) {
            StateEventProcessor.scope = backgroundScope
            val startJob = StateEventProcessor.start()
            startJob.join()
            backgroundScope.testBlock()
        }

    @Test
    fun otherJoin(): Unit = test {
        //setup state
        val selfNick = "testSelfNick"
        val channelName = "#channelName"
        val nick = "otherNick"
        MutableNickState.selfNick.emit(NickStateMachine.Accept(selfNick))
        MutableClientState.mChannels.update { it + (channelName to MutableChannelState(channelName)) }
        val joinEvent = JOIN(channel = channelName, nick = nick, Clock.System.now())
        MutableEventList.mJoin.emit(joinEvent)
        val state: ChannelState = MutableClientState.channels.filterNot { it.isEmpty() }.first().get(channelName)
            ?: fail("channel state not found")
        state.members.filterNot { it.isEmpty() }.first().contains(nick).assert(true)
    }

    @Test
    fun selfJoin(): Unit = test {
        //setup state
        val selfNick = "testSelfNick"
        val channelName = "#channelName"
        MutableNickState.selfNick.emit(NickStateMachine.Accept(selfNick))
        val joinEvent = JOIN(channel = channelName, nick = selfNick, Clock.System.now())
        //fire event
        MutableEventList.mJoin.emit(joinEvent)
        // check state
        val channelMap: Map<ChannelName, ChannelState> = MutableClientState.channels.first { it.isNotEmpty() }
        channelMap.containsKey(channelName).assert(true)
    }


    @Test
    fun notice(): Unit = test {
        val noticeEvent = NOTICE(
            target = IrcTarget.User("testnick"),
            from = IrcFrom.User("fromNick"),
            message = "notice message",
            timestamp = Clock.System.now()
        )

        MutableEventList.mNotice.emit(
            noticeEvent
        )
        val stateSample: NOTICE = MutableClientState.mNotices.filterNot { it.isEmpty() }.first().last()
        stateSample.assert(noticeEvent)
    }

}

