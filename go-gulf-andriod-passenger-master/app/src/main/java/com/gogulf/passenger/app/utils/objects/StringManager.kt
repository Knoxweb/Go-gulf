package com.gogulf.passenger.app.utils.objects

object StringManager {
    fun concatSameString(from: String?, to: String?): String {
        val endData = to ?: ""
        val startDate = from ?: ""

        return if (endData.startsWith(startDate))
            endData.replace(startDate, startDate)
        else "$startDate, $endData"
    }
}