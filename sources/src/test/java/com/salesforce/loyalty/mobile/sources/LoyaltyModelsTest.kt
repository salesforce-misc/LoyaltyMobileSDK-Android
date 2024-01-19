package com.salesforce.loyalty.mobile.sources

import com.salesforce.loyalty.mobile.sources.forceUtils.DateUtils
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyConfig
import com.salesforce.loyalty.mobile.sources.loyaltyExtensions.LoyaltyUtils
import com.salesforce.loyalty.mobile.sources.loyaltyModels.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class LoyaltyModelsTest {

    @Test
    fun testVoucherResponse() {
        val voucherResponse =
            VoucherResponse(
                id = "000001",
                voucherDefinition = "Birthday Discount Voucher",
                voucherCode = "84KFFFS",
                voucherImageUrl = "https://picsum.photos/800",
                voucherNumber = "",
                description = "",
                type = "DiscountPercentage",
                discountPercent = 20,
                expirationDate = "05 Jan 2023",
                effectiveDate = "",
                useDate = "",
                attributesUrl = "",
                status = "Issued",
                partnerAccount = "",
                faceValue = null,
                redeemedValue = "0.0",
                remainingValue = "0.0",
                currencyIsoCode = "",
                isVoucherDefinitionActive = false,
                isVoucherPartiallyRedeemable = false,
                product = "",
                productId = "",
                productCategoryId = "",
                productCategory = "",
                promotionName = "",
                promotionId = ""
            )
        assertEquals(voucherResponse.expirationDate, "05 Jan 2023")

        val voucherResult =
            VoucherResult(voucherCount = 1, voucherResponse = listOf(voucherResponse))
        assertEquals(voucherResult.voucherCount, 1)
    }

    @Test
    fun testPromotionRequest() {
        var processParameterMap: MutableMap<String, Any?> = mutableMapOf()
        processParameterMap[LoyaltyConfig.KEY_MEMBERSHIP_NUMBER] = "Member1"
        val promotionsRequest = PromotionsRequest(listOf(processParameterMap))
        assertEquals(promotionsRequest.processParameters.size, 1)
    }

    @Test
    fun testPromotionsResponse() {
        val promotionsResult = Results(
            loyaltyPromotionType = LoyaltyPromotionType.STANDARD.promotionType,
            fulfillmentAction = "CREDIT_POINTS",
            promotionEnrollmentRqr = false,
            memberEligibilityCategory = MemberEligibilityCriteria.ELIGIBLE.criteria,
            promotionImageUrl = "https://unsplash.com/photos/sTa-fO_VM4k/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjc4NzI2MDcx&force=true&w=640",
            totalPromotionRewardPointsVal = 250,
            promotionName = "Mountain Biking Bonanza",
            promotionId = "0c8RO0000000QbRYAU",
            startDate = "2023-01-01",
            endDate = "2023-12-31",
            loyaltyProgramCurrency = "0lcRO00000000BVYA",
            description = "Promote sustainable travel alternatives and earn rewards by joining us in the Mountain Biking Bonanza event",
            promotionEnrollmentEndDate = "end",
            promEnrollmentStartDate = "start",
            maximumPromotionRewardValue = null
        )
        assertEquals(promotionsResult.loyaltyPromotionType, "STANDARD")
        val promotionsResponse = PromotionsResponse(
            message = null,
            outputParameters = OutputParameters1(
                outputParameters = OutputParameters2(
                    results = listOf(
                        promotionsResult
                    )
                )
            ),
            simulationDetails = null,
            status = true
        )
        assertEquals(promotionsResponse.message, null)
    }

    @Test
    fun testTransactionModel() {
        val transactionJournals = TransactionsJournals(
            activityDate = "10 November 2022",
            additionalTransactionJournalAttributes = listOf(
                AdditionalAttributes(
                    fieldName = "fieldName",
                    value = "value"
                )
            ),
            journalSubTypeName = "subtype",
            journalTypeName = "Accural",
            pointsChange = listOf(
                PointsChange(
                    changeInPoints = 100.0,
                    loyaltyMemberCurrency = "Reward Points"
                )
            ),
            transactionJournalId = "0001",
            transactionJournalNumber = "",
            transactionAmount = ""
        )
        assertEquals(transactionJournals.activityDate, "10 November 2022")

        val transactionsResponse = TransactionsResponse(
            message = "success", status = true,
            transactionJournalCount = 1, externalTransactionNumber = "001",
            transactionJournals = listOf(transactionJournals)
        )
        assertEquals(transactionsResponse.message, "success")
    }

    @Test
    fun testBenefitsResponse() {
        val benefit = MemberBenefit(
            benefitId = "0ji4x0000008REdAAM",
            benefitName = "Extended Returns (7 Days)",
            benefitTypeId = "0jh4x0000008REQAA2",
            benefitTypeName = "Extended Returns",
            createdRecordId = "001",
            createdRecordName = "Return",
            isActive = true
        )
        assertEquals(benefit.benefitId, "0ji4x0000008REdAAM")

        val benefitsResponse = MemberBenefitsResponse(listOf(benefit))
        assertEquals(benefitsResponse.memberBenefits.size, 1)
    }

    @Test
    fun testMemberProfileResponse() {
        val profileResponse = MemberProfileResponse(
            additionalLoyaltyProgramMemberFields = null,
            programName = null,
            associatedContact = AssociatedContact(
                contactId = "0034x00001EhiXSAAZ",
                email = "ab@gmail.com",
                firstName = "Aman",
                lastName = "Bindal"
            ),
            associatedAccount = AssociatedAccount(accountId = "1234567", name = "Aman Bindal"),
            canReceivePartnerPromotions = true,
            canReceivePromotions = true,
            enrollmentChannel = EnrollmentChannel.EMAIL.channel,
            enrollmentDate = "2022-01-01",
            groupCreatedByMember = null,
            groupName = null,
            lastActivityDate = null,
            loyaltyProgramMemberId = "0lM4x000000LECAEA4",
            loyaltyProgramName = "NTO Insider",
            memberCurrencies = listOf(
                MemberCurrency(
                    additionalLoyaltyMemberCurrencyFields = null,
                    escrowPointsBalance = 0.0,
                    expirablePoints = 0.0,
                    lastAccrualProcessedDate = null,
                    lastEscrowProcessedDate = null,
                    lastExpirationProcessRunDate = null,
                    lastPointsAggregationDate = null,
                    lastPointsResetDate = null,
                    loyaltyMemberCurrencyName = "Reward Points",
                    loyaltyProgramCurrencyId = "0lc4x000000L1bmAAC",
                    loyaltyProgramCurrencyName = null,
                    memberCurrencyId = "0lz4x000000LC3nAAG",
                    nextQualifyingPointsResetDate = null,
                    pointsBalance = 17850.0,
                    qualifyingPointsBalanceBeforeReset = 0.0,
                    totalEscrowPointsAccrued = 0.0,
                    totalEscrowRolloverPoints = 0.0,
                    totalPointsAccrued = 0.0,
                    totalPointsExpired = 0.0,
                    totalPointsRedeemed = 0.0
                )
            ),
            memberStatus = MemberStatus.ACTIVE.status,
            memberTiers = listOf(
                MemberTier(
                    additionalLoyaltyMemberTierFields = null,
                    areTierBenefitsAssigned = false,
                    loyaltyMemberTierId = "0ly4x000000btVzAAI",
                    loyaltyMemberTierName = "Silver",
                    tierChangeReason = null,
                    tierChangeReasonType = null,
                    tierEffectiveDate = "2022-01-01",
                    tierExpirationDate = "2023-08-31",
                    tierGroupId = "0lt4x000000L1bXAAS",
                    tierGroupName = null,
                    tierId = "0lg4x000000L1bmAAC",
                    tierSequenceNumber = 10
                )
            ),
            memberType = "Individual",
            membershipEndDate = "2023-01-01",
            membershipLastRenewalDate = null,
            membershipNumber = "Member1",
            referredBy = null,
            relatedCorporateMembershipNumber = null,
            transactionJournalStatementFrequency = TransactionalJournalStatementFrequency.MONTHLY.frequency,
            transactionJournalStatementLastGeneratedDate = null,
            transactionJournalStatementMethod = TransactionalJournalStatementMethod.MAIL.method
        )
        assertEquals(profileResponse.membershipNumber, "Member1")
    }

    @Test
    fun testEnrollInPromotionResponse() {
        val enrollPromotionsResponse = EnrollPromotionsResponse(
            message = null, outputParameters = ERPOutputParameters1(
                ERPOutputParameters2(listOf(EPRResults(MemberId = "1lp4x00000002ffAAA")))
            ), simulationDetails = null, status = true
        )
        assertEquals(enrollPromotionsResponse.message, null)
        assertEquals(enrollPromotionsResponse.status, true)
    }

    @Test
    fun testUnEnrollInPromotionResponse() {
        val unEnrollPromotionsResponse = UnenrollPromotionResponse(
            message = null, outputParameters = UERPOutputParameters1(
                UERPOutputParameters2(listOf(UEPRResults(LoyaltyProgramMbrPromotionId = "1lpT100000000h4IAA")))
            ), simulationDetails = null, status = true
        )
        assertEquals(unEnrollPromotionsResponse.message, null)
        assertEquals(
            unEnrollPromotionsResponse.outputParameters?.outputParameters?.results?.get(0)?.LoyaltyProgramMbrPromotionId,
            "1lpT100000000h4IAA"
        )
        assertEquals(unEnrollPromotionsResponse.status, true)
    }

    @Test
    fun testEnrollmentResponse() {
        val enrollmentResponse = EnrollmentResponse(
            contactId = "0034x00001JdPg6",
            loyaltyProgramName = "NTO Insider",
            loyaltyProgramMemberId = "0lM4x000000LOrM",
            membershipNumber = "Member2",
            transactionJournals = listOf(
                TransactionJournals(
                    activityDate = "2022-01-01T08:00:00.000Z",
                    journalSubType = "Member Enrollment",
                    journalType = "Accrual",
                    loyaltyProgram = "NTO Insider",
                    loyaltyProgramMember = "Member2",
                    referredMember = null,
                    status = "Pending",
                    transactionJournalId = "0lV4x000000CcBF"
                )
            )
        )

        assertEquals(enrollmentResponse.membershipNumber, "Member2")
        assertEquals(enrollmentResponse.transactionJournals.get(0).loyaltyProgram, "NTO Insider")
    }

    @Test
    fun testEnrollmentRequest() {
        val enrollmentRequest = EnrollmentRequest(
            enrollmentDate = "2022-01-01T08:00:00.000Z",
            membershipNumber = "Member2",
            associatedContactDetails = AssociatedContactDetails(
                firstName = "Aman",
                lastName = "Bindal",
                email = "abc@gmail.com",
                allowDuplicateRecords = false,
                AdditionalContactFieldValues(mapOf("Phone" to "9876543211"))
            ),
            memberStatus = MemberStatus.ACTIVE.status,
            createTransactionJournals = true,
            transactionJournalStatementFrequency = TransactionalJournalStatementFrequency.QUARTERLY.frequency,
            transactionJournalStatementMethod = TransactionalJournalStatementMethod.EMAIL.method,
            enrollmentChannel = EnrollmentChannel.MOBILE.channel,
            canReceivePromotions = true,
            canReceivePartnerPromotions = true,
            membershipEndDate = null,
            AdditionalMemberFieldValues()
        )
        assertEquals(enrollmentRequest.associatedContactDetails.firstName, "Aman")
        assertEquals(
            enrollmentRequest.associatedContactDetails.additionalContactFieldValues.attributes?.get(
                "Phone"
            ), "9876543211"
        )
        assertEquals(enrollmentRequest.canReceivePromotions, true)
        assertEquals(enrollmentRequest.enrollmentDate, "2022-01-01T08:00:00.000Z")
        assertEquals(enrollmentRequest.membershipNumber, "Member2")
        assertEquals(enrollmentRequest.membershipEndDate, null)
        assertEquals(enrollmentRequest.canReceivePartnerPromotions, true)
        assertEquals(enrollmentRequest.enrollmentChannel, "Mobile")
        assertEquals(enrollmentRequest.transactionJournalStatementFrequency, "Quarterly")
        assertEquals(enrollmentRequest.transactionJournalStatementMethod, "Email")
        assertEquals(enrollmentRequest.createTransactionJournals, true)
        assertEquals(enrollmentRequest.memberStatus, "Active")
        assertEquals(enrollmentRequest.associatedContactDetails.lastName, "Bindal")
        assertEquals(enrollmentRequest.associatedContactDetails.email, "abc@gmail.com")
        assertEquals(enrollmentRequest.associatedContactDetails.allowDuplicateRecords, false)
        assertEquals(
            enrollmentRequest.additionalMemberFieldValues.attributes,
            mapOf<String, Any?>()
        )
    }
}