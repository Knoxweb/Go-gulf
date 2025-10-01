package com.gogulf.passenger.app.data.model

import java.io.Serializable

data class Route(
    val bounds: Bounds? = null,
    val overview_polyline: OverviewPolyline? = null
): Serializable