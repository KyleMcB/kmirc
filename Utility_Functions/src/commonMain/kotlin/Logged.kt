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
 * @param tag this is the tag used in the logs
 */
interface Logged {
    /**
     * tag for kermit logging
     */
    val tag: String

}

/**
 *
 */
class LogTag(override val tag: String) : Logged {
    init {
        v("constructed")
    }

    /**
     * log on destroy
     */
    fun finalize(): Unit = v("garbage collected")

}

/**
 * Log an error with tag auto included
 */
inline fun Logged.logError(throwable: Throwable? = null, message: () -> String): Unit =
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
inline fun <T> Logged.withErrorLogging(action: () -> T): T {
    return try {
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