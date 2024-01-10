/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities

/**
 * Represents the source of an IRC message.
 */
sealed class IrcFrom : CharSequence {
    /**
     * Represents a user in an IRC message.
     *
     * @property user The username of the user.
     * @constructor Creates a User object with the given username.
     */
    data class User(val user: String) : IrcFrom(), CharSequence by user

    /**
     * Represents a server in an IRC message.
     *
     * This class extends the `IrcFrom` class and implements the `CharSequence` interface,
     * allowing it to be used as a character sequence and accessed using indexing operations.
     *
     * @property server The name of the server.
     * @constructor Creates a Server object with the given server name.
     */
    data class Server(val server: String) : IrcFrom(), CharSequence by server
    companion object {
        /**
         * Returns an instance of IrcFrom based on the provided IrcPrefix.
         *
         * @param ircPrefix The IrcPrefix object to convert to IrcFrom.
         * @return If the user and host fields in IrcPrefix are both null, returns an instance of IrcFrom.Server
         *         with the nickOrServer value as the server field. Otherwise, returns an instance of IrcFrom.User
         *         with the nickOrServer value as the user field.
         */
        fun fromPrefix(ircPrefix: IrcPrefix): IrcFrom = if (ircPrefix.user == null && ircPrefix.host == null) {
            IrcFrom.Server(
                ircPrefix.nickOrServer
            )
        } else {
            IrcFrom.User(user = ircPrefix.nickOrServer)
        }
    }
}