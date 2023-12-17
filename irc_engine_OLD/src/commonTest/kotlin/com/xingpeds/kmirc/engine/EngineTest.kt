package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcMessage
import com.xingpeds.kmirc.entities.IrcParams
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.asserter

class EngineTest {
    @Test
    fun ping() = runTest {
        val stream = MutableSharedFlow<IrcMessage>()
        val testScope = this
        val completionSignal = CompletableDeferred<Unit>()
        val subject = Engine(
            input = stream,
            output = { output: IrcMessage ->
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
        //flowOf(
        // IrcMessage(command = IrcCommand.PING, params = IrcParams(emptyList(), "ServerName"))
        // )
        stream.emit(
            IrcMessage(command = IrcCommand.PING, params = IrcParams(emptyList(), "ServerName"))
        )
        // test needs to wait for the engine to finish
        completionSignal.await() // Wait for the signal
    }

    @Test
    fun selfJoin() = runTest {
        val stream = MutableSharedFlow<IrcMessage>()
        val testScope = this
        val completionSignal = CompletableDeferred<Unit>()
        val subject = Engine(
            input = stream,
            output = { output: IrcMessage ->
                println("Engine sent message: $output")
                asserter.assertEquals(
                    actual = output.command,
                    expected = IrcCommand.JOIN,
                    message = "Command should be JOIN"
                )
                asserter.assertEquals(
                    actual = output.params.longParam,
                    expected = "#channel",
                    message = "Long param should be #channel"
                )
                completionSignal.complete(Unit)
            })
        stream.emit(
            IrcMessage(command = IrcCommand.JOIN, params = IrcParams(emptyList(), "#channel"))
        )
        completionSignal.await()
    }
}