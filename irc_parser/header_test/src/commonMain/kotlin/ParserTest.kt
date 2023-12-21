/*
 * Copyright 2024 Kyle McBurnett
 */

import com.xingpeds.kmirc.parser.IrcLineParser
import com.xingpeds.kmirc.parser.ParseResult
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlin.test.assertTrue


abstract class ParserTestBase : TestBase() {
    abstract fun getTestSubject(): IrcLineParser

    init {
        addTest(Test("join example one", this::exampleOne))
    }

    suspend fun exampleOne() {

        val testSubject = getTestSubject()
        val testInputFlow = flowOf("JOIN #ChannelTest")
        val result = testSubject.mapToIrcCommand(testInputFlow).toList()

        result.size.assert(1)
        assertTrue {
            result[0] is ParseResult.ParseSuccess
        }
    }
}