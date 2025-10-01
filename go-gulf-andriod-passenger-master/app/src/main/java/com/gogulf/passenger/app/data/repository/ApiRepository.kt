package com.gogulf.passenger.app.data.repository

import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.BuildConfig
import com.gogulf.passenger.app.data.api.NewApiService
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.utils.ApiResponse
import com.gogulf.passenger.app.utils.PrefEntity
import com.gogulf.passenger.app.utils.Preferences
import com.gogulf.passenger.app.utils.getDeviceDetails
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException


class ApiRepository {

    val apiService: NewApiService =
        NewMainRepository.getAPIClient().create(NewApiService::class.java)
    val appToken: String = BuildConfig.AppToken
    var authorization: String = ""
    var deviceType = "android"
    var extraInfo = getDeviceDetails()

    init {
        val token = Preferences.getPreference(App.baseApplication, PrefEntity.AUTH_TOKEN)
        authorization = "Bearer $token"
    }

    suspend inline fun <reified T> get(
        defaultRequestModel: DefaultRequestModel
    ): ApiResponse<T> {
        try {
            defaultRequestModel.headers["Authorization"] = authorization
            defaultRequestModel.headers["AppToken"] = appToken
            defaultRequestModel.headers["DeviceType"] = deviceType
            defaultRequestModel.headers["ExtraInfo"] = extraInfo
            val response = apiService.get(
                defaultRequestModel.url, defaultRequestModel.headers, defaultRequestModel.params
            )
            if (response.body() != null) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val gson = Gson()
                        val type = object : TypeToken<T>() {}.type
                        val data: T = gson.fromJson(it, type)
                        return ApiResponse.Success(data)
                    }
                } else {
                    val responseBody = response.errorBody()
                    responseBody?.let {
                        val gson = Gson()
                        val type = object : TypeToken<T>() {}.type
                        val data: T = gson.fromJson(it.charStream(), type)
                        recordExceptionToFirebase(
                            defaultRequestModel, data.toString(), response, T::class.java.simpleName
                        )
                        return ApiResponse.Failure(data)
                    }
                }
            } else {
                val responseBody = response.errorBody()
                responseBody?.let {
                    val gson = Gson()
                    val type = object : TypeToken<T>() {}.type
                    val data: T = gson.fromJson(it.charStream(), type)
                    recordExceptionToFirebase(
                        defaultRequestModel, data.toString(), response, T::class.java.simpleName
                    )
                    return ApiResponse.Failure(data)
                }
            }
            Firebase.crashlytics.recordException(Exception(response.message()))
            return ApiResponse.Error(Exception(response.message()))
        } catch (e: UnknownHostException) {
            Firebase.crashlytics.recordException(e)
            return ApiResponse.Error(Exception("No internet connection"))
        } catch (e: IOException) {
            Firebase.crashlytics.recordException(e)
            return ApiResponse.Error(Exception("Network error"))
        } catch (e: Exception) {
            Firebase.crashlytics.recordException(e)
            return ApiResponse.Error(Exception(e.message))
        }
    }

    suspend inline fun <reified T> get(
        defaultRequestModel: DefaultRequestModel,
        clazz: Class<T>,
    ): ApiResponse<T> {
        try {
            var response: Response<JsonElement>? = null
            defaultRequestModel.headers["Authorization"] = authorization
            defaultRequestModel.headers["AppToken"] = appToken
            defaultRequestModel.headers["DeviceType"] = deviceType
            defaultRequestModel.headers["ExtraInfo"] = extraInfo

            response = apiService.get(
                defaultRequestModel.url, defaultRequestModel.headers, defaultRequestModel.params
            )

            if (response.body() != null) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val gson = Gson()
//                        val type = object : TypeToken<T>() {}.type
                        val data: T = gson.fromJson(it, clazz)
                        return ApiResponse.Success(data)
                    }
                } else {
                    val responseBody = response.errorBody()
                    responseBody?.let {
                        val gson = Gson()
                        val data: T = gson.fromJson(it.charStream(), clazz)
                        recordExceptionToFirebase(
                            defaultRequestModel, data.toString(), response, T::class.java.simpleName
                        )
                        return ApiResponse.Failure(data)
                    }
                }
            } else {
                val responseBody = response.errorBody()
                responseBody?.let {
                    val gson = Gson()
                    val type = object : TypeToken<T>() {}.type
                    val data: T = gson.fromJson(it.charStream(), clazz)
                    recordExceptionToFirebase(
                        defaultRequestModel, data.toString(), response, T::class.java.simpleName
                    )

                    return ApiResponse.Failure(data)
                }
            }
            Firebase.crashlytics.recordException(Exception(response.message()))

            return ApiResponse.Error(Exception(response.message()))
        } catch (e: UnknownHostException) {
            Firebase.crashlytics.recordException(e)
            return ApiResponse.Error(Exception("No internet connection"))
        } catch (e: IOException) {
            Firebase.crashlytics.recordException(e)
            return ApiResponse.Error(Exception("Network error"))
        } catch (e: Exception) {
            Firebase.crashlytics.recordException(e)
            return ApiResponse.Error(Exception(e.message))
        }
    }


    suspend inline fun <reified T> post(
        defaultRequestModel: DefaultRequestModel,
        clazz: Class<T>,
    ): ApiResponse<T> {
        try {
            var response: Response<JsonElement>? = null
            defaultRequestModel.headers["Authorization"] = authorization
            defaultRequestModel.headers["AppToken"] = appToken
            defaultRequestModel.headers["DeviceType"] = deviceType
            defaultRequestModel.headers["ExtraInfo"] = extraInfo

            if (defaultRequestModel.multipartBody != null) {
                response = apiService.post(
                    defaultRequestModel.url,
                    defaultRequestModel.headers,
                    defaultRequestModel.multipartBody!!
                )
            } else {
                response = apiService.post(
                    defaultRequestModel.url, defaultRequestModel.headers, defaultRequestModel.body
                )
            }
            if (response.body() != null) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val gson = Gson()
//                        val type = object : TypeToken<T>() {}.type
                        val data: T = gson.fromJson(it, clazz)
                        return ApiResponse.Success(data)
                    }
                } else {
                    val responseBody = response.errorBody()
                    responseBody?.let {
                        val gson = Gson()
                        val data: T = gson.fromJson(it.charStream(), clazz)
                        recordExceptionToFirebase(
                            defaultRequestModel, data.toString(), response, T::class.java.simpleName
                        )

                        return ApiResponse.Failure(data)
                    }
                }
            } else {
                val responseBody = response.errorBody()
                responseBody?.let {
                    val gson = Gson()
                    val type = object : TypeToken<T>() {}.type
                    val data: T = gson.fromJson(it.charStream(), clazz)
                    recordExceptionToFirebase(
                        defaultRequestModel, data.toString(), response, T::class.java.simpleName
                    )

                    return ApiResponse.Failure(data)
                }
            }
            Firebase.crashlytics.recordException(Exception(response.message()))

            return ApiResponse.Error(Exception(response.message()))
        } catch (e: UnknownHostException) {
            Firebase.crashlytics.recordException(e)
            return ApiResponse.Error(Exception("No internet connection"))
        } catch (e: IOException) {
            Firebase.crashlytics.recordException(e)
            return ApiResponse.Error(Exception("Network error"))
        } catch (e: Exception) {
            Firebase.crashlytics.recordException(e)
            return ApiResponse.Error(Exception(e.message))
        }
    }

    fun recordExceptionToFirebase(
        defaultRequestModel: DefaultRequestModel,
        data: String,
        response: Response<JsonElement>,
        className: String
    ) {

//        val jObjError = JSONObject(response.errorBody()!!.string())
//        Log.e("Tee", "recordExceptionToFirebase: ${response.errorBody()?.contentLength()}")

        val errorMessage = """
    API Failure from server:
    URL: ${defaultRequestModel.url}
    Request Headers: ${defaultRequestModel.headers.map { "${it.key}: ${it.value}" }}
    Request Params: ${defaultRequestModel.params.map { "${it.key}: ${it.value}" }}
    Response Code: ${response.code()}
    Response Headers: ${
            response.headers().joinToString { "${it.first}: ${it.second}" }
        }
    Response Error Body: ${
            Gson().toJson(response.errorBody()!!.charStream().use { reader ->
                reader.readText()
            })
        }
        
    New Response Error Body: ${response.errorBody()?.byteString()}
    Response Raw Body: ${response.raw() ?: "No Response Body"}
    Serialized Body: $data
    Class Name: $className
""".trimIndent()
        Firebase.crashlytics.recordException(Exception(errorMessage))
    }
}
