/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

/**
 * PromotionsResponse data class holds response parameters of Promotions API.
 */
data class EnrollPromotionsResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("outputParameters")
    val outputParameters: ERPOutputParameters1?,
    @SerializedName("simulationDetails")
    val simulationDetails: Map<String, Any?>? = mutableMapOf(),
    @SerializedName("status")
    val status: Boolean?,
)

data class ERPOutputParameters1(
    @SerializedName("outputParameters")
    val outputParameters: ERPOutputParameters2?
)

data class ERPOutputParameters2(
    @SerializedName("results")
    val results: List<EPRResults>
)

data class EPRResults(
    @SerializedName("MemberId")
    val MemberId: String?
)