import com.xingpeds.kmirc.clientnetwork.randomSocketArb
import io.kotest.property.arbitrary.next
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class Prototyping {
    @Test
    fun run() = runTest {

        val hi = randomSocketArb(this)
        hi.next().close()
    }
}