package com.xingpeds.kmirc.engine

import kotlinx.coroutines.flow.MutableStateFlow

class ChannelState(
    val name: String,
    val topic: MutableStateFlow<String> = MutableStateFlow("")
) {

}
