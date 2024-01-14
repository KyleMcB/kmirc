/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.parser

import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IrcMessage
import kotlinx.coroutines.flow.Flow

/**
 * Represents the result of parsing an IRC line.
 * This is a sealed class which acts as a base class for ParseSuccess and InvalidIrcLine classes.
 *
 * Its subclasses should either be object declarations or classes with 'private' primary constructors, thus ensuring all possible subclasses are defined within this file itself.
 */
sealed class ParseResult {
    /**
     * Represents a successful parsing result of an IRC line.
     * This class extends the ParseResult base class and implements the 'IIrcMessage' interface to provide the behavior of an IRC Message.
     *
     * @property message The parsed IRC message.
     */
    data class ParseSuccess(private val message: IrcMessage) : ParseResult(), IIrcMessage by message

    /**
     * Represents an invalid IRC line.
     * This class extends the ParseResult base class and implements the 'CharSequence' interface to provide the behavior of a character sequence.
     *
     * @property line The invalid IRC line.
     */
    class InvalidIrcLine(private val line: String) : ParseResult(), CharSequence by line {
        // Overrides the 'toString' function to return the invalid line it was initialized with.
        override fun toString(): String = line
    }
}

/**
 * Represents an interface for an IRC Line Parser.
 * This interface declares functions that are required for parsing IRC lines.
 */
interface IrcLineParser {
    /**
     * This function takes a Flow of strings as input.
     * It maps the input IRC lines to a Flow of ParseResults.
     * For each input line, the function tries to parse it into an IRC message and returns the result as a ParseResult.
     *
     * @param Flow<String> - the input Flow of IRC lines
     * @return Flow<ParseResult> - the output Flow of ParseResults
     */
    fun Flow<String>.mapIrcParse(): Flow<ParseResult>

    /**
     * This function takes a Flow of strings as input.
     * It maps the input IRC lines to a Flow of ParseResults, but converts the parsed lines into IRC commands.
     *
     * @param inputFlow - the input Flow of IRC lines
     * @return Flow<ParseResult> - the output Flow of ParseResults, representing IRC commands
     */
    fun mapToIrcCommand(inputFlow: Flow<String>): Flow<ParseResult>
}