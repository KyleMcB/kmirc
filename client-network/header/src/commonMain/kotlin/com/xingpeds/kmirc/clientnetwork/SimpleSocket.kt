/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.clientnetwork

import kotlinx.coroutines.flow.SharedFlow
import java.io.Closeable

interface SimpleSocket : Closeable {

    suspend fun write(data: String)

    val incoming: SharedFlow<String>
    val socketClosed: SharedFlow<Boolean>

}

interface Address {
    val hostAddress: String
}

typealias Port = Int

/**
 * domain result type when trying to connect to another computer.
 * Basic success/failure
 * Success -> you have an open socket
 * Failure -> connection refused/ or something else went wrong
 */
sealed class ConnectionResult {
    data class Success(val connection: SimpleSocket) : ConnectionResult()
    sealed class Failure : ConnectionResult() {
        data object UndefinedError : Failure()
        data object ConnectionRefused : Failure()
    }
}

/**
 * attempt to connect to an address at a port
 */
fun interface Connect : suspend (Address, Port) -> ConnectionResult

/**
 *  turn a dns name into a usable address
 */
fun interface DNSLookupFun : (String) -> List<Address>
