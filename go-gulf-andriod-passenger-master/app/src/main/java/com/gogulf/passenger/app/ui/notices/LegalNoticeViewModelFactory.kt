package com.gogulf.passenger.app.ui.notices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LegalNoticeViewModelFactory (
    private val type: String? = null
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LegalNoticeViewModel(
            type
        ) as T
    }
}