/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.clientnetwork


object DnsResolverAdapter : DNSLookupFun {
    override fun invoke(hostname: String): List<Address> = dnsResolver(hostname)
}

expect fun dnsResolver(hostname: String): List<Address>