/*
 * Copyright 2024 Kyle McBurnett
 */

import com.xingpeds.kmirc.parser.IrcLineParser
import kotlinx.coroutines.test.runTest
import kotlin.test.Test


class ParserTest : ParserTestBase() {
    override suspend fun getTestSubject(): IrcLineParser {
        return Parser
    }

    @Test
    fun tests() = runTest {
        runAllTest()
    }
}