package com.salesforce.loyalty.mobile.sources.loyaltyAPI

import android.util.Log
import com.salesforce.loyalty.mobile.sources.forceUtils.DateUtils
import com.salesforce.loyalty.mobile.sources.loyaltyExtensions.LoyaltyUtils
import com.salesforce.loyalty.mobile.sources.loyaltyModels.*

/**
 * LoyaltyAPIManager class holds APIs related to Loyalty Mobile SDK.
 */
object LoyaltyAPIManager {

    private const val TAG = "LoyaltyAPIManager"

    /**
     * API to create new individual enrollment
     *
     * @param firstName First name of the user
     * @param lastName Last name of the user
     * @param email Email ID of the user
     * @param phone Phone number of the user
     * @param emailNotification If user prefers to have email notifications.
     * @return EnrollmentResponse JSON of the enrollment API response.
     */
    suspend fun postEnrollment(
        firstName: String,
        lastName: String,
        email: String,
        additionalContactAttributes: Map<String, Any?>?,
        emailNotification: Boolean,
        memberStatus: MemberStatus,
        createTransactionJournals: Boolean,
        transactionalJournalStatementFrequency: TransactionalJournalStatementFrequency,
        transactionalJournalStatementMethod: TransactionalJournalStatementMethod,
        enrollmentChannel: EnrollmentChannel,
        canReceivePromotions: Boolean,
        canReceivePartnerPromotions: Boolean
    ): Result<EnrollmentResponse> {
        Log.d(TAG, "postEnrollment() $firstName $lastName $email $emailNotification")
        val associatedContactDetails = AssociatedContactDetails(
            firstName = firstName,
            lastName = lastName,
            email = email,
            allowDuplicateRecords = false,
            AdditionalContactFieldValues(additionalContactAttributes)
        )
        val body = EnrollmentRequest(
            enrollmentDate = DateUtils.getCurrentDateTime(DateUtils.DATE_FORMAT_YYYYMMDDTHHMMSS),
            membershipNumber = LoyaltyUtils.generateRandomString(),
            associatedContactDetails = associatedContactDetails,
            memberStatus = memberStatus.status,
            createTransactionJournals = createTransactionJournals,
            transactionJournalStatementFrequency = transactionalJournalStatementFrequency.frequency,
            transactionJournalStatementMethod = transactionalJournalStatementMethod.method,
            enrollmentChannel = enrollmentChannel.channel,
            canReceivePromotions = canReceivePromotions,
            canReceivePartnerPromotions = canReceivePartnerPromotions,
            membershipEndDate = null,
            AdditionalMemberFieldValues()
        )
        return LoyaltyClient.loyaltyApi.postEnrollment(
            LoyaltyConfig.getRequestUrl(LoyaltyConfig.Resource.IndividualEnrollment(LoyaltyConfig.LOYALTY_PROGRAM_NAME)),
            body
        )
    }

    suspend fun getMemberProfile(
        memberId: String
    ): Result<MemberProfileResponse> {
        Log.d(TAG, "getMemberProfile() $memberId")

        return LoyaltyClient.loyaltyApi.getMemberProfile(
            LoyaltyConfig.getRequestUrl(LoyaltyConfig.Resource.MemberProfile(LoyaltyConfig.LOYALTY_PROGRAM_NAME)),
            memberId
        )
    }
}