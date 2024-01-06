/*
 * Copyright 2024 Kyle McBurnett
 */

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

/*
 * Copyright 2024 Kyle McBurnett
 */

@Suppress("SuspendFunctionOnCoroutineScope")
suspend fun CoroutineScope.launchNow(block: suspend CoroutineScope.() -> Unit): Job {
    val job = launch {
        block()
    }
    yield()
    return job
}