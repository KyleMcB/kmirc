/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities

fun prefixToFrom(prefix: IrcPrefix): IrcFrom {
    // this condition is getting an exception in the header module for now.
    // this definition of server vs nick is my own arbitrary choice
    return if (prefix.host == null && prefix.user == null) {
        IrcFrom.Server(prefix.nickOrServer)
    } else {
        IrcFrom.User(prefix.nickOrServer)
    }
}