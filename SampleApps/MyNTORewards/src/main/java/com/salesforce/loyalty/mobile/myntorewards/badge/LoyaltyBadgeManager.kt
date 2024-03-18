package com.salesforce.loyalty.mobile.myntorewards.badge
import com.salesforce.loyalty.mobile.myntorewards.badge.api.BadgeConfig
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeList
import com.salesforce.loyalty.mobile.myntorewards.badge.models.RecordBadgeDetails
import com.salesforce.loyalty.mobile.myntorewards.badge.models.RecordBadgeList
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


    suspend fun fetchBadgeList(membershipKey: String
    ): Result<LoyaltyBadgeList<RecordBadgeList>> {
        Logger.d(TAG, "BadgeList")

        return badgeClient.badgeApi.badgeList(
            getBadgeLSOQLUrl(), fetchBadgeListQuery(membershipKey)
        )
    }
    suspend fun fetchBadgeDetails(membershipKey: String
    ): Result<LoyaltyBadgeList<RecordBadgeDetails>> {
        Logger.d(TAG, "fetchBadgeDetails")

        return badgeClient.badgeApi.badgeDetail(
            getBadgeLSOQLUrl(), fetchBadgeListDetailQuery(membershipKey)
        )
    }

    private fun getBadgeLSOQLUrl(): String {
        return mInstanceUrl + BadgeConfig.SOQL_QUERY_PATH + BadgeConfig.SOQL_QUERY_VERSION + BadgeConfig.QUERY
    }


    //membership Key is not being used. Its placeholder for future use to fetch badges based on membership ID or any other program ID
    private fun fetchBadgeListQuery(membershipKey: String): String {
        return "select Name,StartDate,EndDate,Reason,Status,LoyaltyProgramMemberId,LoyaltyProgramBadgeId FROM LoyaltyProgramMemberBadge"
    }

    //membership Key is not being used. Its placeholder for future use to fetch badges based on membership ID or any other program ID
    private fun fetchBadgeListDetailQuery(membershipKey: String): String {
        return "select Description,ImageUrl,LoyaltyProgramId,Name,StartDate,Status,ValidityDuration,ValidityDurationUnit,ValidityEndDate,ValidityType FROM LoyaltyProgramBadge"
    }

}
