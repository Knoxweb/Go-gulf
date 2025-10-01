package com.gogulf.passenger.app.ui.getaridev2

import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.ProfileResponseData
import com.gogulf.passenger.app.data.model.dashboards.DashboardModel

data class DashboardUIState(
    val data: DashboardModel? = null, val onProfileSuccessData: ProfileResponseData? = null

)

data class LoadingUIState(
    val isLoading: Boolean = false,
    val error: Error? = null
)


