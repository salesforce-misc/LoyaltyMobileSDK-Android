package com.salesforce.loyalty.mobile.myntorewards.referrals.api

import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.QueryResult
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralCode
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnrollmentInfo
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface ReferralsLocalApiService {
    @GET
    suspend fun fetchReferralsInfo(
        @Url url: String,
        @Query("q") query: String?
    ): Response<QueryResult<ReferralEntity>>

    @GET
    suspend fun fetchMemberReferralId(
        @Url url: String,
        @Query("q") query: String?
    ): Response<QueryResult<ReferralCode>>

    @GET
    suspend fun checkIfMemberEnrolled(
        @Url url: String,
        @Query("q") query: String?
    ): Response<QueryResult<ReferralEnrollmentInfo>>
}