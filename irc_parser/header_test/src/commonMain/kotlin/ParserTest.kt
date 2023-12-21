/*
 * Copyright 2024 Kyle McBurnett
 */

import com.xingpeds.kmirc.parser.IrcLineParser
import com.xingpeds.kmirc.parser.ParseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlin.test.assertTrue


abstract class ParserTestBase : TestBase() {
    abstract suspend fun getTestSubject(): IrcLineParser

    init {
        addTest(
            Test(
                name = "join example one"
            ) {
                val testSubject = this.getTestSubject()
                val testInputFlow = flowOf("JOIN #ChannelTest")
                val result = testSubject.mapToIrcCommand(testInputFlow).toList()
                result.size.assert(1)
                assertTrue {
                    result[0] is ParseResult.ParseSuccess
                }
            })
        addTest(
            Test(
                name = "join example with prefix"
            ) {
                val subject = getTestSubject()
                val testInputFlow: Flow<String> = flowOf(":nick!user@hostname JOIN #ChannelTest")
                val result = subject.mapToIrcCommand(testInputFlow).toList()
                result.size.assert(1)
                val parseResult = result[0]
                assertTrue("expecting a success") {
                    parseResult is ParseResult.ParseSuccess
                }
                require(parseResult is ParseResult.ParseSuccess)
                parseResult.prefix!!.nick.assert("nick")
                parseResult.prefix!!.user.assert("user")
                parseResult.prefix!!.host.assert("hostname")
            }
        )
    }

}