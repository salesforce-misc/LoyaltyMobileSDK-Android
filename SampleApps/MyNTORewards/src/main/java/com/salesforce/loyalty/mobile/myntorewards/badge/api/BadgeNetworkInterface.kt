package com.salesforce.loyalty.mobile.myntorewards.badge.api

import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeList
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyProgramBadgeListRecord
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyProgramMemberBadgeListRecord
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface BadgeNetworkInterface {

    @GET()
    suspend fun badgeList(
        @Url url: String,
        @Query("q") query: String?
    ): Result<LoyaltyBadgeList<LoyaltyProgramMemberBadgeListRecord>>


    @GET()
    suspend fun badgeDetail(
        @Url url: String,
        @Query("q") query: String?
    ): Result<LoyaltyBadgeList<LoyaltyProgramBadgeListRecord>>

}