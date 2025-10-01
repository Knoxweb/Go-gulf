package com.gogulf.passenger.app.utils.others

import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.utils.enums.Status

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val title: String?
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null, null)
        }

        fun <T> error(msg: String, data: T?, title: String = "Error"): Resource<T> {
            return Resource(Status.ERROR, data, msg, title)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null, null)
        }

        fun <T> newError(msg: String, title: String = "Invalid"): Error {
            return com.gogulf.passenger.app.data.model.Error(title, message = msg)
        }
    }
}