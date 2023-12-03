package com.xingpeds.kmirc.parser

import com.xingpeds.kmirc.entities.IrcMessage

fun encodeMessage(message: IrcMessage): String {
    val prefix = message.prefix
    val command = message.command.toString()
    val params = message.params

    return if (prefix != null)
        "$prefix"
    else
        ""
}