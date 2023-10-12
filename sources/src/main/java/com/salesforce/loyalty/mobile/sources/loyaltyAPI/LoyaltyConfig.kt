/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.loyalty.mobile.sources.loyaltyAPI

/**
 * LoyaltyConfig class is a utility class for holding constant values.
 */
object LoyaltyConfig {
    const val LOYALTY_PROGRAM_NAME = "NTO Insider"
    const val API_VERSION_55 = "v55.0"
    const val API_VERSION_56 = "v56.0"
    const val API_VERSION_58 = "v58.0"
    const val MEMBER_API_SERVICES_PATH = "/services/data/"
    const val KEY_MEMBERSHIP_NUMBER = "MembershipNumber"
    const val KEY_MEMBER_ID = "MemberId"
    const val KEY_PROMOTION_NAME = "PromotionName"
    const val KEY_PROGRAM_NAME = "programName"
    const val HEADER_AUTHORIZATION = "Authorization"


    /**
     * Enum class to hold the mapping of process name with the Processes.
     */
    enum class ProgramProcessName(val processName: String) {
        GET_PROMOTIONS("GetMemberPromotions"),
        ENROLL_IN_PROMOTION("EnrollInPromotion"),
        UNENROLL_PROMOTION("OptOutOfPromotion")
    }

    /**
     * Sealed class that is used to define the Resources and its corresponding parameters.
     */
    sealed class Resource {
        class IndividualEnrollment(val programName: String) : Resource()
        class MemberProfile(val programName: String) : Resource()
        class MemberBenefits(val memberId: String) : Resource()
        class TransactionsHistory(val programName: String, val membershipNumber: String): Resource()
        class LoyaltyProgramProcess(val programName: String, val programProcessName: ProgramProcessName) : Resource()
        class Vouchers(val programName: String, val membershipNumber: String): Resource()

        class GameReward(): Resource()
    }

    /**
     * Get the request URL with appropriate end path for various resources using their corresponding parameters.
     *
     * @param instanceUrl Base URL used for all APIs.
     * @param resource Resource for which url is generated.
     * @return String The request URL to be used for API call.
     */
    fun getRequestUrl(instanceUrl: String, resource: Resource): String {
        return when (resource) {
            is Resource.IndividualEnrollment -> {
                instanceUrl + MEMBER_API_SERVICES_PATH + API_VERSION_58 + "/loyalty-programs/" + resource.programName + "/individual-member-enrollments"
            }
            is Resource.MemberProfile -> {
                instanceUrl + MEMBER_API_SERVICES_PATH + API_VERSION_58 + "/loyalty-programs/" + resource.programName + "/members"
            }
            is Resource.MemberBenefits -> {
                instanceUrl + MEMBER_API_SERVICES_PATH + API_VERSION_58 + "/connect/loyalty/member/" + resource.memberId + "/memberbenefits"
            }
            is Resource.LoyaltyProgramProcess -> {
                instanceUrl + MEMBER_API_SERVICES_PATH + API_VERSION_58 + "/connect/loyalty/programs/" +
                        resource.programName + "/program-processes/" + resource.programProcessName.processName
            }
            is Resource.TransactionsHistory -> {
                instanceUrl + MEMBER_API_SERVICES_PATH + API_VERSION_58 + "/loyalty/programs/" + resource.programName + "/members/" + resource.membershipNumber + "/transaction-ledger-summary"
            }
            is Resource.Vouchers -> {
                instanceUrl + MEMBER_API_SERVICES_PATH + API_VERSION_58 + "/loyalty/programs/" + resource.programName + "/members/" + resource.membershipNumber + "/vouchers"
            }
            is Resource.GameReward -> {
                instanceUrl + MEMBER_API_SERVICES_PATH + API_VERSION_58 + "/game/definition/Definition123/Participant/Participant123/Play"
            }
        }
    }
}