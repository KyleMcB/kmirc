package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcMessage
import com.xingpeds.kmirc.entities.IrcParams
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.asserter

class EngineTest {
    @Test
    fun ping() = runTest {
        val testScope = this
        val completionSignal = CompletableDeferred<Unit>()
        val subject = Engine(
            input = flowOf(IrcMessage(command = IrcCommand.PING, params = IrcParams(emptyList(), "ServerName"))),
            output = { output ->
                println("Engine sent message: $output")
                asserter.assertEquals(
                    actual = output.command,
                    expected = IrcCommand.PONG,
                    message = "Command should be PONG"
                )
                asserter.assertEquals(
                    actual = output.params.longParam,
                    expected = "ServerName",
                    message = "Long param should be ServerName"
                )
                // test is finished here
                completionSignal.complete(Unit)
            })
        // test needs to wait for the engine to finish
        completionSignal.await() // Wait for the signal
    }
}