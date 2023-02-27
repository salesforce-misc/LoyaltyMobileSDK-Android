package com.salesforce.loyalty.mobile.sources.loyaltyAPI

/**
 * LoyaltyConfig class is a utility class for holding constant values.
 */
object LoyaltyConfig {
    const val LOYALTY_PROGRAM_NAME = "NTO Insider"
    const val API_VERSION = "55.0"
    const val API_VERSION_56 = "56.0"
    const val VERSION = "v"
    const val MEMBER_BASE_URL =
        "https://internalmobileteam-dev-ed.develop.my.salesforce.com/services/data/"
    const val KEY_MEMBERSHIP_NUMBER = "MembershipNumber"
    const val KEY_MEMBER_ID = "MemberId"
    const val KEY_PROMOTION_NAME = "PromotionName"
    enum class ProgramProcessName(val processName: String) {
        GET_PROMOTIONS("GetMemberPromotions"),
        ENROLL_IN_PROMOTION("EnrollInPromotion")
    }
    sealed class Resource {
        class IndividualEnrollment(val programName: String) : Resource()
        class MemberProfile(val programName: String) : Resource()
        class MemberBenefits(val memberId: String) : Resource()
        class LoyaltyProgramProcess(val programName: String, val programProcessName: ProgramProcessName) : Resource()

    }

    fun getRequestUrl(resource: Resource): String {
        return when (resource) {
            is Resource.IndividualEnrollment -> {
                MEMBER_BASE_URL + VERSION + API_VERSION + "/loyalty-programs/" + resource.programName + "/individual-member-enrollments"
            }
            is Resource.MemberProfile -> {
                MEMBER_BASE_URL + VERSION + API_VERSION + "/loyalty-programs/" + resource.programName + "/members"
            }
            is Resource.MemberBenefits -> {
                MEMBER_BASE_URL + VERSION + API_VERSION + "/connect/loyalty/member/" + resource.memberId + "/memberbenefits"
            }
            is Resource.LoyaltyProgramProcess -> {
                MEMBER_BASE_URL + VERSION + API_VERSION_56 + "/connect/loyalty/programs/" +
                        resource.programName + "/program-processes/" + resource.programProcessName.processName
            }
        }
    }
}