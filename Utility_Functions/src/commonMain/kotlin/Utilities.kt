/*
 * Copyright 2024 Kyle McBurnett
 */

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/*
 * Copyright 2024 Kyle McBurnett
 */

/**
 * A flow collector that will launch each collection in a new coroutine.
 * This makes it so the collector does not block the next emission from the flow
 */
suspend fun <T> Flow<T>.collectAsync(collector: suspend (T) -> Unit) = coroutineScope {
    collect {
        launch { collector(it) }
    }
}

suspend fun <T, R> Flow<T>.mapAsync(transform: suspend (T) -> R): Flow<R> = flow {
    coroutineScope {
        collect {
            launch { emit(transform(it)) }
        }
    }
}