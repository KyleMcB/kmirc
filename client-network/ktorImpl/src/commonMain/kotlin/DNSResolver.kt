package com.xingpeds.kmirc.clientnetwork

expect object DNSResolver : DNSLookupFun {
    override fun invoke(hostname: String): List<Address>

}