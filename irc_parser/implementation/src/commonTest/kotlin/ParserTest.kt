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
    override fun `ping exmaple test`() = runTest {
        super.`ping example`()
    }

    @Test
    override fun `join example on test`() = runTest {
        super.`join example one`()
    }

    @Test
    override fun `join with prefix test`() = runTest {
        super.`join example with prefix`()
    }

    @Test
    override fun `nick example test`() = runTest {
        super.`nick exmaple`()
    }

    @Test
    override fun `user example test`() = runTest {
        super.`user example`()
    }


}