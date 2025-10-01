package com.gogulf.passenger.app.ui.shortcuts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddShortCutsViewModelFactory(
    private val id: Int?
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddShortcutsViewModel(id) as T
    }
}