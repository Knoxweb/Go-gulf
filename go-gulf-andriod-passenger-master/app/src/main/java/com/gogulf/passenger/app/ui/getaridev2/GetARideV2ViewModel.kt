package com.gogulf.passenger.app.ui.getaridev2

import CollectionInterface
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.model.CurrentBookingResponseData
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.MainApiResponseData
import com.gogulf.passenger.app.data.model.MainWithDashboardModel
import com.gogulf.passenger.app.data.model.NearbyDriverResponseData
import com.gogulf.passenger.app.data.model.ProfileResponseData
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.FirestoreCollectionLiveRepository
import com.gogulf.passenger.app.data.repository.FirestoreDocumentLiveRepository
import com.gogulf.passenger.app.data.repository.MainRepository
import com.gogulf.passenger.app.ui.getaride.DriverModelIdentity
import com.gogulf.passenger.app.ui.getaride.MutableDriverMarker
import com.gogulf.passenger.app.ui.menu.MenuModels
import com.gogulf.passenger.app.ui.settings.setting.SettingUIState
import com.gogulf.passenger.app.utils.CommonUtils
import com.gogulf.passenger.app.utils.LiveModels
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.firestore.ContentModel
import com.gogulf.passenger.app.data.model.firestore.PromoInfo
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.gogulf.passenger.app.utils.others.Resource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GetARideV2ViewModel(
    private val mainRepository: MainRepository,
    private val firebaseRepository: FirebaseRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private val firestoreDB = Firebase.firestore

    val gMap = MutableLiveData<GoogleMap>(null)
    var shouldEnableTheHamburgerIcon = MutableLiveData<Boolean>().apply {
        value = true
    }

    private val _menuAdapter = MutableLiveData<RecyclerMenuAdapter>()
    val menuAdapter: LiveData<RecyclerMenuAdapter>
        get() = _menuAdapter

    private val _logoutState = MutableStateFlow(SettingUIState())
    val logoutState: StateFlow<SettingUIState> = _logoutState.asStateFlow()


    private val _uiState = MutableStateFlow(DashboardUIState())
    val uiState: StateFlow<DashboardUIState> = _uiState.asStateFlow()

    private val profileResponseLiveDataObserver: Observer<in ProfileResponseData?> =
        Observer { profileResponseData ->
            profileResponseData?.let { data ->
                _uiState.update {
                    it.copy(onProfileSuccessData = data)
                }
            }
        }

    var customLoader: CustomLoader? = null


    val dashboardResponseData = MutableLiveData<MainWithDashboardModel>()

    private fun getListOfOption(): List<MenuModels> {
        return listOf(
            MenuModels(
                R.drawable.ic_menu_dashboard, "Dashboard", "1"
            ),

            MenuModels(
                R.drawable.ic_menu_calender, "Scheduled Bookings", "1"
            ),

            MenuModels(
                R.drawable.ic_menu_scheduled, "Booking History", "1"
            ),

            MenuModels(
                R.drawable.baseline_notifications_24, "Notification", "1"
            ),

            MenuModels(
                R.drawable.ic_invoice, "Invoice", "1"
            ),

            MenuModels(
                R.drawable.ic_support, "Support", "1"
            ),

            MenuModels(
                R.drawable.ic_menu_settings, "Account", "1"
            )

        )
    }

    var profileListener: ListenerRegistration? = null


    init {
        getCurrentRide()
        LiveModels.profileResponseLiveData.observeForever(profileResponseLiveDataObserver)
        getProfile()
        getRequestedBooking()
        _menuAdapter.value = RecyclerMenuAdapter(getListOfOption())
        getDashBoard()
        trackDriversNew()
        getPromotionalText()
    }

    private fun getDashBoard() {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.get_dashboard
        viewModelScope.launch {
            ApiRepository().get(requestModel, MainWithDashboardModel::class.java)
                .onSuccess { onSuccessData ->
                    dashboardResponseData.value = onSuccessData
                    _uiState.update {
                        it.copy(data = onSuccessData.data)
                    }
                }.onError { error ->

                }.onFailure {

                }
//            mainRepository.getMethod(requestModel, dashboardRequest)


        }
    }

    private val animateLive = MutableLiveData<MutableDriverMarker>()

    private var nearbyDriversListener: ListenerRegistration? = null
    val nearbyDriversData: MutableLiveData<ArrayList<DriverModelIdentity>> = MutableLiveData()

    var trackDriverListener: ValueEventListener? = null
    var trackDrivers: DatabaseReference? = null


    fun trackDriversNew() {
        viewModelScope.launch {
            val collectionReference = FirebaseRepository().getNearByCollection()
            FirestoreCollectionLiveRepository().get<NearbyDriverResponseData>(
                object : CollectionInterface {
                    override fun listeners(listener: ListenerRegistration?) {
                        nearbyDriversListener = listener
                    }
                }, collectionReference = collectionReference
            ).collect { response ->
                response.onSuccess { onSuccessData ->
                    val driverList: ArrayList<DriverModelIdentity> = ArrayList()

                    onSuccessData.forEach {
                        val key = it.id
                        driverList.add(
                            DriverModelIdentity(
                                key, it
                            )
                        )
                    }

                    nearbyDriversData.postValue(driverList)

                }
                response.onError { error ->
                    Log.e("ChauffeursViewModel", "hitDrivers: $error")
                }
            }
        }
    }

//    fun trackDrivers() {
//        viewModelScope.launch {
//            trackDrivers = firebaseRepository.trackDrivers()
//
//            trackDriverListener = trackDrivers?.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (!snapshot.exists()) {
//                        return
//                    }
//                    try {
//                        val driverList: ArrayList<DriverModelIdentity> = ArrayList()
//
//                        for (data in snapshot.children) {
//                            val bookingModels = data.getValue(DriverLocationModel::class.java)
//                            val key = data.key
//                            driverList.add(
//                                DriverModelIdentity(
//                                    key, bookingModels
//                                )
//                            )
//
//                        }
////                        val list = ArrayList<DriverModelIdentity>()
////                        list.addAll(driverList)
//                        nearbyDriversData.postValue(driverList)
//
//                        Log.e("RohanPaudelHeyAllData", driverList.toString())
//                    } catch (e: Exception) {
//
//                    }
//
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//
//            })
//
//
//        }
//    }

    private fun getProfile() {
        viewModelScope.launch {
            val document = FirebaseRepository().getBaseDoc().collection("data").document(
                "profile"
            )
            FirestoreDocumentLiveRepository().get<ProfileResponseData>(object :
                CollectionInterface {
                override fun listeners(listener: ListenerRegistration?) {
                    profileListener = listener
                }
            }, document).collect { response ->
                response.onSuccess { onSuccessData ->
                    val pic = onSuccessData.profile_picture_url?.split("?")?.first()
                    if (pic != null) {
                        onSuccessData.profile_picture_url = pic
                    }
                    LiveModels.setProfileResponseData(onSuccessData)
                }
                response.onError { onErrorData ->
                    Log.e("Error", onErrorData.localizedMessage ?: "")
                }
            }
        }
    }

    var requestedRide: MutableLiveData<CurrentBookingResponseData?> = MutableLiveData(null)
    var requestedRideListener: ListenerRegistration? = null

    private fun getRequestedBooking() {
        viewModelScope.launch {
            val document = FirebaseRepository().getBaseDoc().collection("data").document(
                "request"
            )
            FirestoreDocumentLiveRepository().get<CurrentBookingResponseData>(object :
                CollectionInterface {
                override fun listeners(listener: ListenerRegistration?) {
                    requestedRideListener = listener
                }
            }, document).collect { response ->
                response.onSuccess { onSuccessData ->
                    requestedRide.postValue(onSuccessData)
                }
                response.onError { onErrorData ->
                    Log.e("Error", onErrorData.localizedMessage ?: "")
                }
            }
        }
    }

    fun hitLogout() {
        viewModelScope.launch {
            _logoutState.update {
                it.copy(isLoading = true)
            }
            val body = JsonObject()
            body.addProperty("device_token", CommonUtils.getPrefFirebaseToken())
            val request = DefaultRequestModel()
            request.url = UrlName.LOGOUT
            request.body = body

            ApiRepository().post(
                request, MainApiResponseData::class.java
            ).onSuccess { onSuccessData ->
                _logoutState.update { it ->
                    it.copy(
                        isLoading = false, onLogoutSuccess = true
                    )
                }

            }.onFailure { error ->
                _logoutState.update { it ->
                    it.copy(
                        isLoading = false, error = Error(
                            title = error.title ?: "", message = error.message ?: ""
                        )
                    )
                }
            }.onError { error ->
                _logoutState.update { it ->
                    it.copy(
                        isLoading = false, error = Error(
                            title = "Error", message = error.message ?: ""
                        )
                    )
                }
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        trackDriverListener?.let { trackDrivers?.removeEventListener(it) }
        LiveModels.profileResponseLiveData.removeObserver(profileResponseLiveDataObserver)
        profileListener?.remove()
        requestedRideListener?.remove()
    }

    fun clearError() {
        _logoutState.update {
            it.copy(error = null)
        }
    }


    var currentRideListener: ListenerRegistration? = null

    val currentRideResponseData: MutableLiveData<CurrentBookingResponseData?> =
        MutableLiveData(null)

    fun getCurrentRide() {
        viewModelScope.launch {
            val document = FirebaseRepository().getBaseDoc().collection("data").document(
                "current_booking"
            )
            FirestoreDocumentLiveRepository().get<CurrentBookingResponseData>(object :
                CollectionInterface {
                override fun listeners(listener: ListenerRegistration?) {
                    currentRideListener = listener
                }
            }, document).collect { response ->
                response.onSuccess { onSuccessData ->
                    currentRideResponseData.postValue(onSuccessData)
                }
                response.onError { onErrorData ->
                    Log.e("Error", onErrorData.localizedMessage ?: "")
                }
            }
        }
    }


//    get the promotional status and text


    private var _promoInfo =
        MutableLiveData<Resource<PromoInfo>>()
    val promoInfo: MutableLiveData<Resource<PromoInfo>>
        get() = _promoInfo

    private fun getPromotionalText() {
        viewModelScope.launch {
            val document = firestoreDB.collection("discount_promotions").document(
                "ha3BZuaXkEeFLt1vzMCp"
            )
//                FirebaseRepository().getBaseDoc()
//            _promoInfo.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                document.addSnapshotListener { snapshot, error ->

                    Log.d("PARASH", "snapshot :  $snapshot")
                    Log.d("PARASH", "error :  $error")

                    if (error != null) {
                        _promoInfo.postValue(Resource.error(error.message.toString(), null))
                    }

                    if (snapshot != null) {
                        try {
                            val localPromoInfo = snapshot.toObject(PromoInfo::class.java)
                            _promoInfo.postValue(Resource.success(data = localPromoInfo))
                        } catch (e: Exception) {
                            _promoInfo.postValue(Resource.error(e.message.toString(), null))
                        }
                    }

                }
            }


        }
    }


}