package com.salesforce.loyalty.mobile.myntorewards.referrals

import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralsLocalApiService
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralCode
import com.salesforce.referral_sdk.EnrollmentChannel
import com.salesforce.referral_sdk.MemberStatus
import com.salesforce.referral_sdk.TransactionalJournalStatementFrequency
import com.salesforce.referral_sdk.TransactionalJournalStatementMethod
import com.salesforce.referral_sdk.api.ApiResponse
import com.salesforce.referral_sdk.api.ApiService
import com.salesforce.referral_sdk.api.safeApiCall
import com.salesforce.referral_sdk.entities.ATTRIBUTES_COUNTRY
import com.salesforce.referral_sdk.entities.ATTRIBUTES_STATE
import com.salesforce.referral_sdk.entities.AssociatedPersonAccountDetails
import com.salesforce.referral_sdk.entities.QueryResult
import com.salesforce.referral_sdk.entities.ReferralAttributes
import com.salesforce.referral_sdk.entities.ReferralNewEnrollmentRequestBody
import com.salesforce.referral_sdk.entities.ReferralEnrollmentResponse
import com.salesforce.referral_sdk.entities.ReferralEntity
import com.salesforce.referral_sdk.entities.ReferralExistingEnrollmentRequest
import com.salesforce.referral_sdk.entities.referral_event.ReferralEventRequest
import com.salesforce.referral_sdk.entities.referral_event.ReferralEventResponse
import com.salesforce.referral_sdk.repository.ReferralsRepository
import com.salesforce.referral_sdk.utils.getCurrentDateTime
import com.salesforce.referral_sdk.utils.getRandomString
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class ReferralsLocalRepository @Inject constructor(
    private val apiService: ReferralsLocalApiService
) {

    suspend fun fetchMemberReferralCode(accessToken: String, membershipNumber: String): ApiResponse<QueryResult<ReferralCode>> {
        return safeApiCall {
            apiService.fetchMemberReferralId(
                "https://dsb000001oyrq2ai.test1.my.pc-rnd.site.com/NTOInsider/services/data/v59.0/query/",
                "Select MembershipNumber, referralcode from loyaltyprogrammember where MembershipNumber =\'$membershipNumber\'",
                "Bearer $accessToken"
            )
        }
    }

}