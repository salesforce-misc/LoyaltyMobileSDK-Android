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
        phone: String,
        emailNotification: Boolean
    ): Result<EnrollmentResponse> {
        Log.d(TAG, "postEnrollment() $firstName $lastName $email $phone $emailNotification")
        val associatedContactDetails = AssociatedContactDetails(
            firstName = firstName,
            lastName = lastName,
            email = email,
            allowDuplicateRecords = false,
            AdditionalContactFieldValues(Attributes(phone = phone))
        )
        val body = EnrollmentRequest(
            enrollmentDate = DateUtils.getCurrentDateInYYYYMMDDTHHMMSS(),
            membershipNumber = LoyaltyUtils.generateRandomString(),
            associatedContactDetails = associatedContactDetails,
            memberStatus = "Active",
            createTransactionJournals = true,
            transactionJournalStatementFrequency = "Monthly",
            transactionJournalStatementMethod = "Mail",
            enrollmentChannel = "Email",
            canReceivePromotions = true,
            canReceivePartnerPromotions = true,
            membershipEndDate = null,
            AdditionalMemberFieldValues(MemberAttributes(emailNotifications = emailNotification))
        )
        return LoyaltyClient.loyaltyApi.postEnrollment(
            body
        )
    }
}