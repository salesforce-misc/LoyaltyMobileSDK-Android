package com.salesforce.referral_sdk.api

import retrofit2.Response
import java.io.IOException

suspend fun <T: Any> safeApiCall(call: suspend () -> Response<T>): ApiResponse<T> {
    return try {
        val response = call.invoke()
        if (response.isSuccessful) {
            ApiResponse.Success(response.body()!!)
        } else {
            val errorMessage = response.errorBody()?.string()
            ApiResponse.Error(errorMessage)
        }
    } catch (exception: IOException) {
        ApiResponse.NetworkError
    }  catch (exception: Exception) {
        ApiResponse.Error(exception.message)
    }
}