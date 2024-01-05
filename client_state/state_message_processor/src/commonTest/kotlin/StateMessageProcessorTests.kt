/*
 * Copyright 2024 Kyle McBurnett
 */

import com.xingpeds.kmirc.entities.*
import com.xingpeds.kmirc.entities.events.IIrcEvent
import com.xingpeds.kmirc.state.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.fail

/*
 * Copyright 2024 Kyle McBurnett
 */

class StateMessageProcessorTests {
    val testNick = "testNick"
    val state = MutableClientState
    private suspend fun assertNoStateChange() {
        coroutineScope {
            state.mChannels.filterNot { it.isEmpty() }.onEach { fail("state changed: $it") }.launchIn(this)
            state.mNotices.filterNot { it.isEmpty() }.onEach { fail("state changed: $it") }.launchIn(this)
            state.mPrivmsgs.filterNot { it.isEmpty() }.onEach { fail("state changed: $it") }.launchIn(this)
        }
    }


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

            state.privmsgs.collect { messages: List<IIrcEvent.PRIVMSG> ->
                println(messages)
                messages.lastOrNull()?.from.assert(IrcFrom.User("otherNick"))
                finishTest()
            }
        }
    }

    @Test
    fun noStateChange() = runTest {
        backgroundScope.launch {
            assertNoStateChange()
        }

        StateMessageProcessor.process(
            message = IrcMessage(
                command = IrcCommand.PING,
                params = IrcParams(longParam = "irc.server.com")
            ),
            broadcast = {}
        )

    }

    @Test
    fun onOtherJoin() = runWaitingTest { completeTest ->
//        :WiZ JOIN #Twilight_zone        ; JOIN message from WiZ
        // pre-existing state. We are in channel #channelName
        val channelName = "#channelName"
        state.mChannels.emit(
            mapOf(
                channelName to MutableChannelState(channelName)
            )
        )
        //
        StateMessageProcessor.process(
            message = IrcMessage(
                prefix = IrcPrefix("otherNick", host = "otherhost"), command = IrcCommand.JOIN, params = IrcParams(
                    channelName
                )
            ),
            broadcast = { }
        )
        state.channels.filterNot { it.isEmpty() }.onEach { map: Map<ChannelName, ChannelState> ->
            println(map)
            map[channelName]?.members?.first()?.contains("otherNick").assert(true)
            completeTest()
        }.launchIn(backgroundScope)
    }
}