package com.gogulf.passenger.app.ui.dashboard

import CollectionInterface
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.repository.MainRepository
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.base.BaseViewModel
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.gogulf.passenger.app.utils.others.Resource
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.model.CurrentBookingResponseData
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.MainApiResponseData
import com.gogulf.passenger.app.data.model.MainWithDashboardModel
import com.gogulf.passenger.app.data.model.ProfileResponseData
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.FirestoreCollectionLiveRepository
import com.gogulf.passenger.app.data.repository.FirestoreDocumentLiveRepository
import com.gogulf.passenger.app.ui.getaride.ShortcutAdapter
import com.gogulf.passenger.app.ui.getaridev2.DashboardUIState
import com.gogulf.passenger.app.ui.getaridev2.LoadingUIState
import com.gogulf.passenger.app.ui.getaridev2.RecyclerShortcutAdapter
import com.gogulf.passenger.app.ui.shortcuts.ShortcutAddModel
import com.gogulf.passenger.app.utils.LiveModels
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class DashboardVM(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {


    private var dashboardRequest = MutableLiveData<Resource<JsonObject>>()
    val dashResponse: MutableLiveData<Resource<JsonObject>>
        get() = dashboardRequest


    // Backing property to avoid state updates from other classes
//    private val _uiState: MutableStateFlow<Resource<JsonObject>> =
//        MutableStateFlow(Resource.loading(null))
//
//    // The UI collects from this StateFlow to get its state updates
//    val uiState: StateFlow<Resource<JsonObject>> = _uiState

    private val _uiState = MutableStateFlow(DashboardUIState())
    val uiState: StateFlow<DashboardUIState> = _uiState.asStateFlow()
    private val _loadingUiState = MutableStateFlow(LoadingUIState())
    val loadingUiState: StateFlow<LoadingUIState> = _loadingUiState.asStateFlow()

    private val profileResponseLiveDataObserver: Observer<in ProfileResponseData?> =
        Observer { profileResponseData ->
            profileResponseData?.let { data ->
                _uiState.update {
                    it.copy( onProfileSuccessData = data)
                }
            }
        }
    init {
        getDashboard()
//        getDashboardState()
        LiveModels.profileResponseLiveData.observeForever(profileResponseLiveDataObserver)
        getCurrentRide()
        getShortcuts()
        getProfile()
    }


    var customLoader: CustomLoader? = null


    val dashboardResponseData = MutableLiveData<MainWithDashboardModel>()

    private fun getDashboard() {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.get_dashboard
        viewModelScope.launch {
            dashboardRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getMethod(requestModel, dashboardRequest)
            } else {
                dashboardRequest.postValue(Resource.error("No internet connection", null))
            }

        }
    }
    var profileListener: ListenerRegistration? = null

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



    private var profileUpdateRequest = MutableLiveData<Resource<JsonObject>>()
    val profileUpdateResponse: MutableLiveData<Resource<JsonObject>>
        get() = profileUpdateRequest

    fun updateProfilePersonal(body: JsonObject) {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_updateProfile
        requestModel.body = body
        viewModelScope.launch {
            profileUpdateRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, profileUpdateRequest)
            } else {
                profileUpdateRequest.postValue(Resource.error("No internet connection", null))
            }
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

    private var shortcutListener: ListenerRegistration? = null



    var mShortcutList = ArrayList<ShortcutAddModel>()
    var shortcutLiveDataList = MutableLiveData<ArrayList<ShortcutAddModel>>()
    lateinit var adapter: ShortcutAdapter

    private val _shortcutAdapter = MutableLiveData<RecyclerShortcutAdapter>()
    val shortcutAdapter: LiveData<RecyclerShortcutAdapter>
        get() = _shortcutAdapter

    private fun getShortcuts() {
        viewModelScope.launch {
            val collectionReference = FirebaseRepository().getBaseDoc().collection("addresses")
            FirestoreCollectionLiveRepository().get<ShortcutAddModel>(
                object : CollectionInterface {
                    override fun listeners(listener: ListenerRegistration?) {
                        shortcutListener = listener
                    }
                }, collectionReference = collectionReference
            ).collect { response ->
                response.onSuccess { onSuccessData ->
                    mShortcutList.clear()
                    mShortcutList.addAll(onSuccessData)
                    shortcutLiveDataList.postValue(mShortcutList)
//                    _shortcutAdapter.value?.submitList(mShortcutList.toMutableList())
                }
                response.onError { error ->
                    Log.e("ChauffeursViewModel", "hitDrivers: $error")
                }
            }
        }
    }

    fun deleteShortcut(id: Int?) {
        viewModelScope.launch {
            _loadingUiState.update {
                it.copy(
                    isLoading = true
                )
            }
            val requestModel = DefaultRequestModel()
            requestModel.url = "${UrlName.DELETE_ADDRESS}/$id"
            ApiRepository().post(requestModel, MainApiResponseData::class.java)
                .onSuccess {
                    _loadingUiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }.onError {onError ->
                    _loadingUiState.update {
                        it.copy(
                            isLoading = false, error = Error(title = "Invalid", message = onError.localizedMessage?:"")
                        )
                    }
                }.onFailure {onFailure ->
                    _loadingUiState.update {
                        it.copy(
                            isLoading = false, error = Error(title = onFailure.title?:"", message = onFailure.message?:"")
                        )
                    }
                }
        }
    }

    var profileImageBase64: String = ""

    var path: String? = null

    var capturedProfilePicURI: Uri? = null
    fun hitFirstUpdate() {
        viewModelScope.launch {
            val model = DefaultRequestModel()
            model.url = UrlName.UPDATE
            if (path != null) {
                val builder = MultipartBody.Builder()
                builder.setType(MultipartBody.FORM)
                val fileCoverImage = File(path)
                builder.addFormDataPart(
                    "profile_image",
                    fileCoverImage.name,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fileCoverImage)
                )

                uiState.value.onProfileSuccessData?.name?.let {
                    builder.addFormDataPart("name",
                        it
                    )
                }
                uiState.value.onProfileSuccessData?.email?.let {
                    builder.addFormDataPart("email",
                        it
                    )
                }
                model.multipartBody = builder.build()

                ApiRepository().post(model, MainApiResponseData::class.java).onSuccess {
                    _uiState.update {
                        it.copy()
                    }
                }.onFailure { onFailureData ->
                    _loadingUiState.update {
                        it.copy(
                            error = Error(onFailureData.title ?: "", onFailureData.message ?: "")
                        )
                    }
                }.onError { onErrorData ->
                    _loadingUiState.update {
                        it.copy(
                            error = Error("Error", onErrorData.localizedMessage ?: "")
                        )
                    }
                }
            }

        }
    }


    fun clearError() {
        _loadingUiState.update {
            it.copy(
                error = null
            )
        }
    }



}