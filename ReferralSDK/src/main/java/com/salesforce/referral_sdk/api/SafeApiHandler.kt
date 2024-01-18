package com.salesforce.referral_sdk.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.salesforce.referral_sdk.entities.ApiErrorResponse
import retrofit2.Response
import java.io.IOException

suspend fun <T: Any> safeApiCall(call: suspend () -> Response<T>): ApiResponse<T> {
    return try {
        val response = call.invoke()
        if (response.isSuccessful) {
            ApiResponse.Success(response.body()!!)
        } else {
            val errorMessage = response.errorBody()?.string()
            val type = object: TypeToken<List<ApiErrorResponse>>(){}.type
            val errorMessageBody = Gson().fromJson<List<ApiErrorResponse>>(errorMessage, type)
            ApiResponse.Error(errorMessageBody?.firstOrNull()?.message)
        }
    } catch (exception: IOException) {
        ApiResponse.NetworkError
    }  catch (exception: Exception) {
        ApiResponse.Error(exception.message)
    }
}