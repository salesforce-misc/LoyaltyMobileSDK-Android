package com.salesforce.referral_sdk.repository

import com.salesforce.referral_sdk.EnrollmentChannel
import com.salesforce.referral_sdk.MemberStatus
import com.salesforce.referral_sdk.TransactionalJournalStatementFrequency
import com.salesforce.referral_sdk.TransactionalJournalStatementMethod
import com.salesforce.referral_sdk.api.ApiResponse
import com.salesforce.referral_sdk.api.ApiService
import com.salesforce.referral_sdk.api.ReferralAPIConfig
import com.salesforce.referral_sdk.api.ReferralAPIConfig.getRequestUrl
import com.salesforce.referral_sdk.api.safeApiCall
import com.salesforce.referral_sdk.entities.ATTRIBUTES_COUNTRY
import com.salesforce.referral_sdk.entities.ATTRIBUTES_STATE
import com.salesforce.referral_sdk.entities.AssociatedPersonAccountDetails
import com.salesforce.referral_sdk.entities.QueryResult
import com.salesforce.referral_sdk.entities.ReferralAttributes
import com.salesforce.referral_sdk.entities.ReferralNewEnrollmentRequestBody
import com.salesforce.referral_sdk.entities.ReferralEnrollmentResponse
import com.salesforce.referral_sdk.entities.ReferralEntity
import com.salesforce.referral_sdk.entities.ReferralExistingEnrollmentRequest
import com.salesforce.referral_sdk.entities.referral_event.ReferralEventRequest
import com.salesforce.referral_sdk.entities.referral_event.ReferralEventResponse
import com.salesforce.referral_sdk.utils.getCurrentDateTime
import com.salesforce.referral_sdk.utils.getRandomString
import javax.inject.Inject

open class ReferralsRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun fetchReferralsInfo(accessToken: String, durationInDays: Int): ApiResponse<QueryResult<ReferralEntity>> {
        return safeApiCall {
            apiService.fetchReferralsInfo(
                "https://dsb000001oyrq2ai.test1.my.pc-rnd.site.com/NTOInsider/services/data/v59.0/query/",
                referralQuery(durationInDays),
                "Bearer 00DSB000001oyRq!AQEAQLxnVEfUL.qmZZDGHysZTSqDArSW9yHRSo7Pj.yJpn9Zc6Ne7ieJ9iENwhpE8jsRTprt6lH0m9X05DjL6SbogeLDwY6L"
            )
        }
    }

    suspend fun enrollNewCustomerAsAdvocateOfPromotion(
        firstName: String,
        lastName: String,
        email: String,
        state: String? = null,
        country: String? = null,
        enrollmentChannel: EnrollmentChannel = EnrollmentChannel.EMAIL,
        memberStatus: MemberStatus = MemberStatus.ACTIVE,
        transactionalJournalFrequency: TransactionalJournalStatementFrequency = TransactionalJournalStatementFrequency.MONTHLY,
        transactionalJournalMethod: TransactionalJournalStatementMethod = TransactionalJournalStatementMethod.EMAIL,
        accessToken: String,
        promotionName: String,
        promotionCode: String
    ): ApiResponse<ReferralEnrollmentResponse> {
        return safeApiCall {
            apiService.enrollNewCustomerAsAdvocateOfPromotion(
                getRequestUrl(ReferralAPIConfig.Resource.ReferralMemberEnrolment(promotionName, promotionCode)),
                ReferralNewEnrollmentRequestBody(
                    state?.let { ReferralAttributes(mapOf(ATTRIBUTES_STATE to state)) },
                    AssociatedPersonAccountDetails(
                        country?.let { ReferralAttributes(mapOf(ATTRIBUTES_COUNTRY to country)) },
                        "false",
                        email,
                        firstName,
                        lastName
                    ),
                    enrollmentChannel.channel,
                    memberStatus.status,
                    getRandomString(8),
                    transactionalJournalFrequency.frequency,
                    transactionalJournalMethod.method
                ),
                "Bearer $accessToken"
            )
        }
    }

    private fun referralQuery(durationInDays: Int): String {
        return "SELECT Id, ClientEmail, ReferrerEmail, ReferralDate, CurrentPromotionStage.Type FROM Referral WHERE ReferralDate = LAST_N_DAYS:$durationInDays ORDER BY ReferralDate DESC"
    }

    suspend fun enrollExistingAdvocateToPromotionWithMembershipNumber(
        promotionName: String,
        promotionCode: String,
        accessToken: String,
        membershipNumber: String
    ): ApiResponse<ReferralEnrollmentResponse> {
        return enrollExistingAdvocateToNewPromotion(promotionName, promotionCode, accessToken, ReferralExistingEnrollmentRequest(membershipNumber = membershipNumber))
    }

    suspend fun enrollExistingAdvocateToPromotionWithContactId(
        promotionName: String,
        promotionCode: String,
        accessToken: String,
        contactId: String
    ): ApiResponse<ReferralEnrollmentResponse> {
        return enrollExistingAdvocateToNewPromotion(promotionName, promotionCode, accessToken, ReferralExistingEnrollmentRequest(contactId = contactId))
    }

    private suspend fun enrollExistingAdvocateToNewPromotion(
        promotionName: String,
        promotionCode: String,
        accessToken: String,
        existingMemberRequest: ReferralExistingEnrollmentRequest
    ) = safeApiCall {
        apiService.enrollExistingAdvocateToPromotion(
            getRequestUrl(ReferralAPIConfig.Resource.ReferralMemberEnrolment(promotionName, promotionCode)),
            existingMemberRequest,
            "Bearer $accessToken"
        )
    }

    suspend fun sendReferrals(
        referralCode: String,
        emails: String,
        accessToken: String
    ): ApiResponse<ReferralEventResponse> {
        return safeApiCall {
            apiService.sendReferrals(
                getRequestUrl(ReferralAPIConfig.Resource.ReferralEvent),
                ReferralEventRequest(
                    referralCode = "FV1MEANB-TestPromo1",
                    joiningDate = getCurrentDateTime().orEmpty(),
                    email = emails,
                    firstName = "siva",
                    lastName = "polam"
                ),
                "Bearer $accessToken"
            )
        }
    }

    fun setInstanceUrl(instanceUrl: String) {
        ReferralAPIConfig.instanceUrl = instanceUrl
    }
}