/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.loyalty.mobile.sources.loyaltyModels

import com.google.gson.annotations.SerializedName

data class VoucherResponse(
    @SerializedName("voucherId")
    val id: String?,
    @SerializedName("voucherDefinition")
    val voucherDefinition: String?,
    @SerializedName("voucherCode")
    val voucherCode: String?,
    @SerializedName("voucherNumber")
    val voucherNumber: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("discountPercent")
    val discountPercent: Int?,
    @SerializedName("expirationDate")
    val expirationDate: String?,
    @SerializedName("effectiveDate")
    val effectiveDate: String?,
    @SerializedName("useDate")
    val useDate: String?,
    @SerializedName("voucherImageUrl")
    val voucherImageUrl: String?,
    @SerializedName("attributesUrl")
    val attributesUrl: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("partnerAccount")
    val partnerAccount: String?,
    @SerializedName("faceValue")
    val faceValue: String?,
    @SerializedName("redeemedValue")
    val redeemedValue: String?,
    @SerializedName("remainingValue")
    val remainingValue: String?,
    @SerializedName("currencyIsoCode")
    val currencyIsoCode: String?,
    @SerializedName("isVoucherDefinitionActive")
    val isVoucherDefinitionActive: Boolean,
    @SerializedName("isVoucherPartiallyRedeemable")
    val isVoucherPartiallyRedeemable: Boolean,
    @SerializedName("product")
    val product: String?,
    @SerializedName("productId")
    val productId: String?,
    @SerializedName("productCategoryId")
    val productCategoryId: String?,
    @SerializedName("productCategory")
    val productCategory: String?,
    @SerializedName("promotionName")
    val promotionName: String?,
    @SerializedName("promotionId")
    val promotionId: String?
)

data class VoucherResult(
    @SerializedName("voucherCount")
    val voucherCount: Int?,

    @SerializedName("vouchers")
    val voucherResponse: List<VoucherResponse> = mutableListOf()

)
