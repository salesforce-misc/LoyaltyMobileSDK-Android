package com.salesforce.loyalty.mobile.sources

import com.salesforce.loyalty.mobile.sources.forceUtils.DateUtils
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyConfig
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.NetworkClient
import com.salesforce.loyalty.mobile.sources.loyaltyExtensions.LoyaltyUtils
import com.salesforce.loyalty.mobile.sources.loyaltyModels.*
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.net.HttpURLConnection

class NetworkClientTest {

    private lateinit var loyaltyAPIManager: LoyaltyAPIManager

    private lateinit var mockWebServer: MockWebServer

    private lateinit var loyaltyClient: NetworkClient

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mockWebServer = MockWebServer()
        mockWebServer.start()
        loyaltyClient = MockNetworkClient(MockAuthenticator, "https://instanceUrl", mockWebServer)
        loyaltyAPIManager =
            LoyaltyAPIManager(MockAuthenticator, "https://instanceUrl", loyaltyClient)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `read sample success json file`() {
        val reader = MockResponseFileReader("Profile.json")
        assertNotNull(reader.content)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testMemberProfileAPI() {
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
            assertEquals(actualResponse.isSuccess, true)
            actualResponse.onSuccess {
                assertEquals(it.associatedContact?.email, "ab@gmail.com")
                assertEquals(it.associatedContact?.contactId, "0034x00001EhiXSAAZ")
                assertEquals(it.associatedContact?.firstName, "Aman")
                assertEquals(it.associatedContact?.lastName, "Bindal")
                assertEquals(it.canReceivePartnerPromotions, true)
                assertEquals(it.canReceivePromotions, true)
                assertEquals(it.enrollmentChannel, "Email")
                assertEquals(it.enrollmentDate, "2022-01-01")
                assertEquals(it.loyaltyProgramMemberId, "0lM4x000000LECAEA4")
                assertEquals(it.loyaltyProgramName, "NTO Insider")

                assertEquals(
                    it.memberCurrencies?.get(0)?.loyaltyMemberCurrencyName,
                    "Reward Points"
                )
                assertEquals(
                    it.memberCurrencies?.get(0)?.loyaltyProgramCurrencyId,
                    "0lc4x000000L1bmAAC"
                )
                assertEquals(it.memberCurrencies?.get(0)?.memberCurrencyId, "0lz4x000000LC3nAAG")
                assertEquals(it.memberCurrencies?.get(0)?.pointsBalance, 17850.0)
                assertEquals(it.memberCurrencies?.get(0)?.totalEscrowPointsAccrued, 0.0)
                assertEquals(it.memberCurrencies?.get(0)?.totalPointsAccrued, 0.0)
                assertEquals(it.memberCurrencies?.get(0)?.totalEscrowRolloverPoints, 0.0)
                assertEquals(it.memberCurrencies?.get(0)?.totalPointsExpired, 0.0)
                assertEquals(it.memberCurrencies?.get(0)?.totalPointsRedeemed, 0.0)

                assertEquals(it.memberTiers?.get(0)?.loyaltyMemberTierId, "0ly4x000000btVzAAI")
                assertEquals(it.memberTiers?.get(0)?.loyaltyMemberTierName, "Silver")
                assertEquals(it.memberTiers?.get(0)?.tierGroupId, "0lt4x000000L1bXAAS")
                assertEquals(it.memberTiers?.get(0)?.tierId, "0lg4x000000L1bmAAC")
                assertEquals(it.memberTiers?.get(0)?.tierSequenceNumber, 10)
                assertEquals(it.memberTiers?.get(0)?.tierEffectiveDate, "2022-01-01")
                assertEquals(it.memberTiers?.get(0)?.tierExpirationDate, "2023-08-31")

                assertEquals(it.memberStatus, "Active")
                assertEquals(it.memberType, "Individual")
                assertEquals(it.membershipEndDate, "2023-01-01")
                assertEquals(it.membershipNumber, "Member1")
                assertEquals(it.transactionJournalStatementFrequency, "Monthly")
//                assertEquals(it.transactionJournalStatementLastGeneratedDate, "")
                assertEquals(it.transactionJournalStatementMethod, "Mail")
            }
        }
    }

