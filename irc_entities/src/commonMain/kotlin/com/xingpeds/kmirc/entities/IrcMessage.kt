package com.xingpeds.kmirc.entities

data class IrcMessage(
    val prefix: IrcPrefix?,
    val command: IrcCommand,
    val params: IrcParams,
    val longParam: String? = null
)

enum class IrcCommand {
    PRIVMSG,  // Private message between users
    NOTICE,   // Send notice to a user or a channel
    JOIN,     // Join a channel
    PART,     // Leave a channel
    TOPIC,    // Change or view the topic of a channel
    PING,     // Ping a server to test response
    PONG,     // Reply to a ping message
    NICK,     // Change nickname
    MODE,     // Change channel or user modes
    QUIT,     // Disconnect from the server
}

data class IrcPrefix(
    val nick: String,     // The nickname of the user
    val user: String?,    // The username of the user, optional
    val host: String?     // The host of the user, optional
)

data class IrcParams(val params: List<String>, val longParam: String? = null)
