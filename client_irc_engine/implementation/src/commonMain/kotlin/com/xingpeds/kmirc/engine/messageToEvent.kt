/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.*
import com.xingpeds.kmirc.entities.events.IIrcEvent

fun messageToEvent(message: IIrcMessage): IIrcEvent? = when (message.command) {
    IrcCommand.PASS -> TODO()
    IrcCommand.NICK -> TODO()
    IrcCommand.USER -> TODO()
    IrcCommand.SERVER -> TODO()
    IrcCommand.OPER -> TODO()
    IrcCommand.QUIT -> TODO()
    IrcCommand.SQUIT -> TODO()
    IrcCommand.JOIN -> TODO()
    IrcCommand.PART -> TODO()
    IrcCommand.MODE -> TODO()
    IrcCommand.TOPIC -> TODO()
    IrcCommand.NAMES -> TODO()
    IrcCommand.LIST -> TODO()
    IrcCommand.INVITE -> TODO()
    IrcCommand.KICK -> TODO()
    IrcCommand.VERSION -> TODO()
    IrcCommand.STATS -> TODO()
    IrcCommand.LINKS -> TODO()
    IrcCommand.TIME -> TODO()
    IrcCommand.CONNECT -> TODO()
    IrcCommand.TRACE -> TODO()
    IrcCommand.ADMIN -> TODO()
    IrcCommand.INFO -> TODO()
    IrcCommand.PRIVMSG -> sendPrivmsgEvent(message)
    IrcCommand.NOTICE -> sendNoticeEvent(message)
    IrcCommand.WHO -> TODO()
    IrcCommand.WHOIS -> TODO()
    IrcCommand.WHOWAS -> TODO()
    IrcCommand.KILL -> TODO()
    IrcCommand.PING -> IIrcEvent.PING(message.params)
    IrcCommand.PONG -> TODO()
    IrcCommand.ERROR -> TODO()
    IrcCommand.AWAY -> TODO()
    IrcCommand.REHASH -> TODO()
    IrcCommand.RESTART -> TODO()
    IrcCommand.SUMMON -> TODO()
    IrcCommand.USERS -> TODO()
    IrcCommand.WALLOPS -> TODO()
    IrcCommand.USERHOST -> TODO()
    IrcCommand.ISON -> TODO()
    IrcCommand.ERR_NOSUCHNICK -> TODO()
    IrcCommand.ERR_NOSUCHSERVER -> TODO()
    IrcCommand.ERR_NOSUCHCHANNEL -> TODO()
    IrcCommand.ERR_CANNOTSENDTOCHAN -> TODO()
    IrcCommand.ERR_TOOMANYCHANNELS -> TODO()
    IrcCommand.ERR_WASNOSUCHNICK -> TODO()
    IrcCommand.ERR_TOOMANYTARGETS -> TODO()
    IrcCommand.ERR_NOORIGIN -> TODO()
    IrcCommand.ERR_NORECIPIENT -> TODO()
    IrcCommand.ERR_NOTEXTTOSEND -> TODO()
    IrcCommand.ERR_NOTOPLEVEL -> TODO()
    IrcCommand.ERR_WILDTOPLEVEL -> TODO()
    IrcCommand.ERR_UNKNOWNCOMMAND -> TODO()
    IrcCommand.ERR_NOMOTD -> TODO()
    IrcCommand.ERR_NOADMININFO -> TODO()
    IrcCommand.ERR_FILEERROR -> TODO()
    IrcCommand.ERR_NONICKNAMEGIVEN -> TODO()
    IrcCommand.ERR_ERRONEUSNICKNAME -> TODO()
    IrcCommand.ERR_NICKNAMEINUSE -> TODO()
    IrcCommand.ERR_NICKCOLLISION -> TODO()
    IrcCommand.ERR_USERNOTINCHANNEL -> TODO()
    IrcCommand.ERR_NOTONCHANNEL -> TODO()
    IrcCommand.ERR_USERONCHANNEL -> TODO()
    IrcCommand.ERR_NOLOGIN -> TODO()
    IrcCommand.ERR_SUMMONDISABLED -> TODO()
    IrcCommand.ERR_USERSDISABLED -> TODO()
    IrcCommand.ERR_NOTREGISTERED -> TODO()
    IrcCommand.ERR_NEEDMOREPARAMS -> TODO()
    IrcCommand.ERR_ALREADYREGISTRED -> TODO()
    IrcCommand.ERR_NOPERMFORHOST -> TODO()
    IrcCommand.ERR_PASSWDMISMATCH -> TODO()
    IrcCommand.ERR_YOUREBANNEDCREEP -> TODO()
    IrcCommand.ERR_KEYSET -> TODO()
    IrcCommand.ERR_CHANNELISFULL -> TODO()
    IrcCommand.ERR_UNKNOWNMODE -> TODO()
    IrcCommand.ERR_INVITEONLYCHAN -> TODO()
    IrcCommand.ERR_BANNEDFROMCHAN -> TODO()
    IrcCommand.ERR_BADCHANNELKEY -> TODO()
    IrcCommand.ERR_NOPRIVILEGES -> TODO()
    IrcCommand.ERR_CHANOPRIVSNEEDED -> TODO()
    IrcCommand.ERR_CANTKILLSERVER -> TODO()
    IrcCommand.ERR_NOOPERHOST -> TODO()
    IrcCommand.ERR_UMODEUNKNOWNFLAG -> TODO()
    IrcCommand.ERR_USERSDONTMATCH -> TODO()
    IrcCommand.RPL_NONE -> TODO()
    IrcCommand.RPL_USERHOST -> TODO()
    IrcCommand.RPL_ISON -> TODO()
    IrcCommand.RPL_AWAY -> TODO()
    IrcCommand.RPL_UNAWAY -> TODO()
    IrcCommand.RPL_NOWAWAY -> TODO()
    IrcCommand.RPL_WHOISUSER -> TODO()
    IrcCommand.RPL_WHOISSERVER -> TODO()
    IrcCommand.RPL_WHOISOPERATOR -> TODO()
    IrcCommand.RPL_WHOISIDLE -> TODO()
    IrcCommand.RPL_ENDOFWHOIS -> TODO()
    IrcCommand.RPL_WHOISCHANNELS -> TODO()
    IrcCommand.RPL_WHOWASUSER -> TODO()
    IrcCommand.RPL_ENDOFWHOWAS -> TODO()
    IrcCommand.RPL_LISTSTART -> TODO()
    IrcCommand.RPL_LIST -> TODO()
    IrcCommand.RPL_LISTEND -> TODO()
    IrcCommand.RPL_CHANNELMODEIS -> TODO()
    IrcCommand.RPL_NOTOPIC -> TODO()
    IrcCommand.RPL_TOPIC -> TODO()
    IrcCommand.RPL_INVITING -> TODO()
    IrcCommand.RPL_SUMMONING -> TODO()
    IrcCommand.RPL_VERSION -> TODO()
    IrcCommand.RPL_WHOREPLY -> TODO()
    IrcCommand.RPL_ENDOFWHO -> TODO()
    IrcCommand.RPL_NAMREPLY -> TODO()
    IrcCommand.RPL_ENDOFNAMES -> TODO()
    IrcCommand.RPL_LINKS -> TODO()
    IrcCommand.RPL_ENDOFLINKS -> TODO()
    IrcCommand.RPL_BANLIST -> TODO()
    IrcCommand.RPL_ENDOFBANLIST -> TODO()
    IrcCommand.RPL_INFO -> TODO()
    IrcCommand.RPL_ENDOFINFO -> TODO()
    IrcCommand.RPL_MOTDSTART -> TODO()
    IrcCommand.RPL_MOTD -> TODO()
    IrcCommand.RPL_ENDOFMOTD -> TODO()
    IrcCommand.RPL_YOUREOPER -> TODO()
    IrcCommand.RPL_REHASHING -> TODO()
    IrcCommand.RPL_TIME -> TODO()
    IrcCommand.RPL_USERSSTART -> TODO()
    IrcCommand.RPL_USERS -> TODO()
    IrcCommand.RPL_ENDOFUSERS -> TODO()
    IrcCommand.RPL_NOUSERS -> TODO()
    IrcCommand.RPL_TRACELINK -> TODO()
    IrcCommand.RPL_TRACECONNECTING -> TODO()
    IrcCommand.RPL_TRACEHANDSHAKE -> TODO()
    IrcCommand.RPL_TRACEUNKNOWN -> TODO()
    IrcCommand.RPL_TRACEOPERATOR -> TODO()
    IrcCommand.RPL_TRACEUSER -> TODO()
    IrcCommand.RPL_TRACESERVER -> TODO()
    IrcCommand.RPL_TRACENEWTYPE -> TODO()
    IrcCommand.RPL_TRACELOG -> TODO()
    IrcCommand.RPL_STATSLINKINFO -> TODO()
    IrcCommand.RPL_STATSCOMMANDS -> TODO()
    IrcCommand.RPL_STATSCLINE -> TODO()
    IrcCommand.RPL_STATSNLINE -> TODO()
    IrcCommand.RPL_STATSILINE -> TODO()
    IrcCommand.RPL_STATSKLINE -> TODO()
    IrcCommand.RPL_STATSYLINE -> TODO()
    IrcCommand.RPL_ENDOFSTATS -> TODO()
    IrcCommand.RPL_STATSLLINE -> TODO()
    IrcCommand.RPL_STATSUPTIME -> TODO()
    IrcCommand.RPL_STATSOLINE -> TODO()
    IrcCommand.RPL_STATSHLINE -> TODO()
    IrcCommand.RPL_UMODEIS -> TODO()
    IrcCommand.RPL_LUSERCLIENT -> TODO()
    IrcCommand.RPL_LUSEROP -> TODO()
    IrcCommand.RPL_LUSERUNKNOWN -> TODO()
    IrcCommand.RPL_LUSERCHANNELS -> TODO()
    IrcCommand.RPL_LUSERME -> TODO()
    IrcCommand.RPL_ADMINME -> TODO()
    IrcCommand.RPL_ADMINLOC1 -> TODO()
    IrcCommand.RPL_ADMINLOC2 -> TODO()
    IrcCommand.RPL_ADMINEMAIL -> TODO()
    IrcCommand.RPL_TRACECLASS -> TODO()
    IrcCommand.RPL_SERVICEINFO -> TODO()
    IrcCommand.RPL_SERVICE -> TODO()
    IrcCommand.RPL_SERVLISTEND -> TODO()
    IrcCommand.RPL_WHOISCHANOP -> TODO()
    IrcCommand.RPL_CLOSING -> TODO()
    IrcCommand.RPL_INFOSTART -> TODO()
    IrcCommand.ERR_YOUWILLBEBANNED -> TODO()
    IrcCommand.ERR_NOSERVICEHOST -> TODO()
}

