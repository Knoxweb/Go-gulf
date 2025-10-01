package com.gogulf.passenger.app.data.api

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface NewApiService {
    @GET
    suspend fun get(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @QueryMap params: Map<String, String>
    ): Response<JsonElement>

    @POST
    suspend fun post(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body body: JsonObject,
    ): Response<JsonElement>

    @POST
    suspend fun post(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody,
    ): Response<JsonElement>
}