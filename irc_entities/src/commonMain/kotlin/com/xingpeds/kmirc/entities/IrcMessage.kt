/*
 * Copyright (c) Kyle McBurnett 2024.
 */

package com.xingpeds.kmirc.entities

import kotlinx.datetime.Instant

/**
 * basic elements of a serialized irc message
 */
interface IIrcMessage {
    val rawMessage: String?

    /**
     * who/server a message is from
     */
    val prefix: IrcPrefix?

    /**
     * what command is this irc message
     */
    val command: IrcCommand

    /**
     * all parameters after the command
     */
    val params: IrcParams

    /**
     * timestamp of message arrival
     */
    val timestamp: kotlinx.datetime.Instant

    /**
     * serialize back into an irc 1459 message
     */
    fun toIRCString(): String
}

/**
 * basic impl of [IIrcMessage]
 */
data class IrcMessage(
    override val prefix: IrcPrefix? = null,
    override val command: IrcCommand,
    override val params: IrcParams,
    override val rawMessage: String? = null,
    override val timestamp: Instant = kotlinx.datetime.Clock.System.now()
) : IIrcMessage {
    override fun toIRCString(): String {
        return if (prefix != null) {
            "${prefix.toIRCString()} $command ${params.toIRCString()}\r\n"
        } else {
            "$command ${params.toIRCString()}\r\n"
        }
    }
}

/**
 * who a message is from
 * @property nickOrServer this is the nickname or server name a message is from
 * @property user this is the username. Only populated when from a user, not used much in IRC
 * @property host this is the hostname of the user.
 */
data class IrcPrefix(
    val nickOrServer: String,     // The nickname of the user
    val user: String? = null,    // The username of the user, optional
    val host: String? = null     // The host of the user, optional

) {
    /**
     * serialize into IRC format
     */
    fun toIRCString(): String {
        return if (user != null && host != null) {
            ":$nickOrServer!$user@$host"
        } else if (user != null) {
            ":$nickOrServer!$user"
        } else if (host != null) {
            ":$nickOrServer@$host"
        } else {
            ":$nickOrServer"
        }
    }
}

/**
 * parameters on an irc message
 * @property list is the space seperated list of parameters
 * @property longParam this is the terminating parameter at the end of a message that starts with ":"
 */
data class IrcParams(val list: List<String> = emptyList(), val longParam: String? = null) {
    constructor(vararg params: String?, longParam: String? = null) : this(params.asList().filterNotNull(), longParam)

    /**
     * serialize back into IRC format
     */
    fun toIRCString(): String {
        return if (longParam != null) {
            if (list.isEmpty()) {
                ":$longParam"
            } else {
                val params = list.joinToString(" ").trim()
                "$params :$longParam"
            }
        } else {
            list.joinToString(" ")
        }
    }
}