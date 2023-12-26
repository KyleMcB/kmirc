/*
 * Copyright 2024 Kyle McBurnett
 */

import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.parser.IrcLineParser
import com.xingpeds.kmirc.parser.ParseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlin.test.assertTrue


abstract class ParserTestBase {
    abstract suspend fun getTestSubject(): IrcLineParser

    abstract fun `ping exmaple test`()
    suspend fun `ping example`() {
        val testSubject: IrcLineParser = getTestSubject()
        val testInputFlow: Flow<String> = flowOf("PING :irc.example.com")
        val result: List<ParseResult> = testSubject.mapToIrcCommand(testInputFlow).toList()
        result.size.assert(1)
        val item: ParseResult = result[0]
        require(item is ParseResult.ParseSuccess)
        item.prefix.assert(null)
        item.command.assert(IrcCommand.PING)
        item.params.longParam.assert("irc.example.com")
    }

//    :*.freenode.net 372 hellobotlongname :  #anonchan                - Chat anonymously.

    abstract fun `number command example test`()
    suspend fun `number command example`() {
        val testSubject = getTestSubject()

        val input = flowOf(":*.freenode.net 372 hellobotlongname :  #anonchan                - Chat anonymously.")
        val result = testSubject.mapToIrcCommand(input).toList()
        result.size.assert(1)
        val item: ParseResult = result[0]
        require(item is ParseResult.ParseSuccess)
        item.command.assert(IrcCommand.RPL_MOTD)
        item.params.list.assert(listOf("hellobotlongname"))
    }

    abstract fun `join example on test`(): Unit
    suspend fun `join example one`() {
        val testSubject = this.getTestSubject()
        val testInputFlow = flowOf("JOIN #ChannelTest")
        val result = testSubject.mapToIrcCommand(testInputFlow).toList()
        result.size.assert(1)
        assertTrue {
            result[0] is ParseResult.ParseSuccess
        }
    }

    abstract fun `join with prefix test`(): Unit
    suspend fun `join example with prefix`() {
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

    //    NICK MyBot
    abstract fun `nick example test`()
    suspend fun `nick exmaple`() {
        val testSubject: IrcLineParser = getTestSubject()
        val testInputFlow: Flow<String> = flowOf("NICK MyBot")
        val result: List<ParseResult> = testSubject.mapToIrcCommand(testInputFlow).toList()
        result.size.assert(1)
        val item = result[0]
        require(item is ParseResult.ParseSuccess)
        item.prefix.assert(null)
        item.command.assert(IrcCommand.NICK)

    }

    //    USER MyBot 0 * :Ronnie Reagan
    abstract fun `user example test`()
    suspend fun `user example`() {
        val testSubject: IrcLineParser = getTestSubject()
        val testInputFlow: Flow<String> = flowOf("USER MyBot 0 * :Ronnie Reagan")
        val result: List<ParseResult> = testSubject.mapToIrcCommand(testInputFlow).toList()
        result.size.assert(1)
        val item: ParseResult = result[0]
        require(item is ParseResult.ParseSuccess)
        item.prefix.assert(null)
        item.command.assert(IrcCommand.USER)
        item.params.list.assert(listOf("MyBot", "0", "*"))
        item.params.longParam.assert("Ronnie Reagan")
    }

}

