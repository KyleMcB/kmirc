/*
 * Copyright 2024 Kyle McBurnett
 */

import com.xingpeds.kmirc.entities.IrcPrefix
import io.kotest.property.Arb
import io.kotest.property.RandomSource
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.domain
import io.kotest.property.arbitrary.string

/*
 * Copyright 2024 Kyle McBurnett
 */


val nickPrefixArb = arbitrary { rs: RandomSource ->
    IrcPrefix(nick = Arb.string().bind(), user = Arb.string().bind(), host = Arb.string().bind())
}
val serverPrefixArb = arbitrary {
    IrcPrefix(nick = Arb.domain().bind())
}