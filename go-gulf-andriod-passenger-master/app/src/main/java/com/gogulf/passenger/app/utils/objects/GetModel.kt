package com.gogulf.passenger.app.utils.objects

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.gogulf.passenger.app.data.model.base.BaseArray
import com.gogulf.passenger.app.data.model.base.BaseData
import com.gogulf.passenger.app.R

object GetResponseModel {
    fun model(data: JsonObject?): R {
        return Gson().fromJson(
            data,
            object : TypeToken<R>() {}.type
        )
    }

    inline fun <reified T> toObject(data: JsonObject?): BaseData<T> {
        return Gson().fromJson(
            data,
            object : TypeToken<BaseData<T>>() {}.type
        )
    }

    inline fun <reified T> toArray(data: JsonObject?): BaseArray<T> {
        return Gson().fromJson(
            data,
            object : TypeToken<BaseArray<T>>() {}.type
        )
    }

}
