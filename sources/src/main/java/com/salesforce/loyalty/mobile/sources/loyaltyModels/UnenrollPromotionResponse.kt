/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

/**
 * UnenrollPromotionResponse data class holds response parameters of Unenroll Promotion API.
 */
data class UnenrollPromotionResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("outputParameters")
    val outputParameters: UERPOutputParameters1?,
    @SerializedName("simulationDetails")
    val simulationDetails: Map<String, Any?>? = mutableMapOf(),
    @SerializedName("status")
    val status: Boolean?
)

data class UERPOutputParameters1(
    @SerializedName("outputParameters")
    val outputParameters: UERPOutputParameters2?
)

data class UERPOutputParameters2(
    @SerializedName("results")
    val results: List<UEPRResults>
)

data class UEPRResults(
    @SerializedName("LoyaltyProgramMbrPromotionId")
    val LoyaltyProgramMbrPromotionId: String?
)