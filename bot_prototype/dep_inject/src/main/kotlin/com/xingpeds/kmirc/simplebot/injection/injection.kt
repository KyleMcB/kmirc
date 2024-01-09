/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.simplebot.injection

import LogTag
import Logged
import Parser
import com.xingpeds.kmirc.clientnetwork.Connect
import com.xingpeds.kmirc.clientnetwork.DNSLookupFun
import com.xingpeds.kmirc.clientnetwork.KtorSocketFactory
import com.xingpeds.kmirc.engine.EventBroadcaster
import com.xingpeds.kmirc.engine.IBroadcaster
import com.xingpeds.kmirc.engine.NickStateManager
import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IIrcUser
import com.xingpeds.kmirc.parser.IrcLineParser
import com.xingpeds.kmirc.parser.ParseResult
import com.xingpeds.kmirc.parser.ParseResult.InvalidIrcLine
import com.xingpeds.kmirc.parser.marshallIrcPackets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import v


object ImplementationModule {


    fun getDNSLoopFun(): DNSLookupFun = DNSLookupFun()

    fun getConnectFun(): Connect {
        return KtorSocketFactory
    }

    fun getEngine(
        sendFun: suspend (IIrcMessage) -> Unit,
        inputFlow: Flow<IIrcMessage>,
        scope: CoroutineScope,
        wantedNick: IIrcUser
    ): IBroadcaster {
        val eventBroadcaster = EventBroadcaster(sendFun, inputFlow, scope)
        NickStateManager(
            wantedNick = wantedNick,
            send = sendFun,
            broadcast = eventBroadcaster::sendEvent,
            events = eventBroadcaster.events,
            messages = inputFlow
        )
        return eventBroadcaster
    }

    fun getParser(): IrcLineParser = Parser
}

object Adapters : Logged by LogTag("Adapters"){
    suspend fun socketToEngineAdapter(socketFlow: Flow<String>): Flow<IIrcMessage> {
        val marshalled = marshallIrcPackets(socketFlow)
        val parsed = Parser.mapToIrcCommand(marshalled)
        return flow {
            parsed.collect { parsed: ParseResult ->
                when (parsed) {
                    is InvalidIrcLine -> v("""
                        |ignoring parse failure
                        |${parsed}
                    """.trimMargin())
                    is ParseResult.ParseSuccess -> emit(parsed)
                }
            }
        }
//        emitAll(message.filterIsInstance<ParseResult.ParseSuccess>())
    }
}