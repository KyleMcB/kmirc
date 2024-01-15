/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.state.processing

import com.xingpeds.kmirc.entities.events.JOIN
import com.xingpeds.kmirc.state.ChannelName
import com.xingpeds.kmirc.state.MutableChannelState
import com.xingpeds.kmirc.state.MutableClientState
import com.xingpeds.kmirc.state.MutableNickState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.*
import logError
import v
import withErrorLogging
import kotlin.time.Duration.Companion.seconds

@OptIn(FlowPreview::class)
internal suspend fun StateEventProcessor.processJoinEvent(event: JOIN) = withErrorLogging {
    v("handling join event: $event")
    //when join
    //could be a self join or another nick join
    if (MutableNickState.isNickMe(event.nick)) {
        v("self join event")
        //self join
        // add a new channel to state
        val newChannel = MutableChannelState(event.channel)
        MutableClientState.mChannels.update { channelMap: Map<ChannelName, MutableChannelState> ->
            val new = channelMap + (event.channel to newChannel)
            v("new channel list: ${new.keys}")
            new
        }
    } else {
        v("other join event")
        //other join
        // add a nick to a channel's state
        // This could be tricky because channel state might be in flight.

        val channelState: MutableChannelState? =
            MutableClientState.mChannels
                .filter { map ->
                    map.containsKey(event.channel)
                }
                .timeout(4.seconds)
                .catch { e ->
                    if (e is TimeoutCancellationException) {
                        logError {
                            "looking for ${event.channel} timed out while trying to add ${event.nick} to channel roster"
                        }
                    }
                }.firstOrNull()?.get(event.channel)
        if (channelState == null) {
            logError {
                "recevied join event for another nick. ${event.channel}. In channel. ${event.channel}. However, state for that chanel can not be found"
            }
        }

        channelState?.mMembers?.update { members: Set<String> ->
            members + event.nick
        }
    }
}
