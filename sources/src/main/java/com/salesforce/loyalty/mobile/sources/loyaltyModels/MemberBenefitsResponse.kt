/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

/**
 * MemberBenefitsResponse data class holds response parameters of Member Benefits API.
 */
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