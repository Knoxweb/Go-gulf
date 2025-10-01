package com.gogulf.passenger.app.data.apidata

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DefaultRequestModel {
    var url: String = ""
    var headers: HashMap<String, String> = HashMap()
    var body: JsonObject = JsonObject()
    var setToken: Boolean = true
    var fileBody = HashMap<String, RequestBody>()
    var fileBodyDocs = HashMap<String, List<HashMap<String, RequestBody>>>()
    var fileBodyDocs2 = HashMap<String, List<RequestBody>>()
    var fileBodyList = ArrayList<HashMap<String, RequestBody>>()
    var files = ArrayList<MultipartBody.Part>()
    var multipleFile: HashMap<String, MultipartBody.Part> = HashMap()
    var multipleFileDriver: List<MultipartBody.Part> = ArrayList<MultipartBody.Part>()
    var file: MultipartBody.Part? = null
    var params: Map<String, String> = emptyMap()
    var multipartBody: MultipartBody? = null


}