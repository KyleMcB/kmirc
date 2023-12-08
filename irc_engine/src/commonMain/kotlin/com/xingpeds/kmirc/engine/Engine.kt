package com.xingpeds.kmirc.engine

import com.xingpeds.kmirc.entities.IrcCommand
import com.xingpeds.kmirc.entities.IrcMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Engine(
    val input: Flow<IrcMessage>,
    val output: (IrcMessage) -> Unit,
    val state: IrcClientState = IrcClientState(),
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    init {
        scope.launch {

            input.collect { message ->
                println("Engine received message: $message")
                state.addMessage(message)


            }
        }
    }
}

private fun IrcClientState.noImpl(message: IrcMessage) {
    println("message handler for ${message.command} not installed yet")
}

private fun IrcClientState.addMessage(message: IrcMessage) {

    this.ircCommands.update { it + message }
    when (message.command) {
        IrcCommand.ADMIN -> noImpl(message)
        IrcCommand.AWAY -> noImpl(message)
        IrcCommand.CONNECT -> noImpl(message)
        IrcCommand.ERROR -> noImpl(message)
        IrcCommand.INFO -> noImpl(message)
        IrcCommand.INVITE -> noImpl(message)
        IrcCommand.ISON -> noImpl(message)
        IrcCommand.JOIN -> join(message)
        IrcCommand.KICK -> noImpl(message)
        IrcCommand.KILL -> noImpl(message)
        IrcCommand.LINKS -> noImpl(message)
        IrcCommand.LIST -> noImpl(message)
        IrcCommand.MODE -> noImpl(message)
        IrcCommand.NAMES -> noImpl(message)
        IrcCommand.NICK -> noImpl(message)
        IrcCommand.NOTICE -> noImpl(message)
        IrcCommand.OPER -> noImpl(message)
        IrcCommand.PART -> noImpl(message)
        IrcCommand.PASS -> noImpl(message)
        IrcCommand.PING -> noImpl(message)
        IrcCommand.PONG -> noImpl(message)
        IrcCommand.PRIVMSG -> noImpl(message)
        IrcCommand.QUIT -> noImpl(message)
        IrcCommand.REHASH -> noImpl(message)
        IrcCommand.RESTART -> noImpl(message)
        IrcCommand.SERVER -> noImpl(message)
        IrcCommand.SQUIT -> noImpl(message)
        IrcCommand.STATS -> noImpl(message)
        IrcCommand.SUMMON -> noImpl(message)
        IrcCommand.TIME -> noImpl(message)
        IrcCommand.TOPIC -> noImpl(message)
        IrcCommand.TRACE -> noImpl(message)
        IrcCommand.USER -> noImpl(message)
        IrcCommand.USERHOST -> noImpl(message)
        IrcCommand.USERS -> noImpl(message)
        IrcCommand.VERSION -> noImpl(message)
        IrcCommand.WALLOPS -> noImpl(message)
        IrcCommand.WHO -> noImpl(message)
        IrcCommand.WHOIS -> noImpl(message)
        IrcCommand.WHOWAS -> noImpl(message)
        IrcCommand.ERR_NOSUCHNICK -> noImpl(message)
        IrcCommand.ERR_NOSUCHSERVER -> noImpl(message)
        IrcCommand.ERR_NOSUCHCHANNEL -> noImpl(message)
        IrcCommand.ERR_CANNOTSENDTOCHAN -> noImpl(message)
        IrcCommand.ERR_TOOMANYCHANNELS -> noImpl(message)
        IrcCommand.ERR_WASNOSUCHNICK -> noImpl(message)
        IrcCommand.ERR_TOOMANYTARGETS -> noImpl(message)
        IrcCommand.ERR_NOORIGIN -> noImpl(message)
        IrcCommand.ERR_NORECIPIENT -> noImpl(message)
        IrcCommand.ERR_NOTEXTTOSEND -> noImpl(message)
        IrcCommand.ERR_NOTOPLEVEL -> noImpl(message)
        IrcCommand.ERR_WILDTOPLEVEL -> noImpl(message)
        IrcCommand.ERR_UNKNOWNCOMMAND -> noImpl(message)
        IrcCommand.ERR_NOMOTD -> noImpl(message)
        IrcCommand.ERR_NOADMININFO -> noImpl(message)
        IrcCommand.ERR_FILEERROR -> noImpl(message)
        IrcCommand.ERR_NONICKNAMEGIVEN -> noImpl(message)
        IrcCommand.ERR_ERRONEUSNICKNAME -> noImpl(message)
        IrcCommand.ERR_NICKNAMEINUSE -> noImpl(message)
        IrcCommand.ERR_NICKCOLLISION -> noImpl(message)
        IrcCommand.ERR_USERNOTINCHANNEL -> noImpl(message)
        IrcCommand.ERR_NOTONCHANNEL -> noImpl(message)
        IrcCommand.ERR_USERONCHANNEL -> noImpl(message)
        IrcCommand.ERR_NOLOGIN -> noImpl(message)
        IrcCommand.ERR_SUMMONDISABLED -> noImpl(message)
        IrcCommand.ERR_USERSDISABLED -> noImpl(message)
        IrcCommand.ERR_NOTREGISTERED -> noImpl(message)
        IrcCommand.ERR_NEEDMOREPARAMS -> noImpl(message)
        IrcCommand.ERR_ALREADYREGISTRED -> noImpl(message)
        IrcCommand.ERR_NOPERMFORHOST -> noImpl(message)
        IrcCommand.ERR_PASSWDMISMATCH -> noImpl(message)
        IrcCommand.ERR_YOUREBANNEDCREEP -> noImpl(message)
        IrcCommand.ERR_KEYSET -> noImpl(message)
        IrcCommand.ERR_CHANNELISFULL -> noImpl(message)
        IrcCommand.ERR_UNKNOWNMODE -> noImpl(message)
        IrcCommand.ERR_INVITEONLYCHAN -> noImpl(message)
        IrcCommand.ERR_BANNEDFROMCHAN -> noImpl(message)
        IrcCommand.ERR_BADCHANNELKEY -> noImpl(message)
        IrcCommand.ERR_NOPRIVILEGES -> noImpl(message)
        IrcCommand.ERR_CHANOPRIVSNEEDED -> noImpl(message)
        IrcCommand.ERR_CANTKILLSERVER -> noImpl(message)
        IrcCommand.ERR_NOOPERHOST -> noImpl(message)
        IrcCommand.ERR_UMODEUNKNOWNFLAG -> noImpl(message)
        IrcCommand.ERR_USERSDONTMATCH -> noImpl(message)
        IrcCommand.RPL_NONE -> noImpl(message)
        IrcCommand.RPL_USERHOST -> noImpl(message)
        IrcCommand.RPL_ISON -> noImpl(message)
        IrcCommand.RPL_AWAY -> noImpl(message)
        IrcCommand.RPL_UNAWAY -> noImpl(message)
        IrcCommand.RPL_NOWAWAY -> noImpl(message)
        IrcCommand.RPL_WHOISUSER -> noImpl(message)
        IrcCommand.RPL_WHOISSERVER -> noImpl(message)
        IrcCommand.RPL_WHOISOPERATOR -> noImpl(message)
        IrcCommand.RPL_WHOISIDLE -> noImpl(message)
        IrcCommand.RPL_ENDOFWHOIS -> noImpl(message)
        IrcCommand.RPL_WHOISCHANNELS -> noImpl(message)
        IrcCommand.RPL_WHOWASUSER -> noImpl(message)
        IrcCommand.RPL_ENDOFWHOWAS -> noImpl(message)
        IrcCommand.RPL_LISTSTART -> noImpl(message)
        IrcCommand.RPL_LIST -> noImpl(message)
        IrcCommand.RPL_LISTEND -> noImpl(message)
        IrcCommand.RPL_CHANNELMODEIS -> noImpl(message)
        IrcCommand.RPL_NOTOPIC -> noImpl(message)
        IrcCommand.RPL_TOPIC -> noImpl(message)
        IrcCommand.RPL_INVITING -> noImpl(message)
        IrcCommand.RPL_SUMMONING -> noImpl(message)
        IrcCommand.RPL_VERSION -> noImpl(message)
        IrcCommand.RPL_WHOREPLY -> noImpl(message)
        IrcCommand.RPL_ENDOFWHO -> noImpl(message)
        IrcCommand.RPL_NAMREPLY -> noImpl(message)
        IrcCommand.RPL_ENDOFNAMES -> noImpl(message)
        IrcCommand.RPL_LINKS -> noImpl(message)
        IrcCommand.RPL_ENDOFLINKS -> noImpl(message)
        IrcCommand.RPL_BANLIST -> noImpl(message)
        IrcCommand.RPL_ENDOFBANLIST -> noImpl(message)
        IrcCommand.RPL_INFO -> noImpl(message)
        IrcCommand.RPL_ENDOFINFO -> noImpl(message)
        IrcCommand.RPL_MOTDSTART -> noImpl(message)
        IrcCommand.RPL_MOTD -> noImpl(message)
        IrcCommand.RPL_ENDOFMOTD -> noImpl(message)
        IrcCommand.RPL_YOUREOPER -> noImpl(message)
        IrcCommand.RPL_REHASHING -> noImpl(message)
        IrcCommand.RPL_TIME -> noImpl(message)
        IrcCommand.RPL_USERSSTART -> noImpl(message)
        IrcCommand.RPL_USERS -> noImpl(message)
        IrcCommand.RPL_ENDOFUSERS -> noImpl(message)
        IrcCommand.RPL_NOUSERS -> noImpl(message)
        IrcCommand.RPL_TRACELINK -> noImpl(message)
        IrcCommand.RPL_TRACECONNECTING -> noImpl(message)
        IrcCommand.RPL_TRACEHANDSHAKE -> noImpl(message)
        IrcCommand.RPL_TRACEUNKNOWN -> noImpl(message)
        IrcCommand.RPL_TRACEOPERATOR -> noImpl(message)
        IrcCommand.RPL_TRACEUSER -> noImpl(message)
        IrcCommand.RPL_TRACESERVER -> noImpl(message)
        IrcCommand.RPL_TRACENEWTYPE -> noImpl(message)
        IrcCommand.RPL_TRACELOG -> noImpl(message)
        IrcCommand.RPL_STATSLINKINFO -> noImpl(message)
        IrcCommand.RPL_STATSCOMMANDS -> noImpl(message)
        IrcCommand.RPL_STATSCLINE -> noImpl(message)
        IrcCommand.RPL_STATSNLINE -> noImpl(message)
        IrcCommand.RPL_STATSILINE -> noImpl(message)
        IrcCommand.RPL_STATSKLINE -> noImpl(message)
        IrcCommand.RPL_STATSYLINE -> noImpl(message)
        IrcCommand.RPL_ENDOFSTATS -> noImpl(message)
        IrcCommand.RPL_STATSLLINE -> noImpl(message)
        IrcCommand.RPL_STATSUPTIME -> noImpl(message)
        IrcCommand.RPL_STATSOLINE -> noImpl(message)
        IrcCommand.RPL_STATSHLINE -> noImpl(message)
        IrcCommand.RPL_UMODEIS -> noImpl(message)
        IrcCommand.RPL_LUSERCLIENT -> noImpl(message)
        IrcCommand.RPL_LUSEROP -> noImpl(message)
        IrcCommand.RPL_LUSERUNKNOWN -> noImpl(message)
        IrcCommand.RPL_LUSERCHANNELS -> noImpl(message)
        IrcCommand.RPL_LUSERME -> noImpl(message)
        IrcCommand.RPL_ADMINME -> noImpl(message)
        IrcCommand.RPL_ADMINLOC1 -> noImpl(message)
        IrcCommand.RPL_ADMINLOC2 -> noImpl(message)
        IrcCommand.RPL_ADMINEMAIL -> noImpl(message)
        IrcCommand.RPL_TRACECLASS -> noImpl(message)
        IrcCommand.RPL_SERVICEINFO -> noImpl(message)
        IrcCommand.RPL_SERVICE -> noImpl(message)
        IrcCommand.RPL_SERVLISTEND -> noImpl(message)
        IrcCommand.RPL_WHOISCHANOP -> noImpl(message)
        IrcCommand.RPL_CLOSING -> noImpl(message)
        IrcCommand.RPL_INFOSTART -> noImpl(message)
        IrcCommand.ERR_YOUWILLBEBANNED -> noImpl(message)
        IrcCommand.ERR_NOSERVICEHOST -> noImpl(message)
    }

}

private fun IrcClientState.join(message: IrcMessage) {
// example
//
//    :WiZ JOIN #Twilight_zone        ; JOIN message from WiZ
    // if the user is me then add this channel my list of channels
    // else add this user to a channel we are already in
    
}

class InvalidIRCline(message: IrcMessage) : Exception(message = message.toString()) {

}

