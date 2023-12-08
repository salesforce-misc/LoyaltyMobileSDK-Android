package com.salesforce.referral_sdk.repository

import com.salesforce.referral_sdk.api.ApiResponse
import com.salesforce.referral_sdk.api.ApiService
import com.salesforce.referral_sdk.entities.QueryResult
import com.salesforce.referral_sdk.entities.ReferralEntity
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class ReferralsRepository @Inject constructor(
    private val apiService: ApiService
) {
    private suspend fun <T: Any> safeApiCall(call: suspend () -> Response<T>): ApiResponse<T> {
        return try {
            val response = call.invoke()
            if (response.isSuccessful) {
                ApiResponse.Success(response.body()!!)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Generic Error"
                ApiResponse.Error(errorMessage)
            }
        } catch (exception: IOException) {
            ApiResponse.NetworkError
        }  catch (exception: Exception) {
            ApiResponse.Error("Generic Error")
        }
    }

    suspend fun fetchReferralsInfo(): ApiResponse<QueryResult<ReferralEntity>> {
        return safeApiCall { apiService.fetchReferralsInfo(
            "https://dsb000001oyrq2ai.test1.my.pc-rnd.site.com/NTOInsider/services/data/v59.0/query/",
            referralQuery(),
//            "Bearer 00DSB000001oyRq!AQEAQDuosoC5VCO.IPNEdkhcWl57txTEOnDgH.4zuRq3MWxPZxOyRhNmOnwS4dUqWMz5na_dizXtJ0xn0AdGzaBcblVle09Q"
            "Bearer 00DSB000001oyRq!AQEAQKCmpcLFdgcOMKEKv7JlgCnDKC9r6YZnAZ_PKHcrSkGEoGjB2o6z.BfiqaTBPA1AJJrXS.QTojW9mEKQ6xPkKZuUPScn"
        ) }
    }

    private fun referralQuery(): String {
        return "SELECT Id,Name,Status FROM Referral"
    }
}