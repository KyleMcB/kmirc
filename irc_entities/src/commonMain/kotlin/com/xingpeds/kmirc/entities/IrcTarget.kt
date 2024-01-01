/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities

sealed class IrcTarget : CharSequence {
    data class User(val user: String) : IrcTarget(), CharSequence by user
    data class Channel(val channel: String) : IrcTarget(), CharSequence by channel
}