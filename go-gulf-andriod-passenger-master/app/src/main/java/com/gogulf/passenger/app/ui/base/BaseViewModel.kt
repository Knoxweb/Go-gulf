package com.gogulf.passenger.app.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.data.apidata.NetworkError
import com.gogulf.passenger.app.data.internal.PreferenceHelper
import com.gogulf.passenger.app.data.model.others.ErrorModel
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.disposables.CompositeDisposable
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException


abstract class BaseViewModel<N> : ViewModel() {
    private var compositeDisposable = CompositeDisposable()
    private lateinit var mNavigator: WeakReference<N>
    private var liveErrorResponse = MutableLiveData<String>()
    val preferenceHelper = PreferenceHelper(App.baseApplication)
//    val appRepository = AppRepository()

     fun jsonParsers(data: String): ErrorModel {
        return try {
            val jsonParser = JsonParser()
            val jsonObject = jsonParser.parse(data).asJsonObject
            Gson().fromJson(
                jsonObject,
                object : TypeToken<ErrorModel>() {}.type
            )
        } catch (e: Exception) {
            ErrorModel("API Error", e.message.toString())
        }

    }
    var navigator: N
        get() = mNavigator.get()!!
        set(navigator) {
            this.mNavigator = WeakReference(navigator)
        }

    fun getCompositeDisposable() = compositeDisposable

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getErrorObservable(): MutableLiveData<String> {
        return liveErrorResponse
    }


    fun getErrorMessage(e: Throwable): String? {
        return when (e) {
            is JsonSyntaxException -> {
                NetworkError.DATA_EXCEPTION
            }
            is HttpException -> {
                val responseBody = e.response()!!.errorBody()
                getErrorMessage(responseBody!!)
            }
            is SocketTimeoutException -> NetworkError.TIME_OUT
            is IOException -> NetworkError.IO_EXCEPTION
            else -> {
                NetworkError.SERVER_EXCEPTION
            }
        }
    }

    private fun getErrorMessage(responseBody: ResponseBody): String? {
        return try {
            val jsonObject = JSONObject(responseBody.string())
            jsonObject.getString("message")
        } catch (e: Exception) {
            e.message
        }
    }
}