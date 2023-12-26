/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.IIrcEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface EventList {
    val onPING: SharedFlow<IIrcEvent.PING>
    val onJOIN: SharedFlow<IIrcEvent.JOIN>
    val onPART: SharedFlow<IIrcEvent.PART>
    val onChannelMessage: SharedFlow<IIrcEvent.ChannelMessage>
    val onPrivateMessage: SharedFlow<IIrcEvent.PrivateMessage>
    val onQuit: SharedFlow<IIrcEvent.Quit>
    val onNickChange: SharedFlow<IIrcEvent.NickChange>
    val onUserModeChange: SharedFlow<IIrcEvent.UserModeChange>
    val onChannelModeChange: SharedFlow<IIrcEvent.ChannelModeChange>
    val onTopicChange: SharedFlow<IIrcEvent.TopicChange>
    val onChannelNotice: SharedFlow<IIrcEvent.ChannelNotice>
    val onINIT: SharedFlow<IIrcEvent.INIT>
}

internal object EventListImpl : EventList {
    val mPing = MutableSharedFlow<IIrcEvent.PING>(replay = 2)
    val mJoin = MutableSharedFlow<IIrcEvent.JOIN>(replay = 2)
    val mPart = MutableSharedFlow<IIrcEvent.PART>(replay = 2)
    val mChannelMessage = MutableSharedFlow<IIrcEvent.ChannelMessage>(replay = 2)
    val mPrivateMessage = MutableSharedFlow<IIrcEvent.PrivateMessage>(replay = 2)
    val mQuit = MutableSharedFlow<IIrcEvent.Quit>(replay = 2)
    val mNickChange = MutableSharedFlow<IIrcEvent.NickChange>(replay = 2)
    val mUserModeChange = MutableSharedFlow<IIrcEvent.UserModeChange>(replay = 2)
    val mChannelModeChange = MutableSharedFlow<IIrcEvent.ChannelModeChange>(replay = 2)
    val mTopicChange = MutableSharedFlow<IIrcEvent.TopicChange>(replay = 2)
    val mChannelNotice = MutableSharedFlow<IIrcEvent.ChannelNotice>(replay = 2)

    override val onPING: SharedFlow<IIrcEvent.PING>
        get() = mPing
    override val onJOIN: SharedFlow<IIrcEvent.JOIN>
        get() = mJoin
    override val onPART: SharedFlow<IIrcEvent.PART>
        get() = mPart
    override val onChannelMessage: SharedFlow<IIrcEvent.ChannelMessage>
        get() = mChannelMessage
    override val onPrivateMessage: SharedFlow<IIrcEvent.PrivateMessage>
        get() = mPrivateMessage
    override val onQuit: SharedFlow<IIrcEvent.Quit>
        get() = mQuit
    override val onNickChange: SharedFlow<IIrcEvent.NickChange>
        get() = mNickChange
    override val onUserModeChange: SharedFlow<IIrcEvent.UserModeChange>
        get() = mUserModeChange
    override val onChannelModeChange: SharedFlow<IIrcEvent.ChannelModeChange>
        get() = mChannelModeChange
    override val onTopicChange: SharedFlow<IIrcEvent.TopicChange>
        get() = mTopicChange
    override val onChannelNotice: SharedFlow<IIrcEvent.ChannelNotice>
        get() = mChannelNotice
    val mINIT = MutableSharedFlow<IIrcEvent.INIT>(1)
    override val onINIT: SharedFlow<IIrcEvent.INIT>
        get() = mINIT
}