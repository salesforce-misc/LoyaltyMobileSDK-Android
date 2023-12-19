/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.loyalty.mobile.sources.loyaltyAPI

import com.google.gson.Gson
import com.salesforce.loyalty.mobile.sources.forceUtils.DateUtils
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import com.salesforce.loyalty.mobile.sources.loyaltyExtensions.LoyaltyUtils
import com.salesforce.loyalty.mobile.sources.loyaltyModels.*
import kotlinx.coroutines.delay
import java.io.InputStreamReader
import java.lang.RuntimeException

/**
 * LoyaltyAPIManager class manages the requests related to loyalty program and it inturn invokes the rest APIs
 */
class LoyaltyAPIManager constructor(auth: ForceAuthenticator, instanceUrl: String, loyaltyClient: NetworkClient){

    companion object {
        private const val TAG = "LoyaltyAPIManager"
    }

    private val authenticator: ForceAuthenticator

    private val mLoyaltyClient: NetworkClient

    private val mInstanceUrl: String

    init {
        authenticator = auth
        mInstanceUrl = instanceUrl
        mLoyaltyClient = loyaltyClient
    }

    /**
     * API to create new individual enrollment.
     * Reference: https://developer.salesforce.com/docs/atlas.en-us.loyalty.meta/loyalty/connect_resources_enroll_individual_member.htm
     *
     * @param firstName First name of the user
     * @param lastName Last name of the user
     * @param email Email ID of the user
     * @param additionalContactAttributes Map of contact related attributes like phone number
     * @param emailNotification If user prefers to have email notifications.
     * @param memberStatus Status of the enrolled member [MemberStatus]
     * @param createTransactionJournals Indicates whether to create the transaction journal records (true) or not (false)
     * @param transactionalJournalStatementFrequency The frequency at which transaction journal statements must be delivered to the member.[TransactionalJournalStatementFrequency]
     * @param transactionalJournalStatementMethod The method used to deliver transaction journal statements to the member.[TransactionalJournalStatementMethod]
     * @param enrollmentChannel The channel used to by loyalty program member to enroll in to the loyalty program. [EnrollmentChannel]
     * @param canReceivePromotions Indicates whether the loyalty program member can receive promotions.
     * @param canReceivePartnerPromotions Indicates whether the member can receive partner promotions for the program (true) or not (false).
     * @return [EnrollmentResponse] wrapped in Kotlin [Result] class if successful.
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
        Logger.d(TAG, "postEnrollment() $firstName $lastName $email $emailNotification")
        val associatedContactDetails = AssociatedContactDetails(
            firstName = firstName,
            lastName = lastName,
            email = email,
            allowDuplicateRecords = false,
            AdditionalContactFieldValues(additionalContactAttributes)
        )
        val body = EnrollmentRequest(
            enrollmentDate = DateUtils.getCurrentDateTime(DateUtils.DATE_FORMAT_YYYYMMDDTHHMMSS),
            membershipNumber = LoyaltyUtils.getRandomString(8),
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
        return mLoyaltyClient.getNetworkClient().postEnrollment(
            LoyaltyConfig.getRequestUrl(
                mInstanceUrl,
                LoyaltyConfig.Resource.IndividualEnrollment(LoyaltyConfig.LOYALTY_PROGRAM_NAME)
            ),
            body
        )
    }

    /**
     * API to retrieve Member Profile details.
     * Reference: https://developer.salesforce.com/docs/atlas.en-us.loyalty.meta/loyalty/connect_resources_member_profile.htm
     *
     * @param memberId The ID of the loyalty program member.
     * @param memberShipNumber The membership number of the loyalty program member.
     * @param programCurrencyName The name of the loyalty program currency associated with the member.
     * @return [MemberProfileResponse] wrapped in Kotlin [Result] class if successful.
     */
    suspend fun getMemberProfile(
        memberId: String?,
        memberShipNumber: String?,
        programCurrencyName: String?
    ): Result<MemberProfileResponse> {
        Logger.d(TAG, "getMemberProfile() $memberId $memberShipNumber")

        return mLoyaltyClient.getNetworkClient().getMemberProfile(
            LoyaltyConfig.getRequestUrl(
                mInstanceUrl,
                LoyaltyConfig.Resource.MemberProfile(LoyaltyConfig.LOYALTY_PROGRAM_NAME)
            ),
            memberId = memberId,
            membershipNumber = memberShipNumber,
            programCurrencyName = programCurrencyName
        )
    }

    /**
     * API to retrieve Member Benefits details.
     * Reference: https://developer.salesforce.com/docs/atlas.en-us.loyalty.meta/loyalty/connect_resources_member_benefits.htm
     *
     * @param memberId The ID of the loyalty program member.
     * @param membershipNumber The membership number of the loyalty program member.
     * @return [MemberBenefitsResponse] wrapped in Kotlin [Result] class if successful.
     */
    suspend fun getMemberBenefits(
        memberId: String,
        membershipNumber: String?
    ): Result<MemberBenefitsResponse> {
        Logger.d(TAG, "getMemberBenefits() $memberId")

        return mLoyaltyClient.getNetworkClient().getMemberBenefits(
            LoyaltyConfig.getRequestUrl(mInstanceUrl, LoyaltyConfig.Resource.MemberBenefits(memberId)),
            membershipNumber
        )
    }
    
