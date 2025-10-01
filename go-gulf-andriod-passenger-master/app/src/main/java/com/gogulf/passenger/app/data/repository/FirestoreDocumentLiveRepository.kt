package com.gogulf.passenger.app.data.repository

import CollectionInterface
import com.google.firebase.firestore.DocumentReference
import com.google.gson.Gson
import com.gogulf.passenger.app.utils.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FirestoreDocumentLiveRepository {

    val gson = Gson()

    inline fun <reified T> get(
        listen: CollectionInterface,
        documentReference: DocumentReference? = null,
    ): StateFlow<ApiResponse<T>> {
        require(documentReference != null) {
            "Either collectionReference or orderQuery must be provided."
        }
        val stateFlow = MutableStateFlow<ApiResponse<T>>(ApiResponse.Loading())

        val listener = documentReference.addSnapshotListener { value, error ->
            stateFlow.value = ApiResponse.Loading()
            if (error != null) {
                stateFlow.value = ApiResponse.Error(error)
                return@addSnapshotListener
            }

            if (value != null) {
                val data: T? = value.toObject(T::class.java)
                if (data == null) {
                    stateFlow.value = ApiResponse.Error(Exception("Data not found"))
                    return@addSnapshotListener
                }
                // Print the JSON string
                stateFlow.value = ApiResponse.Success(data)
            }
        }
        listen.listeners(listener)
        return stateFlow
    }
}

