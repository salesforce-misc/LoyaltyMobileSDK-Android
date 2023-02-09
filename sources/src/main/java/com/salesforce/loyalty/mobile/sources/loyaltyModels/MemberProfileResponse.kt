package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

/**
 * MemberProfileResponse data class holds response parameters of Member Profile API.
 */
data class MemberProfileResponse(
    @SerializedName("additionalLoyaltyProgramMemberFields")
    val additionalLoyaltyProgramMemberFields: Map<String, Any?>? = mutableMapOf(),
    @SerializedName("ProgramName")
    val programName: String?,
    @SerializedName("associatedContact")
    val associatedContact: AssociatedContact?,
    @SerializedName("associatedAccount")
    val associatedAccount: AssociatedAccount?,
    @SerializedName("canReceivePromotions")
    val canReceivePromotions: Boolean,
    @SerializedName("canReceivePartnerPromotions")
    val canReceivePartnerPromotions: Boolean,
    @SerializedName("enrollmentChannel")
    val enrollmentChannel: String?,
    @SerializedName("enrollmentDate")
    val enrollmentDate: String?,
    @SerializedName("groupCreatedByMember")
    val groupCreatedByMember: String?,
    @SerializedName("groupName")
    val groupName: String?,
    @SerializedName("lastActivityDate")
    val lastActivityDate: String?,
    @SerializedName("loyaltyProgramMemberId")
    val loyaltyProgramMemberId: String?,
    @SerializedName("loyaltyProgramName")
    val loyaltyProgramName: String?,
    @SerializedName("memberStatus")
    val memberStatus: String?,
    @SerializedName("memberType")
    val memberType: String?,
    @SerializedName("membershipEndDate")
    val membershipEndDate: String?,
    @SerializedName("membershipLastRenewalDate")
    val membershipLastRenewalDate: String?,
    @SerializedName("membershipNumber")
    val membershipNumber: String?,
    @SerializedName("referredBy")
    val referredBy: String?,
    @SerializedName("relatedCorporateMembershipNumber")
    val relatedCorporateMembershipNumber: String?,
    @SerializedName("transactionJournalStatementFrequency")
    val transactionJournalStatementFrequency: String?,
    @SerializedName("transactionJournalStatementLastGeneratedDate")
    val transactionJournalStatementLastGeneratedDate: String?,
    @SerializedName("transactionJournalStatementMethod")
    val transactionJournalStatementMethod: String?,
    @SerializedName("memberCurrencies")
    val memberCurrencies: List<MemberCurrency>? = mutableListOf(),
    @SerializedName("memberTiers")
    val memberTiers: List<MemberTier>? = mutableListOf()
)

data class AssociatedContact(
    @SerializedName("contactId")
    val contactId: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String
)

data class AssociatedAccount(
    @SerializedName("accountId")
    val accountId: String,
    @SerializedName("name")
    val name: String,
)

data class MemberCurrency(
    @SerializedName("escrowPointsBalance")
    val escrowPointsBalance: Double?,
    @SerializedName("expirablePoints")
    val expirablePoints: Double?,
    @SerializedName("lastAccrualProcessedDate")
    val lastAccrualProcessedDate: String?,
    @SerializedName("lastEscrowProcessedDate")
    val lastEscrowProcessedDate: String?,
    @SerializedName("lastExpirationProcessRunDate")
    val lastExpirationProcessRunDate: String?,
    @SerializedName("lastPointsAggregationDate")
    val lastPointsAggregationDate: String?,
    @SerializedName("lastPointsResetDate")
    val lastPointsResetDate: String?,
    @SerializedName("loyaltyMemberCurrencyName")
    val loyaltyMemberCurrencyName: String?,
    @SerializedName("loyaltyProgramCurrencyId")
    val loyaltyProgramCurrencyId: String?,
    @SerializedName("loyaltyProgramCurrencyName")
    val loyaltyProgramCurrencyName: String?,
    @SerializedName("memberCurrencyId")
    val memberCurrencyId: String?,
    @SerializedName("nextQualifyingPointsResetDate")
    val nextQualifyingPointsResetDate: String?,
    @SerializedName("pointsBalance")
    val pointsBalance: Double?,
    @SerializedName("qualifyingPointsBalanceBeforeReset")
    val qualifyingPointsBalanceBeforeReset: Double?,
    @SerializedName("totalEscrowPointsAccrued")
    val totalEscrowPointsAccrued: Double?,
    @SerializedName("totalEscrowRolloverPoints")
    val totalEscrowRolloverPoints: Double?,
    @SerializedName("totalPointsAccrued")
    val totalPointsAccrued: Double?,
    @SerializedName("totalPointsExpired")
    val totalPointsExpired: Double?,
    @SerializedName("totalPointsRedeemed")
    val totalPointsRedeemed: Double?,
    @SerializedName("additionalLoyaltyMemberCurrencyFields")
    val additionalLoyaltyMemberCurrencyFields: Map<String, Any?>? = mutableMapOf()
)

data class MemberTier(
    @SerializedName("additionalLoyaltyMemberTierFields")
    val additionalLoyaltyMemberTierFields: Map<String, Any?>? = mutableMapOf(),
    @SerializedName("areTierBenefitsAssigned")
    val areTierBenefitsAssigned: Boolean,
    @SerializedName("loyaltyMemberTierId")
    val loyaltyMemberTierId: String?,
    @SerializedName("loyaltyMemberTierName")
    val loyaltyMemberTierName: String?,
    @SerializedName("tierChangeReason")
    val tierChangeReason: String?,
    @SerializedName("tierChangeReasonType")
    val tierChangeReasonType: String?,
    @SerializedName("tierEffectiveDate")
    val tierEffectiveDate: String?,
    @SerializedName("tierExpirationDate")
    val tierExpirationDate: String?,
    @SerializedName("tierGroupId")
    val tierGroupId: String?,
    @SerializedName("tierGroupName")
    val tierGroupName: String?,
    @SerializedName("tierId")
    val tierId: String?,
    @SerializedName("tierSequenceNumber")
    val tierSequenceNumber: Int?,
)