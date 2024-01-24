/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.compose.channel

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled._3p
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

@Composable
fun PrivateChatTabView(
    channel: IOneOnOneViewInfo,
    selected: Boolean,
    image: @Composable BoxScope.() -> Unit = { Icon(Icons.Default._3p, "one on one chat") },
    onClick: () -> Unit
) {
    ListItem(colors = if (selected) ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    else ListItemDefaults.colors(),
        modifier = Modifier.selectable(selected = selected, onClick = onClick),
        headlineContent = { Text(channel.name) },
        supportingContent = {
            val time = channel.mostRecentActivity.collectAsState(Instant.DISTANT_PAST)
            val duration = System.now().minus(time.value)

            Text(duration.timeAgoString())
        },
        leadingContent = {
            Box(
                content = image,
                modifier = Modifier.background(color = Color.LightGray, shape = RoundedCornerShape(100)).padding(6.dp)
            )
        })
}

@OptIn(ExperimentalTime::class)
fun Duration.timeAgoString(): String {

    return when {
        inWholeDays > 0 -> "$inWholeDays days ago"
        inWholeHours > 0 -> "$inWholeHours hours ago"
        inWholeMinutes > 0 -> "$inWholeMinutes minutes ago"
        else -> "recent activity"
    }
}

private data class OneOnOne(
    override val name: String, override val mostRecentActivity: Flow<Instant> = flowOf(
        generateTimestamp()
    )
) : IOneOnOneViewInfo

@Preview
@Composable
private fun PrivateChatTabPreview() {
    PrivateChatTabView(OneOnOne("patrick"), true) {}
}

private fun generateTimestamp(): Instant {

    return System.now() - (0..9999).random().minutes
}

private fun main() = singleWindowApplication {
    PrivateChatTabPreview()
}