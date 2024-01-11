/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.simplebot

import com.xingpeds.kmirc.clientnetwork.Address
import com.xingpeds.kmirc.clientnetwork.Connect
import com.xingpeds.kmirc.clientnetwork.ConnectionResult
import com.xingpeds.kmirc.engine.IIrcClientEngine
import com.xingpeds.kmirc.entities.*
import com.xingpeds.kmirc.parser.IrcLineParser
import com.xingpeds.kmirc.simplebot.injection.Adapters
import com.xingpeds.kmirc.simplebot.injection.ImplementationModule.getConnectFun
import com.xingpeds.kmirc.simplebot.injection.ImplementationModule.getDNSLoopFun
import com.xingpeds.kmirc.simplebot.injection.ImplementationModule.getEngine
import com.xingpeds.kmirc.simplebot.injection.ImplementationModule.getParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

const val serverHostName = "chat.freenode.net"
const val serverPort = 6667

val dnsLookUp: (String) -> List<Address> = getDNSLoopFun()
val connect: Connect = getConnectFun()
val parser: IrcLineParser = getParser()
fun main(args: Array<String>): Unit = runBlocking {
    //attempt to connect
    val primaryScope = CoroutineScope(Dispatchers.Default)
    val addresses = dnsLookUp(serverHostName)
    when (val connectionAttempt = connect(addresses[0], serverPort)) {
        is ConnectionResult.Success -> {
            val connection = connectionAttempt.connection
            launch {
                connection.socketClosed.collect {
                    exitProcess(0)
                }
            }
//        val nickManager = Nick
            val inputFlow = Adapters.socketToEngineAdapter(primaryScope, connection.incoming)
            val engine: IIrcClientEngine = getEngine(
                inputFlow = inputFlow, // I need to get the marshaller and parser
                sendFun = { iIrcMessage: IIrcMessage ->
                    connection.write(iIrcMessage.toIRCString())
                },
                scope = primaryScope,
                wantedNick = IrcUser(
                    nick = "longnicknamebot",
                    username = "kmirc bot",
                    realName = "IRC bot made by kyle mcburnett"
                )
            )
            launch {
                engine.eventList.onEndOfMOTD.collect {
                    engine.send(IrcMessage(command = IrcCommand.JOIN, params = IrcParams("#kmirc")))
                }
            }
        }

        is ConnectionResult.Failure.UndefinedError -> throw Exception("connection failed")

        is ConnectionResult.Failure.ConnectionRefused -> {
            throw Exception("connection refused")
        }
    }
}


