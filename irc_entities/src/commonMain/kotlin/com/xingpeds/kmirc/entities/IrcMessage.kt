package com.xingpeds.kmirc.entities

data class IrcMessage(
    val prefix: IrcPrefix? = null,
    val command: IrcCommand,
    val params: IrcParams,
) {
    override fun toString(): String {
        return if (prefix != null) {
            "$prefix $command $params\r\n"
        } else {
            "$command $params\r\n"
        }
    }
}

data class IrcPrefix(
    val nick: String,     // The nickname of the user
    val user: String?,    // The username of the user, optional
    val host: String?     // The host of the user, optional

) {
    override fun toString(): String {
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

data class IrcParams(val params: List<String>, val longParam: String? = null) {
    override fun toString(): String {
        return if (longParam != null) {
            "${params.joinToString(" ")} :$longParam"
        } else {
            params.joinToString(" ")
        }
    }
}
