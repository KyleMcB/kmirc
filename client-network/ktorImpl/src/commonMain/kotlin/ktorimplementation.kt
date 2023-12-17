package com.xingpeds.kmirc.clientnetwork

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.net.ConnectException

class SimpleSocketKtorAdapter(private val socket: Socket) : SimpleSocket {
    init {
        val readChannel = socket.openReadChannel()
        socket.launch {
            while (!readChannel.isClosedForRead) {
                val line: String = readChannel.readUTF8Line() ?: continue
                _incoming.emit(line)
            }
            close()
        }
    }

    override fun close() {

        socket.close()
        socket.cancel()
    }


    override suspend fun write(data: String) {
        val sendChannel = socket.openWriteChannel(autoFlush = true)
        sendChannel.writeStringUtf8(data)
    }

    private val _incoming = MutableSharedFlow<String>()
    override val incoming: SharedFlow<String> = _incoming
}


object KtorSocketFactory : Connect {
    override suspend fun invoke(address: Address, port: Port): ConnectionResult {
        val selectorManager = SelectorManager(Dispatchers.IO)
        return try {
            ConnectionResult.Success(
                SimpleSocketKtorAdapter(
                    aSocket(selectorManager).tcp().connect(address.hostAddress, port)
                )
            )

        } catch (e: ConnectException) {
            ConnectionResult.Failure.ConnectionRefused
        } catch (e: Throwable) {
            ConnectionResult.Failure.UndefinedError
        }
    }

}
