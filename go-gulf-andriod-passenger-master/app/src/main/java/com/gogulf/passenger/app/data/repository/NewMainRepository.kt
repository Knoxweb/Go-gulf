package com.gogulf.passenger.app.data.repository

import android.util.Log
import com.google.gson.GsonBuilder
import com.gogulf.passenger.app.utils.SlyykDeviceInfo
import com.gogulf.passenger.app.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NewMainRepository {

    private var retrofit: Retrofit? = null
    var baseUrl = BuildConfig.BASE_URL
    fun getAPIClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            ).client(getOkHttpClient()).build()
        }
        return retrofit!!
    }

    private fun getOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor { chain ->
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()

                requestBuilder.addHeader(
                    "DeviceName",
                    "${SlyykDeviceInfo.getManufacturer()} ${SlyykDeviceInfo.getBrand()} ${SlyykDeviceInfo.getModel()}"
                )

                // Add headers
//                val token = Preferences.getPreference(App.baseApplication, PrefEntity.AUTH_TOKEN)
//                requestBuilder.addHeader("Authorization", "Bearer $token")
//                requestBuilder.addHeader("DeviceType", "android")
//                requestBuilder.addHeader("ExtraInfo", getDeviceDetails())
//                requestBuilder.addHeader(
//                    "AppToken", "RK9AIh0GFJ640FZhe1eSTUpgoU1h48OmwqrbbDFGL9zqbtREix1hxUr"
//                )

                val request = requestBuilder.build()

                // Log all headers
                request.headers.forEach { header ->
                    Log.d("OkHttpClient", "Header: ${header.first} = ${header.second}")
                }

                chain.proceed(request)
            }
            .build()
    }




//    private fun getOkHttpClient(): OkHttpClient {
//        val httpLoggingInterceptor = HttpLoggingInterceptor()
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//        return OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
//            .addInterceptor(httpLoggingInterceptor).addNetworkInterceptor { chain ->
//                val request = chain.request().newBuilder()
//                request.method(chain.request().method, chain.request().body)
//                val token = Preferences.getPreference(App.baseApplication, PrefEntity.AUTH_TOKEN)
//                request.addHeader("Authorization", "Bearer $token")
//                request.addHeader(
//                    "AppToken", "RK9AIh0GFJ640FZhe1eSTUpgoU1h48OmwqrbbDFGL9zqbtREix1hxUr"
//                )
//                request.build()
//                chain.proceed(request.build())
//            }.readTimeout(30, TimeUnit.SECONDS).build()
//    }


    // No Log
//    private fun getOkHttpClientNoLog(): OkHttpClient {
//        return OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).addNetworkInterceptor {
//            val request = it.request().newBuilder()
//              val token =
//                      preferenceHelper.getValue(PrefConstant.AUTH_TOKEN, APIConstants.static_token) as String
//            request.addHeader("Authorization", "Bearer $token")
//            request.addHeader("AppToken", "RK9AIh0GFJ640FZhe1eSTUpgoU1h48OmwqrbbDFGL9zqbtREix1hxUr")
//            it.proceed(request.build())
//        }.readTimeout(30, TimeUnit.SECONDS).build()
//    }

}