    /**
     * API to get Transactions - Makes an asynchronous request for transactions data from the Salesforce
     *
     * @param membershipNumber The membership number of the loyalty program member.
     * @param pageNumber Number of the page you want returned.
     * @param journalTypeName The journal type of transaction journals that are retrieved.
     * @param journalSubTypeName The journal subtype of transaction journals that are retrieved.
     * @param periodStartDate Retrieve transaction journals until this date.
     * @param periodEndDate Retrieve transaction journals until this date.
     * @return [TransactionsResponse] wrapped in Kotlin [Result] class if successful.
     */
    suspend fun getTransactions(
        membershipNumber: String,
        pageNumber: Int?,
        journalTypeName: String?,
        journalSubTypeName: String?,
        periodStartDate: String?,
        periodEndDate: String?
    ): Result<TransactionsResponse> {
        Logger.d(TAG, "getTransactions() $membershipNumber")
        val transPageNumber = pageNumber ?: 1
        return mLoyaltyClient.getNetworkClient().getTransactions(
            LoyaltyConfig.getRequestUrl(mInstanceUrl,
                LoyaltyConfig.Resource.TransactionsHistory(
                    LoyaltyConfig.LOYALTY_PROGRAM_NAME, membershipNumber
                )
            ),
            transPageNumber,
            journalTypeName,
            journalSubTypeName,
            periodStartDate,
            periodEndDate
        )
    }



    /**
     * API to retrieve Eligible Promotions of the loyalty member.
     * Reference: https://developer.salesforce.com/docs/atlas.en-us.loyalty.meta/loyalty/connect_resources_enroll_ln_promotion.htm
     *
     * @param membershipNumber The membership number of the loyalty program member.
     * @param memberId The ID of the loyalty program member.
     * @return [PromotionsResponse] wrapped in Kotlin [Result] class if successful.
     */
    suspend fun getEligiblePromotions(
        membershipNumber: String?,
        memberId: String?
    ): Result<PromotionsResponse> {
        Logger.d(TAG, "getEligiblePromotions() $membershipNumber")

        var processParameterMap: MutableMap<String, Any?> = mutableMapOf()
        if (membershipNumber?.isNotEmpty() == true) {
            processParameterMap[LoyaltyConfig.KEY_MEMBERSHIP_NUMBER] = membershipNumber
        } else if (memberId?.isNotEmpty() == true) {
            processParameterMap[LoyaltyConfig.KEY_MEMBER_ID] = memberId
        }

        val requestBody: PromotionsRequest =
            PromotionsRequest(listOf(processParameterMap))
        return mLoyaltyClient.getNetworkClient().getEligiblePromotions(
            LoyaltyConfig.getRequestUrl(mInstanceUrl,
                LoyaltyConfig.Resource.LoyaltyProgramProcess(
                    LoyaltyConfig.LOYALTY_PROGRAM_NAME,
                    LoyaltyConfig.ProgramProcessName.GET_PROMOTIONS
                )
            ),
            requestBody
        )
    }

    /**
     * API to Enroll in Promotion.
     * Reference: https://developer.salesforce.com/docs/atlas.en-us.loyalty.meta/loyalty/connect_resources_enroll_ln_promotion.htm
     *
     * @param membershipNumber The membership number of the loyalty program member.
     * @param promotionName Name of the promotion enrolled to.
     * @return [EnrollPromotionsResponse] wrapped in Kotlin [Result] class if successful.
     */
    suspend fun enrollInPromotions(
        membershipNumber: String,
        promotionName: String
    ): Result<EnrollPromotionsResponse> {
        Logger.d(TAG, "enrollInPromotions() $membershipNumber Promotion Name: $promotionName")

        val requestBody: PromotionsRequest =
            PromotionsRequest(listOf(mapOf(LoyaltyConfig.KEY_MEMBERSHIP_NUMBER to membershipNumber,
            LoyaltyConfig.KEY_PROMOTION_NAME to promotionName)))
        return mLoyaltyClient.getNetworkClient().enrollInPromotion(
            LoyaltyConfig.getRequestUrl(mInstanceUrl,
                LoyaltyConfig.Resource.LoyaltyProgramProcess(
                    LoyaltyConfig.LOYALTY_PROGRAM_NAME,
                    LoyaltyConfig.ProgramProcessName.ENROLL_IN_PROMOTION
                )
            ),
            requestBody
        )
    }

