package com.salesforce.referral_sdk.api

import com.salesforce.referral_sdk.entities.QueryResult
import com.salesforce.referral_sdk.entities.ReferralEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET()
    suspend fun fetchReferralsInfo(
        @Url url: String,
        @Query("q") query: String?,
        @Header("Authorization")bearerToken: String
    ): Response<QueryResult<ReferralEntity>>
}