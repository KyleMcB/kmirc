package com.xingpeds.kmirc.parser

import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcMessage
import com.xingpeds.kmirc.entities.IrcParams
import com.xingpeds.kmirc.entities.IrcPrefix
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


interface IrcPacketMarshaller : (Flow<String>) -> Flow<String>

fun IrcPacketMarshaller() = IrcPacketMarshallerImpl()
class IrcPacketMarshallerImpl() : IrcPacketMarshaller {
    override fun invoke(input: Flow<String>): Flow<String> = flow {
        val buffer = StringBuilder()
        input.collect { line ->
            buffer.append(line)
            // is "\r\n" in the buffer?
            var index = buffer.indexOf("\r\n")
            while (index > -1) {
                val split = buffer.substring(0, index + 2)
                emit(split)
                buffer.delete(0, index + 2)
                index = buffer.indexOf("\r\n")
            }
        }
    }
}

interface IrcParser : (String) -> Flow<IrcMessage>

fun IrcParser() = IrcParserImpl()
class IrcParserImpl : IrcParser {
    override fun invoke(line: String): Flow<IrcMessage> = flow {
        val splitMessage = line.split("\r\n")
        for (message in splitMessage) {
            val (prefix, messageWithoutPrefix) = extractPrefix(message)
            val (command, messageWithoutCommand) = extractCommand(messageWithoutPrefix)
            if (command == null) {
                continue
            }
            val params = extractParameters(messageWithoutCommand)

            val value = IrcMessage(prefix, command, params)
            println(value)
            emit(value)
        }
    }

    private fun extractParameters(message: String): IrcParams {
        val params: List<String> = message.split(":", limit = 2)
        val longParam = if (params.size > 1) params[1] else null
        val paramList = params[0].trim().split(" ")
        return if (longParam != null) {
            println("$paramList :$longParam")
            IrcParams(paramList, longParam)
        } else {
            println(paramList)
            IrcParams(paramList)
        }
    }

    private fun extractCommand(message: String): Pair<IrcCommand?, String> {
        val words = message.split(" ", limit = 2)
        val firstWord = words.first().uppercase()
        val remainingString: String = if (words.size > 1) words[1] else ""

        val command = try {
            IrcCommand.valueOf(firstWord)
        } catch (e: IllegalArgumentException) {
            null // Return null if the word does not match any command
        }

        val pair = Pair(command, remainingString)
        println("command $command")
        return pair
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
                println("ircPrefix $ircPrefix")

                val remainder = message.substring(endOfPrefix).trimStart()
                println("remainder ($remainder)")
                return ircPrefix to remainder
            }
        }
        return null to message
    }
}