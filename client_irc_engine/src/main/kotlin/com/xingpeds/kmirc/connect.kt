/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc

import com.xingpeds.kmirc.clientnetwork.*
import com.xingpeds.kmirc.engine.IIrcClientEngine
import com.xingpeds.kmirc.entities.IrcUser
import com.xingpeds.kmirc.parser.IrcLineParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed interface ConnectResult {
    data class Success(val engine: IIrcClientEngine) : ConnectResult, IIrcClientEngine by engine
    data class Failure(val attempt: Int) : ConnectResult,
}

fun connect(
    hostname: String,
    port: Port,
    ircUser: IrcUser,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
): Flow<ConnectResult> = flow {
    val dnsLookUp: (String) -> List<Address> = getDNSLookupFun()
    val connect: Connect = getConnectFun()
    val parser: IrcLineParser = getParser()
}