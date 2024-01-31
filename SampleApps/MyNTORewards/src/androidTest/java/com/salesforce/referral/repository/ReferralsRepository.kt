package com.salesforce.referral.repository

import android.util.Log
import com.google.gson.Gson
import com.salesforce.referral.EnrollmentChannel
import com.salesforce.referral.MemberStatus
import com.salesforce.referral.TransactionalJournalStatementFrequency
import com.salesforce.referral.TransactionalJournalStatementMethod
import com.salesforce.referral.api.ApiResponse
import com.salesforce.referral.api.ApiService
import com.salesforce.referral.api.ReferralAPIConfig
import com.salesforce.referral.api.ReferralAPIConfig.MEMBER_SHIP_RANDOM_NUMBER_COUNT
import com.salesforce.referral.api.ReferralAPIConfig.getRequestUrl
import com.salesforce.referral.api.safeApiCall
import com.salesforce.referral.entities.AssociatedPersonAccountDetails
import com.salesforce.referral.entities.ReferralEnrollmentResponse
import com.salesforce.referral.entities.ReferralExistingEnrollmentRequest
import com.salesforce.referral.entities.ReferralNewEnrollmentRequestBody
import com.salesforce.referral.entities.referral_event.Emails
import com.salesforce.referral.entities.referral_event.ReferralEventRequest
import com.salesforce.referral.entities.referral_event.ReferralEventResponse
import com.salesforce.referral.utils.getCurrentDateTime
import com.salesforce.referral.utils.getRandomString
import javax.inject.Inject

/**
 * @ReferralsRepository class processes requests from the client app and then responds with an ApiResponse based on network requests.
 * @param apiService ApiService instance to make network API calls
 */
open class ReferralsRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun enrollNewCustomerAsAdvocateOfPromotion(
        promotionName: String,
        promotionCode: String,
        firstName: String,
        lastName: String,
        email: String,
        state: String? = null,
        country: String? = null,
        enrollmentChannel: EnrollmentChannel = EnrollmentChannel.EMAIL,
        memberStatus: MemberStatus = MemberStatus.ACTIVE,
        transactionalJournalFrequency: TransactionalJournalStatementFrequency = TransactionalJournalStatementFrequency.MONTHLY,
        transactionalJournalMethod: TransactionalJournalStatementMethod = TransactionalJournalStatementMethod.EMAIL
    ): ApiResponse<ReferralEnrollmentResponse> {
        val gson = Gson()
        val mockResponse = MockResponseFileReader("memberEnrolledResponse.json").content
        var response= gson.fromJson(
            mockResponse,
            ReferralEnrollmentResponse::class.java
        ) as ReferralEnrollmentResponse

        return ApiResponse.Success(response)

    }

    suspend fun enrollExistingAdvocateToPromotionWithMembershipNumber(
        promotionName: String,
        promotionCode: String,
        membershipNumber: String,
        memberStatus: MemberStatus = MemberStatus.ACTIVE
    ): ApiResponse<ReferralEnrollmentResponse> {
        val gson = Gson()
        val mockResponse = MockResponseFileReader("memberEnrolledResponse.json").content
        var response= gson.fromJson(
            mockResponse,
            ReferralEnrollmentResponse::class.java
        ) as ReferralEnrollmentResponse

        return ApiResponse.Success(response)

    }


    suspend fun enrollExistingAdvocateToPromotionWithContactId(
        promotionName: String,
        promotionCode: String,
        contactId: String,
        memberStatus: MemberStatus = MemberStatus.ACTIVE
    ): ApiResponse<ReferralEnrollmentResponse> {
        Log.d("Akash", "method3")
        val gson = Gson()
        val mockResponse = MockResponseFileReader("memberEnrolledResponse.json").content
        var response= gson.fromJson(
            mockResponse,
            ReferralEnrollmentResponse::class.java
        ) as ReferralEnrollmentResponse

        return ApiResponse.Success(response)
    }
    private suspend fun enrollExistingAdvocateToNewPromotion(
        promotionName: String,
        promotionCode: String,
        existingMemberRequest: ReferralExistingEnrollmentRequest
    ) {

    }

    /**
     * This function is responsible for making a POST network request to send referral events/emails to given recipients
     * @param referralCode - Referral Code to share with given recipients
     * @param emails - Emails list to share the referral code
     */
    suspend fun sendReferrals(
        referralCode: String,
        emails: List<String>
    ): ApiResponse<ReferralEventResponse> {
        val gson = Gson()
        val mockResponse = MockResponseFileReader("memberEnrolledResponse.json").content
        var response= gson.fromJson(
            mockResponse,
            ReferralEventResponse::class.java
        ) as ReferralEventResponse

        return ApiResponse.Success(response)
    }

    /**
     * Set Instance or Base URL based on environment selection at client side
     */
    fun setInstanceUrl(instanceUrl: String) {
        ReferralAPIConfig.instanceUrl = instanceUrl
    }
}