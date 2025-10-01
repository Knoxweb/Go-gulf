package com.gogulf.passenger.app.ui.splash

import com.gogulf.passenger.app.data.model.Error

data class SplashUIState(
    val isLoading: Boolean = false,
    val error: Error? = null
)