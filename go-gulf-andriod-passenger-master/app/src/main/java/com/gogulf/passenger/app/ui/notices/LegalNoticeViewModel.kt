package com.gogulf.passenger.app.ui.notices

import CollectionInterface
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.gogulf.passenger.app.data.apidata.FirebaseConstant.TYPE_PRIVACY_POLICY
import com.gogulf.passenger.app.data.model.PoliciesResponseData
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.FirestoreDocumentLiveRepository
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LegalNoticeViewModel(
    val type: String?
) : ViewModel() {

    var customLoader: CustomLoader? = null
    private val _uiState = MutableStateFlow(LegalNoticeUIState())
    val uiState: StateFlow<LegalNoticeUIState> = _uiState.asStateFlow()

    private var policiesListener: ListenerRegistration? = null

    private val _xAdapter = MutableLiveData<RecyclerLegalNoticeAdapter>()
    val xAdapter: LiveData<RecyclerLegalNoticeAdapter>
        get() = _xAdapter

    init {
        _xAdapter.value = RecyclerLegalNoticeAdapter()
        hitPolicies()
    }

    private fun hitPolicies() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val document: DocumentReference = when (type) {
                TYPE_PRIVACY_POLICY -> {
                    FirebaseRepository().getApiCollection().document("passenger_privacy_policy")
                }

                else -> {
                    FirebaseRepository().getApiCollection().document("passenger_term_of_use")
                }
            }
            FirestoreDocumentLiveRepository().get<PoliciesResponseData>(
                object : CollectionInterface {
                    override fun listeners(listener: ListenerRegistration?) {
                        policiesListener = listener
                    }
                }, document
            ).collect { response ->
                response.onSuccess { onSuccessData ->
                    _uiState.update {
                        it.copy(isLoading = false, legalNotice = onSuccessData)
                    }
                    setXAdapter()
                }
                response.onError { error ->
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                    Log.e("Error", error.localizedMessage ?: "")
                }
            }
        }
    }

    private fun setXAdapter() {
        _xAdapter.value?.submitList(uiState.value.legalNotice?.data?.toMutableList())
    }


    override fun onCleared() {
        super.onCleared()
        policiesListener?.remove()
    }

}