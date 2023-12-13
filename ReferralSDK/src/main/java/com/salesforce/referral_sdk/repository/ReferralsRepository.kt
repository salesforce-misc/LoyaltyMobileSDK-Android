package com.salesforce.referral_sdk.repository

import com.salesforce.referral_sdk.api.ApiResponse
import com.salesforce.referral_sdk.api.ApiService
import com.salesforce.referral_sdk.entities.QueryResult
import com.salesforce.referral_sdk.entities.ReferralEnrollmentRequestBody
import com.salesforce.referral_sdk.entities.ReferralEnrollmentResponse
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
                val errorMessage = response.errorBody()?.string()
                ApiResponse.Error(errorMessage)
            }
        } catch (exception: IOException) {
            ApiResponse.NetworkError
        }  catch (exception: Exception) {
            ApiResponse.Error(exception.message)
        }
    }

    suspend fun fetchReferralsInfo(): ApiResponse<QueryResult<ReferralEntity>> {
        return safeApiCall { apiService.fetchReferralsInfo(
            "https://dsb000001oyrq2ai.test1.my.pc-rnd.site.com/NTOInsider/services/data/v59.0/query/",
            referralQuery(),
//            "Bearer 00DSB000001oyRq!AQEAQDuosoC5VCO.IPNEdkhcWl57txTEOnDgH.4zuRq3MWxPZxOyRhNmOnwS4dUqWMz5na_dizXtJ0xn0AdGzaBcblVle09Q"
            "Bearer 00DSB000001oyRq!AQEAQEKSzmVG3YGEznqp0fKcCKdRFTG3UywROfcbDrT6HoJ1S8_WJZkvnKBmA3xEirk3gtN7geT.k9Uwoh.Nb5A1NrVh.xTJ"
        ) }
    }

    suspend fun enrollToReferralProgram(
        membershipNumber: String,
        promotionName: String,
        promotionCode: String
    ): ApiResponse<ReferralEnrollmentResponse> {
        return safeApiCall {
            apiService.enrollToReferralProgram(
                "https://hutl.my.salesforce.com/services/data/v59.0/referral-programs/Referral%20Promotions/promotions/0c8B0000000CwW2IAK/member-enrollments",
                ReferralEnrollmentRequestBody(membershipNumber),
                "Bearer 00DSB000001oyRq!AQEAQEKSzmVG3YGEznqp0fKcCKdRFTG3UywROfcbDrT6HoJ1S8_WJZkvnKBmA3xEirk3gtN7geT.k9Uwoh.Nb5A1NrVh.xTJ"
            )
        }
    }

    private fun referralQuery(): String {
        return "SELECT Id, ClientEmail, ReferrerEmail, ReferralDate, CurrentPromotionStage.Type FROM Referral ORDER BY ReferralDate DESC"
    }
}