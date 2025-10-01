package com.gogulf.passenger.app.data.repository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.data.api.ApiHelper
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.internal.PreferenceHelper
import com.gogulf.passenger.app.data.model.base.BaseData
import com.gogulf.passenger.app.data.model.dashboards.DashboardModel
import com.gogulf.passenger.app.data.model.others.ErrorModel
import com.gogulf.passenger.app.di.module.getErrorMessage
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.gogulf.passenger.app.utils.others.Resource
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


@SuppressLint("CheckResult")
class MainRepository(private val apiHelper: ApiHelper) {

    private val preferenceHelper = PreferenceHelper(App.baseApplication)

    private fun jsonParsers(data: String): ErrorModel {
        return try {
//            val jsonParser = JsonParser()
            val jsonObject = JsonParser.parseString(data).asJsonObject
            Gson().fromJson(
                jsonObject,
                object : TypeToken<ErrorModel>() {}.type
            )
        } catch (e: Exception) {
            ErrorModel("API Error", e.message.toString())
        }

    }

    @SuppressLint("CheckResult")
    suspend fun postMethod(
        defaultRequestModel: DefaultRequestModel,
        values: MutableLiveData<Resource<JsonObject>>
    ) {
        defaultRequestModel.headers = HashMap()
        if (defaultRequestModel.setToken) {
            defaultRequestModel.headers["Authorization"] =
                "Bearer ${preferenceHelper.getValue(PrefConstant.AUTH_TOKEN, "") as String}"
        }

        DebugMode.e("postMethod", "body" + defaultRequestModel.body, "postMethod")
        DebugMode.e("postMethod", "url" + defaultRequestModel.url, "postMethod")
        DebugMode.e("postMethod", "header" + defaultRequestModel.headers, "postMethod")
        values.postValue(Resource.loading(null))
        apiHelper.postMethod(
            defaultRequestModel.url,
            defaultRequestModel.headers,
            defaultRequestModel.body
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /*.map { values.postValue(Resource.success(it)) }
            .doOnError {
                val model = jsonParsers(getErrorMessage(it)!!)
                values.postValue(Resource.error(model.message, null, model.title))
            }*/
            .subscribe({
                DebugMode.e("MainRepository", "onSuccess  $it")
                values.postValue(Resource.success(it))
            }, {
                val model = jsonParsers(getErrorMessage(it)!!)
                values.postValue(Resource.error(model.message, null, model.title))
//                values.postValue(Resource.error(getErrorMessage(it)!!, null))
            })


    }

    suspend fun getMethodState(
        defaultRequestModel: DefaultRequestModel
    ): Flow<BaseData<DashboardModel>> {
        defaultRequestModel.headers = HashMap()
        if (defaultRequestModel.setToken) {
            defaultRequestModel.headers["Authorization"] =
                "Bearer ${preferenceHelper.getValue(PrefConstant.AUTH_TOKEN, "") as String}"
        }


        DebugMode.e("getMethodState", "body" + defaultRequestModel.body, "getMethodState")
        DebugMode.e("getMethodState", "url" + defaultRequestModel.url, "getMethodState")
        DebugMode.e("getMethodState", "header" + defaultRequestModel.headers, "getMethodState")
        return flow {
            emit(apiHelper.getMethodState(defaultRequestModel.url, defaultRequestModel.headers))
        }.flowOn(Dispatchers.IO)

    }

    suspend fun postMethodFiles(
        defaultRequestModel: DefaultRequestModel,
        values: MutableLiveData<Resource<JsonObject>>
    ) {
        defaultRequestModel.headers = HashMap()
        if (defaultRequestModel.setToken) {
            defaultRequestModel.headers["Authorization"] =
                "Bearer ${preferenceHelper.getValue(PrefConstant.AUTH_TOKEN, "") as String}"
        }

        /**
         *
         *  hashMap.put(
        "first_name",
        RequestBody.create("text/plain".toMediaTypeOrNull(), firstNameString)
        )

        val pictureFile = File(localPath?.path)
        val requestFile = RequestBody.create(
        "* / *".toMediaTypeOrNull(),
        pictureFile
        )

        val fileBody =
        MultipartBody.Part.createFormData("picture", pictureFile.name, requestFile)
        accountViewModel.createAccountData(fileBody)

         * */

        DebugMode.e("Requestdata", "body" + defaultRequestModel.files, "postMethodFiles")
        DebugMode.e("Requestdata", "header" + defaultRequestModel.headers, "postMethodFiles")
        values.postValue(Resource.loading(null))
        apiHelper.postMethodFile(
            defaultRequestModel.url,
            defaultRequestModel.headers,
            defaultRequestModel.fileBody,
            defaultRequestModel.files
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                DebugMode.e("MainRepository", "onSuccess  $it")
                values.postValue(Resource.success(it))
            }, {
//                values.postValue(Resource.error(getErrorMessage(it)!!, null))
                val model = jsonParsers(getErrorMessage(it)!!)
                values.postValue(Resource.error(model.message, null, model.title))
            })


    }

