/*
 * Copyright 2024 Kyle McBurnett
 */

import kotlinx.coroutines.flow.Flow
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