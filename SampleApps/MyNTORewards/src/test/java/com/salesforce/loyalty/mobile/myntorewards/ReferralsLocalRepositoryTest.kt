package com.salesforce.loyalty.mobile.myntorewards

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralsLocalRepository
import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralsLocalApiService
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.PromotionsResponse
import com.salesforce.referral.api.ApiResponse
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

class ReferralsLocalRepositoryTest {

    private val apiService: ReferralsLocalApiService = mockk()
    private val localFileManager: LocalFileManager = mockk()
    private val instanceUrl: String = ""
    private val context = mock(Context::class.java)
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


            val result = repository.fetchReferralsInfo("1234", 90)

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
            val result = repository.fetchReferralsInfo("1234",  90)

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
    fun `Given API failure with error info, when fetching referral enrolment status, then verify api failure with error data`() {
        runBlocking {
            // Given
            coEvery {
                apiService.checkIfMemberEnrolled(
                    any(),
                    any()
                )
            } returns (errorResponse() as Response<QueryResult<ReferralEnrollmentInfo>>)

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

    @Test
    fun `Given API success with valid data,  fetching Promotion status, then verify success data returned`() {
        runBlocking {
            // Given
            val referralPromotionStatusAndPromoCodeInfo = ReferralPromotionStatusAndPromoCode(
                name = "123456789",
                promotionCode = "TEMPRP9",
                promotionPageUrl = null,
                "Invite your friends and get a voucher when they shop for the first time.",
                "2030-12-31",
                "https://rb.gy/wa6jw7",
                isReferralPromotion = true
            )
            coEvery {
                apiService.checkIfGivenPromotionIsReferralAndEnrolled(
                    any(),
                    any()
                )
            } returns (
                    Response.success(
                        QueryResult(
                            1,
                            true,
                            listOf(referralPromotionStatusAndPromoCodeInfo),
                            null
                        )
                    )
                    )

            // When
            val result = repository.checkIfGivenPromotionIsReferralAndEnrolled("1234")

            // Then
            assert(result is ApiResponse.Success)
            val queryResult =
                result as ApiResponse.Success<QueryResult<ReferralPromotionStatusAndPromoCode>>
            TestCase.assertEquals(1, queryResult.data.records?.size)
            val promotionResponseObject = queryResult.data.records?.firstOrNull()
            TestCase.assertEquals("123456789", promotionResponseObject?.name)
            TestCase.assertEquals("TEMPRP9", promotionResponseObject?.promotionCode)
            TestCase.assertNull(promotionResponseObject?.promotionPageUrl)
            TestCase.assertEquals(true, promotionResponseObject?.isReferralPromotion)
        }
    }

    @Test
    fun `Given API success with valid data,  fetching Promotion status from cache, then verify success data returned`() {
        runBlocking {
            // Given
            repository.saveReferralStatusInCache(
                "TEMPRP9",
                isReferralPromotion = true,
                isEnrolled = true
            )

            repository.savePromoCodeAndUrlInCache(
                "PROMO123",
                promotionDetails = ReferralPromotionStatusAndPromoCode(
                    name = "123456789",
                    promotionCode = "TEMPRP9",
                    promotionPageUrl = "http://abc",
                    "Invite your friends and get a voucher when they shop for the first time.",
                    "2030-12-31",
                    "https://rb.gy/wa6jw7",
                    isReferralPromotion = true
                )
            )
            // When
            val referralStatus = repository.getReferralStatusFromCache("TEMPRP9")

            // Then
            TestCase.assertEquals(true, referralStatus.first)
            TestCase.assertEquals(true, referralStatus.second)

            // When
            val prmoCode = repository.getPromoCodeFromCache("PROMO123")

            // Then
            TestCase.assertEquals("TEMPRP9", prmoCode)
            // When
            val prmoUrl = repository.getPromoUrlFromCache("PROMO123")

            // Then
            TestCase.assertEquals("http://abc", prmoUrl)
        }
    }

    @Test
    fun `Given API success with valid data,  fetching default Promotion Details from cache, then verify success data returned`() {
        runBlocking {
            // Given
            val mockResponse = MockResponseFileReader("Promotions.json").content
            val mockPromotionsResponse =
                Gson().fromJson(mockResponse, PromotionsResponse::class.java)
            coEvery {
                localFileManager.getData(
                    any(),
                    any(),
                    any(),
                    PromotionsResponse::class.java
                )
            } returns (
                    mockPromotionsResponse
                    )

            val promotionResponse = repository.getDefaultPromotionDetailsFromCache(
                context = context,
                "12345678", "0c84x000000CoNhAAK"
            )

            // Then
            TestCase.assertNotNull(promotionResponse)
        }
    }

    @Test
    fun `Given Referral enable API status, fetching the status, then verify success data returned`() {
        runBlocking {
            // Given
            val responseType = object : TypeToken<QueryResult<ReferralEnablementStatus>>() {}.type
            val mockResponse = mockResponse("ReferralEnbleStatus.json", responseType) as QueryResult<ReferralEnablementStatus>
            coEvery {
                apiService.checkIfReferralIsEnabled(any(), any())
            } returns Response.success(mockResponse)

            val result = repository.checkIfReferralIsEnabled()

            // Then
            assert(result is ApiResponse.Success)
            val queryResult = result as ApiResponse.Success<QueryResult<ReferralEnablementStatus>>
            TestCase.assertEquals(1, queryResult.data.records?.size)
        }
    }

    @Test
    fun `Given Referral disable API status, fetching the status, then verify success data returned`() {
        runBlocking {
            // Given
            coEvery {
                apiService.checkIfReferralIsEnabled(any(), any())
            } returns Response.success(QueryResult(records = emptyList(), isDone = true, nextRecordsUrl = "", totalSize = 0))

            val result = repository.checkIfReferralIsEnabled()

            // Then
            assert(result is ApiResponse.Success)
            val queryResult = result as ApiResponse.Success<QueryResult<ReferralEnablementStatus>>
            TestCase.assertEquals(0, queryResult.data.records?.size)
        }
    }

    @Test
    fun `Clear cache, then verify empty data returned`() {
        runBlocking {
            // Given
            ReferralsLocalRepository.clearReferralsData()

            val promoCode = repository.getPromoCodeFromCache("PROMO123")

            // Then
            TestCase.assertNull(promoCode)
        }
    }

    private fun errorResponse(): Response<QueryResult<*>> {
        val errorResponse = MockResponseFileReader("referrals_error.json").content
        val responseBody = errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
        return Response.error(400, responseBody)
    }

}