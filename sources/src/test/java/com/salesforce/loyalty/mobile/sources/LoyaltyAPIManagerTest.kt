package com.salesforce.loyalty.mobile.sources

import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyConfig
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.NetworkClient
import com.salesforce.loyalty.mobile.sources.loyaltyModels.PromotionsRequest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.net.HttpURLConnection

class LoyaltyAPIManagerTest {

    private lateinit var loyaltyAPIManager: LoyaltyAPIManager

    private lateinit var mockWebServer: MockWebServer

    private lateinit var loyaltyClient: NetworkClient

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mockWebServer = MockWebServer()
        mockWebServer.start()
        loyaltyClient = MockNetworkClient(MockAuthenticator, "https://instanceUrl",mockWebServer)
        loyaltyAPIManager =
            LoyaltyAPIManager(MockAuthenticator, "https://instanceUrl", loyaltyClient)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `read sample success json file`(){
        val reader = MockResponseFileReader("Profile.json")
        assertNotNull(reader.content)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testMemberProfileAPI(){
        runBlocking {
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("Profile.json").content)
            mockWebServer.enqueue(response)

            val actualResponse = loyaltyClient.getNetworkClient().getMemberProfile(
                mockWebServer.url("/").toString(),
                memberId = "MRI706",
                membershipNumber = null,
                programCurrencyName = "MyNTORewards"
            )
            mockWebServer.takeRequest()
            assertEquals(actualResponse.isSuccess,true)
            actualResponse.onSuccess {
                assertEquals(it.associatedContact?.email, "ab@gmail.com")
            }
            /*actualResponse.onSuccess { Log.d("UnitTesting","onSuccess Response: ${it}") }
                .onFailure {Log.d("UnitTesting","onFailure Response: ${it.localizedMessage}")  }*/


        }
    }

    @Test
    fun testMemberBenefitsAPI(){
        runBlocking {
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("Benefits.json").content)
            mockWebServer.enqueue(response)

            val actualResponse = loyaltyClient.getNetworkClient().getMemberBenefits(
                mockWebServer.url("/").toString(),
                membershipNumber = "1234",
            )
            mockWebServer.takeRequest()
            assertEquals(actualResponse.isSuccess,true)
            actualResponse.onSuccess {it ->
                assertEquals(it.memberBenefits.size, 6)
                assertEquals(it.memberBenefits[0].benefitId, "0ji4x0000008REdAAM")
                assertEquals(it.memberBenefits[1].benefitId, "0ji4x0000008REjAAM")
                assertEquals(it.memberBenefits[2].benefitId, "0ji4x0000008REeAAM")
                assertEquals(it.memberBenefits[3].benefitId, "0ji4x0000008REZAA2")
                assertEquals(it.memberBenefits[4].benefitId, "0ji4x0000008REiAAM")
                assertEquals(it.memberBenefits[5].benefitId, "0ji4x0000008REfAAM")
            }
        }
    }

    @Test
    fun testEnrollInPromotion() {
        runBlocking {
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("EnrollmentPromotion.json").content)
            mockWebServer.enqueue(response)

            val requestBody: PromotionsRequest =
                PromotionsRequest(
                    listOf(
                        mapOf(
                            LoyaltyConfig.KEY_MEMBERSHIP_NUMBER to "1234",
                            LoyaltyConfig.KEY_PROMOTION_NAME to "health"
                        )
                    )
                )
            val actualResponse = loyaltyClient.getNetworkClient().enrollInPromotion(
                mockWebServer.url("/").toString(),
                requestBody = requestBody,
            )
            mockWebServer.takeRequest()
            assertEquals(actualResponse.isSuccess, true)
            actualResponse.onSuccess { it ->
                assertNotNull(it)
            }
        }
    }

    @Test
    fun testUnenrollPromotion() {
        runBlocking {
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("UnenrollmentPromotion.json").content)
            mockWebServer.enqueue(response)

            val requestBody: PromotionsRequest =
                PromotionsRequest(
                    listOf(
                        mapOf(
                            LoyaltyConfig.KEY_MEMBERSHIP_NUMBER to "1234",
                            LoyaltyConfig.KEY_PROMOTION_NAME to "health"
                        )
                    )
                )
            val actualResponse = loyaltyClient.getNetworkClient().unenrollPromotion(
                mockWebServer.url("/").toString(),
                requestBody = requestBody,
            )
            mockWebServer.takeRequest()
            assertEquals(actualResponse.isSuccess, true)
            actualResponse.onSuccess { it ->
                assertNotNull(it)
            }
        }
    }

    @Test
    fun testGetPromotions(){
        runBlocking {
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("Promotions.json").content)
            mockWebServer.enqueue(response)

            var processParameterMap: MutableMap<String, Any?> = mutableMapOf()
            processParameterMap[LoyaltyConfig.KEY_MEMBERSHIP_NUMBER] = "1234"

            val requestBody: PromotionsRequest =
                PromotionsRequest(listOf(processParameterMap))

            val actualResponse = loyaltyClient.getNetworkClient().getEligiblePromotions(
                mockWebServer.url("/").toString(),
                requestBody = requestBody
            )
            mockWebServer.takeRequest()
            assertEquals(actualResponse.isSuccess, true)
            actualResponse.onSuccess { it ->
                assertEquals(it.status, true)
                assertEquals(it.outputParameters?.outputParameters?.results?.size, 8)
                assertEquals(it.message, null)
            }
        }
    }

    @Test
    fun testGetTransactions(){
        runBlocking {
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("Transactions.json").content)
            mockWebServer.enqueue(response)

            val actualResponse = loyaltyClient.getNetworkClient().getTransactions(
                mockWebServer.url("/").toString(),
                null,null,null,null,null
            )
            mockWebServer.takeRequest()
            assertEquals(actualResponse.isSuccess, true)
            actualResponse.onSuccess { it ->
                assertEquals(it.transactionJournalCount, 4)
                assertEquals(it.transactionJournals[0].journalTypeName, "Manual Points Adjustment")
                assertEquals(it.transactionJournals[0].transactionJournalId, "0lVRO00000002og2AA")
                assertEquals(it.transactionJournals[0].activityDate, "2023-04-08T04:59:40.000Z")

                assertEquals(it.transactionJournals[1].journalTypeName, "Manual Points Adjustment")
                assertEquals(it.transactionJournals[1].transactionJournalId, "0lVRO00000002ob2AA")
                assertEquals(it.transactionJournals[1].activityDate, "2023-04-08T04:58:07.000Z")
            }
        }
    }

    @Test
    fun testGetVouchers(){
        runBlocking {
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("Vouchers.json").content)
            mockWebServer.enqueue(response)

            val actualResponse = loyaltyClient.getNetworkClient().getVouchers(
                mockWebServer.url("/").toString(),
                null,1,null,null,null, null
            )
            mockWebServer.takeRequest()
            assertEquals(actualResponse.isSuccess, true)
            actualResponse.onSuccess { it ->
                assertEquals(it.voucherCount, 4)
                assertEquals(it.voucherResponse[0].id, "0kDRO00000000Hk2AI")
                assertEquals(it.voucherResponse[0].discountPercent, 50)

                assertEquals(it.voucherResponse[1].id, "0kDRO00000000Hp2AI")
                assertEquals(it.voucherResponse[1].discountPercent, 40)

                assertEquals(it.voucherResponse[2].id, "0kDRO00000000Hp2BI")
                assertEquals(it.voucherResponse[2].discountPercent, 40)
            }
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}