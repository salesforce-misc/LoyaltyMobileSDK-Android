package com.salesforce.referral_sdk.api

object ReferralAPIConfig {
    const val REFERRAL_PROGRAM_NAME = "Referral Program"
    const val HEADER_AUTHORIZATION = "Authorization"

    private const val MEMBER_API_SERVICES_PATH = "/services/data/"
    private const val API_VERSION_59 = "v59.0"
    lateinit var instanceUrl: String

    /**
     * Sealed class that is used to define the Resources and its corresponding parameters.
     */
    sealed class Resource {
        class ReferralMemberEnrolment(val programName: String, val promotionName: String): Resource()
        object ReferralEvent : Resource()
    }

    /**
     * Get the request URL with appropriate end path for various resources using their corresponding parameters.
     *
     * @param resource Resource for which url is generated.
     * @return String The request URL to be used for API call.
     */
    fun getRequestUrl(resource: Resource): String {
        return when (resource) {
            is Resource.ReferralMemberEnrolment -> {
                instanceUrl + MEMBER_API_SERVICES_PATH + API_VERSION_59 + "/referral-programs/" + resource.programName + "/promotions/"+resource.promotionName+"/member-enrollments"
            }

            is Resource.ReferralEvent -> "$instanceUrl$MEMBER_API_SERVICES_PATH$API_VERSION_59/referral-program/referral-event"
        }
    }
}