/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class IrcEngine(
    val send: suspend (IIrcMessage) -> Unit,
    input: Flow<IIrcMessage>,
    private val processors: List<MessageProcessor> = emptyList(),
    private val engineScope: CoroutineScope,
) : IClientIrcEngine {
    private val mEvents =
        MutableSharedFlow<IIrcEvent>(replay = 100, extraBufferCapacity = 100, onBufferOverflow = BufferOverflow.SUSPEND)

//    private val inputFlow = input.shareIn(engineScope, SharingStarted.Eagerly, replay = 100)


    val eventList: EventList = EventListImpl

    override val events: SharedFlow<IIrcEvent>
        get() = mEvents


    init {
        input.onEach { message ->
            val event = try {
                println("Attempting to convert message to event; $message") // Debug statement
                messageToEvent(message)
            } catch (e: Throwable) {
                null
            }
            if (event != null) {
                println("Event successfully created") // Debug statement
                processors.forEach { processor ->
                    processor.process(message) { event ->
                        println("Processing event") // Debug statement
                        engineScope.launch {
                            mEvents.emit(event)
                        }
                    }
                }
                println("Emitting event") // Debug statement
                mEvents.emit(event)
            }
        }.launchIn(engineScope)

        startEventBroadcaster()

        engineScope.launch {
            mEvents.emit(IIrcEvent.INIT)
        }
    }

    internal fun startEventBroadcaster() = engineScope.launch {
        events.collect { event ->

            println("Starting to broadcast event $event") // Debug statement

            when (event) {
                IIrcEvent.INIT -> {
                    EventListImpl.mINIT.emit(event as IIrcEvent.INIT)
                }

                is IIrcEvent.PING -> {
                    EventListImpl.mPing.emit(event)
                }

                is IIrcEvent.Notice -> {
                    EventListImpl.mNotice.emit(event)
                }

                is IIrcEvent.PRIVMSG -> {
                    println("Received PRIVMSG. Broadcasting...") // Debug statement
                    EventListImpl.mPRIVMSG.emit(event)
                }

                else -> TODO("add single event broadcaster for $event")
            }

            println("Finished broadcasting event") // Debug statement

        }
    }


    init {
        eventList.onPING.onEach { ping: IIrcEvent.PING ->
            send(IrcMessage(command = IrcCommand.PONG, params = ping.ircParams))
        }.launchIn(engineScope)

        eventList.onPRIVMSG.onEach { msg: IIrcEvent.PRIVMSG ->
            println("Caught a PRIVMSG event: ${msg.toString()}") // Debug statement
        }.launchIn(engineScope)

    }
}