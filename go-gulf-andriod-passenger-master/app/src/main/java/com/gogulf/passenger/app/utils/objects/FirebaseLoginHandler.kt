package com.gogulf.passenger.app.utils.objects

import android.app.Activity
import android.util.Log
import com.gogulf.passenger.app.App.Companion.preferenceHelper
import com.gogulf.passenger.app.utils.interfaces.FirebaseLoginListener
import com.gogulf.passenger.app.utils.interfaces.FirebaseOTPListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import java.util.concurrent.TimeUnit


object FirebaseLoginHandler {
    private val TAG = "FirebaseLoginHandler"

    fun loginFirebase(
        activity: Activity,
        mobile: String,
        mAuth: FirebaseAuth,
        firebaseInterface: FirebaseLoginListener
    ) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(mobile)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)                 // Activity (for callback binding)
            .setCallbacks(firebaseBaseCallback(firebaseInterface))          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun firebaseBaseCallback(firebaseInterface: FirebaseLoginListener): PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
                Log.d(TAG, "onCodeAutoRetrievalTimeOut: $p0")
                firebaseInterface.onTimeOut(p0)
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: $credential")
                firebaseInterface.onSuccess(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                e.printStackTrace();
                when(e){
                    is FirebaseTooManyRequestsException ->{
                        Log.e(TAG, "Firebase Error :  ${e.message}")
                        firebaseInterface.onUserBlock(e.message!!)
                    }
                    else->{
                        Log.e(TAG, "Firebase Error :  ${e.message}")
                        firebaseInterface.onError(e.message!!)
                    }
                }

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                preferenceHelper.setValue(PrefConstant.VERIFICATION_ID, verificationId)
                preferenceHelper.setValue(PrefConstant.RESEND_TOKEN, token.toString())
                firebaseInterface.onCodeSent()

            }


        }

    fun otpCheckCredentials(
        otp: String,
        mAuth: FirebaseAuth,
        activity: Activity,
        firebaseOTPListener: FirebaseOTPListener
    ) {
        val credential = PhoneAuthProvider.getCredential(
            preferenceHelper.getValue(PrefConstant.VERIFICATION_ID, "") as String,
            otp
        )
        signInWithPhoneAuthCredential(credential, mAuth, activity, firebaseOTPListener)
    }

    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        mAuth: FirebaseAuth,
        activity: Activity,
        firebaseOTPListener: FirebaseOTPListener
    ) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(
                activity
            ) { task ->
                if (task.isSuccessful) {

                    val user = task.result.user?.uid
                    preferenceHelper.setValue(PrefConstant.FIREBASE_UID, user)
                    //update Ui
                    firebaseOTPListener.onSuccess(user.toString())

                } else {
                    if (task.exception != null) {
                        when (task.exception) {
                            is FirebaseAuthInvalidCredentialsException -> {
                                firebaseOTPListener.onError("Invalid OTP code, please enter valid OTP code")
                            }
                            else -> {
                                Log.w(TAG, "signInWithCredential:failure", task.exception)
                                firebaseOTPListener.onError(task.exception?.message.toString())
                            }
                        }
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        firebaseOTPListener.onError("Something went wrong")
                    }


                }
            }
    }

    fun getFirebaseToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            preferenceHelper.setValue(PrefConstant.DEVICE_TOKEN, token)
            DebugMode.e(TAG, token, "Firebase token")
        })
    }

}