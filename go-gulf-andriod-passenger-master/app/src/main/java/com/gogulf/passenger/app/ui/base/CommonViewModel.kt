package com.gogulf.passenger.app.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.repository.MainRepository
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.gogulf.passenger.app.utils.others.Resource
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.model.firestore.ContentModel
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import kotlinx.coroutines.launch

class CommonViewModel(
    private val mainRepository: MainRepository,
    private val firebaseRepository: FirebaseRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {

    private var dashboardRequest = MutableLiveData<Resource<JsonObject>>()
    val dashResponse: MutableLiveData<Resource<JsonObject>>
        get() = dashboardRequest

    var getPolicies: ListenerRegistration? = null
    var getTerms: ListenerRegistration? = null

    private var requestPolicies =
        MutableLiveData<Resource<ContentModel>>()
    val myPolicy: MutableLiveData<Resource<ContentModel>>
        get() = requestPolicies

    private var requestTerms =
        MutableLiveData<Resource<ContentModel>>()
    val myTerms: MutableLiveData<Resource<ContentModel>>
        get() = requestTerms

    fun onBackPress() {
        navigator.onBackPress()
    }

    fun onValidated() {
        navigator.onValidated()
    }

    fun onSubmit() {
        navigator.onSubmit()
    }

    fun getDashboard() {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.get_profile
        viewModelScope.launch {
            dashboardRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getMethod(requestModel, dashboardRequest)
            } else {
                dashboardRequest.postValue(Resource.error("No internet connection", null))
            }

        }
    }


    fun getPolicy() {
        viewModelScope.launch {
            requestPolicies.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                getPolicies = firebaseRepository.getPolicies()
                    .addSnapshotListener(EventListener { value, e ->
                        if (e != null) {
                            requestPolicies.postValue(Resource.error("Data not Found", null))
                            return@EventListener
                        }

                        val bookList: MutableList<ContentModel> = mutableListOf()
                        for (doc in value!!) {
                            val allBookings = doc.toObject(ContentModel::class.java)
                            if (allBookings.user?.lowercase() == "passenger")
                                bookList.add(allBookings)
                        }
                        if (bookList.isNotEmpty()) {
                            requestPolicies.postValue(Resource.success(bookList[0]))
                        } else {
                            requestPolicies.postValue(Resource.error("Data not Found", null))
                        }
                    })
            } else {
                requestPolicies.postValue(Resource.error("Data not Found", null))

            }
        }
    }


    fun getTerms() {
        viewModelScope.launch {
            requestTerms.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                getTerms = firebaseRepository.getTermsConditions()
                    .addSnapshotListener(EventListener { value, e ->
                        if (e != null) {
                            requestTerms.postValue(Resource.error("Data not Found", null))
                            return@EventListener
                        }

                        val bookList: MutableList<ContentModel> = mutableListOf()
                        for (doc in value!!) {
                            val allBookings = doc.toObject(ContentModel::class.java)
                            if (allBookings.user?.lowercase() == "passenger")
                                bookList.add(allBookings)
                        }
                        if (bookList.isNotEmpty()) {
                            requestTerms.postValue(Resource.success(bookList[0]))
                        } else {
                            requestTerms.postValue(Resource.error("Data not Found", null))
                        }
                    })
            } else {
                requestTerms.postValue(Resource.error("Data not Found", null))

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        getPolicies?.remove()
        getTerms?.remove()
    }


}