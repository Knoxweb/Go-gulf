package com.gogulf.passenger.app.di.module

import android.content.Context
import com.gogulf.passenger.app.App.Companion.is403
import com.gogulf.passenger.app.data.api.ApiHelper
import com.gogulf.passenger.app.data.api.ApiHelperImpl
import com.gogulf.passenger.app.data.api.ApiService
import com.gogulf.passenger.app.data.apidata.NetworkError.DATA_EXCEPTION
import com.gogulf.passenger.app.data.apidata.NetworkError.IO_EXCEPTION
import com.gogulf.passenger.app.data.apidata.NetworkError.SERVER_EXCEPTION
import com.gogulf.passenger.app.data.apidata.NetworkError.TIME_OUT
import com.gogulf.passenger.app.data.internal.PreferenceHelper
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


val appModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get(), BuildConfig.BASE_URL) }
    single { provideApiService(get()) }
    single { provideNetworkHelper(androidContext()) }
    single { PreferenceHelper(androidContext()) }
//    single { MainRepository(get()) }
//    single { NetworkHelper(androidContext()) }

    single<ApiHelper> {
        return@single ApiHelperImpl(get())
    }
}

private fun provideNetworkHelper(context: Context) = NetworkHelper(context)

private fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.MINUTES)
        .readTimeout(10, TimeUnit.MINUTES)
        .writeTimeout(10, TimeUnit.MINUTES)
        .addInterceptor(loggingInterceptor)
        .build()
} else OkHttpClient
    .Builder()
    .connectTimeout(10, TimeUnit.MINUTES)
    .readTimeout(10, TimeUnit.MINUTES)
    .writeTimeout(10, TimeUnit.MINUTES)
    .build()

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    BASE_URL: String
): Retrofit =
    Retrofit.Builder()
//        .addConverterFactory(MoshiConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

private fun provideApiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)

fun getErrorMessage(e: Throwable): String? {
    return when (e) {
        is JsonSyntaxException -> {
            val jObj = JsonObject()
            jObj.addProperty("title", "JsonSyntaxException")
            jObj.addProperty("message", DATA_EXCEPTION)
            jObj.toString()

        }
        is HttpException -> {
            val responseBody = e.response()!!.errorBody()
            val code = e.response()!!.code().toString()
            if (code == "403") {
                is403.postValue(true)
//               preferenceHelper.setValue(PrefConstant.IS_LOGGED_IN, true)
            } else {
                is403.postValue(false)
//                preferenceHelper.setValue(PrefConstant.IS_LOGGED_IN, false)
            }
            getErrorMessage(responseBody!!)
        }
        is SocketTimeoutException -> {
            val jObj = JsonObject()
            jObj.addProperty("title", "Socket Timeout")
            jObj.addProperty("message", TIME_OUT)
            jObj.toString()
//            TIME_OUT
        }
        is IOException -> {
            val jObj = JsonObject()
            jObj.addProperty("title", "IO Error")
            jObj.addProperty("message", IO_EXCEPTION)
            jObj.toString()
//            IO_EXCEPTION
        }
        else -> {
            val jObj = JsonObject()
            jObj.addProperty("title", "Server Error")
            jObj.addProperty("message", SERVER_EXCEPTION)
            jObj.toString()
//            SERVER_EXCEPTION
        }
    }
}

private fun getErrorMessage(responseBody: ResponseBody): String? {
    return try {
        val jsonObject = JSONObject(responseBody.string())
        jsonObject.getString("title")
        jsonObject.getString("message")
        val jObj = JsonObject()
        jObj.addProperty("title", jsonObject.getString("title"))
        jObj.addProperty("message", jsonObject.getString("message"))
        jObj.toString()
    } catch (e: Exception) {
        DebugMode.e("AppModule", "error -> ${e.message.toString()}")
        val jObj = JsonObject()
        jObj.addProperty("title", "Error")
        jObj.addProperty("message", "Something went wrong.")
        jObj.toString()
    }
}
