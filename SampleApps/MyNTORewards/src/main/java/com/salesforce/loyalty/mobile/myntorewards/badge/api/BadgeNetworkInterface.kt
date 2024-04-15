package com.salesforce.loyalty.mobile.myntorewards.badge.api

import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeListProgramMember
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeProgramList

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface BadgeNetworkInterface {

    @GET()
    suspend fun badgeList(
        @Url url: String,
        @Query("q") query: String?
    ): Result<LoyaltyBadgeListProgramMember>


    @GET()
    suspend fun badgeDetail(
        @Url url: String,
        @Query("q") query: String?
    ): Result<LoyaltyBadgeProgramList>

}