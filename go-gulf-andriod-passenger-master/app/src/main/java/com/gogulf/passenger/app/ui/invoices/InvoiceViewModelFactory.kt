package com.gogulf.passenger.app.ui.invoices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.io.File

class InvoiceViewModelFactory(private val cacheDir: File) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InvoiceViewModel(cacheDir) as T
    }
}