/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import LogTag
import Logged
import com.xingpeds.kmirc.entities.IIrcMessage
import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.events.*
import logError
import withErrorLogging

/**
 * this object hosts the message to event fun. Being in an object gives it logging
 */
object Converter : Logged by LogTag("messageToEvent") {
    /**
     * convert an IRC message into a client event
     */
    fun messageToEvent(message: IIrcMessage): IIrcEvent = withErrorLogging {
        return when (message.command) {
            IrcCommand.PASS -> notYetImpl(message.command)
            IrcCommand.NICK -> notYetImpl(message.command)
            IrcCommand.USER -> notYetImpl(message.command)
            IrcCommand.SERVER -> notYetImpl(message.command)
            IrcCommand.OPER -> notYetImpl(message.command)
            IrcCommand.QUIT -> UserQuit(message)
            IrcCommand.SQUIT -> notYetImpl(message.command)
            IrcCommand.JOIN -> JOIN(message)
            IrcCommand.PART -> PART(message)
            IrcCommand.MODE -> MODE(message)
            IrcCommand.TOPIC -> TOPIC(message)
            IrcCommand.NAMES -> notYetImpl(message.command)
            IrcCommand.LIST -> notYetImpl(message.command)
            IrcCommand.INVITE -> notYetImpl(message.command)
            IrcCommand.KICK -> notYetImpl(message.command)
            IrcCommand.VERSION -> notYetImpl(message.command)
            IrcCommand.STATS -> notYetImpl(message.command)
            IrcCommand.LINKS -> notYetImpl(message.command)
            IrcCommand.TIME -> notYetImpl(message.command)
            IrcCommand.CONNECT -> notYetImpl(message.command)
            IrcCommand.TRACE -> notYetImpl(message.command)
            IrcCommand.ADMIN -> notYetImpl(message.command)
            IrcCommand.INFO -> notYetImpl(message.command)
            IrcCommand.PRIVMSG -> PRIVMSG(message)
            IrcCommand.NOTICE -> NOTICE(message)
            IrcCommand.WHO -> notYetImpl(message.command)
            IrcCommand.WHOIS -> notYetImpl(message.command)
            IrcCommand.WHOWAS -> notYetImpl(message.command)
            IrcCommand.KILL -> notYetImpl(message.command)
            IrcCommand.PING -> PING(message.params)
            IrcCommand.PONG -> notYetImpl(message.command)
            IrcCommand.ERROR -> notYetImpl(message.command)
            IrcCommand.AWAY -> notYetImpl(message.command)
            IrcCommand.REHASH -> notYetImpl(message.command)
            IrcCommand.RESTART -> notYetImpl(message.command)
            IrcCommand.SUMMON -> notYetImpl(message.command)
            IrcCommand.USERS -> notYetImpl(message.command)
            IrcCommand.WALLOPS -> notYetImpl(message.command)
            IrcCommand.USERHOST -> notYetImpl(message.command)
            IrcCommand.ISON -> notYetImpl(message.command)
            IrcCommand.ERR_NOSUCHNICK -> notYetImpl(message.command)
            IrcCommand.ERR_NOSUCHSERVER -> notYetImpl(message.command)
            IrcCommand.ERR_NOSUCHCHANNEL -> notYetImpl(message.command)
            IrcCommand.ERR_CANNOTSENDTOCHAN -> notYetImpl(message.command)
            IrcCommand.ERR_TOOMANYCHANNELS -> notYetImpl(message.command)
            IrcCommand.ERR_WASNOSUCHNICK -> notYetImpl(message.command)
            IrcCommand.ERR_TOOMANYTARGETS -> notYetImpl(message.command)
            IrcCommand.ERR_NOORIGIN -> notYetImpl(message.command)
            IrcCommand.ERR_NORECIPIENT -> notYetImpl(message.command)
            IrcCommand.ERR_NOTEXTTOSEND -> notYetImpl(message.command)
            IrcCommand.ERR_NOTOPLEVEL -> notYetImpl(message.command)
            IrcCommand.ERR_WILDTOPLEVEL -> notYetImpl(message.command)
            IrcCommand.ERR_UNKNOWNCOMMAND -> notYetImpl(message.command)
            IrcCommand.ERR_NOMOTD -> ServerInfoMessage(message)
            IrcCommand.ERR_NOADMININFO -> notYetImpl(message.command)
            IrcCommand.ERR_FILEERROR -> notYetImpl(message.command)
            IrcCommand.ERR_NONICKNAMEGIVEN -> notYetImpl(message.command)
            IrcCommand.ERR_ERRONEUSNICKNAME -> notYetImpl(message.command)
            IrcCommand.ERR_NICKNAMEINUSE -> notYetImpl(message.command)
            IrcCommand.ERR_NICKCOLLISION -> notYetImpl(message.command)
            IrcCommand.ERR_USERNOTINCHANNEL -> notYetImpl(message.command)
            IrcCommand.ERR_NOTONCHANNEL -> notYetImpl(message.command)
            IrcCommand.ERR_USERONCHANNEL -> notYetImpl(message.command)
            IrcCommand.ERR_NOLOGIN -> notYetImpl(message.command)
            IrcCommand.ERR_SUMMONDISABLED -> notYetImpl(message.command)
            IrcCommand.ERR_USERSDISABLED -> notYetImpl(message.command)
            IrcCommand.ERR_NOTREGISTERED -> notYetImpl(message.command)
            IrcCommand.ERR_NEEDMOREPARAMS -> notYetImpl(message.command)
            IrcCommand.ERR_ALREADYREGISTRED -> notYetImpl(message.command)
            IrcCommand.ERR_NOPERMFORHOST -> notYetImpl(message.command)
            IrcCommand.ERR_PASSWDMISMATCH -> notYetImpl(message.command)
            IrcCommand.ERR_YOUREBANNEDCREEP -> notYetImpl(message.command)
            IrcCommand.ERR_KEYSET -> notYetImpl(message.command)
            IrcCommand.ERR_CHANNELISFULL -> notYetImpl(message.command)
            IrcCommand.ERR_UNKNOWNMODE -> notYetImpl(message.command)
            IrcCommand.ERR_INVITEONLYCHAN -> notYetImpl(message.command)
            IrcCommand.ERR_BANNEDFROMCHAN -> notYetImpl(message.command)
            IrcCommand.ERR_BADCHANNELKEY -> notYetImpl(message.command)
            IrcCommand.ERR_NOPRIVILEGES -> notYetImpl(message.command)
            IrcCommand.ERR_CHANOPRIVSNEEDED -> notYetImpl(message.command)
            IrcCommand.ERR_CANTKILLSERVER -> notYetImpl(message.command)
            IrcCommand.ERR_NOOPERHOST -> notYetImpl(message.command)
            IrcCommand.ERR_UMODEUNKNOWNFLAG -> notYetImpl(message.command)
            IrcCommand.ERR_USERSDONTMATCH -> notYetImpl(message.command)
            IrcCommand.RPL_NONE -> notYetImpl(message.command)
            IrcCommand.RPL_USERHOST -> notYetImpl(message.command)
            IrcCommand.RPL_ISON -> notYetImpl(message.command)
            IrcCommand.RPL_AWAY -> notYetImpl(message.command)
            IrcCommand.RPL_UNAWAY -> notYetImpl(message.command)
            IrcCommand.RPL_NOWAWAY -> notYetImpl(message.command)
            IrcCommand.RPL_WHOISUSER -> notYetImpl(message.command)
            IrcCommand.RPL_WHOISSERVER -> notYetImpl(message.command)
            IrcCommand.RPL_WHOISOPERATOR -> notYetImpl(message.command)
            IrcCommand.RPL_WHOISIDLE -> notYetImpl(message.command)
            IrcCommand.RPL_ENDOFWHOIS -> notYetImpl(message.command)
            IrcCommand.RPL_WHOISCHANNELS -> notYetImpl(message.command)
            IrcCommand.RPL_WHOWASUSER -> notYetImpl(message.command)
            IrcCommand.RPL_ENDOFWHOWAS -> notYetImpl(message.command)
            IrcCommand.RPL_LISTSTART -> notYetImpl(message.command)
            IrcCommand.RPL_LIST -> notYetImpl(message.command)
            IrcCommand.RPL_LISTEND -> notYetImpl(message.command)
            IrcCommand.RPL_CHANNELMODEIS -> notYetImpl(message.command)
            IrcCommand.RPL_NOTOPIC -> notYetImpl(message.command)
            IrcCommand.RPL_TOPIC -> notYetImpl(message.command)
            IrcCommand.RPL_INVITING -> notYetImpl(message.command)
            IrcCommand.RPL_SUMMONING -> notYetImpl(message.command)
            IrcCommand.RPL_VERSION -> notYetImpl(message.command)
            IrcCommand.RPL_WHOREPLY -> notYetImpl(message.command)
            IrcCommand.RPL_ENDOFWHO -> notYetImpl(message.command)
            IrcCommand.RPL_NAMREPLY -> NAMES(message)
            IrcCommand.RPL_ENDOFNAMES -> notYetImpl(message.command)
            IrcCommand.RPL_LINKS -> notYetImpl(message.command)
            IrcCommand.RPL_ENDOFLINKS -> notYetImpl(message.command)
            IrcCommand.RPL_BANLIST -> notYetImpl(message.command)
            IrcCommand.RPL_ENDOFBANLIST -> notYetImpl(message.command)
            IrcCommand.RPL_INFO -> notYetImpl(message.command)
            IrcCommand.RPL_ENDOFINFO -> notYetImpl(message.command)
            IrcCommand.RPL_MOTDSTART -> StartOfMOTD
            IrcCommand.RPL_MOTD -> MOTDLINE(message)
            IrcCommand.RPL_ENDOFMOTD -> EndOfMOTD
            IrcCommand.RPL_YOUREOPER -> notYetImpl(message.command)
            IrcCommand.RPL_REHASHING -> notYetImpl(message.command)
            IrcCommand.RPL_TIME -> notYetImpl(message.command)
            IrcCommand.RPL_USERSSTART -> notYetImpl(message.command)
            IrcCommand.RPL_USERS -> notYetImpl(message.command)
            IrcCommand.RPL_ENDOFUSERS -> notYetImpl(message.command)
            IrcCommand.RPL_NOUSERS -> notYetImpl(message.command)
            IrcCommand.RPL_TRACELINK -> notYetImpl(message.command)
            IrcCommand.RPL_TRACECONNECTING -> notYetImpl(message.command)
            IrcCommand.RPL_TRACEHANDSHAKE -> notYetImpl(message.command)
            IrcCommand.RPL_TRACEUNKNOWN -> notYetImpl(message.command)
            IrcCommand.RPL_TRACEOPERATOR -> notYetImpl(message.command)
            IrcCommand.RPL_TRACEUSER -> notYetImpl(message.command)
            IrcCommand.RPL_TRACESERVER -> notYetImpl(message.command)
            IrcCommand.RPL_TRACENEWTYPE -> notYetImpl(message.command)
            IrcCommand.RPL_TRACELOG -> notYetImpl(message.command)
            IrcCommand.RPL_STATSLINKINFO -> notYetImpl(message.command)
            IrcCommand.RPL_STATSCOMMANDS -> notYetImpl(message.command)
            IrcCommand.RPL_STATSCLINE -> notYetImpl(message.command)
            IrcCommand.RPL_STATSNLINE -> notYetImpl(message.command)
            IrcCommand.RPL_STATSILINE -> notYetImpl(message.command)
            IrcCommand.RPL_STATSKLINE -> notYetImpl(message.command)
            IrcCommand.RPL_STATSYLINE -> notYetImpl(message.command)
            IrcCommand.RPL_ENDOFSTATS -> notYetImpl(message.command)
            IrcCommand.RPL_STATSLLINE -> notYetImpl(message.command)
            IrcCommand.RPL_STATSUPTIME -> notYetImpl(message.command)
            IrcCommand.RPL_STATSOLINE -> notYetImpl(message.command)
            IrcCommand.RPL_STATSHLINE -> notYetImpl(message.command)
            IrcCommand.RPL_UMODEIS -> notYetImpl(message.command)
            IrcCommand.RPL_LUSERCLIENT -> notYetImpl(message.command)
            IrcCommand.RPL_LUSEROP -> notYetImpl(message.command)
            IrcCommand.RPL_LUSERUNKNOWN -> notYetImpl(message.command)
            IrcCommand.RPL_LUSERCHANNELS -> notYetImpl(message.command)
            IrcCommand.RPL_LUSERME -> notYetImpl(message.command)
            IrcCommand.RPL_ADMINME -> notYetImpl(message.command)
            IrcCommand.RPL_ADMINLOC1 -> notYetImpl(message.command)
            IrcCommand.RPL_ADMINLOC2 -> notYetImpl(message.command)
            IrcCommand.RPL_ADMINEMAIL -> notYetImpl(message.command)
            IrcCommand.RPL_TRACECLASS -> notYetImpl(message.command)
            IrcCommand.RPL_SERVICEINFO -> notYetImpl(message.command)
            IrcCommand.RPL_SERVICE -> notYetImpl(message.command)
            IrcCommand.RPL_SERVLISTEND -> notYetImpl(message.command)
            IrcCommand.RPL_WHOISCHANOP -> notYetImpl(message.command)
            IrcCommand.RPL_CLOSING -> notYetImpl(message.command)
            IrcCommand.RPL_INFOSTART -> notYetImpl(message.command)
            IrcCommand.ERR_YOUWILLBEBANNED -> notYetImpl(message.command)
            IrcCommand.ERR_NOSERVICEHOST -> notYetImpl(message.command)
            IrcCommand.RPL_WELCOME -> ServerInfoMessage(message)
            IrcCommand.RPL_YOURHOST -> ServerInfoMessage(message)
            IrcCommand.RPL_CREATED -> ServerInfoMessage(message)
            IrcCommand.RPL_MYINFO -> ServerInfoMessage(message)
            IrcCommand.RPL_ISUPPORT -> ServerInfoMessage(message)
            IrcCommand.RPL_LOCALUSERS -> ServerInfoMessage(message)
            IrcCommand.RPL_GLOBALUSERS -> ServerInfoMessage(message)
        }
    }


    /**
     * stub for events that have not been created yet.
     */
    private fun notYetImpl(command: IrcCommand): IIrcEvent {

        logError {
            "No event mapping for $command"
        }
        return NotImplYet
    }


}