    suspend fun postMethodFilesMultiple(
        defaultRequestModel: DefaultRequestModel,
        values: MutableLiveData<Resource<JsonObject>>
    ) {
        defaultRequestModel.headers = HashMap()
        if (defaultRequestModel.setToken) {
            defaultRequestModel.headers["Authorization"] =
                "Bearer ${preferenceHelper.getValue(PrefConstant.AUTH_TOKEN, "") as String}"
        }



        DebugMode.e("Requestdata", "body" + defaultRequestModel.fileBody, "postMethodFilesMultiple")
        DebugMode.e(
            "Requestdata",
            "header" + defaultRequestModel.headers,
            "postMethodFilesMultiple"
        )
        values.postValue(Resource.loading(null))
        apiHelper.postMethodFile(
            defaultRequestModel.url,
            defaultRequestModel.headers,
            defaultRequestModel.fileBody,
            defaultRequestModel.multipleFile
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                DebugMode.e("MainRepository", "onSuccess  $it")
                values.postValue(Resource.success(it))
            }, {
//                values.postValue(Resource.error(getErrorMessage(it)!!, null))
                val model = jsonParsers(getErrorMessage(it)!!)
                values.postValue(Resource.error(model.message, null, model.title))
            })


    }

    suspend fun postMethodFilesMultipleDOCS(
        defaultRequestModel: DefaultRequestModel,
        values: MutableLiveData<Resource<JsonObject>>
    ) {
        defaultRequestModel.headers = HashMap()
        if (defaultRequestModel.setToken) {
            defaultRequestModel.headers["Authorization"] =
                "Bearer ${preferenceHelper.getValue(PrefConstant.AUTH_TOKEN, "") as String}"
        }



        DebugMode.e(
            "Requestdata",
            "body" + defaultRequestModel.fileBodyDocs,
            "postMethodFilesMultipleDOCS"
        )
        DebugMode.e(
            "Requestdata",
            "header" + defaultRequestModel.headers,
            "postMethodFilesMultipleDOCS"
        )
        values.postValue(Resource.loading(null))
        apiHelper.postMethodFileDocs(
            defaultRequestModel.url,
            defaultRequestModel.headers,
            defaultRequestModel.fileBodyDocs
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                DebugMode.e("MainRepository", "onSuccess  $it")
                values.postValue(Resource.success(it))
            }, {
//                values.postValue(Resource.error(getErrorMessage(it)!!, null))
                val model = jsonParsers(getErrorMessage(it)!!)
                values.postValue(Resource.error(model.message, null, model.title))
            })


    }

    suspend fun postMethodFilesMultipleDOCS2(
        defaultRequestModel: DefaultRequestModel,
        values: MutableLiveData<Resource<JsonArray>>
    ) {
        defaultRequestModel.headers = HashMap()
        if (defaultRequestModel.setToken) {
            defaultRequestModel.headers["Authorization"] =
                "Bearer ${preferenceHelper.getValue(PrefConstant.AUTH_TOKEN, "") as String}"
        }



        DebugMode.e(
            "Requestdata",
            "body" + defaultRequestModel.fileBody,
            "postMethodFilesMultipleDOCS2"
        )
        DebugMode.e(
            "Requestdata",
            "files" + defaultRequestModel.files,
            "postMethodFilesMultipleDOCS2"
        )
        DebugMode.e(
            "Requestdata",
            "header" + defaultRequestModel.headers,
            "postMethodFilesMultipleDOCS2"
        )
        values.postValue(Resource.loading(null))
        apiHelper.postMethodFileDocs2(
            defaultRequestModel.url,
            defaultRequestModel.headers,
            defaultRequestModel.fileBody,
            defaultRequestModel.files
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                DebugMode.e("MainRepository", "onSuccess  $it")
                values.postValue(Resource.success(it))
            }, {

//                values.postValue(Resource.error(getErrorMessage(it)!!, null))
                val model = jsonParsers(getErrorMessage(it)!!)
                values.postValue(Resource.error(model.message, null, model.title))
            })


    }

    suspend fun postMethodFilesMultipleDOCS2Any(
        defaultRequestModel: DefaultRequestModel,
        values: MutableLiveData<Resource<Any>>
    ) {
        defaultRequestModel.headers = HashMap()
        if (defaultRequestModel.setToken) {
            defaultRequestModel.headers["Authorization"] =
                "Bearer ${preferenceHelper.getValue(PrefConstant.AUTH_TOKEN, "") as String}"
        }



        DebugMode.e(
            "Requestdata",
            "body" + defaultRequestModel.fileBody,
            "postMethodFilesMultipleDOCS2Any"
        )
        DebugMode.e(
            "Requestdata",
            "files" + defaultRequestModel.files,
            "postMethodFilesMultipleDOCS2Any"
        )
        DebugMode.e(
            "Requestdata",
            "header" + defaultRequestModel.headers,
            "postMethodFilesMultipleDOCS2Any"
        )
        values.postValue(Resource.loading(null))
        apiHelper.postMethodFileDocs2Any(
            defaultRequestModel.url,
            defaultRequestModel.headers,
            defaultRequestModel.fileBody,
            defaultRequestModel.files
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                DebugMode.e("MainRepository", "onSuccess  $it")
                values.postValue(Resource.success(it))
            }, {

//                values.postValue(Resource.error(getErrorMessage(it)!!, null))
                val model = jsonParsers(getErrorMessage(it)!!)
                values.postValue(Resource.error(model.message, null, model.title))
            })


    }

    suspend fun postMethodFilesMultipleArray(
        defaultRequestModel: DefaultRequestModel,
        values: MutableLiveData<Resource<JsonObject>>
    ) {
        defaultRequestModel.headers = HashMap()
        if (defaultRequestModel.setToken) {
            defaultRequestModel.headers["Authorization"] =
                "Bearer ${preferenceHelper.getValue(PrefConstant.AUTH_TOKEN, "") as String}"
        }



        DebugMode.e(
            "Requestdata",
            "body" + defaultRequestModel.fileBodyList,
            "postMethodFilesMultipleArray"
        )
        DebugMode.e(
            "Requestdata",
            "header" + defaultRequestModel.headers,
            "postMethodFilesMultipleArray"
        )
        values.postValue(Resource.loading(null))
        apiHelper.postMethodFileArray(
            defaultRequestModel.url,
            defaultRequestModel.headers,
            defaultRequestModel.fileBodyList,
            defaultRequestModel.multipleFileDriver
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                DebugMode.e("MainRepository", "onSuccess  $it")
                values.postValue(Resource.success(it))
            }, {

//                values.postValue(Resource.error(getErrorMessage(it)!!, null))
                val model = jsonParsers(getErrorMessage(it)!!)
                values.postValue(Resource.error(model.message, null, model.title))
            })


    }

    suspend fun postMethodRegister(
        defaultRequestModel: DefaultRequestModel,
        values: MutableLiveData<Resource<JsonObject>>
    ) {
        defaultRequestModel.headers = HashMap()
        if (defaultRequestModel.setToken) {
            defaultRequestModel.headers["Authorization"] =
                "Bearer ${preferenceHelper.getValue(PrefConstant.AUTH_TOKEN, "") as String}"
        }

        /**
         *
         *  hashMap.put(
        "first_name",
        RequestBody.create("text/plain".toMediaTypeOrNull(), firstNameString)
        )

        val pictureFile = File(localPath?.path)
        val requestFile = RequestBody.create(
        "* / *".toMediaTypeOrNull(),
        pictureFile
        )

        val fileBody =
        MultipartBody.Part.createFormData("picture", pictureFile.name, requestFile)
        accountViewModel.createAccountData(fileBody)

         * */

        DebugMode.e("RequestdataR", "body" + defaultRequestModel.fileBody, "postMethodRegister")
//        DebugMode.e("RequestdataR", "image" + defaultRequestModel.file!!)
        DebugMode.e("RequestdataR", "url" + defaultRequestModel.url, "postMethodRegister")
        DebugMode.e("RequestdataR", "header" + defaultRequestModel.headers, "postMethodRegister")
        values.postValue(Resource.loading(null))
        apiHelper.postMethodRegister(
            defaultRequestModel.url,
            defaultRequestModel.headers,
            defaultRequestModel.fileBody,
            defaultRequestModel.file!!
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                DebugMode.e("MainRepository", "onSuccess  $it")
                values.postValue(Resource.success(it))
            }, {

//                values.postValue(Resource.error(getErrorMessage(it)!!, null))
                val model = jsonParsers(getErrorMessage(it)!!)
                values.postValue(Resource.error(model.message, null, model.title))
            })
    }

    suspend fun postMethodRegisterWithoutImage(
        defaultRequestModel: DefaultRequestModel,
        values: MutableLiveData<Resource<JsonObject>>
    ) {
        defaultRequestModel.headers = HashMap()
        if (defaultRequestModel.setToken) {
            defaultRequestModel.headers["Authorization"] =
                "Bearer ${preferenceHelper.getValue(PrefConstant.AUTH_TOKEN, "") as String}"
        }

        /**
         *
         *  hashMap.put(
        "first_name",
        RequestBody.create("text/plain".toMediaTypeOrNull(), firstNameString)
        )

        val pictureFile = File(localPath?.path)
        val requestFile = RequestBody.create(
        "* / *".toMediaTypeOrNull(),
        pictureFile
        )

        val fileBody =
        MultipartBody.Part.createFormData("picture", pictureFile.name, requestFile)
        accountViewModel.createAccountData(fileBody)

         * */

        DebugMode.e(
            "RequestdataWOI",
            "body" + defaultRequestModel.fileBody,
            "postMethodRegisterWithoutImage"
        )
        DebugMode.e(
            "RequestdataWOI",
            "url" + defaultRequestModel.url,
            "postMethodRegisterWithoutImage"
        )
        DebugMode.e(
            "RequestdataWOI",
            "header" + defaultRequestModel.headers,
            "postMethodRegisterWithoutImage"
        )
        values.postValue(Resource.loading(null))
        apiHelper.postMethodRegister(
            defaultRequestModel.url,
            defaultRequestModel.headers,
            defaultRequestModel.fileBody
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                DebugMode.e("MainRepository", "onSuccess  $it")
                values.postValue(Resource.success(it))
            }, {

//                values.postValue(Resource.error(getErrorMessage(it)!!, null))
                val model = jsonParsers(getErrorMessage(it)!!)
                values.postValue(Resource.error(model.message, null, model.title))
            })
    }

    suspend fun getMethod(
        defaultRequestModel: DefaultRequestModel,
        values: MutableLiveData<Resource<JsonObject>>
    ) {
        defaultRequestModel.headers = HashMap()
        if (defaultRequestModel.setToken) {
            val token = preferenceHelper.getValue(PrefConstant.AUTH_TOKEN, "") as String
            defaultRequestModel.headers["Authorization"] =
                "Bearer $token"
        }
        DebugMode.e("Requestdata", "header" + defaultRequestModel.headers, "getMethod")
        values.postValue(Resource.loading(null))
        apiHelper.getMethod(defaultRequestModel.url, defaultRequestModel.headers)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /*.map { values.postValue(Resource.success(it)) }
            .doOnError {
                val model = jsonParsers(getErrorMessage(it)!!)
                values.postValue(Resource.error(model.message, null, model.title))
            }*/
        .subscribe({
            DebugMode.e("MainRepository", "onSuccess  $it")
            values.postValue(Resource.success(it))
        }, {
//                values.postValue(Resource.error(getErrorMessage(it)!!, null))
            val model = jsonParsers(getErrorMessage(it)!!)
            values.postValue(Resource.error(model.message, null, model.title))
        })
    }

    suspend fun getMethodArray(
        defaultRequestModel: DefaultRequestModel,
        values: MutableLiveData<Resource<JsonArray>>
    ) {
        defaultRequestModel.headers = HashMap()
        if (defaultRequestModel.setToken) {
            defaultRequestModel.headers["Authorization"] =
                "Bearer ${preferenceHelper.getValue(PrefConstant.AUTH_TOKEN, "") as String}"
        }
        DebugMode.e("Requestdata", "header" + defaultRequestModel.headers, "getMethodArray")
        values.postValue(Resource.loading(null))
        apiHelper.getMethodArray(defaultRequestModel.url, defaultRequestModel.headers)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                DebugMode.e("MainRepository", "onSuccess  $it")
                values.postValue(Resource.success(it))
            }, {

//                values.postValue(Resource.error(getErrorMessage(it)!!, null))
                val model = jsonParsers(getErrorMessage(it)!!)
                values.postValue(Resource.error(model.message, null, model.title))
            })
    }


    suspend fun getMethodComposite(
        defaultRequestModel: DefaultRequestModel,
        values: MutableLiveData<Resource<JsonObject>>
    ): Disposable {
        defaultRequestModel.headers = HashMap()
        if (defaultRequestModel.setToken) {
            defaultRequestModel.headers["Authorization"] =
                "Bearer ${preferenceHelper.getValue(PrefConstant.AUTH_TOKEN, "") as String}"
        }
        DebugMode.e("getMethodComposite", "header" + defaultRequestModel.headers)
        values.postValue(Resource.loading(null))
        return apiHelper.getMethod(defaultRequestModel.url, defaultRequestModel.headers)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                DebugMode.e("getMethodComposite", "onSuccess  $it")
                values.postValue(Resource.success(it))
            }, {
//                DebugMode.e("MainRepository", "onFailed $it ")
//                values.postValue(Resource.error(getErrorMessage(it)!!, null))
                val model = jsonParsers(getErrorMessage(it)!!)
                errorModel(values, model)
            })
    }


    suspend fun postMethodComposite(
        defaultRequestModel: DefaultRequestModel,
        values: MutableLiveData<Resource<JsonObject>>
    ): Disposable {
        defaultRequestModel.headers = HashMap()
        if (defaultRequestModel.setToken) {
            defaultRequestModel.headers["Authorization"] =
                "Bearer ${preferenceHelper.getValue(PrefConstant.AUTH_TOKEN, "") as String}"
        }

        DebugMode.e("postMethodComposite", "body" + defaultRequestModel.body)
        DebugMode.e("postMethodComposite", "url" + defaultRequestModel.url)
        DebugMode.e("postMethodComposite", "header" + defaultRequestModel.headers)
        values.postValue(Resource.loading(null))
        return apiHelper.postMethod(
            defaultRequestModel.url,
            defaultRequestModel.headers,
            defaultRequestModel.body
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                DebugMode.e("postMethodComposite", "onSuccess  $it")
                values.postValue(Resource.success(it))
            }, {
//                DebugMode.e("MainRepository", "onFailed $it")
                val model = jsonParsers(getErrorMessage(it)!!)
                errorModel(values, model)
//                values.postValue(Resource.error(getErrorMessage(it)!!, null))
            })
    }

    private fun errorModel(
        values: MutableLiveData<Resource<JsonObject>>,
        model: ErrorModel
    ) {
        values.postValue(Resource.error(model.message, null, model.title))
    }

}