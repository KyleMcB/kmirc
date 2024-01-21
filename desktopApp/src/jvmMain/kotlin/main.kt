/*
 * Copyright (c) Kyle McBurnett 2024.
 */
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.xingpeds.kmirc.Engine
import com.xingpeds.kmirc.clientnetwork.Address
import com.xingpeds.kmirc.clientnetwork.DNSLookupFun
import com.xingpeds.kmirc.clientnetwork.getDNSLookupFun
import com.xingpeds.kmirc.desktop.views.IChannelMessage
import com.xingpeds.kmirc.desktop.views.IChannelNick
import com.xingpeds.kmirc.desktop.views.TripplePane
import com.xingpeds.kmirc.engine.IIrcClientEngine
import com.xingpeds.kmirc.entities.*
import com.xingpeds.kmirc.entities.events.NOTICE
import com.xingpeds.kmirc.entities.events.PRIVMSG
import com.xingpeds.kmirc.state.ChannelName
import com.xingpeds.kmirc.state.ChannelState
import com.xingpeds.kmirc.state.MutableChannelState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

val dnsLookup: DNSLookupFun = getDNSLookupFun()

/**
 * entry point of KMIRC desktop application
 */
@OptIn(ExperimentalResourceApi::class)
fun main(): Unit = application {
    Window(
        state = WindowState(size = DpSize(1000.dp, 500.dp)),
        onCloseRequest = ::exitApplication,
        title = "KMIRC",
        icon = painterResource("kmirc_icon.png")
    ) {

        Navigator(LoginScreen)
    }
}

/**
 *
 */
data class IrcMainScreen(val engine: IIrcClientEngine) : Screen, Logged by LogTag("IrcMainScreen") {
    val scope = CoroutineScope(Dispatchers.Default)

    init {
        engine.eventList.onEndOfMOTD.onEach {
            engine.send(IrcMessage(command = IrcCommand.JOIN, params = IrcParams("#kmirc")))
        }.launchIn(scope)
    }


    @Composable
    override fun Content() {
        val channels: State<List<ChannelState>> =
            engine.state.channels.map<Map<ChannelName, ChannelState>, List<ChannelState>> {
                listOf(
                    MutableChannelState(engine.serverHostName)
                ) + it.values
            }.collectAsState<List<ChannelState>, List<ChannelState>>(
                listOf(MutableChannelState(engine.serverHostName))
            )
        var selected by remember { mutableStateOf(0) }
        val channelMessages: List<IChannelMessage> = if (selected == 0) {
            engine.state.notices.value.map<NOTICE, IChannelMessage> {
                object : IChannelMessage {
                    override val from: String
                        get() = when (val _form = it.from) {
                            is IrcFrom.Server -> _form.server
                            is IrcFrom.User -> _form.user
                        }
                    override val message: String
                        get() = it.message

                }
            }
        } else {
            val channel: ChannelState = channels.value[selected]
            val hi: List<IChannelMessage> =
                engine.state.privmsgs.map { list: List<PRIVMSG> -> list.filter { (it.target as? IrcTarget.Channel)?.channel == channel.name } }
                    .map<List<PRIVMSG>, List<IChannelMessage>> { privmsgList ->
                        privmsgList.map { privmsg ->
                            object : IChannelMessage {
                                override val from: String
                                    get() = (privmsg.from as? IrcFrom.User)?.user ?: engine.serverHostName
                                override val message: String
                                    get() = privmsg.message

                            }
                        }
                    }.collectAsState(emptyList()).value
            hi
        }
        val nicks: List<IChannelNick> = if (selected == 0) {
            emptyList<IChannelNick>()
        } else {
            val channel: ChannelState = channels.value[selected]
            channel.members.value.map {
                object : IChannelNick {
                    override val nick: String
                        get() = it

                }
            }
        }
        val channelName = channels.value[selected].name
        TripplePane(channels = channels.value,
            selected = selected,
            channelMessages = channelMessages,
            nicks = nicks,
            onChannelClick = { selected = it },
            onSend = {
                engine.send(
                    IrcMessage(
                        command = IrcCommand.PRIVMSG, params = IrcParams(channelName, longParam = it)
                    )
                )
            })
    }
}


object LoginScreen : Screen {
    /**
     * Login screen entry point
     */
    val dnsLookup: DNSLookupFun = getDNSLookupFun()

    @Composable
    override fun Content(): Unit {
        val hostname = remember { mutableStateOf("") }
        val hostnameValid = remember { mutableStateOf(true) }
        val port = remember { mutableStateOf(6667) }
        val nick = remember { mutableStateOf("") }
        val userName = remember { mutableStateOf("") }
        val realName = remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {


            Column {

                OutlinedTextField(
                    value = nick.value,
                    onValueChange = { nick.value = it },
                    label = { Text("Nick") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = hostname.value,
                    onValueChange = { hostname.value = it },
                    label = { Text("Irc Server") },
                    isError = !hostnameValid.value,
                    singleLine = true
                )
                OutlinedTextField(
                    value = port.value.toString(),
                    onValueChange = { port.value = it.toInt() },
                    label = { Text("port") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = userName.value,
                    onValueChange = { userName.value = it },
                    label = { Text("username") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = realName.value,
                    onValueChange = { realName.value = it },
                    label = { Text("real name") },
                    singleLine = true
                )
                val hi = LocalNavigator.currentOrThrow

                Button(onClick = {

                    scope.launch {

                        val engine = Engine.connect(
                            hostname = hostname.value,
                            port = port.value,
                            ircUser = IrcUser(
                                nick = nick.value,
                                username = userName.value,
                                hostname = null,
                                realName = realName.value,
                            ),
                        ).filterIsInstance<Engine.EngineConnectResult.Success>().first()
                        hi.push(IrcMainScreen(engine))
                    }
                }) {
                    Text("Connect")
                }
            }
        }
        LaunchedEffect(hostname.value) {
            if (hostname.value.isBlank()) return@LaunchedEffect
            delay(2000)
            val addresses: List<Address> = try {
                dnsLookup(hostname.value)
            } catch (e: Throwable) {
                emptyList<Address>()
            }
            hostnameValid.value = (addresses.size > 0)
        }

//        Engine.connect(
//            hostname = ,
//            port = ,
//            ircUser = IrcUser(
//                nick = ,
//                username = ,
//                hostname = null,
//                realName = ,
//            ),
//        )
    }

}