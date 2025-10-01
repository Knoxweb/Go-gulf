package com.gogulf.passenger.app.ui.schedulebooking.activity

import com.gogulf.passenger.app.data.model.Error

data class ScheduleUiState(
    val isLoading: Boolean = false,
    val error: Error? = null
)
