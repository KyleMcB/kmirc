/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.simplebot

import com.xingpeds.kmirc.clientnetwork.Address
import com.xingpeds.kmirc.clientnetwork.Connect
import com.xingpeds.kmirc.clientnetwork.ConnectionResult
import com.xingpeds.kmirc.engine.IClientIrcEngine
import com.xingpeds.kmirc.entities.IIrcMessage
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
fun main(args: Array<String>) = runBlocking {
    //attempt to connect
    val addresses = dnsLookUp(serverHostName)
    val connectionAttempt = connect(addresses[0], serverPort)
    when (connectionAttempt) {
        is ConnectionResult.Success -> {
            val connection = connectionAttempt.connection
            launch {
                connection.socketClosed.collect {
                    exitProcess(0)
                }
            }
//        val nickManager = Nick
            val engine: IClientIrcEngine = getEngine(
                inputFlow = Adapters.socketToEngineAdapter(connection.incoming), // I need to get the marshaller and parser
                sendFun = { iIrcMessage: IIrcMessage ->
                    connection.write(iIrcMessage.toIRCString())
                },
                scope = CoroutineScope(Dispatchers.Default),
                processors = emptySet() //needs to get a nickstatemanager...
            )
        }

        is ConnectionResult.Failure.UndefinedError -> throw Exception("connection failed")

        is ConnectionResult.Failure.ConnectionRefused -> {
            throw Exception("connection refused")
        }
    }
}


