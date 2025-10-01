package com.gogulf.passenger.app.ui.search_ride

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gogulf.passenger.app.data.model.Location
import com.gogulf.passenger.app.data.model.Route

class SearchDriverViewModelFactory(
    private val status: String? = null,
    private val id: Int? = null,
    private val route: Route? = null,
    private val pickup: Location? = null,
    private val drop: Location? = null,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchDriverViewModel(
            status, id, route, pickup, drop
        ) as T
    }
}