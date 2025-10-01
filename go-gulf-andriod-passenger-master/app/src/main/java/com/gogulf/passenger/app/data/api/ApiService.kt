package com.gogulf.passenger.app.data.api

import com.gogulf.passenger.app.data.model.base.BaseData
import com.gogulf.passenger.app.data.model.dashboards.DashboardModel
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST()
    fun postMethod(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>,
        @Body jsonObject: JsonObject
    ): Observable<JsonObject>

    @Multipart
    @POST()
    fun postMethodFile(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>,
        @PartMap body: HashMap<String, RequestBody>,
        @Part files: List<MultipartBody.Part>
    ): Observable<JsonObject>

    @Multipart
    @POST()
    fun postMethodFileDocs(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>,
        @PartMap body: HashMap<String, List<HashMap<String, RequestBody>>>
    ): Observable<JsonObject>

    @Multipart
    @POST()
    fun postMethodFileDocs2(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>,
        @PartMap body: HashMap<String, RequestBody>,
        @Part files: List<MultipartBody.Part>
    ): Observable<JsonArray>

    @Multipart
    @POST()
    fun postMethodFileDocs2Any(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>,
        @PartMap body: HashMap<String, RequestBody>,
        @Part files: List<MultipartBody.Part>
    ): Observable<Any>


    @Multipart
    @POST()
    fun postMethodFile(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>,
        @PartMap body: HashMap<String, RequestBody>,
        @Part files: HashMap<String, MultipartBody.Part>
    ): Observable<JsonObject>


    @Multipart
    @POST()
    fun postMethodFileArray(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>,
        @PartMap body: List<HashMap<String, RequestBody>>,
        @Part files: List<MultipartBody.Part>
    ): Observable<JsonObject>

    @Multipart
    @POST()
    fun postMethodRegister(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>,
        @PartMap body: HashMap<String, RequestBody>,
        @Part files: MultipartBody.Part
    ): Observable<JsonObject>

    @Multipart
    @POST()
    fun postMethodRegister(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>,
        @PartMap body: HashMap<String, RequestBody>
    ): Observable<JsonObject>

    @GET()
    fun getMethod(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>
    ): Observable<JsonObject>

    @GET()
    fun getMethodState(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>
    ): BaseData<DashboardModel>

    @GET()
    fun getMethodArray(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>
    ): Observable<JsonArray>
}