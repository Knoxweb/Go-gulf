package com.gogulf.passenger.app.ui.shortcuts

import com.gogulf.passenger.app.data.model.Error

data class AddShortcutsUIState (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: Error? = null,
    val successMessage: Error? = null
)