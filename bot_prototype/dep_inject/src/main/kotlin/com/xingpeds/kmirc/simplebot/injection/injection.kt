/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.simplebot.injection

import Parser
import com.xingpeds.kmirc.clientnetwork.Connect
import com.xingpeds.kmirc.clientnetwork.DNSLookupFun
import com.xingpeds.kmirc.clientnetwork.KtorSocketFactory
import com.xingpeds.kmirc.engine.IClientIrcEngine
import com.xingpeds.kmirc.engine.IrcEngine
import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IIrcUser
import com.xingpeds.kmirc.parser.IrcLineParser
import com.xingpeds.kmirc.parser.ParseResult
import com.xingpeds.kmirc.parser.marshallIrcPackets
import com.xingpeds.kmirc.state.MutableClientState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow


object ImplementationModule {


    fun getDNSLoopFun(): DNSLookupFun = DNSLookupFun()

    fun getConnectFun(): Connect {
        return KtorSocketFactory
    }

    fun getEngine(
        wantedNick: IIrcUser,
        sendFun: suspend (IIrcMessage) -> Unit,
        mState: MutableClientState,
        inputFlow: Flow<IIrcMessage>,
        scope: CoroutineScope
    ): IClientIrcEngine =
        IrcEngine(wantedNick, sendFun, inputFlow, scope, mState)

    fun getParser(): IrcLineParser = Parser
}

object Adapters {
    fun socketToEngineAdapter(socketFlow: Flow<String>): Flow<IIrcMessage> = flow {
        val message: Flow<ParseResult> = Parser.mapToIrcCommand(marshallIrcPackets(socketFlow))
        emitAll(message.filterIsInstance<ParseResult.ParseSuccess>())
    }
}