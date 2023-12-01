package com.xingpeds.kmirc.clientnetwork

import io.kotest.property.Arb
import io.kotest.property.RandomSource
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean


fun randomSocketArb(scope: CoroutineScope) = arbitrary { rs: RandomSource ->

    FakeSocket(scope, rs)
}

class FakeSocket(val scope: CoroutineScope, val rs: RandomSource) : SimpleSocket {
    private val closed = AtomicBoolean(false)
    private val strings = Arb.string()
    private val output = MutableSharedFlow<String>().apply {
        scope.launch {
            emit(strings.next(rs))
        }
    }

    override suspend fun write(data: String) {
        if (closed.get()) {
            throw SimpleSocket.SocketClosedException()
        }
    }

    override val incoming: SharedFlow<String>
        get() = MutableSharedFlow()

    override fun close() {
        closed.set(true)
    }
}
