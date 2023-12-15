package com.salesforce.referral_sdk.repository

import com.salesforce.referral_sdk.api.ApiResponse
import com.salesforce.referral_sdk.api.ApiService
import com.salesforce.referral_sdk.entities.AdditionalMemberFieldValues
import com.salesforce.referral_sdk.entities.AdditionalPersonAccountFieldValues
import com.salesforce.referral_sdk.entities.AssociatedPersonAccountDetails
import com.salesforce.referral_sdk.entities.AttributesX
import com.salesforce.referral_sdk.entities.QueryResult
import com.salesforce.referral_sdk.entities.ReferralAttributes
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
        return safeApiCall {
            apiService.fetchReferralsInfo(
                "https://dsb000001oyrq2ai.test1.my.pc-rnd.site.com/NTOInsider/services/data/v59.0/query/",
                referralQuery(),
                "Bearer 00DSB000001oyRq!AQEAQPZ.MQEf0vQfskeS1XleR.d5XrBQNusiI99NCV.bytZawAO8JZ8a9WOvUDUiIQhg.77yChQ2BXxA11lWIHb4mgYxz0Bp"
            )
        }
    }

    suspend fun enrollToReferralProgram(
        accessToken: String,
        membershipNumber: String,
        promotionName: String,
        promotionCode: String
    ): ApiResponse<ReferralEnrollmentResponse> {
        return safeApiCall {
            apiService.enrollToReferralProgram(
                "https://dsb000001oyrq2ai.test1.my.pc-rnd.site.com/NTOInsider/services/data/v59.0/referral-programs/NTO%20Insider/promotions/Test1009/member-enrollments",
                ReferralEnrollmentRequestBody(
                    AdditionalMemberFieldValues(ReferralAttributes("California")),
                    AssociatedPersonAccountDetails(
                        AdditionalPersonAccountFieldValues(AttributesX("US")),
                        "false",
                        "testmember@salesforce.com",
                        "Test",
                        "PS"
                    ),
                    "Email",
                    "Active",
                    membershipNumber,
                    "Monthly",
                    "Mail"
                ),
                "Bearer $accessToken"
            )
        }
    }

    private fun referralQuery(): String {
        return "SELECT Id, ClientEmail, ReferrerEmail, ReferralDate, CurrentPromotionStage.Type FROM Referral WHERE ReferralDate = LAST_N_DAYS:90 ORDER BY ReferralDate DESC"
    }
}