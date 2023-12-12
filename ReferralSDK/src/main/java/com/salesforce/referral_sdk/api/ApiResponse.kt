package com.salesforce.referral_sdk.api

sealed interface ApiResponse<out T: Any> {
    data class Success<out T: Any>(val data: T): ApiResponse<T>
    data class Error(val errorMessage: String?): ApiResponse<Nothing>
    object NetworkError: ApiResponse<Nothing>
}