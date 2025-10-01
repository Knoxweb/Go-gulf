package com.gogulf.passenger.app.data.repository

import CollectionInterface
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.gogulf.passenger.app.utils.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FirestoreCollectionLiveRepository {

    val gson = Gson()

    inline fun <reified T> get(
        listen: CollectionInterface,
        collectionReference: CollectionReference? = null,
        orderQuery: Query? = null
    ): StateFlow<ApiResponse<List<T>>> {
        require(collectionReference != null || orderQuery != null) {
            "Either collectionReference or orderQuery must be provided."
        }
        val stateFlow = MutableStateFlow<ApiResponse<List<T>>>(ApiResponse.Loading())
        val arrayListOfDocuments = ArrayList<T>()
        if (orderQuery != null) {
            val listener = orderQuery.addSnapshotListener { value, error ->
                stateFlow.value = ApiResponse.Loading()
                if (error != null) {
                    stateFlow.value = ApiResponse.Error(error)
                    return@addSnapshotListener
                }
                arrayListOfDocuments.clear()
                value?.documents?.mapNotNull { document ->
                    val thing = document.toObject(T::class.java)
                    thing?.let {
                        arrayListOfDocuments.add(thing)
                    }
                }
                stateFlow.value = ApiResponse.Success(arrayListOfDocuments)
            }
            listen.listeners(listener)
        } else {
            val listener = collectionReference!!.addSnapshotListener { value, error ->
                stateFlow.value = ApiResponse.Loading()
                if (error != null) {
                    stateFlow.value = ApiResponse.Error(error)
                    return@addSnapshotListener
                }

                if (value != null) {
                    arrayListOfDocuments.clear()
                    value.documents.mapNotNull { document ->
                        val thing = document.toObject(T::class.java)
                        thing?.let {
                            arrayListOfDocuments.add(thing)
                        }
                    }
                    stateFlow.value = ApiResponse.Success(arrayListOfDocuments)
                }
            }
            listen.listeners(listener)
        }

        return stateFlow
    }
}