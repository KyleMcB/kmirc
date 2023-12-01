package com.xingpeds.kmirc.clientnetwork
import java.net.InetAddress

class JavaAddress(val inet:InetAddress) : Address {
    override val hostAddress: String
        get() = inet.hostAddress
}

actual object DNSResolver : DNSLookupFun {
     actual override fun invoke(hostname: String): List<Address> {

        return InetAddress.getAllByName(hostname).map {
            JavaAddress(it)
        }
    }


}