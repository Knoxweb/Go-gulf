package com.gogulf.passenger.app.ui.invoices.pdf

import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.utils.PrefEntity
import com.gogulf.passenger.app.utils.Preferences
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import kotlinx.coroutines.Dispatchers
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
import kotlin.random.Random


class PdfViewModel(
    private val url: String?, private val getTheFile: GetTheFileData?, private val cacheDir: File
) : ViewModel() {
    var myLoadedFile: MutableLiveData<File?> = MutableLiveData()
    var customLoader: CustomLoader? = null

    init {
        if (url != null) {
            viewModelScope.launch(Dispatchers.IO) {
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
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val inputStreamInside = setBlukInputStreamFromUrl()
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

//    private fun setInputStreamFromUrl(urlString: String): InputStream? {
//        var connection: HttpURLConnection? = null
//        try {
//            val url = URL(urlString)
//            connection = url.openConnection() as HttpURLConnection
//            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
//                return connection.inputStream
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
////            connection?.disconnect()
//        }
//        return null
//    }

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

    private fun setBlukInputStreamFromUrl(): InputStream? {
        var connection: HttpURLConnection? = null
        try {
            val url = URL("https://booking.connect-smartdrive.com/api/passenger/invoice-pdf")
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer " + getAuthToken())
            connection.setRequestProperty("AppToken", "RK9AIh0GFJ640FZhe1eSTUpgoU1h48OmwqrbbDFGL9zqbtREix1hxUr")
//            connection.doOutput = true

            val invoicesArray = JSONArray().apply {
                getTheFile?.invoices?.forEach {
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

    private fun getAuthToken(): String {
        val fromPref = Preferences.getPreference(App.baseApplication, PrefEntity.AUTH_TOKEN)

        return fromPref ?: ""
    }

}