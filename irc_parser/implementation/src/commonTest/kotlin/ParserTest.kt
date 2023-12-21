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
    override fun `test join example on`() = runTest {
        super.`join example one`()
    }

    @Test
    override fun `test join with prefix`() = runTest {
        super.`join example with prefix`()
    }


}