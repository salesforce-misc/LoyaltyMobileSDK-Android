package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

data class MemberBenefitsResponse(
    @SerializedName("memberBenefits")
    val memberBenefits: List<MemberBenefit>
)

data class MemberBenefit(
    @SerializedName("benefitId")
    val benefitId: String?,
    @SerializedName("benefitName")
    val benefitName: String?,
    @SerializedName("firstName")
    val benefitTypeId: String?,
    @SerializedName("benefitTypeName")
    val benefitTypeName: String?,
    @SerializedName("createdRecordId")
    val createdRecordId: String?,
    @SerializedName("createdRecordName")
    val createdRecordName: String?,
    @SerializedName("isActive")
    val isActive: Boolean?
)