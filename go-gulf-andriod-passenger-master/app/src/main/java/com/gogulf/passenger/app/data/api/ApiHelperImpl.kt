package com.gogulf.passenger.app.data.api

import com.gogulf.passenger.app.data.model.base.BaseData
import com.gogulf.passenger.app.data.model.dashboards.DashboardModel
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override suspend fun postMethod(
        url: String,
        headers: HashMap<String, String>,
        body: JsonObject
    ): Observable<JsonObject> = apiService.postMethod(url, headers, body)

    override suspend fun postMethodFile(
        url: String,
        headers: HashMap<String, String>,
        body: HashMap<String, RequestBody>,
        files: List<MultipartBody.Part>
    ): Observable<JsonObject> = apiService.postMethodFile(url, headers, body, files)

    override suspend fun postMethodFile(
        url: String,
        headers: HashMap<String, String>,
        body: HashMap<String, RequestBody>,
        files: HashMap<String, MultipartBody.Part>
    ): Observable<JsonObject> = apiService.postMethodFile(url, headers, body, files)

    override suspend fun postMethodFileDocs(
        url: String,
        headers: HashMap<String, String>,
        body: HashMap<String, List<HashMap<String, RequestBody>>>
    ): Observable<JsonObject> = apiService.postMethodFileDocs(url, headers, body)

    override suspend fun postMethodFileDocs2(
        url: String,
        headers: HashMap<String, String>,
        body: HashMap<String, RequestBody>,
        files: List<MultipartBody.Part>
    ): Observable<JsonArray> = apiService.postMethodFileDocs2(url, headers, body, files)

    override suspend fun postMethodFileDocs2Any(
        url: String,
        headers: HashMap<String, String>,
        body: HashMap<String, RequestBody>,
        files: List<MultipartBody.Part>
    ): Observable<Any> = apiService.postMethodFileDocs2Any(url, headers, body, files)

    override suspend fun postMethodFileArray(
        url: String,
        headers: HashMap<String, String>,
        body: List<HashMap<String, RequestBody>>,
        files: List<MultipartBody.Part>
    ): Observable<JsonObject> = apiService.postMethodFileArray(url, headers, body, files)

    override suspend fun postMethodRegister(
        url: String,
        headers: HashMap<String, String>,
        body: HashMap<String, RequestBody>,
        files: MultipartBody.Part
    ): Observable<JsonObject> = apiService.postMethodRegister(url, headers, body, files)

    override suspend fun postMethodRegister(
        url: String,
        headers: HashMap<String, String>,
        body: HashMap<String, RequestBody>
    ): Observable<JsonObject> = apiService.postMethodRegister(url, headers, body)

    override suspend fun getMethod(
        url: String,
        headers: HashMap<String, String>
    ): Observable<JsonObject> = apiService.getMethod(url, headers)

    override suspend fun getMethodState(url: String, headers: HashMap<String, String>): BaseData<DashboardModel> =
        apiService.getMethodState(url, headers)

    override suspend fun getMethodArray(
        url: String,
        headers: HashMap<String, String>
    ): Observable<JsonArray> = apiService.getMethodArray(url, headers)

}