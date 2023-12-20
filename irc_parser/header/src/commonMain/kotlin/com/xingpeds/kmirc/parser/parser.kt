/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.parser

import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IrcMessage
import kotlinx.coroutines.flow.Flow

sealed class ParseResult {
    data class ParseSuccess(private val message: IrcMessage) : ParseResult(), IIrcMessage by message
    data object InvalidIrcLine : ParseResult()
}

interface IrcLineParser {
    fun Flow<String>.mapIrcParse(): Flow<ParseResult>
    fun mapToIrcCommand(inputFlow: Flow<String>): Flow<ParseResult>
}



