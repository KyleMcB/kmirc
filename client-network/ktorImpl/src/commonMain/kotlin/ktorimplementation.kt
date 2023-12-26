/*
 * Copyright 2024 Kyle McBurnett
 */

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
import java.nio.ByteBuffer

class SimpleSocketKtorAdapter(private val socket: Socket) : SimpleSocket {
    private val sendChannel = socket.openWriteChannel(autoFlush = true)

    // New SharedFlow for socket closed signal
    private val _socketClosed = MutableSharedFlow<Boolean>()
    override val socketClosed: SharedFlow<Boolean> = _socketClosed

    init {
        val readChannel = socket.openReadChannel()
        socket.launch {
            while (!readChannel.isClosedForRead) {
                readChannel.read(5) { byteBuffer: ByteBuffer ->
                    val bytes = ByteArray(byteBuffer.remaining())
                    byteBuffer.get(bytes)
                    launch {
                        val string: String = bytes.decodeToString()
                        _incoming.emit(string)
                    }
                }
            }
            _socketClosed.emit(true)
            close()
        }
    }

    override fun close() {
        socket.close()
        socket.cancel()

    }

    override suspend fun write(data: String) {
        println("[socket] out: $data")
        sendChannel.writeStringUtf8(data)
        sendChannel.flush()
    }

    private val _incoming = MutableSharedFlow<String>(replay = 1000)
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