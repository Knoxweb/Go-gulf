package com.gogulf.passenger.app.utils

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

sealed interface ApiResponse<T> {

    data class Success<T>(val data: T) : ApiResponse<T>
    data class Failure<T>(val data: T) : ApiResponse<T>
    data class Loading<T>(val isLoading: Boolean = true) : ApiResponse<T>
    data class Error<T>(val exception: java.lang.Exception) : ApiResponse<T>
    data class PhoneVerified<T>(val data: PhoneAuthCredential) : ApiResponse<T>
    data class CodeSent<T>(
        val verificationId: String, val resendingToken: PhoneAuthProvider.ForceResendingToken
    ) : ApiResponse<T>


    suspend fun onSuccess(block: suspend (T) -> Unit): ApiResponse<T> {
        if (this is Success) block(data)
        return this
    }

    suspend fun onPhoneVerified(block: suspend (PhoneAuthCredential) -> Unit): ApiResponse<T> {
        if (this is PhoneVerified) block(data)
        return this
    }

    suspend fun onCodeSent(block: suspend (String, PhoneAuthProvider.ForceResendingToken) -> Unit): ApiResponse<T> {
        if (this is CodeSent) block(verificationId, resendingToken)
        return this
    }

    suspend fun onFailure(block: suspend (T) -> Unit): ApiResponse<T> {
        if (this is Failure) block(data)
        return this
    }

    suspend fun onError(block: suspend (java.lang.Exception) -> Unit): ApiResponse<T> {
        if (this is Error) block(exception)
        return this
    }

    suspend fun isLoading(block: suspend () -> Unit): ApiResponse<T> {
        if (this is Loading) block()
        return this
    }
}