package com.gogulf.passenger.app.ui.addextras

import CollectionInterface
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.repository.MainRepository
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.base.BaseViewModel
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.gogulf.passenger.app.utils.others.Resource
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.model.ConfirmBookingSearchDriverResponseMainData
import com.gogulf.passenger.app.data.model.request.ConfirmBookingModel
import com.gogulf.passenger.app.data.model.response.mycards.CardModels
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.FirestoreCollectionLiveRepository
import kotlinx.coroutines.launch

class AddExtrasVM(
    private val mainRepository: MainRepository, private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {
    private var requestJob = MutableLiveData<Resource<ConfirmBookingSearchDriverResponseMainData>>()
    val jobResponse: MutableLiveData<Resource<ConfirmBookingSearchDriverResponseMainData>>
        get() = requestJob

    private val _chooseCardAdapter = MutableLiveData<RecyclerChoosePaymentAdapter>()
    val chooseCardAdapter: LiveData<RecyclerChoosePaymentAdapter>
        get() = _chooseCardAdapter
    var pageCardData: MutableLiveData<CardModels?> = MutableLiveData(null)

    var passengerCardId: String = ""

    fun requestJob(addExtraRequestModel: ConfirmBookingModel, id: Int?) {
        viewModelScope.launch {
            val requestModel = DefaultRequestModel()
            requestModel.url = "${UrlName.CONFIRM_BOOKING}/$id"
            addExtraRequestModel.passenger_card_id = pageCardData.value?.id.toString()
            requestModel.body = Gson().toJsonTree(addExtraRequestModel).asJsonObject
            requestJob.postValue(Resource.loading(null))
            ApiRepository().post(requestModel, ConfirmBookingSearchDriverResponseMainData::class.java).onSuccess {
                requestJob.postValue(Resource.success(it))
            }.onError {
                requestJob.postValue(Resource.error(it.localizedMessage, null))
            }.onFailure {
                requestJob.postValue(
                    Resource.error(
                        it.message ?: "Something went wrong", null, it.title ?: ""
                    )
                )
            }
        }
    }

    var paymentListener: ListenerRegistration? = null
    val cardOfUsers: ArrayList<CardModels> = ArrayList()

    fun updateSelectedCard(item: CardModels) {
        passengerCardId = item.id.toString()
        pageCardData.postValue(item)
    }

    private fun hitGetCards() {
        viewModelScope.launch {
            val collection = FirebaseRepository().getBaseDoc().collection("cards")
            viewModelScope.launch {
                FirestoreCollectionLiveRepository().get<CardModels>(object : CollectionInterface {
                    override fun listeners(listener: ListenerRegistration?) {
                        paymentListener = listener
                    }
                }, collection).collect { response ->
                    response.onSuccess { onSuccessData ->
                        cardOfUsers.clear()
                        if (onSuccessData.isNotEmpty()) {
                            val data = onSuccessData.first { it.is_active == true }
                            val otherData = onSuccessData.filter { it.is_active == false }
                            cardOfUsers.add(data)
                            cardOfUsers.addAll(otherData)
                            pageCardData.postValue(cardOfUsers.first())
                            setCards()
                        }

                    }

                    response.onError { exception ->
                        Log.e("Error", exception.localizedMessage ?: "")
                    }
                }
            }

        }
    }

    init {
        _chooseCardAdapter.value = RecyclerChoosePaymentAdapter(this)

        hitGetCards()
    }

    private fun setCards() {
        _chooseCardAdapter.value?.submitList(cardOfUsers.toMutableList())
    }


    private val flightResponse = MutableLiveData<Resource<JsonObject>>()
    val fResponse: MutableLiveData<Resource<JsonObject>>
        get() = flightResponse

    fun getFlightDetail(quoteId: String, flightNumber: String) {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_flightStatus
        requestModel.body.addProperty("flight_number", flightNumber)
        requestModel.body.addProperty("quote_id", quoteId)
        viewModelScope.launch {
            flightResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, flightResponse)
            } else {
                flightResponse.postValue(Resource.error("No internet connection", null))
            }
        }

    }


}