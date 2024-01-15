/*
 * Copyright 2024 Kyle McBurnett
 */

package com.xingpeds.kmirc.clientnetwork

fun getConnectFun(): Connect = KtorSocketFactory
fun getDNSLookupFun(): DNSLookupFun = DnsResolverAdapter