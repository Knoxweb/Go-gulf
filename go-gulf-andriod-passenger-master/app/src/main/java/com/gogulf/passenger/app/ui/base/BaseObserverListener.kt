package com.gogulf.passenger.app.ui.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.interfaces.ApiListener
import com.gogulf.passenger.app.utils.others.Resource
import com.google.gson.JsonObject
import com.gogulf.passenger.app.utils.interfaces.AnyApiListener
import com.gogulf.passenger.app.utils.interfaces.AnyApiListeners
import com.gogulf.passenger.app.utils.objects.DebugMode


object BaseObserverListener {

    private val TAG ="BaseObserverListener"
    fun observe(
        data: MutableLiveData<Resource<JsonObject>>,
        owner: LifecycleOwner,
        listener: ApiListener
    ) {
        data.observe(owner) {
            when (it.status) {
                Status.ERROR -> {
                    listener.onError(it.title, it.message)

                }

                Status.SUCCESS -> {
                    listener.onSuccess(it.data)

                }

                Status.LOADING -> {
                    listener.onLoading()

                }
            }

        }
    }


    fun observe(
        data: MutableLiveData<Resource<Any>>,
        owner: LifecycleOwner,
        listener: AnyApiListener
    ) {
        data.observe(owner) {
            when (it.status) {
                Status.ERROR -> {
                    DebugMode.e(TAG, it.message!!, "Error from Listener")
                    listener.onError(it.title, it.message)

                }

                Status.SUCCESS -> {
                    DebugMode.e(TAG, it.data.toString(), "Success from Listener")
                    listener.onSuccess(it.data)

                }

                Status.LOADING -> {
                    listener.onLoading()

                }
            }

        }
    }


    fun <T> observe(
        data: MutableLiveData<Resource<T>>,
        owner: LifecycleOwner,
        listener: AnyApiListeners<T>
    ) {
        data.observe(owner) {
            when (it.status) {
                Status.ERROR -> {
                    DebugMode.e(TAG, it.message!!, "Error from Listener")
                    listener.onError(it.title, it.message)

                }

                Status.SUCCESS -> {
                    DebugMode.e(TAG, it.data.toString(), "Success from Listener")
                    listener.onSuccess(it.data)

                }

                Status.LOADING -> {
                    listener.onLoading()

                }
            }

        }
    }
}