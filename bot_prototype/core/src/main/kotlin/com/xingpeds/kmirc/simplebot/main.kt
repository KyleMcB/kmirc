/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.simplebot

import com.xingpeds.kmirc.clientnetwork.Address
import com.xingpeds.kmirc.clientnetwork.Connect
import com.xingpeds.kmirc.clientnetwork.ConnectionResult
import com.xingpeds.kmirc.engine.IClientIrcEngine
import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IrcUser
import com.xingpeds.kmirc.parser.IrcLineParser
import com.xingpeds.kmirc.parser.ParseResult
import com.xingpeds.kmirc.parser.marshallIrcPackets
import com.xingpeds.kmirc.simplebot.injection.getConnectFun
import com.xingpeds.kmirc.simplebot.injection.getDNSLoopFun
import com.xingpeds.kmirc.simplebot.injection.getEngine
import com.xingpeds.kmirc.simplebot.injection.getParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val serverHostName = "chat.freenode.net"
const val serverPort = 6667

val dnsLookUp: (String) -> List<Address> = getDNSLoopFun()
val connect: Connect = getConnectFun()
val parser: IrcLineParser = getParser()
fun main(args: Array<String>) = runBlocking {
    //attempt to connect
    val addresses = dnsLookUp(serverHostName)
    println(addresses)
    val connectionAttempt = connect(addresses[0], serverPort)
    when (connectionAttempt) {
        is ConnectionResult.Success -> {
            val connection = connectionAttempt.connection
            launch {
                connection.incoming.collect { message ->
                    println(message)
                }
            }
            val inputAdapter: Flow<IIrcMessage> = flow {
                val message: Flow<ParseResult> = parser.mapToIrcCommand(marshallIrcPackets(connection.incoming))
                emitAll(message.filterIsInstance<ParseResult.ParseSuccess>())
            }
            val engine: IClientIrcEngine = getEngine(
                wantedNick = IrcUser(nick = "hellobotlongname", "kotlinBot", "*", "kotlin irc bot by Kyle McBurnett"),
                inputFlow = inputAdapter, // I need to get the marshaller and parser
                sendFun = { iIrcMessage: IIrcMessage ->
                    connection.write(iIrcMessage.toIRCString())
                },
                mState = com.xingpeds.kmirc.state.MutableClientState(),
                scope = CoroutineScope(Dispatchers.IO)
            )
        }

        is ConnectionResult.Failure.UndefinedError -> throw Exception("connection failed")

        is ConnectionResult.Failure.ConnectionRefused -> {
            throw Exception("connection refused")
        }
    }
}


