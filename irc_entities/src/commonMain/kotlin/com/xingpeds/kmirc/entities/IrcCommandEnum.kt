/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.entities

/**
 * exhaustive (eventually) list of commands in the IRC protocol
 * @property command this is the expected text for this command
 */
@Suppress("KDocMissingDocumentation")
enum class IrcCommand(val command: String) {
    PASS("PASS"), //client to server only to provide password
    NICK("NICK"), //nick change event added
    USER("USER"), //client to server only, no event
    SERVER("SERVER"), //server to server only, no event
    OPER("OPER"), //client to server only, no event
    QUIT("QUIT"), //quit message event impl
    SQUIT("SQUIT"), //todo impl event, might be client to server only
    JOIN("JOIN"), //join event impl
    PART("PART"), //part event impl
    MODE("MODE"), //event impl
    TOPIC("TOPIC"),
    NAMES("NAMES"), //client server only, no event
    LIST("LIST"),  //client to server only, no event
    INVITE("INVITE"), //event impl1
    KICK("KICK"), //impled
    VERSION("VERSION"), //client to server only
    STATS("STATS"), //skipping event for this, ugly and complicated
    LINKS("LINKS"), //client to server only
    TIME("TIME"), // clien to server only
    CONNECT("CONNECT"), //client to server only
    TRACE("TRACE"), //skip
    ADMIN("ADMIN"), //client to server only
    INFO("INFO"), //client to server only
    PRIVMSG("PRIVMSG"), // event impl
    NOTICE("NOTICE"), //event impl
    WHO("WHO"), //client to server
    WHOIS("WHOIS"), //client to server only
    WHOWAS("WHOWAS"), //client to server only
    KILL("KILL"), //client to server only
    PING("PING"), //event impl
    PONG("PONG"), //client to server only
    ERROR("ERROR"), //event impl
    AWAY("AWAY"), //event impl
    REHASH("REHASH"),
    RESTART("RESTART"),
    SUMMON("SUMMON"),
    USERS("USERS"),
    WALLOPS("WALLOPS"),
    USERHOST("USERHOST"),
    ISON("ISON"),
    ERR_NOSUCHNICK("401"),
    ERR_NOSUCHSERVER("402"),
    ERR_NOSUCHCHANNEL("403"),
    ERR_CANNOTSENDTOCHAN("404"),
    ERR_TOOMANYCHANNELS("405"),
    ERR_WASNOSUCHNICK("406"),
    ERR_TOOMANYTARGETS("407"),
    ERR_NOORIGIN("409"),
    ERR_NORECIPIENT("411"),
    ERR_NOTEXTTOSEND("412"),
    ERR_NOTOPLEVEL("413"),
    ERR_WILDTOPLEVEL("414"),
    ERR_UNKNOWNCOMMAND("421"),
    ERR_NOMOTD("422"),
    ERR_NOADMININFO("423"),
    ERR_FILEERROR("424"),
    ERR_NONICKNAMEGIVEN("431"),
    ERR_ERRONEUSNICKNAME("432"),
    ERR_NICKNAMEINUSE("433"),
    ERR_NICKCOLLISION("436"),
    ERR_USERNOTINCHANNEL("441"),
    ERR_NOTONCHANNEL("442"),
    ERR_USERONCHANNEL("443"),
    ERR_NOLOGIN("444"),
    ERR_SUMMONDISABLED("445"),
    ERR_USERSDISABLED("446"),
    ERR_NOTREGISTERED("451"),
    ERR_NEEDMOREPARAMS("461"),
    ERR_ALREADYREGISTRED("462"),
    ERR_NOPERMFORHOST("463"),
    ERR_PASSWDMISMATCH("464"),
    ERR_YOUREBANNEDCREEP("465"),
    ERR_KEYSET("467"),
    ERR_CHANNELISFULL("471"),
    ERR_UNKNOWNMODE("472"),
    ERR_INVITEONLYCHAN("473"),
    ERR_BANNEDFROMCHAN("474"),
    ERR_BADCHANNELKEY("475"),
    ERR_NOPRIVILEGES("481"),
    ERR_CHANOPRIVSNEEDED("482"),
    ERR_CANTKILLSERVER("483"),
    ERR_NOOPERHOST("491"),
    ERR_UMODEUNKNOWNFLAG("501"),
    ERR_USERSDONTMATCH("502"),
    RPL_NONE("300"),
    RPL_USERHOST("302"),
    RPL_ISON("303"),
    RPL_AWAY("301"),
    RPL_UNAWAY("305"),
    RPL_NOWAWAY("306"),
    RPL_WHOISUSER("311"),
    RPL_WHOISSERVER("312"),
    RPL_WHOISOPERATOR("313"),
    RPL_WHOISIDLE("317"),
    RPL_ENDOFWHOIS("318"),
    RPL_WHOISCHANNELS("319"),
    RPL_WHOWASUSER("314"),
    RPL_ENDOFWHOWAS("369"),
    RPL_LISTSTART("321"),
    RPL_LIST("322"),
    RPL_LISTEND("323"),
    RPL_CHANNELMODEIS("324"),
    RPL_NOTOPIC("331"),
    RPL_TOPIC("332"),
    RPL_INVITING("341"),
    RPL_SUMMONING("342"),
    RPL_VERSION("351"),
    RPL_WHOREPLY("352"),
    RPL_ENDOFWHO("315"),
    RPL_NAMREPLY("353"),
    RPL_ENDOFNAMES("366"),
    RPL_LINKS("364"),
    RPL_ENDOFLINKS("365"),
    RPL_BANLIST("367"),
    RPL_ENDOFBANLIST("368"),
    RPL_INFO("371"),
    RPL_ENDOFINFO("374"),
    RPL_MOTDSTART("375"),
    RPL_MOTD("372"),
    RPL_ENDOFMOTD("376"),
    RPL_YOUREOPER("381"),
    RPL_REHASHING("382"),
    RPL_TIME("391"),
    RPL_USERSSTART("392"),
    RPL_USERS("393"),
    RPL_ENDOFUSERS("394"),
    RPL_NOUSERS("395"),
    RPL_TRACELINK("200"),
    RPL_TRACECONNECTING("201"),
    RPL_TRACEHANDSHAKE("202"),
    RPL_TRACEUNKNOWN("203"),
    RPL_TRACEOPERATOR("204"),
    RPL_TRACEUSER("205"),
    RPL_TRACESERVER("206"),
    RPL_TRACENEWTYPE("208"),
    RPL_TRACELOG("261"),
    RPL_STATSLINKINFO("211"),
    RPL_STATSCOMMANDS("212"),
    RPL_STATSCLINE("213"),
    RPL_STATSNLINE("214"),
    RPL_STATSILINE("215"),
    RPL_STATSKLINE("216"),
    RPL_STATSYLINE("218"),
    RPL_ENDOFSTATS("219"),
    RPL_STATSLLINE("241"),
    RPL_STATSUPTIME("242"),
    RPL_STATSOLINE("243"),
    RPL_STATSHLINE("244"),
    RPL_UMODEIS("221"),
    RPL_LUSERCLIENT("251"),
    RPL_LUSEROP("252"),
    RPL_LUSERUNKNOWN("253"),
    RPL_LUSERCHANNELS("254"),
    RPL_LUSERME("255"),
    RPL_ADMINME("256"),
    RPL_ADMINLOC1("257"),
    RPL_ADMINLOC2("258"),
    RPL_ADMINEMAIL("259"),
    RPL_TRACECLASS("209"),
    RPL_SERVICEINFO("231"),
    RPL_SERVICE("233"),
    RPL_SERVLISTEND("235"),
    RPL_WHOISCHANOP("316"),
    RPL_CLOSING("362"),
    RPL_INFOSTART("373"),
    ERR_YOUWILLBEBANNED("466"),
    ERR_NOSERVICEHOST("492"),
    RPL_WELCOME("001"),
    RPL_YOURHOST("002"),
    RPL_CREATED("003"),
    RPL_MYINFO("004"),
    RPL_ISUPPORT("005"),
    RPL_LOCALUSERS("265"),
    RPL_GLOBALUSERS("266"),
}

/**
 * Text to command lookup for message parsing
 */
val commandLookup: Map<String, IrcCommand> = mapOf(*IrcCommand.entries.map { it.command to it }.toTypedArray())