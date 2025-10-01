package com.gogulf.passenger.app.ui.locationselector

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.utils.LocationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resumeWithException

class LocationSelectorViewModel : ViewModel() {
    var placesClient: PlacesClient? = null
    private val _uiState = MutableStateFlow(LocationSelectorUIState())
    val uiState: StateFlow<LocationSelectorUIState> = _uiState.asStateFlow()


    val searchQuery = ObservableField("")
    private val _locationSelectorScreenState = MutableStateFlow(LocationSelectorScreenState(""))
    private val locationSelectorScreenState: StateFlow<LocationSelectorScreenState> =
        _locationSelectorScreenState.asStateFlow()

    private val _googleAutoCompleteAdapter = MutableLiveData<RecyclerGoogleAutoCompleteAdapter>()
    val googleAutoCompleteAdapter: MutableLiveData<RecyclerGoogleAutoCompleteAdapter>
        get() = _googleAutoCompleteAdapter


    init {
        _googleAutoCompleteAdapter.value = RecyclerGoogleAutoCompleteAdapter(this)
        placesClient = Places.createClient(App.baseApplication)
        viewModelScope.launch {
            val list = getAutocomplete()
            _googleAutoCompleteAdapter.value?.submitList(list.toMutableList())
        }
    }

    fun updateSearchQuery() {
        _locationSelectorScreenState.update {
            it.copy(searchQuery = searchQuery.get().toString())
        }
        viewModelScope.launch {
            val list = getAutocomplete()
            _googleAutoCompleteAdapter.value =
                RecyclerGoogleAutoCompleteAdapter(this@LocationSelectorViewModel)
            _googleAutoCompleteAdapter.value?.submitList(list.toMutableList())

        }
    }

    private suspend fun getAutocomplete(): List<AutocompletePrediction> {
        val query = if (searchQuery.get().isNullOrBlank()) {
            "Alabama"
        } else {
            searchQuery.get()
        }
        val token = AutocompleteSessionToken.newInstance()
        val request =
            FindAutocompletePredictionsRequest.builder().setQuery(query).setSessionToken(token)
//                .setCountries("NP", "AL")

        LocationUtils.myCurrentLocation.value?.let {
           request.setLocationBias(RectangularBounds.newInstance(
               LatLng(it.latitude - 0.05, it.longitude - 0.05),
               LatLng(it.latitude + 0.05, it.longitude + 0.05)
           ))
        }

        return withContext(Dispatchers.IO) {
            var prediction: FindAutocompletePredictionsResponse? = null
            try {
                prediction = placesClient!!.findAutocompletePredictions(request.build()).await()
            } catch (e: Exception) {
                Log.e("exception", e.localizedMessage)
            }
            prediction?.autocompletePredictions ?: emptyList()

        }
    }


    private suspend fun <T> Task<T>.await(): T {
        return suspendCancellableCoroutine { continuation ->
            addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result, null)
                } else {
                    continuation.resumeWithException(
                        task.exception ?: Exception("Unknown task exception")
                    )
                }
            }
        }
    }

    fun onAutoCompleteClicked(address: AutocompletePrediction) {
        viewModelScope.launch {
            fetchLatLong(address)
        }
    }

    private suspend fun fetchLatLong(address: AutocompletePrediction) {
        val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.ADDRESS_COMPONENTS)

        val request = FetchPlaceRequest.builder(address.placeId, placeFields).build()
        withContext(Dispatchers.IO) {
            var fetchPlaceResponse: FetchPlaceResponse? = null
            try {
                fetchPlaceResponse = placesClient!!.fetchPlace(request).await()
            } catch (e: Exception) {
                Log.e("exception", e.localizedMessage)
            }

            if (fetchPlaceResponse?.place?.latLng != null) {
                val latLng = fetchPlaceResponse.place.latLng
                val addressComponents = fetchPlaceResponse.place.addressComponents

                val fullText = address.getFullText(null).toString()
                val postalCode =
                    addressComponents?.asList()?.find { it.types.contains("postal_code") }?.name

                val fullAddressWithZip = if (!postalCode.isNullOrEmpty()) {
                    fullText
                } else {
                    fullText
                }

                _uiState.update {
                    it.copy(
                        selectedAddress = fullAddressWithZip,
                        selectedLat = latLng!!.latitude,
                        selectedLog = latLng.longitude
                    )
                }
            } else {
                null
            }
        }
    }

}