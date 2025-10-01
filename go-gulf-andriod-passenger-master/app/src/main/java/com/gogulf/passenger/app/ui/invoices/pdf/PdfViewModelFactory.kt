package com.gogulf.passenger.app.ui.invoices.pdf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.io.File

class PdfViewModelFactory(private val url: String?, private val getTheFile: GetTheFileData?, private val cacheDir: File) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PdfViewModel(url,getTheFile, cacheDir) as T
    }
}