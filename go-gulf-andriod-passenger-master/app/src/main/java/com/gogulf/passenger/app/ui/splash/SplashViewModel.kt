package com.gogulf.passenger.app.ui.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.MainResponseData
import com.gogulf.passenger.app.data.model.StatusResponseData
import com.gogulf.passenger.app.data.repository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    fun resetError() {
        _uiState.update {
            it.copy(error = null)
        }
    }

    private val _splashLiveData = MutableLiveData<StatusResponseData?>()
    val splashLiveData: MutableLiveData<StatusResponseData?> = _splashLiveData

    private val _uiState = MutableStateFlow(SplashUIState())
    val uiState: MutableStateFlow<SplashUIState> = _uiState

    init {
        viewModelScope.launch {
            val model = DefaultRequestModel()
            model.url = UrlName.STATUS
            ApiRepository().get<MainResponseData<StatusResponseData>>(model).onSuccess {
                _splashLiveData.value = (it.data)
            }.onFailure {
                _splashLiveData.value = it.data
            }.onError { onError ->
                Log.e("SplashViewModel", onError.localizedMessage ?: "")
                _uiState.update {
                    it.copy(
                        error = Error(
                            "Error",
                            "Looks like Something Went Wrong " + "\nPlease try again"
                        )
                    )
                }
            }

        }
    }


}