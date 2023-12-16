package com.xingpeds.kmirc.clientnetwork

import kotlinx.coroutines.flow.SharedFlow
import java.io.Closeable

interface SimpleSocket : Closeable {

    suspend fun write(data: String)

    val incoming: SharedFlow<String>

}


interface Address {
    val hostAddress: String
}
typealias Port = Long

sealed class ConnectionResult {
    data class Success(val connection: SimpleSocket) : ConnectionResult()
    sealed class Failure : ConnectionResult() {
        data object UndefinedError : Failure()
    }
}

fun interface Connect : suspend (Address, Port) -> ConnectionResult

/**
 *  turn a dns name into a usable address
 */
fun interface DNSLookupFun : (String) -> List<Address>