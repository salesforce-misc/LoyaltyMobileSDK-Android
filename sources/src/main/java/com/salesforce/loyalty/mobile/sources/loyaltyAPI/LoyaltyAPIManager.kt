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

    /**
     * API to retrieve Member Profile details
     *
     * @param memberId Loyalty Program Member Id
     * @return MemberProfileResponse
     */
    suspend fun getMemberProfile(
        memberId: String?,
        memberShipNumber: String?,
        programCurrencyName: String?
    ): Result<MemberProfileResponse> {
        Log.d(TAG, "getMemberProfile() $memberId $memberShipNumber")

        return LoyaltyClient.loyaltyApi.getMemberProfile(
            LoyaltyConfig.getRequestUrl(LoyaltyConfig.Resource.MemberProfile(LoyaltyConfig.LOYALTY_PROGRAM_NAME)),
            memberId = memberId,
            membershipNumber = memberShipNumber,
            programCurrencyName = programCurrencyName
        )
    }

    /**
     * API to retrieve Member Benefits detail
     *
     * @param memberId Loyalty Program Member Id
     * @param membershipNumber Unique membership number of the user
     * @return MemberBenefitsResponse
     */
    suspend fun getMemberBenefits(
        memberId: String,
        membershipNumber: String
    ): Result<MemberBenefitsResponse> {
        Log.d(TAG, "getMemberBenefits() $memberId")

        return LoyaltyClient.loyaltyApi.getMemberBenefits(
            LoyaltyConfig.getRequestUrl(LoyaltyConfig.Resource.MemberBenefits(memberId)),
            membershipNumber
        )
    }

    /**
     * API to retrieve Eligible Promotions
     *
     * @param membershipNumber Unique membership number of the user
     * @return PromotionsResponse
     */
    suspend fun getEligiblePromotions(
        membershipNumber: String?,
        memberId: String?
    ): Result<PromotionsResponse> {
        Log.d(TAG, "getEligiblePromotions() $membershipNumber")

        var processParameterMap: MutableMap<String, Any?> = mutableMapOf()
        if (membershipNumber?.isNotEmpty() == true) {
            processParameterMap[LoyaltyConfig.KEY_MEMBERSHIP_NUMBER] = membershipNumber
        } else if (memberId?.isNotEmpty() == true) {
            processParameterMap[LoyaltyConfig.KEY_MEMBER_ID] = memberId
        }

        val requestBody: PromotionsRequest =
            PromotionsRequest(listOf(processParameterMap))
        return LoyaltyClient.loyaltyApi.getEligiblePromotions(
            LoyaltyConfig.getRequestUrl(
                LoyaltyConfig.Resource.LoyaltyProgramProcess(
                    LoyaltyConfig.LOYALTY_PROGRAM_NAME,
                    LoyaltyConfig.ProgramProcessName.GET_PROMOTIONS
                )
            ),
            requestBody
        )
    }

    /**
     * API to Enroll in Promotion
     *
     * @param membershipNumber Unique membership number of the user
     * @param promotionName Name of the promotion enrolled to
     * @return EnrollPromotionsResponse
     */
    suspend fun enrollInPromotions(
        membershipNumber: String,
        promotionName: String
    ): Result<EnrollPromotionsResponse> {
        Log.d(TAG, "enrollInPromotions() $membershipNumber Promotion Name: $promotionName")

        val requestBody: PromotionsRequest =
            PromotionsRequest(listOf(mapOf(LoyaltyConfig.KEY_MEMBERSHIP_NUMBER to membershipNumber,
            LoyaltyConfig.KEY_PROMOTION_NAME to promotionName)))
        return LoyaltyClient.loyaltyApi.enrollInPromotion(
            LoyaltyConfig.getRequestUrl(
                LoyaltyConfig.Resource.LoyaltyProgramProcess(
                    LoyaltyConfig.LOYALTY_PROGRAM_NAME,
                    LoyaltyConfig.ProgramProcessName.ENROLL_IN_PROMOTION
                )
            ),
            requestBody
        )
    }

    /**
     * API to Unenroll from Promotion
     *
     * @param membershipNumber Unique membership number of the user
     * @param promotionName Name of the promotion enrolled to
     * @return EnrollPromotionsResponse
     */
    suspend fun unrollPromotion(
        membershipNumber: String,
        promotionName: String
    ): Result<UnenrollPromotionResponse> {
        Log.d(TAG, "unrollPromotion() $membershipNumber Promotion Name: $promotionName")

        val requestBody: PromotionsRequest =
            PromotionsRequest(listOf(mapOf(LoyaltyConfig.KEY_MEMBERSHIP_NUMBER to membershipNumber,
                LoyaltyConfig.KEY_PROMOTION_NAME to promotionName)))

        return LoyaltyClient.loyaltyApi.unenrollPromotion(
            LoyaltyConfig.getRequestUrl(
                LoyaltyConfig.Resource.LoyaltyProgramProcess(
                    LoyaltyConfig.LOYALTY_PROGRAM_NAME,
                    LoyaltyConfig.ProgramProcessName.UNENROLL_PROMOTION
                )
            ),
            requestBody
        )
    }
}