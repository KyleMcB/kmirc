/*
 * Copyright 2024 Kyle McBurnett
 */

import com.xingpeds.kmirc.entities.*
import com.xingpeds.kmirc.entities.events.IIrcEvent
import com.xingpeds.kmirc.state.MutableClientState
import com.xingpeds.kmirc.state.NickStateMachine
import com.xingpeds.kmirc.state.SelfNickState
import com.xingpeds.kmirc.state.StateMessageProcessor
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.test.Test

/*
 * Copyright 2024 Kyle McBurnett
 */

class StateMessageProcessorTests {
    val testNick = "testNick"

    init {
        SelfNickState.selfNick.update { NickStateMachine.Accept(testNick) }
    }

    @Test
    fun setEquality() {
        val set = setOf(StateMessageProcessor, StateMessageProcessor)
        set.size.assert(1)
        StateMessageProcessor.equals(StateMessageProcessor).assert(true)
    }

    @Test
    fun privmsg() = runWaitingTest { finishTest ->
        StateMessageProcessor.process(IrcMessage(
            prefix = IrcPrefix("otherNick", host = "otherhost"),
            command = IrcCommand.PRIVMSG,
            params = IrcParams(testNick, longParam = "hello world")
        ), broadcast = {
            println(it)
        })
        backgroundScope.launch {

            MutableClientState.privmsgs.collect { messages: List<IIrcEvent.PRIVMSG> ->
                println(messages)
                messages.lastOrNull()?.from.assert(IrcFrom.User("otherNick"))
                finishTest()
            }
        }
    }
}