/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

/**
 * PromotionsRequest data class holds request parameters of Promotions API.
 */
data class PromotionsRequest(
    @SerializedName("processParameters")
    val processParameters: List<Map<String, Any?>?> = mutableListOf()
)