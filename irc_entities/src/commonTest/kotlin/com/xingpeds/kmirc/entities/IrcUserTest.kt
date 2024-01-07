/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities

import LogTag
import Logged
import io.kotest.property.checkAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class IrcUserTest : Logged by LogTag("IrcUserTest") {
    private val nick = "([a-zA-Z0-9\\-\\[\\]\\\\\\`\\^\\{\\}]+)" // Nicks can have a lot of special characters
    private val user = "([a-zA-Z0-9\\-]+)" // Assuming alphanumeric for user
    private val host = "([a-zA-Z0-9\\-\\.]+)" // Hosts could be alphanumeric & can include periods and hyphens

    private val servername = user // Assuming that server names are similar to user, update if required

    private val prefixPattern = "^(($servername)|($nick(!$user)?(@$host)?))\$".toRegex()


    @Test
    fun constructTest(): Unit = runTest {
        val job = CoroutineScope(Dispatchers.Unconfined).launch {

            checkAll<String, String?, String?, String?> { a, b, c, d ->
                IrcUser(a, b, c, d)
            }
        }
        job.join()
    }

    @Test
    fun validPrefixOutput(): Unit = runTest {
        val job = CoroutineScope(Dispatchers.Unconfined).launch {

            checkAll<String, String?, String?, String?> { a, b, c, d ->
                val output = IrcUser(a, b, c, d).toPrefix()
                val match: MatchResult? = prefixPattern.matchEntire(output)
                match != null
            }
        }
        job.join()
    }
}