//todo move this logic to the constructor of the ircEvent and write kotest propetry tests
fun sendPrivmsgEvent(message: IIrcMessage): IIrcEvent? {

// user or server
    val host = message.prefix?.host
    val nickOrServer = message.prefix?.nick ?: return null
    val from: IrcFrom = if (host == null) {
        IrcFrom.Server(nickOrServer)
    } else {
        IrcFrom.User(nickOrServer)
    }
    val target: IrcTarget = if (isChannel(message.params.list[0])) {
        IrcTarget.Channel(message.params.list[0])
    } else {
        IrcTarget.User(message.params.list[0])
    }
    val privmsgText: String = message.params.longParam ?: return null
    return IIrcEvent.PRIVMSG(from = from, target = target, message = privmsgText)
}

private fun sendNoticeEvent(message: IIrcMessage): IIrcEvent.Notice? {
    val user = message.prefix?.user
    val host = message.prefix?.host
    val nick = message.prefix?.nick
    val text = message.params.longParam
    return if (nick != null) {
        val targetString: String = message.params.list[0]
        val target: IrcTarget = if (targetString.startsWith("#")) {
            IrcTarget.Channel(targetString)
        } else {
            IrcTarget.User(targetString)
        }
        val from: IrcFrom = if (user == null && host == null) {
            IrcFrom.Server(nick)
        } else {
            IrcFrom.User(nick)
        }
        val noticeEvent = IIrcEvent.Notice(target = target, from = from, message = text ?: "")
        noticeEvent
    } else {
        null
    }
}