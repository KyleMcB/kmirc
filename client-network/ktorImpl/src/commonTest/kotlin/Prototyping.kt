import com.xingpeds.kmirc.clientnetwork.ConnectionResult
import com.xingpeds.kmirc.clientnetwork.DNSResolver
import com.xingpeds.kmirc.clientnetwork.KtorSocketFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContains

class Prototyping {
    @Test
    fun connectToGoogle() = runBlocking {
        val request = """
            GET / HTTP/1.1
            Host: google.com
            Connection: close


        """.trimIndent()
        val addresses = DNSResolver.invoke("google.com")
        addresses.first().let { address ->

            when (val socket = KtorSocketFactory(address, 80)) {
                ConnectionResult.Failure.UndefinedError -> TODO()
                is ConnectionResult.Success -> {
                    var input:String = ""
                    val readJob = launch {
                    socket.connection.incoming.onEach {
                        input = it
                    }.launchIn(this)

                    }
                    socket.connection.write(request)
                    delay(2000)
                    readJob.cancel()
                    socket.connection.close()
                    assertContains(input, "HTML")
                }
            }
        }
    }
}