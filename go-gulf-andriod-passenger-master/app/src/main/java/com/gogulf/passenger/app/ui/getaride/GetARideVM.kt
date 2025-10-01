package com.gogulf.passenger.app.ui.getaride

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.repository.MainRepository
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.base.BaseViewModel
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.gogulf.passenger.app.utils.others.Resource
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import kotlinx.coroutines.launch

class GetARideVM(
    private val mainRepository: MainRepository,
    private val firebaseRepository: FirebaseRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {
    private var currentRequest = MutableLiveData<Resource<JsonObject>>()
    private var driverModelIdentity = MutableLiveData<Resource<ArrayList<DriverModelIdentity>>>()
    val myDriverModelIdentity: MutableLiveData<Resource<ArrayList<DriverModelIdentity>>>
        get() = driverModelIdentity


    val currentResponse: MutableLiveData<Resource<JsonObject>>
        get() = currentRequest

    init {
        getCurrentRide()
    }

    private fun getCurrentRide() {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.get_currentRide
        viewModelScope.launch {
            currentRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, currentRequest)
            } else {
                currentRequest.postValue(Resource.error("No internet connection", null))
            }
        }
    }

    var trackDrivers: DatabaseReference? = null
    var trackDriverListener: ValueEventListener? = null
//    fun trackDrivers() {
//        viewModelScope.launch {
//            driverModelIdentity.postValue(Resource.loading(null))
//            if (networkHelper.isNetworkConnected()) {
//                trackDrivers = firebaseRepository.trackDrivers()
//                trackDriverListener =
//                    trackDrivers?.addValueEventListener(object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            if (!snapshot.exists()) {
//                                driverModelIdentity.postValue(
//                                    Resource.error(
//                                        "No data available",
//                                        null
//                                    )
//                                )
//                                return
//                            }
//                            try {
//                                val driverList: MutableList<DriverModelIdentity> =
//                                    mutableListOf()
//
//                                for (data in snapshot.children) {
//                                    val bookingModels =
//                                        data.getValue(DriverLocationModel::class.java)
//                                    val key = data.key
//                                    driverList.add(
//                                        DriverModelIdentity(
//                                            key,
//                                            bookingModels
//                                        )
//                                    )
//
//                                }
//                                val list = ArrayList<DriverModelIdentity>()
//                                list.addAll(driverList)
//                                driverModelIdentity.postValue(Resource.success(list))
//                            } catch (e: Exception) {
//                                driverModelIdentity.postValue(
//                                    Resource.error(
//                                        e.message.toString(),
//                                        null
//                                    )
//                                )
//                            }
//
//
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            driverModelIdentity.postValue(
//                                Resource.error(
//                                    error.message,
//                                    null
//                                )
//                            )
//                        }
//
//                    })
//            } else {
//                driverModelIdentity.postValue(Resource.error("No internet connection", null))
//            }
//
//        }
//    }


}