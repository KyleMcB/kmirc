/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state

import assert
import com.xingpeds.kmirc.entities.IrcFrom
import com.xingpeds.kmirc.entities.IrcTarget
import com.xingpeds.kmirc.entities.events.IIrcEvent
import com.xingpeds.kmirc.events.MutableEventList
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class StateEventProcessorTests {

    fun test(testBlock: suspend () -> Unit) = runTest() {
        StateEventProcessor.scope = backgroundScope
        val startJob = StateEventProcessor.start()
        testBlock()
        startJob.cancel()
    }

    @Test
    fun notice() = test {
        val noticeEvent = IIrcEvent.Notice(
            target = IrcTarget.User("testnick"),
            from = IrcFrom.User("fromNick"),
            message = "notice message"
        )
        println("expected: $noticeEvent")
        MutableEventList.mNotice.emit(
            noticeEvent
        )
        val stateSample: IIrcEvent.Notice = MutableClientState.mNotices.filterNot { it.isEmpty() }.first().last()
        println("actual: $stateSample")
        stateSample.assert(noticeEvent)
    }
}