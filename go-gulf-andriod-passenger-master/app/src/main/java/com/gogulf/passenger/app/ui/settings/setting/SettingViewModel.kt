package com.gogulf.passenger.app.ui.settings.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.model.MainApiResponseData
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.ui.getaridev2.RecyclerMenuAdapter
import com.gogulf.passenger.app.ui.menu.MenuModels
import com.gogulf.passenger.app.utils.CommonUtils
import com.gogulf.passenger.app.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader

class SettingViewModel : ViewModel() {

    private val _menuAdapter = MutableLiveData<RecyclerMenuAdapter>()
    val menuAdapter: LiveData<RecyclerMenuAdapter>
        get() = _menuAdapter

    private val _uiState = MutableStateFlow(SettingUIState())
    val uiState: StateFlow<SettingUIState> = _uiState.asStateFlow()
    private fun getListOfOption(): List<MenuModels> {
        return listOf(
            MenuModels(
                R.drawable.ic_accounts, "Profile", "1"
            ),

            MenuModels(
                R.drawable.ic_privacy, "Privacy Policy", "1"
            ),

            MenuModels(
                R.drawable.ic_legals, "Terms & Conditions", "1"
            )

        )
    }
    var customLoader: CustomLoader? = null


    init {
        _menuAdapter.value = RecyclerMenuAdapter(getListOfOption())

    }

    fun hitLogout() {
        viewModelScope.launch {
            _uiState.update {
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
                _uiState.update { it ->
                    it.copy(
                        isLoading = false, onLogoutSuccess = true
                    )
                }

            }.onFailure { error ->
                _uiState.update { it ->
                    it.copy(
                        isLoading = false, error = Error(
                            title = error.title ?: "", message = error.message ?: ""
                        )
                    )
                }
            }.onError { error ->
                _uiState.update { it ->
                    it.copy(
                        isLoading = false, error = Error(
                            title = "Error", message = error.message ?: ""
                        )
                    )
                }
            }
        }

    }

    fun hitDeleteAccount() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val model = DefaultRequestModel()
            model.body.addProperty("device_token", CommonUtils.getPrefFirebaseToken())
            model.url = UrlName.DELETE_ACCOUNT
            ApiRepository().post(model, MainApiResponseData::class.java)
                .onSuccess { onSuccessData ->
                    _uiState.update { it ->
                        it.copy(
                            isLoading = false, onLogoutSuccess = true
                        )
                    }
                }.onFailure { onFailureData ->
                    _uiState.update { it ->
                        it.copy(
                            isLoading = false, error = Error(
                                title = onFailureData?.title ?: "Invalid",
                                message = onFailureData?.message ?: "Something went wrong"
                            )
                        )
                    }
                }.onError { error ->
                    _uiState.update { it ->
                        it.copy(
                            isLoading = false, error = Error(
                                title = "Error", message = error.message ?: ""
                            )
                        )
                    }
                }
        }
    }

    fun resetError() {
        _uiState.update {
            it.copy(error = null)
        }
    }

}