/*
 * Copyright 2024 Kyle McBurnett
 */

import co.touchlab.kermit.Logger.Companion.e

/*
 * Copyright 2024 Kyle McBurnett
 */

/**
 * interface for kermit logging. experimental
 */
interface Logged {
    /**
     * tag for kermit logging
     */
    val tag: String

}

/**
 * basic error logging
 */
inline fun Logged.withErrorLogging(action: () -> Unit) {
    try {
        action()
    } catch (e: Throwable) {
        val trace = e.stackTrace.joinToString(separator = "\n") { stackTraceElement ->
            stackTraceElement.toString()
        }
        val typename: String = e.javaClass.name
        e(tag) {
            """
                wild caught exception
                type: $typename
                message: ${e.message}
                cause: ${e.cause}
            """.trimIndent()
        }
        e(tag) { "trace: $trace" }
        throw e
    }
}