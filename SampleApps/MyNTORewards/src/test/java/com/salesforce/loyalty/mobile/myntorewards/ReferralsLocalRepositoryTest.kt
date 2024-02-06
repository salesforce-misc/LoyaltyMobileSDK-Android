package com.salesforce.loyalty.mobile.myntorewards

import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralsLocalRepository
import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralsLocalApiService
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.CurrentPromotionStage
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.QueryResult
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralCode
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralContactInfo
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnrollmentInfo
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEntity
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferredAccount
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferredParty
import com.salesforce.referral.api.ApiResponse
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import retrofit2.Response

class ReferralsLocalRepositoryTest {

    private val apiService: ReferralsLocalApiService = mockk()
    private val instanceUrl: String = ""

    private lateinit var repository: ReferralsLocalRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = ReferralsLocalRepository(apiService, instanceUrl)
    }

    @Test
    fun `Given API success with valid data, when fetching referral list, then verify success data`(){
        runBlocking {
            val referralEntity = ReferralEntity(
                "2024-01-22",
                CurrentPromotionStage("Friend Signs Up"),
                ReferredParty(ReferredAccount(personEmail = "testemail@test.com"))
            )
            coEvery { apiService.fetchReferralsInfo(any(), any()) } returns (
                    Response.success(QueryResult(1, true, listOf(referralEntity), null))
            )

            val result = repository.fetchReferralsInfo("1234", "PROMO123", 90)
            assert(result is ApiResponse.Success)
            val queryResult = result as ApiResponse.Success<QueryResult<ReferralEntity>>
            val entityQueryResult = queryResult.data
            TestCase.assertEquals(1, entityQueryResult.records?.size)
            TestCase.assertEquals(1, entityQueryResult.totalSize)
            TestCase.assertEquals(true, entityQueryResult.isDone)
            TestCase.assertNull(entityQueryResult.nextRecordsUrl)
            val referralEntityResponse = entityQueryResult.records?.firstOrNull()
            TestCase.assertEquals("Friend Signs Up", referralEntityResponse?.promotionStage?.type)
            TestCase.assertEquals("2024-01-22", referralEntityResponse?.referralDate)
            TestCase.assertEquals("testemail@test.com", referralEntityResponse?.referredParty?.account?.personEmail)
        }
    }

    @Test
    fun `Given API failure with error info, when fetching referral list, then verify api failure with error data`(){
        runBlocking {
            // Given
            coEvery { apiService.fetchReferralsInfo(any(), any()) } returns (errorResponse() as Response<QueryResult<ReferralEntity>>)

            // When
            val result = repository.fetchReferralsInfo("1234", "PROMO123", 90)

            // Then
            assert(result is ApiResponse.Error)
            val queryResult = result as ApiResponse.Error
            TestCase.assertEquals(
                "An unexpected error occurred. Please include this ErrorId if you contact support: 1933027655-144670 (-464075996)",
                queryResult.errorMessage
            )
        }
    }

    @Test
    fun `Given API success with valid data, when fetching Member Referral Code, then verify success data returned`(){
        runBlocking {
            // Given
            val referralCode = ReferralCode("12345678", "WGPIC1K")
            coEvery { apiService.fetchMemberReferralId(any(), any()) } returns (
                    Response.success(QueryResult(1, true, listOf(referralCode), null))
            )

            // When
            val result = repository.fetchMemberReferralCode("1234", )

            // Then
            assert(result is ApiResponse.Success)
            val queryResult = result as ApiResponse.Success<QueryResult<ReferralCode>>
            TestCase.assertEquals(1, queryResult.data.records?.size)
            val referralCodeResponse = queryResult.data.records?.firstOrNull()
            TestCase.assertEquals("WGPIC1K", referralCodeResponse?.referralCode)
            TestCase.assertEquals("12345678", referralCodeResponse?.membershipNumber)
        }
    }

    @Test
    fun `Given API failure with error info, when fetching referral code, then verify api failure with error data`(){
        runBlocking {
            // Given
            coEvery { apiService.fetchMemberReferralId(any(), any()) } returns (errorResponse() as Response<QueryResult<ReferralCode>>)

            // When
            val result = repository.fetchMemberReferralCode("1234", "REFERRAL PROGRAM")

            // Then
            assert(result is ApiResponse.Error)
            val queryResult = result as ApiResponse.Error
            TestCase.assertEquals(
                "An unexpected error occurred. Please include this ErrorId if you contact support: 1933027655-144670 (-464075996)",
                queryResult.errorMessage
            )
        }
    }

    @Test
    fun `Given API success with valid data, when fetching Member Referral enrolment status, then verify success data returned`(){
        runBlocking {
            // Given
            val referralEnrollmentInfo = ReferralEnrollmentInfo("123456789", ReferralContactInfo("ABCN123IAM"))
            coEvery { apiService.checkIfMemberEnrolled(any(), any()) } returns (
                    Response.success(QueryResult(1, true, listOf(referralEnrollmentInfo), null))
            )

            // When
            val result = repository.checkIfMemberEnrolled("1234", "PROMO123")

            // Then
            assert(result is ApiResponse.Success)
            val queryResult = result as ApiResponse.Success<QueryResult<ReferralEnrollmentInfo>>
            TestCase.assertEquals(1, queryResult.data.records?.size)
            val enrolmentResponseObject = queryResult.data.records?.firstOrNull()
            TestCase.assertEquals("123456789", enrolmentResponseObject?.loyaltyProgramMemberId)
            TestCase.assertEquals("ABCN123IAM", enrolmentResponseObject?.loyaltyProgramMember?.contactId)
        }
    }

    @Test
    fun `Given API failure with error info, when fetching referral enrolment status, then verify api failure with error data`(){
        runBlocking {
            // Given
            coEvery { apiService.checkIfMemberEnrolled(any(), any()) } returns (errorResponse() as Response<QueryResult<ReferralEnrollmentInfo>>)

            // When
            val result = repository.checkIfMemberEnrolled("TEMPRP9", "123456789")

            // Then
            assert(result is ApiResponse.Error)
            val queryResult = result as ApiResponse.Error
            TestCase.assertEquals(
                "An unexpected error occurred. Please include this ErrorId if you contact support: 1933027655-144670 (-464075996)",
                queryResult.errorMessage
            )
        }
    }

    private fun errorResponse(): Response<QueryResult<*>> {
        val errorResponse = MockResponseFileReader("referrals_error.json").content
        val responseBody = errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
        return Response.error(400, responseBody)
    }

}