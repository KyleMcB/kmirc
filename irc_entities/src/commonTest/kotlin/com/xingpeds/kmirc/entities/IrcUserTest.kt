/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities

import io.kotest.property.checkAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class IrcUserTest {
    val nick = "([a-zA-Z0-9\\-\\[\\]\\\\\\`\\^\\{\\}]+)" // Nicks can have a lot of special characters
    val user = "([a-zA-Z0-9\\-]+)" // Assuming alphanumeric for user
    val host = "([a-zA-Z0-9\\-\\.]+)" // Hosts could be alphanumeric & can include periods and hyphens

    val servername = user // Assuming that server names are similar to user, update if required

    val prefixPattern = "^(($servername)|($nick(!$user)?(@$host)?))\$".toRegex()

    @Test
    fun constructTest() = runTest {
        val job = CoroutineScope(Dispatchers.Unconfined).launch {

            checkAll<String, String?, String?, String?> { a, b, c, d ->
                IrcUser(a, b, c, d)
            }
        }
        job.join()
    }

    @Test
    fun validPrefixOutput() = runTest {
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