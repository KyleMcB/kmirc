/*
 * Copyright 2024 Kyle McBurnett
 */

import com.xingpeds.kmirc.entities.*
import com.xingpeds.kmirc.parser.IrcLineParser
import com.xingpeds.kmirc.parser.ParseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object Parser : IrcLineParser, Logged by LogTag("parser") {
    override fun Flow<String>.mapIrcParse(): Flow<ParseResult> = map { ircLine ->
        val (prefix, messageWithoutPrefix) = extractPrefix(ircLine)
        v("prefix = $prefix")
        v("messageWithoutPrefix = $messageWithoutPrefix")
        val (command, messageWithoutCommand) = extractCommand(messageWithoutPrefix)
        v("command = $command")
        v("messageWithoutCommand = $messageWithoutCommand")

        if (command == null) {
            ParseResult.InvalidIrcLine(ircLine)
        } else {
            val params = extractParameters(messageWithoutCommand)
            val value = IrcMessage(prefix, command, params)
            ParseResult.ParseSuccess(value)
        }
    }

    override fun mapToIrcCommand(inputFlow: Flow<String>): Flow<ParseResult> = inputFlow.mapIrcParse()

    private fun extractCommand(message: String): Pair<IrcCommand?, String> {
        val words = message.split(" ", limit = 2)
        val firstWord = words.first().uppercase()
        val remainingString: String = if (words.size > 1) words[1] else ""
        val command = commandLookup.get(firstWord)
        if (command == null) logError {
            "commandLookup for $firstWord failed"
        }

        return Pair(command, remainingString)
    }

    private fun extractPrefix(message: String): Pair<IrcPrefix?, String> {
        if (message.startsWith(":")) {
            val endOfPrefix = message.indexOf(" ")
            if (endOfPrefix != -1) {
                val prefixPart = message.substring(1, endOfPrefix)
                val parts = prefixPart.split("!", "@", limit = 3)
                val nick = parts.getOrElse(0) { "" }.let { if (it[0] == ':') it.substring(1) else it }
                val user = parts.elementAtOrNull(1)
                val host = parts.elementAtOrNull(2)
                val ircPrefix = IrcPrefix(nick, user, host)
                val remainder = message.substring(endOfPrefix).trimStart()
                return ircPrefix to remainder
            }
        }
        return null to message
    }

    private fun extractParameters(message: String): IrcParams {
        val params: List<String> = message.split(":", limit = 2)
        val longParam = if (params.size > 1) params[1] else null
        val paramList = params[0].trim().split(" ")

        return if (longParam != null) {
            IrcParams(paramList, longParam)
        } else {
            IrcParams(paramList)
        }
    }
}