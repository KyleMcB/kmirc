package com.xingpeds.kmirc.clientnetwork

import java.net.InetAddress

actual fun dnsResolver(hostname: String): List<Address> {
    return InetAddress.getAllByName(hostname).map {
        JavaAddressAdapter(it)
    }
}

data class JavaAddressAdapter(val inet: InetAddress) : Address {
    override val hostAddress: String
        get() = inet.hostAddress
}