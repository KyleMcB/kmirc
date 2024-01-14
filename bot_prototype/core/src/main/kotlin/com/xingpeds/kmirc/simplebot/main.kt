/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.simplebot

import com.xingpeds.kmirc.Engine
import com.xingpeds.kmirc.engine.IIrcClientEngine
import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcMessage
import com.xingpeds.kmirc.entities.IrcParams
import com.xingpeds.kmirc.entities.IrcUser
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

const val serverHostName = "chat.freenode.net"
const val serverPort = 6667


/**
 *
 */
@Suppress("UNUSED_PARAMETER")
fun main(args: Array<String>): Unit = runBlocking {
    //attempt to connect
    val engine: IIrcClientEngine = Engine.connect(
        hostname = serverHostName,
        port = serverPort,
        ircUser = IrcUser(
            nick = "DogBot2",
            username = "simplebot",
            realName = "irc bot being developed in kotlin multiplatform"
        )
    ).filterIsInstance(Engine.EngineConnectResult.Success::class).first()



    launch {
        engine.eventList.onEndOfMOTD.collect {
            engine.send(IrcMessage(command = IrcCommand.JOIN, params = IrcParams("#kmirc")))
        }
    }
    launch {
        engine.eventList.onPRIVMSG.collect { priv ->
            if (priv.message.startsWith("sleep")) {
                engine.send(
                    IrcMessage(
                        command = IrcCommand.QUIT,
                        params = IrcParams(longParam = "farewell")
                    )
                )
                exitProcess(0)
            }
        }
    }
}