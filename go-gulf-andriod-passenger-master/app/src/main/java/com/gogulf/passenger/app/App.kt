package com.gogulf.passenger.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.MapsInitializer
import com.google.android.libraries.places.api.Places
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.crashlytics.crashlytics
import com.gogulf.passenger.app.data.internal.PreferenceHelper
import com.gogulf.passenger.app.di.module.appModule
import com.gogulf.passenger.app.di.module.repoModule
import com.gogulf.passenger.app.di.module.viewModelModule
import com.gogulf.passenger.app.utils.logDeviceToCrashlytics
import com.gogulf.passenger.app.utils.objects.FirebaseLoginHandler
import com.gogulf.passenger.app.BuildConfig
import com.stripe.android.PaymentConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        baseApplication = this
        preferenceHelper = PreferenceHelper(this)
        FirebaseApp.initializeApp(this)


        if (BuildConfig.DEBUG) {
            Firebase.appCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance()
            )
        } else {
            Firebase.appCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance(),
            )
        }


        logDeviceToCrashlytics()
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(true)


        FirebaseLoginHandler.getFirebaseToken()
        Places.initialize(this, BuildConfig.MAPS_API_KEY)
        PaymentConfiguration.init(
            this, publishableKey  // Replace with your own publishable key
        )
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST) {
            when (it) {
                MapsInitializer.Renderer.LEGACY -> {}
                MapsInitializer.Renderer.LATEST -> {}
            }
        }

        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, repoModule, viewModelModule))
        }



        //        Attaching Debugging of PARASH
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {
                Log.d("CurrentActivity", "Running : ${activity.localClassName} \n extras : ${activity.intent.extras}")
            }

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })


    }

    companion object {
        lateinit var baseApplication: Context
        lateinit var preferenceHelper: PreferenceHelper
        val passengerName = MutableLiveData<String>()
        var is403 = MutableLiveData<Boolean>()
        const val publishableKey = BuildConfig.StripeKey


    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}