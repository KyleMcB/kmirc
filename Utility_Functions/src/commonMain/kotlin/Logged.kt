/*
 * Copyright 2024 Kyle McBurnett
 */

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Logger.Companion.e
import co.touchlab.kermit.Severity

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
 * Log an error with tag auto included
 */
inline fun Logged.e(throwable: Throwable? = null, message: () -> String): Unit =
    Logger.log(severity = Severity.Error, tag = this.tag, throwable = throwable, message = message())

/**
 * Log a debug statement with tag auto included
 */
fun Logged.d(message: String): Unit =
    Logger.log(severity = Severity.Debug, tag = this.tag, throwable = null, message = message)

/**
 * log a verbose message with tag auto included
 */
fun Logged.v(message: String): Unit =
    Logger.log(severity = Severity.Verbose, tag = this.tag, throwable = null, message = message)

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