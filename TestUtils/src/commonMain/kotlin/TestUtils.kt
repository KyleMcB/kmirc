/*
 * Copyright 2024 Kyle McBurnett
 */

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals


suspend fun assertFlowNeverEmits(flow: Flow<*>) {
    var hasValues = false
    flow.collect {
        hasValues = true
    }

    assert(!hasValues) { "Flow emitted some values" }
}

fun Any?.assert(expected: Any?) {
    assertEquals(actual = this, expected = expected)
}

fun runWaitingTest(block: suspend TestScope.(testComplete: () -> Unit) -> Unit) = runTest {
    val completed = CompletableDeferred<Boolean>()
    block({ completed.complete(true) })
    completed.await()
}