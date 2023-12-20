/*
 * Copyright 2024 Kyle McBurnett
 */

import com.xingpeds.kmirc.parser.IrcLineParser
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

/*
 * Copyright 2024 Kyle McBurnett
 */

class ParserTest : ParserTestBase() {
    override fun getTestSubject(): IrcLineParser {
        return Parser
    }

    @Test
    fun test1() = runTest {
        super.exampleOne()
    }
}