/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities

interface IIrcUser {
    val nick: String
    val username: String?
    val hostname: String?
    val realName: String?
    fun toPrefix(): String
}

//TODO this has conditional logic. This should not be in a header module
data class IrcUser(
    override val nick: String,
    override val username: String? = null,
    override val hostname: String? = null,
    override val realName: String? = null
) : IIrcUser {
    override fun toPrefix(): String {
        val builder = StringBuilder()
        builder.append(":$nick")
        username?.let { builder.append("!$it") }
        hostname?.let { builder.append("@$it") }
        return builder.toString()
    }
}

