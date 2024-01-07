/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import assert
import com.xingpeds.kmirc.entities.IrcFrom
import com.xingpeds.kmirc.entities.IrcTarget
import com.xingpeds.kmirc.entities.events.IIrcEvent
import com.xingpeds.kmirc.events.MutableEventList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import launchNow
import org.junit.Test
import co.touchlab.kermit.Logger.Companion.i as info

class StateEventProcessorTests {

    fun test(testBlock: suspend CoroutineScope.() -> Unit): Unit = runTest() {
        StateEventProcessor.scope = backgroundScope
        val startJob = StateEventProcessor.start()
        startJob.join()
        backgroundScope.testBlock()
    }

    @Test
    fun selfJoin(): Unit = test {
        //setup state
        val selfNick = "testSelfNick"
        val channelName = "#channelName"
        MutableNickState.selfNick.emit(NickStateMachine.Accept(selfNick))
        val joinEvent = IIrcEvent.JOIN(channel = channelName, nick = selfNick)
        //fire event
        MutableEventList.mJoin.emit(joinEvent)
        // check state
        val channelMap: Map<ChannelName, ChannelState> = MutableClientState.channels.first { it.isNotEmpty() }
        channelMap.containsKey(channelName).assert(true)
    }


    @Test
    fun notice(): Unit = test {
        val noticeEvent = IIrcEvent.Notice(
            target = IrcTarget.User("testnick"), from = IrcFrom.User("fromNick"), message = "notice message"
        )
        info("test") {
            "expected: $noticeEvent"
        }
        launchNow {
            MutableEventList.mNotice.collect {
                info("test") {
                    it.toString()
                }
            }
        }
        MutableEventList.mNotice.emit(
            noticeEvent
        )
        val stateSample: IIrcEvent.Notice = MutableClientState.mNotices.filterNot { it.isEmpty() }.first().last()
        stateSample.assert(noticeEvent)
    }

}

