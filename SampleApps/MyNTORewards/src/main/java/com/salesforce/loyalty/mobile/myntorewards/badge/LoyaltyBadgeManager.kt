package com.salesforce.loyalty.mobile.myntorewards.badge
import com.salesforce.loyalty.mobile.myntorewards.badge.api.BadgeConfig
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeList
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyProgramBadgeListRecord
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyProgramMemberBadgeListRecord
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.NetworkClient
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


    suspend fun fetchLoyaltyProgramMemberBadge(membershipKey: String= ""
    ): Result<LoyaltyBadgeList<LoyaltyProgramMemberBadgeListRecord>> {
        Logger.d(TAG, "BadgeList")

        return badgeClient.badgeApi.badgeList(
            getBadgeLSOQLUrl(), fetchProgramMemberBadge(membershipKey)
        )
    }

    suspend fun fetchLoyaltyProgramBadge(membershipKey: String=""
    ): Result<LoyaltyBadgeList<LoyaltyProgramBadgeListRecord>> {
        Logger.d(TAG, "fetchBadgeDetails")

        return badgeClient.badgeApi.badgeDetail(
            getBadgeLSOQLUrl(), fetchProgramBadgeQuery(membershipKey)
        )
    }

    private fun getBadgeLSOQLUrl(): String {
        return mInstanceUrl + BadgeConfig.SOQL_QUERY_PATH + BadgeConfig.SOQL_QUERY_VERSION + BadgeConfig.QUERY
    }


    //membership Key is not being used. Its placeholder for future use to fetch badges based on membership ID or any other program ID
    private fun fetchProgramMemberBadge(membershipKey: String): String {
        return "select Name,StartDate,EndDate,Reason,Status,LoyaltyProgramMemberId,LoyaltyProgramBadgeId FROM LoyaltyProgramMemberBadge"
    }

    //membership Key is not being used. Its placeholder for future use to fetch badges based on membership ID or any other program ID
    private fun fetchProgramBadgeQuery(membershipKey: String): String {
        return "select Description,ImageUrl,LoyaltyProgramId,Name,StartDate,Id,Status,ValidityDuration,ValidityDurationUnit,ValidityEndDate,ValidityType FROM LoyaltyProgramBadge"
    }

}
