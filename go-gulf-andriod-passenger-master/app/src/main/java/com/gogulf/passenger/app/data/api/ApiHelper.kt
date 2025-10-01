package com.gogulf.passenger.app.data.api

import com.gogulf.passenger.app.data.model.base.BaseData
import com.gogulf.passenger.app.data.model.dashboards.DashboardModel
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ApiHelper {

    suspend fun postMethod(
        url: String,
        headers: HashMap<String, String>,
        body: JsonObject
    ): Observable<JsonObject>


    suspend fun postMethodFile(
        url: String,
        headers: HashMap<String, String>,
        body: HashMap<String, RequestBody>,
        files: List<MultipartBody.Part>
    ): Observable<JsonObject>


    suspend fun postMethodFile(
        url: String,
        headers: HashMap<String, String>,
        body: HashMap<String, RequestBody>,
        files: HashMap<String, MultipartBody.Part>
    ): Observable<JsonObject>

    suspend fun postMethodFileDocs(
        url: String,
        headers: HashMap<String, String>,
        body: HashMap<String, List<HashMap<String, RequestBody>>>
    ): Observable<JsonObject>

    suspend fun postMethodFileDocs2(
        url: String,
        headers: HashMap<String, String>,
        body: HashMap<String, RequestBody>,
        files: List<MultipartBody.Part>
    ): Observable<JsonArray>

    suspend fun postMethodFileDocs2Any(
        url: String,
        headers: HashMap<String, String>,
        body: HashMap<String, RequestBody>,
        files: List<MultipartBody.Part>
    ): Observable<Any>

    suspend fun postMethodFileArray(
        url: String,
        headers: HashMap<String, String>,
        body: List<HashMap<String, RequestBody>>,
        files: List<MultipartBody.Part>
    ): Observable<JsonObject>

    suspend fun postMethodRegister(
        url: String,
        headers: HashMap<String, String>,
        body: HashMap<String, RequestBody>,
        files: MultipartBody.Part
    ): Observable<JsonObject>


    suspend fun postMethodRegister(
        url: String,
        headers: HashMap<String, String>,
        body: HashMap<String, RequestBody>
    ): Observable<JsonObject>

    suspend fun getMethod(url: String, headers: HashMap<String, String>): Observable<JsonObject>
    suspend fun getMethodState(url: String, headers: HashMap<String, String>): BaseData<DashboardModel>

    suspend fun getMethodArray(url: String, headers: HashMap<String, String>): Observable<JsonArray>
}