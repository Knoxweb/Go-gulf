package com.gogulf.passenger.app.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.internal.PreferenceHelper
import com.gogulf.passenger.app.data.repository.MainRepository
import com.gogulf.passenger.app.utils.objects.Constants
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.gogulf.passenger.app.utils.others.Resource
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference


abstract class BaseVMNew<N>(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private var compositeDisposable = CompositeDisposable()
    private lateinit var mNavigator: WeakReference<N>

    //    val appRepository = AppRepository()
    val preferenceHelper = PreferenceHelper(App.baseApplication)


    var navigator: N
        get() = mNavigator.get()!!
        set(navigator) {
            this.mNavigator = WeakReference(navigator)
        }

    fun getCompositeDisposable() = compositeDisposable


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

    fun requestPostMethod(
        requestModel: DefaultRequestModel,
        liveData: MutableLiveData<Resource<JsonObject>>
    ) {
        viewModelScope.launch {
            liveData.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected())
                compositeDisposable.add(mainRepository.postMethodComposite(requestModel, liveData))
            else {
                liveData.postValue(Resource.error(Constants.NO_INTERNET_CONNECTION, null))
            }
        }
    }


    fun requestGetMethodDispose(
        requestModel: DefaultRequestModel,
        liveData: MutableLiveData<Resource<JsonObject>>
    ) {
        viewModelScope.launch {
            liveData.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected())
                compositeDisposable.add(mainRepository.getMethodComposite(requestModel, liveData))
            else {
                liveData.postValue(Resource.error(Constants.NO_INTERNET_CONNECTION, null))
            }
        }
    }


}