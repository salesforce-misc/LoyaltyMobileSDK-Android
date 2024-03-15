package com.salesforce.loyalty.mobile.myntorewards.badge

import com.salesforce.loyalty.mobile.myntorewards.badge.api.BadgeConfig
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeDetails
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeList
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.NetworkClient
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger

class LoyaltyBadgeManager constructor(auth: ForceAuthenticator, instanceUrl: String) {

    companion object {
        private const val TAG = "LoyaltyBadgeManager"
    }

    private val authenticator: ForceAuthenticator

    private val badgeClient: NetworkClient

    private val mInstanceUrl: String

    init {
        authenticator = auth
        mInstanceUrl = instanceUrl
        badgeClient = NetworkClient(auth, instanceUrl)
    }


    suspend fun fetchBadgeList(membershipKey: String
    ): Result<LoyaltyBadgeList> {
        Logger.d(TAG, "BadgeList")

        return badgeClient.badgeApi.badgeList(
            getBadgeLSOQLUrl(), fetchBadgeListQuery(membershipKey)
        )
    }
    suspend fun fetchBadgeDetails(membershipKey: String
    ): Result<LoyaltyBadgeDetails> {
        Logger.d(TAG, "fetchBadgeDetails")

        return badgeClient.badgeApi.badgeDetail(
            getBadgeLSOQLUrl(), fetchBadgeListDetailQuery(membershipKey)
        )
    }

    private fun getBadgeLSOQLUrl(): String {
        return mInstanceUrl + BadgeConfig.SOQL_QUERY_PATH + BadgeConfig.SOQL_QUERY_VERSION + ReceiptScanningConfig.QUERY
    }

    private fun fetchBadgeListQuery(membershipKey: String): String {
        return "select Name,StartDate,EndDate,Reason,Status,LoyaltyProgramMemberId,LoyaltyProgramBadgeId FROM LoyaltyProgramMemberBadge"
    }
    private fun fetchBadgeListDetailQuery(membershipKey: String): String {
        return "select Description,ImageUrl,LoyaltyProgramId,Name,StartDate,Status,ValidityDuration,ValidityDurationUnit,ValidityEndDate,ValidityType FROM LoyaltyProgramBadge"
    }

}
