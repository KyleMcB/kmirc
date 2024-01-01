/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities

sealed class IrcFrom : CharSequence {
    data class User(val user: String) : IrcFrom(), CharSequence by user
    data class Server(val server: String) : IrcFrom(), CharSequence by server
}