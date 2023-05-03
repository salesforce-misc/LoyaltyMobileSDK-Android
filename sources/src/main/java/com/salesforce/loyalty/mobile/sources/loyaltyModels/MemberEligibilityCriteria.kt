/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.loyalty.mobile.sources.loyaltyModels

enum class MemberEligibilityCriteria(val criteria: String) {
    ELIGIBLE("Eligible"),
    ELIGIBLE_BUT_NOT_ENROLLED("EligibleButNotEnrolled"),
    INELIGIBLE("Ineligible")
}