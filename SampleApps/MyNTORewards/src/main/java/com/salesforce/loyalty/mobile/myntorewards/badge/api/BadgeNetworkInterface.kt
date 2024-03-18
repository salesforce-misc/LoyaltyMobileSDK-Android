package com.salesforce.loyalty.mobile.myntorewards.badge.api

import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeList
import com.salesforce.loyalty.mobile.myntorewards.badge.models.RecordBadgeDetails
import com.salesforce.loyalty.mobile.myntorewards.badge.models.RecordBadgeList
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface BadgeNetworkInterface {

    @GET()
    suspend fun badgeList(
        @Url url: String,
        @Query("q") query: String?
    ): Result<LoyaltyBadgeList<RecordBadgeList>>


    @GET()
    suspend fun badgeDetail(
        @Url url: String,
        @Query("q") query: String?
    ): Result<LoyaltyBadgeList<RecordBadgeDetails>>

}