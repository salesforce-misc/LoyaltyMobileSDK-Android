package com.salesforce.gamification.api

object GameAPIConfig {

    const val HEADER_AUTHORIZATION = "Authorization"

    const val MEMBER_API_SERVICES_PATH = "/services/data/"

    const val API_VERSION_60 = "v60.0"

    /**
     * Sealed class that is used to define the Resources and its corresponding parameters.
     */
    sealed class Resource {

        class GameReward(val gameParticipantRewardId: String) : Resource()
        class Games(val participantId: String) : Resource()
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

            is Resource.GameReward -> {
                instanceUrl + MEMBER_API_SERVICES_PATH + API_VERSION_60 + "/game/gameParticipantReward/" + resource.gameParticipantRewardId + "/game-reward"
            }

            is Resource.Games -> {
                instanceUrl + MEMBER_API_SERVICES_PATH + API_VERSION_60 + "/game/participant/" + resource.participantId + "/games"
            }
        }
    }
}