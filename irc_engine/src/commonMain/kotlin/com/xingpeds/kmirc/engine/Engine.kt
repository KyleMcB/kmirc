package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcMessage
import com.xingpeds.kmirc.entities.IrcParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Engine(
    val input: Flow<IrcMessage>,
    val output: (IrcMessage) -> Unit,
    val state: IrcClientState = IrcClientState(),
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    init {
        scope.launch {

            input.collect { message ->
                println("Engine received message: $message")
                state.ircCommands.update { it + message }
                if (message.command == IrcCommand.PING) {
                    output(
                        IrcMessage(
                            command = IrcCommand.PONG,
                            params = IrcParams(emptyList(), message.params.longParam)
                        )
                    )
                }

            }
        }
    }
}