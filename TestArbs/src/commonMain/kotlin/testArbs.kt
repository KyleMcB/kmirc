/*
 * Copyright 2024 Kyle McBurnett
 */

import com.xingpeds.kmirc.entities.IrcPrefix
import com.xingpeds.kmirc.entities.IrcUser
import io.kotest.property.Arb
import io.kotest.property.RandomSource
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.domain
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.string


val nickPrefixArb = arbitrary { rs: RandomSource ->
    IrcPrefix(nickOrServer = Arb.string().bind(), user = Arb.string().bind(), host = Arb.string().bind())
}
val serverPrefixArb = arbitrary {
    IrcPrefix(nickOrServer = Arb.domain().bind())
}
val ircUserArb = arbitrary {

    IrcUser(
        nick = Arb.string().bind(),
        username = Arb.string().orNull().bind(),
        hostname = Arb.string().orNull().bind(),
        realName = Arb.string().orNull().bind()
    )
}