/*
 * Copyright 2024 Kyle McBurnett
 */

import com.xingpeds.kmirc.parser.IrcLineParser
import com.xingpeds.kmirc.parser.ParseResult
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlin.test.assertTrue


abstract class ParserTestBase {
    abstract fun getTestSubject(): IrcLineParser

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