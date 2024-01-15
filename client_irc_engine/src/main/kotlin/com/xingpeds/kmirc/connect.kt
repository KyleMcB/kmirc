/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc

import LogTag
import Logged
import com.xingpeds.kmirc.clientnetwork.*
import com.xingpeds.kmirc.engine.EventBroadcaster
import com.xingpeds.kmirc.engine.IBroadcaster
import com.xingpeds.kmirc.engine.IIrcClientEngine
import com.xingpeds.kmirc.engine.NickStateManager
import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IrcMessage
import com.xingpeds.kmirc.entities.IrcUser
import com.xingpeds.kmirc.events.EventList
import com.xingpeds.kmirc.parser.ParseResult
import com.xingpeds.kmirc.parser.Parser.mapIrcParse
import com.xingpeds.kmirc.parser.marshallIrcPackets
import com.xingpeds.kmirc.state.ClientState
import com.xingpeds.kmirc.state.startEventProcessing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import logError
import v
import withErrorLogging


/**
 * Singleton object wraps the operations related to the IIrcClientEngine
 */
object Engine : Logged by LogTag("public Engine") {
    /**
     * simple success or fail return type for the Engine's connection method
     */
    sealed interface EngineConnectResult {
        /**
         * The Success class represents a successful connection to the IRC server.
         *
         * @property engine The instance of IIrcClientEngine representing the connected engine.
         */
        data class Success(val engine: IIrcClientEngine) : EngineConnectResult, IIrcClientEngine by engine

        /**
         * Represents a failure in the connection process.
         * @property address IP address that failed to connect.
         * @property port Port that failed in connection process.
         */
        data class Failure(val address: Address, val port: Port) : EngineConnectResult
    }

    /**
     * Connect to the IRC server.
     * @param hostname hostname of the server.
     * @param port port of the server.
     * @param ircUser IrcUser instance representing the user.
     * @param scope CoroutineScope in which the function is processed.
     * @return Flow of EngineConnectResult representing the process of connection.
     */
    fun connect(
        hostname: String,
        port: Port,
        ircUser: IrcUser,
        scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    ): Flow<EngineConnectResult> = flow {
        withErrorLogging {
            val dnsLookUp: (String) -> List<Address> = getDNSLookupFun()
            val connect: Connect = getConnectFun()
            val addresses = dnsLookUp(hostname)
            for (address in addresses) {
                when (val connectionResult = connect(address, port)) {
                    is ConnectionResult.Success -> {
                        val socket = connectionResult.connection
                        v(
                            """TCP connection successful: 
                            |hostname = $hostname
                            |address = $address
                            |port = $port""".trimMargin()
                        )
                        emit(
                            EngineConnectResult.Success(
                                assembleEngine(
                                    hostname, socket, ircUser, engineScope = scope
                                )
                            )
                        )
                        return@flow
                    }

                    is ConnectionResult.Failure -> {
                        logError {
                            """
                                TCP connection failed:
                                hostname = $hostname
                                address = $address
                                port = $port 
                            """.trimIndent()
                        }
                        emit(EngineConnectResult.Failure(address, port))
                    }
                }
            }
        }
    }

    /**
     * Assembles an instance of IIrcClientEngine using the provided parameters.
     *
     * @param hostname The hostname of the IRC server.
     * @param socket The SimpleSocket used for communication with the IRC server.
     * @param ircUser The IrcUser representing the current user.
     * @param engineScope CoroutineScope in which the function is processed.
     * @return An instance of IIrcClientEngine.
     */
    fun assembleEngine(
        hostname: String,
        socket: SimpleSocket,
        ircUser: IrcUser,
        engineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    ): IIrcClientEngine {
        val ircMessageFlow = marshallIrcPackets(socket.incoming).mapIrcParse().map { result: ParseResult ->
            when (result) {
                is ParseResult.ParseSuccess -> result
                is ParseResult.InvalidIrcLine -> {

                    null
                }
            }
        }.filterNotNull().shareIn(engineScope, started = SharingStarted.Lazily)
        val sendFun: suspend (IIrcMessage) -> Unit = { iIrcMessage: IIrcMessage ->
            socket.write(iIrcMessage.toIRCString())
        }
        val eventBroadcaster = EventBroadcaster(
            send = sendFun,
            input = ircMessageFlow,
            engineScope = engineScope
        )
        NickStateManager(
            wantedNick = ircUser,
            send = sendFun,
            broadcast = eventBroadcaster::sendEvent,
            events = eventBroadcaster.events,
            messages = ircMessageFlow
        )
        startEventProcessing()
        return object : IIrcClientEngine {
            override val broadcaster: IBroadcaster
                get() = eventBroadcaster
            override val serverHostName: String = hostname
            override val eventList: EventList
                get() = EventList()
            override val state: ClientState
                get() = ClientState()

            override suspend fun send(ircMessage: IrcMessage) = sendFun(ircMessage)

        }
    }
}