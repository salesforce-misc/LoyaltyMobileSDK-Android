package com.salesforce.loyalty.mobile.sources

import com.google.gson.Gson
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyApiInterface
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyClient
import com.salesforce.loyalty.mobile.sources.loyaltyModels.*
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LoyaltyAPIManagerTest {

    private lateinit var loyaltyAPIManager: LoyaltyAPIManager

    private lateinit var loyaltyClient: LoyaltyClient

    private lateinit var loyaltyApiInterface: LoyaltyApiInterface

    private lateinit var authenticator: ForceAuthenticator

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        loyaltyClient = mockk<LoyaltyClient>(relaxed = true)

        authenticator = mockk<ForceAuthenticator>(relaxed = true)

        loyaltyApiInterface = mockk<LoyaltyApiInterface>(relaxed = true)

        loyaltyAPIManager =
            LoyaltyAPIManager(authenticator, "https://instanceUrl", loyaltyClient)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `post Enrollment success response`() {
        coEvery { loyaltyClient.getNetworkClient() } returns loyaltyApiInterface
        val mockResponse = MockResponseFileReader("Enrollment.json").content
        val mockEnrollmentResponse =
            Gson().fromJson(mockResponse, EnrollmentResponse::class.java)
        coEvery {
            loyaltyClient.getNetworkClient().postEnrollment(any(), any())
        } returns Result.success(mockEnrollmentResponse)

        coEvery {
            val result = loyaltyAPIManager.postEnrollment(
                firstName = "testFirstName",
                lastName = "testSecondName",
                email = "test@mail.com",
                additionalContactAttributes = mapOf("Phone" to "12335699"),
                emailNotification = false,
                memberStatus = MemberStatus.ACTIVE,
                true,
                TransactionalJournalStatementFrequency.MONTHLY,
                TransactionalJournalStatementMethod.EMAIL,
                EnrollmentChannel.EMAIL,
                true,
                true
            )
            assertEquals(result.isSuccess, true)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get MemberProfile success response`() {
        runBlocking {
            coEvery { loyaltyClient.getNetworkClient() } returns loyaltyApiInterface
            val mockResponse = MockResponseFileReader("Profile.json").content
            val mockMemberProfileResponse =
                Gson().fromJson(mockResponse, MemberProfileResponse::class.java)
            coEvery {
                loyaltyClient.getNetworkClient().getMemberProfile(any(), any(), any(), any())
            } returns Result.success(mockMemberProfileResponse)

            val result = loyaltyAPIManager.getMemberProfile(
                memberId = "MRI706",
                memberShipNumber = null,
                programCurrencyName = "MyNTORewards"
            )
            assertEquals(result.isSuccess, true)
            var responseMemberId: String? = null
            result.onSuccess {
                responseMemberId = it.loyaltyProgramMemberId
            }
            assertEquals(responseMemberId, "0lM4x000000LECAEA4")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get MemberProfile failure response`() {
        runBlocking {
            coEvery { loyaltyClient.getNetworkClient() } returns loyaltyApiInterface
            coEvery {
                loyaltyClient.getNetworkClient().getMemberProfile(any(), any(), any(), any())
            } returns Result.failure(Exception("HTTP 401 Unauthorized"))

            val result = loyaltyAPIManager.getMemberProfile(
                memberId = "MRI706",
                memberShipNumber = null,
                programCurrencyName = "MyNTORewards"
            )
            result.onFailure {
                assertEquals(result.isFailure, true)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get MemberBenefits success response`() {
        runBlocking {
            coEvery { loyaltyClient.getNetworkClient() } returns loyaltyApiInterface
            val mockResponse = MockResponseFileReader("Benefits.json").content
            val mockMemberBenefitsResponse =
                Gson().fromJson(mockResponse, MemberBenefitsResponse::class.java)
            coEvery {
                loyaltyClient.getNetworkClient().getMemberBenefits(any(), any())
            } returns Result.success(mockMemberBenefitsResponse)

            val result = loyaltyAPIManager.getMemberBenefits(
                memberId = "MRI706",
                membershipNumber = "1234"
            )
            assertEquals(result.isSuccess, true)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get MemberBenefits failure response`() {
        runBlocking {
            coEvery { loyaltyClient.getNetworkClient() } returns loyaltyApiInterface
            coEvery {
                loyaltyClient.getNetworkClient().getMemberBenefits(any(), any())
            } returns Result.failure(Exception("HTTP 401 Unauthorized"))

            val result = loyaltyAPIManager.getMemberBenefits(
                memberId = "MRI706",
                membershipNumber = "1234"
            )
            result.onFailure {
                assertEquals(result.isFailure, true)
            }
            assertEquals(result.isFailure, true)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get Transactions success response`() {
        runBlocking {
            coEvery { loyaltyClient.getNetworkClient() } returns loyaltyApiInterface
            val mockResponse = MockResponseFileReader("Transactions.json").content
            val mockTransactionsResponse =
                Gson().fromJson(mockResponse, TransactionsResponse::class.java)
            coEvery {
                loyaltyClient.getNetworkClient()
                    .getTransactions(any(), any(), any(), any(), any(), any())
            } returns Result.success(mockTransactionsResponse)

            val result = loyaltyAPIManager.getTransactions(
                "1234", null, null, null, null, null
            )
            result.onSuccess {
                assertEquals(result.isSuccess, true)
            }
            val resultWithPageNumber = loyaltyAPIManager.getTransactions(
                "1234", 1, null, null, null, null
            )
            assertEquals(resultWithPageNumber.isSuccess, true)
            val resultWithJTN = loyaltyAPIManager.getTransactions(
                "1234", 1, "JTN", null, null, null
            )
            assertEquals(resultWithJTN.isSuccess, true)
            val resultWithJSTN = loyaltyAPIManager.getTransactions(
                "1234", 1, null, "JSTN", null, null
            )
            assertEquals(resultWithJSTN.isSuccess, true)
            val resultWithSD = loyaltyAPIManager.getTransactions(
                "1234", 1, null, null, "Date1234", null
            )
            assertEquals(resultWithSD.isSuccess, true)
            val resultWithED = loyaltyAPIManager.getTransactions(
                "1234", 1, null, null, null, "ED1234"
            )
            assertEquals(resultWithED.isSuccess, true)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get Transactions failure response`() {
        runBlocking {
            coEvery { loyaltyClient.getNetworkClient() } returns loyaltyApiInterface
            coEvery {
                loyaltyClient.getNetworkClient()
                    .getTransactions(any(), any(), any(), any(), any(), any())
            } returns Result.failure(Exception("HTTP 500 Server error"))

            val result = loyaltyAPIManager.getTransactions(
                "1234", null, null, null, null, null
            )
            result.onFailure {
                assertEquals(result.isFailure, true)
            }
            val resultWithPageNumber = loyaltyAPIManager.getTransactions(
                "1234", 1, null, null, null, null
            )
            resultWithPageNumber.onFailure {
                assertEquals(resultWithPageNumber.isFailure, true)
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get Vouchers success response`() {
        runBlocking {
            coEvery { loyaltyClient.getNetworkClient() } returns loyaltyApiInterface
            val mockResponse = MockResponseFileReader("Vouchers.json").content
            val mockVouchersResponse =
                Gson().fromJson(mockResponse, VoucherResult::class.java)
            val mockClass = spyk(
                LoyaltyAPIManager(authenticator, "https://instanceUrl", loyaltyClient),
                recordPrivateCalls = true
            )
            every {
                mockClass["getStringOfArrayItems"](any<Array<String>>())
            } returns null
            coEvery {
                loyaltyClient.getNetworkClient()
                    .getVouchers(any(), any(), any(), any(), any(), any(), any())
            } returns Result.success(mockVouchersResponse)

            val result = loyaltyAPIManager.getVouchers(
                "1234", null, null, null, null, null, null
            )
            assertEquals(result.isSuccess, true)
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get promotions success response`() {
        runBlocking {
            coEvery { loyaltyClient.getNetworkClient() } returns loyaltyApiInterface
            val mockResponse = MockResponseFileReader("Promotions.json").content
            val mockPromotionsResponse =
                Gson().fromJson(mockResponse, PromotionsResponse::class.java)
            coEvery {
                loyaltyClient.getNetworkClient().getEligiblePromotions(any(), any())
            } returns Result.success(mockPromotionsResponse)

            val result = loyaltyAPIManager.getEligiblePromotions(
                "1234", null
            )
            assertEquals(result.isSuccess, true)

            val resultWithMemberId = loyaltyAPIManager.getEligiblePromotions(
                null, "Member1234"
            )
            assertEquals(resultWithMemberId.isSuccess, true)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `enroll in promotion success response`() {
        runBlocking {
            coEvery { loyaltyClient.getNetworkClient() } returns loyaltyApiInterface
            val mockResponse = MockResponseFileReader("EnrollmentPromotion.json").content
            val mockPromotionEnrollResponse =
                Gson().fromJson(mockResponse, EnrollPromotionsResponse::class.java)
            coEvery {
                loyaltyClient.getNetworkClient().enrollInPromotion(any(), any())
            } returns Result.success(mockPromotionEnrollResponse)

            val result = loyaltyAPIManager.enrollInPromotions(
                membershipNumber = "MRI706",
                promotionName = "health"
            )
            assertEquals(result.isSuccess, true)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `unenroll in promotion success response`() {
        runBlocking {
            coEvery { loyaltyClient.getNetworkClient() } returns loyaltyApiInterface
            val mockResponse = MockResponseFileReader("UnenrollmentPromotion.json").content
            val mockPromotionUnEnrollResponse =
                Gson().fromJson(mockResponse, UnenrollPromotionResponse::class.java)
            coEvery {
                loyaltyClient.getNetworkClient().unenrollPromotion(any(), any())
            } returns Result.success(mockPromotionUnEnrollResponse)

            val result = loyaltyAPIManager.unEnrollPromotion(
                membershipNumber = "MRI706",
                promotionName = "health"
            )
            assertEquals(result.isSuccess, true)
        }
    }
}