package com.gogulf.passenger.app.ui.invoices

import com.gogulf.passenger.app.data.model.Error

import CollectionInterface
import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.model.InvoiceResponseData
import com.gogulf.passenger.app.data.model.MainResponseData
import com.gogulf.passenger.app.data.model.response.mycards.CardModels
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.FirestoreCollectionLiveRepository
import com.gogulf.passenger.app.ui.addextras.RecyclerChoosePaymentAdapter
import com.gogulf.passenger.app.ui.invoices.pdf.GetTheFileData
import com.gogulf.passenger.app.utils.PrefEntity
import com.gogulf.passenger.app.utils.Preferences
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

class InvoiceViewModel(
    private val cacheDir: File
) : ViewModel() {
    private val _chooseCardAdapter = MutableLiveData<RecyclerChoosePaymentAdapter>()
    val chooseCardAdapter: LiveData<RecyclerChoosePaymentAdapter>
        get() = _chooseCardAdapter

    var currentInvoiceId: Int? = null
    private val _invoiceAdapter = MutableLiveData<RecyclerInvoiceAdapter>()
    val invoiceAdapter: LiveData<RecyclerInvoiceAdapter>
        get() = _invoiceAdapter

    var invoiceListener: ListenerRegistration? = null

    var customLoader: CustomLoader? = null

    private val listForInvoice: ArrayList<InvoiceResponseData> = ArrayList()

    var startDateRange: Long = 1718884801
    var endDateRange: Long = 1718884803

    private val _uiState = MutableStateFlow(InvoiceUIState())
    val uiState: StateFlow<InvoiceUIState>
        get() = _uiState

    val count = ObservableField("")

    var paymentListener: ListenerRegistration? = null

    var selectedListId: ArrayList<Int> = ArrayList()

    init {
        _invoiceAdapter.value = RecyclerInvoiceAdapter(this)
        _chooseCardAdapter.value = RecyclerChoosePaymentAdapter(invoiceViewModel = this)

        val initialFromCalender: Calendar = Calendar.getInstance()
//        initialFromCalender[Calendar.DAY_OF_MONTH] = 1
        initialFromCalender.set(Calendar.HOUR_OF_DAY, 0)
        initialFromCalender.set(Calendar.MINUTE, 0)
        initialFromCalender.set(Calendar.SECOND, 0)
        initialFromCalender.set(Calendar.MILLISECOND, 0)
        initialFromCalender.add(Calendar.MONTH, -1)

        startDateRange = initialFromCalender.timeInMillis
        val dateFormatFromToView = SimpleDateFormat("dd MMM',' yyyy", Locale.getDefault())
        updateStartDateString(dateFormatFromToView.format(initialFromCalender.time))

        val initialToCalender: Calendar = Calendar.getInstance()

        endDateRange = initialToCalender.timeInMillis
        val dateFormatToToView = SimpleDateFormat("dd MMM',' yyyy", Locale.getDefault())
        updateToDateString(dateFormatToToView.format(initialToCalender.time))

        hitGetCards()
        getInvoiceList()
    }


    private fun getInvoiceList() {
        viewModelScope.launch {
            val collection = FirebaseRepository().getBaseDoc().collection("invoices")
            FirestoreCollectionLiveRepository().get<InvoiceResponseData>(
                object : CollectionInterface {
                    override fun listeners(listener: ListenerRegistration?) {
                        invoiceListener = listener
                    }
                }, collection
            ).collect { response ->
                response.onSuccess { onSuccessData ->

                    _uiState.update {
                        it.copy(isLoading = false, invoiceResponseData = onSuccessData)
                    }
                    setListForAdapter()
                }
                response.onError { error ->
                    Log.e("NotificationResponseDataError", error.message.toString())
                }
            }

        }
    }

    fun hitInvoiceStatus(paymentId: String? = null, isPaid: Boolean) {
        viewModelScope.launch {
            val model = DefaultRequestModel()
            _uiState.update {
                it.copy(isLoading = true)
            }
            model.url = "invoice-status/$currentInvoiceId"
            model.body.addProperty("payment_status", if (isPaid) "paid" else "error")
            model.body.addProperty("payment_id", paymentId)
            ApiRepository().post(
                model, MainResponseData::class.java
            ).onSuccess { onSuccessData ->
                _uiState.update {
                    it.copy(
                        isLoading = false, error = Error(
                            onSuccessData.title ?: "", onSuccessData.message ?: ""
                        )
                    )
                }
            }.onFailure { onFailureData ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = Error(onFailureData.title ?: "", onFailureData.message ?: "")
                    )
                }
            }.onError { onErrorData ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = Error("Error", onErrorData.localizedMessage ?: "")
                    )
                }

            }

        }
    }

    fun hitInvoiceRetry(cardId: Int) {
        viewModelScope.launch {
            val model = DefaultRequestModel()
            _uiState.update {
                it.copy(isLoading = true)
            }
            model.url = "invoice-retry/$currentInvoiceId"
            model.body.addProperty("card_id", cardId)
            ApiRepository().post(
                model, MainResponseData::class.java
            ).onSuccess { onSuccessData ->
                _uiState.update {
                    it.copy(
                        isLoading = false, error = Error(
                            onSuccessData.title ?: "", onSuccessData.message ?: ""
                        )
                    )
                }
            }.onFailure { onFailureData ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = Error(onFailureData.title ?: "", onFailureData.message ?: "")
                    )
                }
            }.onError { onErrorData ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = Error("Error", onErrorData.localizedMessage ?: "")
                    )
                }

            }

        }
    }

    fun setListForAdapter() {
        Log.e(
            "InvoiceViewModel",
            "setListForAdapter: startDate : ${startDateRange / 1000} endDate : ${endDateRange / 1000}"
        )
        listForInvoice.clear()
        uiState.value.invoiceResponseData?.forEach {
            if (it.timestamp!! >= (startDateRange / 1000) && it.timestamp <= ((endDateRange / 1000))) {
                listForInvoice.add(it)
            }
        }
        setInvoiceAdapter()
    }

    private fun setInvoiceAdapter() {
        _invoiceAdapter.value?.submitList(listForInvoice.toMutableList())
    }


    override fun onCleared() {
        super.onCleared()
        invoiceListener?.remove()
        paymentListener?.remove()
    }

    fun updateStartDateString(format: String) {
        _uiState.update {
            it.copy(dateFrom = format)

        }
    }

    private fun hitGetCards() {
        viewModelScope.launch {
            val collection = FirebaseRepository().getBaseDoc().collection("cards")
            viewModelScope.launch {
                FirestoreCollectionLiveRepository().get<CardModels>(object :
                    CollectionInterface {
                    override fun listeners(listener: ListenerRegistration?) {
                        paymentListener = listener
                    }
                }, collection).collect { response ->
                    response.onSuccess { onSuccessData ->
                        _uiState.value.cardOfUsers?.clear()
                        _uiState.value.cardOfUsers?.addAll(onSuccessData)
//                        if (uiState.value.pageCardData == null) {
//                            try {
//                                _uiState.update {
//                                    it.copy(pageCardData = onSuccessData.first())
//                                }
//                            } catch (_: Exception) {
//
//                            }
//                        }
                        setCards()
                    }

                    response.onError { exception ->
                        Log.e("Error", exception.localizedMessage ?: "")
                    }
                }
            }

        }
    }

    private fun setCards() {
        _chooseCardAdapter.value?.submitList(uiState.value.cardOfUsers?.toMutableList())
    }


    fun updateToDateString(format: String) {
        _uiState.update {
            it.copy(dateTo = format)

        }
    }

    fun resetError() {
        _uiState.update {
            it.copy(error = null)
        }
    }

    fun stopLoading() {
        _uiState.update {
            it.copy(isLoading = false)
        }
    }

    fun updateCount(size: Int) {
        count.set(size.toString())
    }

    var myLoadedFile: MutableLiveData<File?> = MutableLiveData()


    fun downloadFile(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val inputStreamInside = setInputStreamFromUrl(url)
            if (inputStreamInside != null) {
                val file = File(cacheDir, "temp.pdf")
                val outputStream = FileOutputStream(file)
                try {
                    inputStreamInside.buffered().use { input ->
                        outputStream.buffered().use { output ->
                            input.copyTo(output)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        myLoadedFile.value = file
                    }
                    saveFileToDownloads(file, "invoice-${+Random.nextInt()}.pdf")

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun bulkDownloadFile(bulkFile: GetTheFileData) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val inputStreamInside = setBlukInputStreamFromUrl(bulkFile)
            if (inputStreamInside != null) {
                val file = File(cacheDir, "temp.pdf")
                val outputStream = FileOutputStream(file)
                try {
                    inputStreamInside.buffered().use { input ->
                        outputStream.buffered().use { output ->
                            input.copyTo(output)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        myLoadedFile.value = file
                    }
                    saveFileToDownloads(file, "invoice-${+Random.nextInt()}.pdf")

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setBlukInputStreamFromUrl(bulkFile: GetTheFileData): InputStream? {
        var connection: HttpURLConnection? = null
        try {
            val url = URL("https://booking.connect-smartdrive.com/api/passenger/invoice-pdf")
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer " + getAuthToken())
            connection.setRequestProperty("AppToken", "XaegOjtATlnKIrF01imnZ6GgBH9H9A")
//            connection.doOutput = true

            val invoicesArray = JSONArray().apply {
                bulkFile?.invoices?.forEach {
                    put(it)
                }
            }

            val jsonPayload = JSONObject().apply {
                put("invoices", invoicesArray)
            }
            val outputStream = connection.outputStream
            outputStream.write(jsonPayload.toString().toByteArray())
            outputStream.flush()
//            outputStream.close()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                return connection.inputStream
            }
            Log.e("responseCode", connection.responseCode.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
//            connection?.disconnect()
        }
        return null
    }

    private fun saveFileToDownloads(file: File, fileName: String) {
        val resolver = App.baseApplication.contentResolver

        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            try {
                resolver.openOutputStream(uri).use { outputStream ->
                    if (outputStream != null) {
                        file.inputStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                }
                println("File saved to Downloads successfully")
            } catch (e: IOException) {
                e.printStackTrace()
                println("Error saving file: ${e.message}")
            }
        } else {
            println("Failed to create new MediaStore record.")
        }
    }

    private fun setInputStreamFromUrl(urlString: String): InputStream? {
        var connection: HttpURLConnection? = null
        try {
            val url = URL(urlString)
            connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("Authorization", "Bearer " + getAuthToken())
            connection.setRequestProperty("AppToken", "RK9AIh0GFJ640FZhe1eSTUpgoU1h48OmwqrbbDFGL9zqbtREix1hxUr")
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                return connection.inputStream
            }
            Log.e("responseCode", connection.responseCode.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
//            connection?.disconnect()
        }
        return null
    }


    fun getOneMonthBack(from: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = from * 1000
        calendar.add(Calendar.MONTH, -1)
        return calendar.timeInMillis / 1000
    }


    private fun getAuthToken(): String {
        val fromPref = Preferences.getPreference(App.baseApplication, PrefEntity.AUTH_TOKEN)

        return fromPref ?: ""
    }

}