package com.gogulf.passenger.app.data.repository

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.gogulf.passenger.app.utils.ApiResponse
import com.gogulf.passenger.app.utils.TOKEN
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuthRepository {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend inline fun <reified T> sendOTP(
        activity: Activity, phoneNumber: String
    ): ApiResponse<T>? {
        return try {
            when (val verificationResult = sendOTPInPhoneNumber(activity, phoneNumber)) {
                is VerificationResult.VerificationCompleted -> {
                    Log.e("sendOTP", "SendOTP: VerificationCompleted")
                    ApiResponse.PhoneVerified(verificationResult.credential)
                }

                is VerificationResult.CodeSent -> {
                    TOKEN.resendingToken = verificationResult.token

                    Log.e("sendOTP", "SendOTP: CodeSent")
                    val it = JsonObject().apply {
                        addProperty("title", "Code sent successfully")
                        addProperty("message", "Please check your phone for the code")
                        add("data", JsonObject().apply {
                            addProperty("verificationId", verificationResult.verificationId)
                            addProperty("isVerificationCompleted", false)
                        })
                    }
                    val gson = Gson()
                    val type = object : TypeToken<T>() {}.type
                    val data: T = gson.fromJson(it, type)
                    ApiResponse.Success(data)
                }
            }
        } catch (e: FirebaseException) {
            val it = JsonObject().apply {
                addProperty("title", "Error")
                addProperty("message", e.localizedMessage)
                add("data", null)
            }
            val gson = Gson()
            Log.e("sendOTP", "SendOTP: Failure")

            val type = object : TypeToken<T>() {}.type
            val data: T = gson.fromJson(it, type)
            ApiResponse.Failure(data)
        }  catch (e: TimeoutCancellationException) {
            val it = JsonObject().apply {
                addProperty("title", "Error")
                addProperty("message", "Request timed out. Please try again.")
                add("data", null)
            }
            val gson = Gson()
            val type = object : TypeToken<T>() {}.type
            val data: T = gson.fromJson(it, type)
            ApiResponse.Failure(data)
        } catch (e: Exception) {
            val it = JsonObject().apply {
                addProperty("title", "Error")
                addProperty("message", e.localizedMessage ?: "An unknown error occurred.")
                add("data", null)
            }
            val gson = Gson()
            val type = object : TypeToken<T>() {}.type
            val data: T = gson.fromJson(it, type)
            ApiResponse.Failure(data)
        }
    }

    suspend inline fun <reified T> resendOtp(
        activity: Activity, phoneNumber: String, forceResendingToken: ForceResendingToken
    ): ApiResponse<T>? {
        return try {
            when (val verificationResult =
                reSendOTPInPhoneNumber(activity, phoneNumber, forceResendingToken)) {
                is VerificationResult.VerificationCompleted -> {
                    Log.e("sendOTP", "SendOTP: VerificationCompleted")
                    ApiResponse.PhoneVerified(verificationResult.credential)
                }

                is VerificationResult.CodeSent -> {
                    Log.e("sendOTP", "SendOTP: CodeSent")
                    ApiResponse.CodeSent(
                        verificationResult.verificationId, verificationResult.token
                    )
                }
            }
        } catch (e: FirebaseException) {
            val it = JsonObject().apply {
                addProperty("title", "Error")
                addProperty("message", e.localizedMessage)
                add("data", null)
            }
            val gson = Gson()
            Log.e("sendOTP", "SendOTP: Failure")

            val type = object : TypeToken<T>() {}.type
            val data: T = gson.fromJson(it, type)
            ApiResponse.Failure(data)
        }
    }

    suspend inline fun <reified T> signIn(credential: PhoneAuthCredential): ApiResponse<T>? {
        return try {
            val taskResult = withTimeout(40_000L) {
                auth.signInWithCredential(credential).await()
            }
            val it = JsonObject().apply {
                addProperty("title", "Successful")
                addProperty("message", "Login successful")
                add("data", JsonObject().apply {
                    addProperty("status", true)
                })
            }
            val gson = Gson()
            val type = object : TypeToken<T>() {}.type
            val data: T = gson.fromJson(it, type)
            ApiResponse.Success(data)
        } catch (e: FirebaseException) {
            val it = JsonObject().apply {
                addProperty("title", "Error")
                addProperty("message", e.localizedMessage ?: "")
                add("data", null)
            }
            val gson = Gson()
            val type = object : TypeToken<T>() {}.type
            val data: T = gson.fromJson(it, type)
            ApiResponse.Failure(data)
        } catch (e: TimeoutCancellationException) {
            val it = JsonObject().apply {
                addProperty("title", "Error")
                addProperty("message", "Request timed out. Please try again.")
                add("data", null)
            }
            val gson = Gson()
            val type = object : TypeToken<T>() {}.type
            val data: T = gson.fromJson(it, type)
            ApiResponse.Failure(data)
        }

        catch (e: Exception) {
            val it = JsonObject().apply {
                addProperty("title", "Error")
                addProperty("message", e.localizedMessage ?: "An unknown error occurred.")
                add("data", null)
            }
            val gson = Gson()
            val type = object : TypeToken<T>() {}.type
            val data: T = gson.fromJson(it, type)
            ApiResponse.Failure(data)
        }
    }

    suspend inline fun <reified T> signInWithCustomToken(token: String): ApiResponse<T> {
        return try {
            val taskResult = withTimeout(40_000L) { // 30 seconds timeout
                auth.signInWithCustomToken(token).await()
            }
            val it = JsonObject().apply {
                addProperty("title", "Successful")
                addProperty("message", "Login successful")
                add("data", JsonObject().apply {
                    addProperty("status", true)
                })
            }
            val gson = Gson()
            val type = object : TypeToken<T>() {}.type
            val data: T = gson.fromJson(it, type)
            ApiResponse.Success(data)
        } catch (e: FirebaseException) {
            val it = JsonObject().apply {
                addProperty("title", "Error")
                addProperty("message", e.localizedMessage ?: "")
                add("data", null)
            }
            val gson = Gson()
            val type = object : TypeToken<T>() {}.type
            val data: T = gson.fromJson(it, type)
            ApiResponse.Failure(data)
        } catch (e: TimeoutCancellationException) {
            val it = JsonObject().apply {
                addProperty("title", "Error")
                addProperty("message", "Request timed out. Please try again.")
                add("data", null)
            }
            val gson = Gson()
            val type = object : TypeToken<T>() {}.type
            val data: T = gson.fromJson(it, type)
            ApiResponse.Failure(data)
        } catch (e: Exception) {
            val it = JsonObject().apply {
                addProperty("title", "Error")
                addProperty("message", e.localizedMessage ?: "An unknown error occurred.")
                add("data", null)
            }
            val gson = Gson()
            val type = object : TypeToken<T>() {}.type
            val data: T = gson.fromJson(it, type)
            ApiResponse.Failure(data)
        }
    }

    suspend fun sendOTPInPhoneNumber(
        activity: Activity, phoneNumber: String
    ): VerificationResult {
        return withTimeout(40_000L) {
            suspendCancellableCoroutine { cont ->
                if (TOKEN.resendingToken == null) {
                    val options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS).setActivity(activity)
                        .setCallbacks(object :
                            PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                cont.resume(VerificationResult.VerificationCompleted(credential))
                            }

                            override fun onVerificationFailed(e: FirebaseException) {
                                Log.e("Localized message", e.localizedMessage ?: "")
                                Log.e("Message", e.message ?: "")
                                cont.resumeWithException(e)
                            }

                            override fun onCodeSent(
                                verificationId: String, token: ForceResendingToken
                            ) {
                                cont.resume(VerificationResult.CodeSent(verificationId, token))
                            }

                        }).build()
                    if (isLocaleFrench()) {
                        auth.setLanguageCode("fr")
                    }
                    PhoneAuthProvider.verifyPhoneNumber(options)

                } else {
                    val options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS).setActivity(activity)
                        .setForceResendingToken(TOKEN.resendingToken!!).setCallbacks(object :
                            PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                cont.resume(VerificationResult.VerificationCompleted(credential))
                            }

                            override fun onVerificationFailed(e: FirebaseException) {
                                Log.e("Localized message", e.localizedMessage ?: "")
                                Log.e("Message", e.message ?: "")
                                cont.resumeWithException(e)
                            }

                            override fun onCodeSent(
                                verificationId: String, token: ForceResendingToken
                            ) {
                                cont.resume(VerificationResult.CodeSent(verificationId, token))
                            }

                        }).build()
                    if (isLocaleFrench()) {
                        auth.setLanguageCode("fr")
                    }
                    PhoneAuthProvider.verifyPhoneNumber(options)

                }

            }
        }
    }

    suspend fun reSendOTPInPhoneNumber(
        activity: Activity, phoneNumber: String, forceResendingToken: ForceResendingToken
    ): VerificationResult {
        return withTimeout(30_000L) {
            suspendCancellableCoroutine { cont ->
            val options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS).setActivity(activity)
                .setForceResendingToken(forceResendingToken)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        cont.resume(VerificationResult.VerificationCompleted(credential))
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        Log.e("Localized message", e.localizedMessage ?: "")
                        Log.e("Message", e.message ?: "")
                        cont.resumeWithException(e)
                    }

                    override fun onCodeSent(
                        verificationId: String, token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        cont.resume(VerificationResult.CodeSent(verificationId, token))
                    }
                }).build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
        }
    }


    sealed class VerificationResult {
        data class VerificationCompleted(val credential: PhoneAuthCredential) : VerificationResult()
        data class CodeSent(
            val verificationId: String, val token: ForceResendingToken
        ) : VerificationResult()
    }

    fun isLocaleFrench(): Boolean {
        val currentLocale = Locale.getDefault()
        return currentLocale.language == "fr"
    }
}