    @Test
    fun tesPostEnrollmentAPI() {
        runBlocking {
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("Enrollment.json").content)
            mockWebServer.enqueue(response)

            val associatedContactDetails = AssociatedContactDetails(
                firstName = "testFirstName",
                lastName = "testSecondName",
                email = "test@mail.com",
                allowDuplicateRecords = false,
                AdditionalContactFieldValues(mapOf("phone" to "12335699"))
            )

            val body = EnrollmentRequest(
                enrollmentDate = DateUtils.getCurrentDateTime(DateUtils.DATE_FORMAT_YYYYMMDDTHHMMSS),
                membershipNumber = LoyaltyUtils.generateRandomString(),
                associatedContactDetails = associatedContactDetails,
                memberStatus = MemberStatus.ACTIVE.status,
                createTransactionJournals = true,
                transactionJournalStatementFrequency = TransactionalJournalStatementFrequency.MONTHLY.frequency,
                transactionJournalStatementMethod = TransactionalJournalStatementMethod.EMAIL.method,
                enrollmentChannel = EnrollmentChannel.EMAIL.channel,
                canReceivePromotions = true,
                canReceivePartnerPromotions = true,
                membershipEndDate = null,
                AdditionalMemberFieldValues()
            )
            val actualResponse = loyaltyClient.getNetworkClient().postEnrollment(
                mockWebServer.url("/").toString(),
               body,
            )
            mockWebServer.takeRequest()
            assertEquals(actualResponse.isSuccess, true)
            actualResponse.onSuccess { it ->
                assertEquals(it.contactId, "0034x00001JdPg6")
                assertEquals(it.loyaltyProgramMemberId, "0lM4x000000LOrM")
                assertEquals(it.loyaltyProgramName, "NTO Insider")
                assertEquals(it.membershipNumber, "Member2")
                assertEquals(it.transactionJournals[0].activityDate, "2022-01-01T08:00:00.000Z")
                assertEquals(it.transactionJournals[0].journalSubType, "Member Enrollment")
                assertEquals(it.transactionJournals[0].journalType, "Accrual")
                assertEquals(it.transactionJournals[0].loyaltyProgram, "NTO Insider")
                assertEquals(it.transactionJournals[0].loyaltyProgramMember, "Member2")
                assertEquals(it.transactionJournals[0].status, "Pending")
                assertEquals(it.transactionJournals[0].transactionJournalId, "0lV4x000000CcBF")
            }
        }
    }

    @Test
    fun testMemberBenefitsAPI() {
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
            assertEquals(actualResponse.isSuccess, true)
            actualResponse.onSuccess { it ->
                assertEquals(it.memberBenefits.size, 6)
                assertEquals(it.memberBenefits[0].benefitId, "0ji4x0000008REdAAM")
                assertEquals(it.memberBenefits[0].benefitName, "Extended Returns (7 Days)")
                assertEquals(it.memberBenefits[0].benefitTypeId, "0jh4x0000008REQAA2")
                assertEquals(it.memberBenefits[0].createdRecordId, null)
                assertEquals(it.memberBenefits[0].createdRecordName, null)
                assertEquals(it.memberBenefits[0].isActive, true)
                assertEquals(it.memberBenefits[0].createdRecordName, null)
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
                assertNull(it.message)
                assertEquals(
                    it.outputParameters?.outputParameters?.results?.get(0)?.MemberId,
                    "1lp4x00000002ffAAA"
                )
                assertEquals(it.status , true)
                assertEquals(it.simulationDetails, mapOf<String, Any?>())
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
                assertNull(it.message)
                assertEquals(
                    it.outputParameters?.outputParameters?.results?.get(0)?.LoyaltyProgramMbrPromotionId,
                    "1lpT100000000h4IAA"
                )
                assertEquals(it.status , true)
                assertEquals(it.simulationDetails, mapOf<String, Any?>())
            }
        }
    }

    @Test
    fun testGetPromotions() {
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
                assertEquals(it.outputParameters?.outputParameters?.results?.get(0)?.loyaltyPromotionType, "STANDARD")
                assertEquals(it.outputParameters?.outputParameters?.results?.get(0)?.maximumPromotionRewardValue, 0)
                assertEquals(it.outputParameters?.outputParameters?.results?.get(0)?.totalPromotionRewardPointsVal, 0)
                assertEquals(it.outputParameters?.outputParameters?.results?.get(0)?.loyaltyProgramCurrency, "0lc4x000000L1bmAAC")
                assertEquals(it.outputParameters?.outputParameters?.results?.get(0)?.memberEligibilityCategory, "Eligible")
                assertEquals(it.outputParameters?.outputParameters?.results?.get(0)?.promotionEnrollmentRqr, true)
                assertEquals(it.outputParameters?.outputParameters?.results?.get(0)?.fulfillmentAction, "CREDIT_POINTS")
                assertEquals(it.outputParameters?.outputParameters?.results?.get(0)?.promotionName, "Anniversary promotion")
                assertEquals(it.outputParameters?.outputParameters?.results?.get(0)?.promotionId, "0c84x000000CoNhAAK")
                assertEquals(it.outputParameters?.outputParameters?.results?.get(0)?.startDate, "2020-01-01")
                assertEquals(it.outputParameters?.outputParameters?.results?.get(0)?.endDate, "2022-12-31")
                assertEquals(it.outputParameters?.outputParameters?.results?.get(0)?.description, "Double point promotion on member purchases on their anniversary")

            }
        }
    }

    @Test
    fun testGetTransactions() {
        runBlocking {
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("Transactions.json").content)
            mockWebServer.enqueue(response)

            val actualResponse = loyaltyClient.getNetworkClient().getTransactions(
                mockWebServer.url("/").toString(),
                null, null, null, null, null
            )
            mockWebServer.takeRequest()
            assertEquals(actualResponse.isSuccess, true)
            actualResponse.onSuccess { it ->
                assertEquals(it.transactionJournalCount, 4)
                assertEquals(it.transactionJournals[0].journalTypeName, "Manual Points Adjustment")
                assertEquals(it.transactionJournals[0].transactionJournalId, "0lVRO00000002og2AA")
                assertEquals(it.transactionJournals[0].activityDate, "2023-04-08T04:59:40.000Z")
                assertEquals(it.transactionJournals[0].pointsChange.get(0).changeInPoints, 100.0)
                assertEquals(it.transactionJournals[0].pointsChange.get(0).loyaltyMemberCurrency, "Reward Points")
                assertEquals(it.transactionJournals[0].transactionJournalNumber, "00000035")

                assertEquals(it.transactionJournals[1].journalTypeName, "Manual Points Adjustment")
                assertEquals(it.transactionJournals[1].transactionJournalId, "0lVRO00000002ob2AA")
                assertEquals(it.transactionJournals[1].activityDate, "2023-04-08T04:58:07.000Z")

                assertEquals(it.transactionJournals[0].additionalTransactionJournalAttributes, listOf<AdditionalAttributes>())
            }
        }
    }

    @Test
    fun testGetVouchers() {
        runBlocking {
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("Vouchers.json").content)
            mockWebServer.enqueue(response)

            val actualResponse = loyaltyClient.getNetworkClient().getVouchers(
                mockWebServer.url("/").toString(),
                null, 1, null, null, null, null
            )
            mockWebServer.takeRequest()
            assertEquals(actualResponse.isSuccess, true)
            actualResponse.onSuccess { it ->
                assertEquals(it.voucherCount, 4)
                assertEquals(it.voucherResponse[0].id, "0kDRO00000000Hk2AI")
                assertEquals(it.voucherResponse[0].discountPercent, 50)
                assertEquals(it.voucherResponse[0].description, "20% discount on children group party at selected event centers.")
                assertEquals(it.voucherResponse[0].effectiveDate, "2023-01-01")
                assertEquals(it.voucherResponse[0].expirationDate, "2023-04-01")
                assertEquals(it.voucherResponse[0].isVoucherDefinitionActive, true)
                assertEquals(it.voucherResponse[0].isVoucherPartiallyRedeemable, false)
                assertEquals(it.voucherResponse[0].promotionId, "0c8RO0000000ILdYAM")
                assertEquals(it.voucherResponse[0].promotionName, "Birthday Promotion")
                assertEquals(it.voucherResponse[0].redeemedValue, "0.0")
                assertEquals(it.voucherResponse[0].remainingValue, "0.0")
                assertEquals(it.voucherResponse[0].status, "Issued")
                assertEquals(it.voucherResponse[0].type, "DiscountPercentage")
                assertEquals(it.voucherResponse[0].voucherCode, "VOU10101011")
                assertEquals(it.voucherResponse[0].voucherDefinition, "Birthday Discount Voucher")
                assertEquals(it.voucherResponse[0].voucherImageUrl, "https://thumbs.dreamstime.com/b/10-percent-discount-24471598.jpg")
                assertEquals(it.voucherResponse[0].voucherNumber, "00000001")

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