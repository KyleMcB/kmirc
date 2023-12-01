import com.xingpeds.kmirc.clientnetwork.randomSocketArb
import io.kotest.property.arbitrary.next
import kotlin.test.Test

class Prototyping {
    @Test
    fun run() {
        val hi = randomSocketArb()
        hi.next().close()
    }
}