    /**
     * API to Unenroll from a Promotion.
     *
     * @param membershipNumber The membership number of the loyalty program member.
     * @param promotionName Name of the promotion un enrolled from.
     * @return [UnenrollPromotionResponse] wrapped in Kotlin [Result] class if successful.
     */
    suspend fun unEnrollPromotion(
        membershipNumber: String,
        promotionName: String
    ): Result<UnenrollPromotionResponse> {
        Logger.d(TAG, "unEnrollPromotion() $membershipNumber Promotion Name: $promotionName")

        val requestBody: PromotionsRequest =
            PromotionsRequest(listOf(mapOf(LoyaltyConfig.KEY_MEMBERSHIP_NUMBER to membershipNumber,
                LoyaltyConfig.KEY_PROMOTION_NAME to promotionName)))

        return mLoyaltyClient.getNetworkClient().unenrollPromotion(
            LoyaltyConfig.getRequestUrl(mInstanceUrl,
                LoyaltyConfig.Resource.LoyaltyProgramProcess(
                    LoyaltyConfig.LOYALTY_PROGRAM_NAME,
                    LoyaltyConfig.ProgramProcessName.UNENROLL_PROMOTION
                )
            ),
            requestBody
        )
    }

    /**
     * API to get vouchers information for the loyalty member.
     * Reference: https://developer.salesforce.com/docs/atlas.en-us.loyalty.meta/loyalty/connect_resources_member_vouchers.htm
     *
     * @param membershipNumber The membership number of the loyalty program member.
     * @param voucherStatus The list of statuses for which you want to the get member’s vouchers.
     * @param pageNumber Number of the page you want returned. If you don’t specify a value, the first page is returned. Each page contains 200 vouchers and the vouchers are sorted based on the date on which the Voucher record was created.
     * @param productId The ID of products that are related with the member vouchers you want to get. You can specify the ID of up to 20 products.
     * @param productCategoryId The ID of product categories that are related with the member vouchers you want to get. You can specify the ID of up to 20 product categories.
     * @param productName The product name associated with the vouchers to be retrieved. You can specify up to 20 product names.
     * @param productCategoryName The names of product categories that are related with the member vouchers you want to get. You can specify the ID of up to 20 product categories.
     * @return [VoucherResult] wrapped in Kotlin [Result] class if successful.
     */
    suspend fun getVouchers(
        membershipNumber: String,
        voucherStatus: Array<String>?,
        pageNumber: Int?,
        productId: Array<String>?,
        productCategoryId: Array<String>?,
        productName: Array<String>?,
        productCategoryName: Array<String>?
    ): Result<VoucherResult> {
        Logger.d(TAG, "getVouchers() $membershipNumber")
        val voucherStatus = getStringOfArrayItems(voucherStatus)
        val productId = getStringOfArrayItems(productId)
        val productCategoryId = getStringOfArrayItems(productCategoryId)
        val productName = getStringOfArrayItems(productName)
        val productCategoryName = getStringOfArrayItems(productCategoryName)

        return mLoyaltyClient.getNetworkClient().getVouchers(
            LoyaltyConfig.getRequestUrl(
                mInstanceUrl,
                LoyaltyConfig.Resource.Vouchers(
                    LoyaltyConfig.LOYALTY_PROGRAM_NAME,
                    membershipNumber
                )
            ),
            voucherStatus, pageNumber, productId, productCategoryId, productName, productCategoryName
        )
    }

    /**
     * Convert array items into comma seperated string.
     * @param items Array of strings
     * @return String Comma seperated array items into a string
     */
    private fun getStringOfArrayItems(items: Array<String>?): String? = items?.reduce { acc, item -> "$acc,$item" }

    suspend fun getGameReward(gameParticipantRewardId: String, mockResponse: Boolean): Result<GameRewardResponse> {
        Logger.d(TAG, "getGameReward()")

        if (mockResponse) {
            val reader =
                InputStreamReader(this.javaClass.classLoader?.getResourceAsStream("GameRewards.json"))
            val content: String = reader.readText()
            reader.close()
            val response =
                Gson().fromJson(content, GameRewardResponse::class.java)
            delay(2000)
            //return Result.failure(RuntimeException()) // to test failure scenario
            return Result.success(response)
        } else {
            return mLoyaltyClient.getNetworkClient().getGameReward(
                LoyaltyConfig.getRequestUrl(mInstanceUrl, LoyaltyConfig.Resource.GameReward(gameParticipantRewardId)),
            )
        }
    }

    suspend fun getGames(participantId: String, gameParticipantRewardId: String? = null, mockResponse: Boolean): Result<Games> {
        Logger.d(TAG, "getGames() participantId: $participantId gameParticipantRewardId: $gameParticipantRewardId")

        if (mockResponse) {
            val reader =
                InputStreamReader(this.javaClass.classLoader?.getResourceAsStream("Games.json"))
            val content: String = reader.readText()
            reader.close()
            val response =
                Gson().fromJson(content, Games::class.java)
            return Result.success(response)
        } else {
            return mLoyaltyClient.getNetworkClient().getGames(
                LoyaltyConfig.getRequestUrl(
                    mInstanceUrl,
                    LoyaltyConfig.Resource.Games(participantId)
                ), gameParticipantRewardId = gameParticipantRewardId
            )
        }
    }
}