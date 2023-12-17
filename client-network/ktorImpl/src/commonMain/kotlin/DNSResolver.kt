package com.xingpeds.kmirc.clientnetwork

fun DNSLookupFun() = DnsResolverAdapter

object DnsResolverAdapter : DNSLookupFun {
    override fun invoke(hostname: String): List<Address> = dnsResolver(hostname)
}

expect fun dnsResolver(hostname: String): List<Address>