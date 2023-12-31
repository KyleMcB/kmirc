/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities

interface IIrcChannel : {

    val name: String
}

fun isChannel(name: String): Boolean {
    return name.startsWith("#")
}