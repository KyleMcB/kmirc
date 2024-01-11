/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.simplebot.injection

import LogTag
import Logged
import Parser
import com.xingpeds.kmirc.clientnetwork.Connect
import com.xingpeds.kmirc.clientnetwork.DNSLookupFun
import com.xingpeds.kmirc.clientnetwork.KtorSocketFactory
import com.xingpeds.kmirc.engine.EventBroadcaster
import com.xingpeds.kmirc.engine.IIrcClientEngine
import com.xingpeds.kmirc.engine.NickStateManager
import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IIrcUser
import com.xingpeds.kmirc.entities.IrcMessage
import com.xingpeds.kmirc.events.EventList
import com.xingpeds.kmirc.parser.IrcLineParser
import com.xingpeds.kmirc.parser.ParseResult
import com.xingpeds.kmirc.parser.ParseResult.InvalidIrcLine
import com.xingpeds.kmirc.parser.marshallIrcPackets
import com.xingpeds.kmirc.state.ClientState
import com.xingpeds.kmirc.state.getClientState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import v


object ImplementationModule {


    fun getDNSLoopFun(): DNSLookupFun = DNSLookupFun()

    fun getConnectFun(): Connect {
        return KtorSocketFactory
    }

    fun getEngine(
        sendFun: suspend (IIrcMessage) -> Unit,
        inputFlow: SharedFlow<IIrcMessage>,
        scope: CoroutineScope,
        wantedNick: IIrcUser
    ): IIrcClientEngine {
        val eventBroadcaster = EventBroadcaster(sendFun, inputFlow, scope)
        NickStateManager(
            wantedNick = wantedNick,
            send = sendFun,
            broadcast = eventBroadcaster::sendEvent,
            events = eventBroadcaster.events,
            messages = inputFlow
        )
        return object : IIrcClientEngine {
            override val eventList: EventList
                get() = EventList()
            override val state: ClientState
                get() = getClientState()

            override suspend fun send(ircMessage: IrcMessage) = sendFun(ircMessage)

        }
    }

    fun getParser(): IrcLineParser = Parser
}

object Adapters : Logged by LogTag("Adapters") {
    fun socketToEngineAdapter(scope: CoroutineScope, socketFlow: SharedFlow<String>): SharedFlow<IIrcMessage> {
        val marshalled = marshallIrcPackets(socketFlow)
        val parsed = Parser.mapToIrcCommand(marshalled)
        return flow {
            parsed.collect { parsed: ParseResult ->
                when (parsed) {
                    is InvalidIrcLine -> v(
                        """
                        |ignoring parse failure
                        |${parsed}
                    """.trimMargin()
                    )

                    is ParseResult.ParseSuccess -> emit(parsed)
                }
            }
        }.shareIn(scope, SharingStarted.Lazily)
//        emitAll(message.filterIsInstance<ParseResult.ParseSuccess>())
    }
}