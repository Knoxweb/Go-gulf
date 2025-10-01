package com.gogulf.passenger.app.ui.settings.setting

import com.gogulf.passenger.app.data.model.Error

data class SettingUIState (
    val isLoading: Boolean = false,
    val onLogoutSuccess: Boolean = false,
    val error: Error?=null,

)