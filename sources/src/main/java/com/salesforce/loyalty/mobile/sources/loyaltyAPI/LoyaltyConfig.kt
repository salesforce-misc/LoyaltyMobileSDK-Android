package com.salesforce.loyalty.mobile.sources.loyaltyAPI

/**
 * LoyaltyConfig class is a utility class for holding constant values.
 */
object LoyaltyConfig {
    const val LOYALTY_PROGRAM_NAME = "NTO Insider"
    const val API_VERSION_55 = "v55.0"
    const val API_VERSION_56 = "v56.0"
    const val API_VERSION_58 = "v58.0"
    const val MEMBER_BASE_URL_OLD =
        "https://internalmobileteam-dev-ed.develop.my.salesforce.com/services/data/"
    const val MEMBER_BASE_URL_APEXREST =
        "https://internalmobileteam-dev-ed.develop.my.salesforce.com/services/apexrest/"
    const val MEMBER_BASE_URL = "https://dro000000kef12ao.test1.my.pc-rnd.salesforce.com/services/data/"
    const val KEY_MEMBERSHIP_NUMBER = "MembershipNumber"
    const val KEY_MEMBER_ID = "MemberId"
    const val KEY_PROMOTION_NAME = "PromotionName"
    const val KEY_PROGRAM_NAME = "programName"
    enum class ProgramProcessName(val processName: String) {
        GET_PROMOTIONS("GetMemberPromotions"),
        ENROLL_IN_PROMOTION("EnrollInPromotion"),
        UNENROLL_PROMOTION("OptOutOfPromotion")
    }
    sealed class Resource {
        class IndividualEnrollment(val programName: String) : Resource()
        class MemberProfile(val programName: String) : Resource()
        class MemberBenefits(val memberId: String) : Resource()
        class TransactionsHistory(val programName: String, val membershipNumber: String): Resource()
        class LoyaltyProgramProcess(val programName: String, val programProcessName: ProgramProcessName) : Resource()
    }

    fun getRequestUrl(resource: Resource): String {
        return when (resource) {
            is Resource.IndividualEnrollment -> {
                MEMBER_BASE_URL + API_VERSION_58 + "/loyalty-programs/" + resource.programName + "/individual-member-enrollments"
            }
            is Resource.MemberProfile -> {
                MEMBER_BASE_URL + API_VERSION_58 + "/loyalty-programs/" + resource.programName + "/members"
            }
            is Resource.MemberBenefits -> {
                MEMBER_BASE_URL + API_VERSION_58 + "/connect/loyalty/member/" + resource.memberId + "/memberbenefits"
            }
            is Resource.LoyaltyProgramProcess -> {
                MEMBER_BASE_URL + API_VERSION_58 + "/connect/loyalty/programs/" +
                        resource.programName + "/program-processes/" + resource.programProcessName.processName
            }
            is Resource.TransactionsHistory -> {
                MEMBER_BASE_URL + API_VERSION_58 + "/loyalty/programs/" + resource.programName + "/members/" + resource.membershipNumber + "/transaction-ledger-summary"
            }
        }
    }
}