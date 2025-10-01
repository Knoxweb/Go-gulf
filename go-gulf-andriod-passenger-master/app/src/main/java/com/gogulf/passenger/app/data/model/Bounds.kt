package com.gogulf.passenger.app.data.model

import java.io.Serializable

data class Bounds(
    val northeast: Northeast? = null,
    val southwest: Southwest? = null
): Serializable