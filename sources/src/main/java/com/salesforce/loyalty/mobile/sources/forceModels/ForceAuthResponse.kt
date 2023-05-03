/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.loyalty.mobile.sources.forceModels

import com.google.gson.annotations.SerializedName

/**
 * ForceAuthResponse data class holds response of access token generation API call.
 */
data class ForceAuthResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("instance_url")
    val instanceUrl: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("issued_at")
    val issuedAt: String,
    @SerializedName("signature")
    val signature: String
)