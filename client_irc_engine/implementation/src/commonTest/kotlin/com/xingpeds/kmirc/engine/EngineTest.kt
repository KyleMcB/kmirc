/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.IrcUser
import com.xingpeds.kmirc.state.MutableClientState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test

class EngineTest {
    @Test
    fun prototyping() = runTest {
        val subject = IrcEngine(
            wantedNick = IrcUser(
                "TestNick", username = "TestUser", hostname = "testHostname", realName = "test realname"
            ), send = {
                println(it)
                println(it.toIRCString())
            }, MutableClientState(), inputFlow = emptyFlow(), this
        )
    }


    suspend fun <T> Flow<T>.collectAsync(collector: suspend (T) -> Unit) = coroutineScope {
        collect {
            launch { collector(it) }
        }
    }


}
