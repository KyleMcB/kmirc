/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities

interface IIrcMessage {
    val prefix: IrcPrefix?
    val command: IrcCommand
    val params: IrcParams
    fun toIRCString(): String
}

data class IrcMessage(
    override val prefix: IrcPrefix? = null,
    override val command: IrcCommand,
    override val params: IrcParams,
) : IIrcMessage {
    override fun toIRCString(): String {
        return if (prefix != null) {
            "${prefix.toIRCString()} $command ${params.toIRCString()}\r\n"
        } else {
            "$command ${params.toIRCString()}\r\n"
        }
    }
}

data class IrcPrefix(
    val nick: String,     // The nickname of the user
    val user: String?,    // The username of the user, optional
    val host: String?     // The host of the user, optional

) {
    fun toIRCString(): String {
        return if (user != null && host != null) {
            ":$nick!$user@$host"
        } else if (user != null) {
            ":$nick!$user"
        } else if (host != null) {
            ":$nick@$host"
        } else {
            ":$nick"
        }
    }
}

data class IrcParams(val list: List<String> = emptyList(), val longParam: String? = null) {
    fun toIRCString(): String {
        return if (longParam != null) {
            "${list.joinToString(" ")} :$longParam"
        } else {
            list.joinToString(" ")
        }
    }
}
