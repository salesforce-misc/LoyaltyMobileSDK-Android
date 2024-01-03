package com.salesforce.gamification

import com.salesforce.gamification.model.*
import junit.framework.TestCase
import org.junit.Test

class GameModelsTest {

    @Test
    fun testGamesResponse() {
        val games = Games(
            errorMessage = null,
            gameDefinitions = listOf(
                GameDefinition(
                    gameDefinitionId = "1",
                    description = "Play Spin The Wheel to Get Amazing Rewards",
                    endDate = "2023-10-31T19:00:00.000Z",
                    gameRewards = listOf(
                        GameReward(
                            gameRewardId = "501",
                            rewardDefinitionId = "101",
                            segColor = "01CD6C",
                            description = "Win\n $12 \n Off",
                            expirationDate = "2023-10-31T19:00:00.000Z",
                            imageUrl = null,
                            name = "10% Off",
                            rewardType = "Voucher",
                            rewardValue = null
                        )
                    ),
                    participantGameRewards = listOf(
                        ParticipantGameReward(
                            gameRewardId = "501",
                            gameParticipantRewardId = "201",
                            sourceActivityId = "201",
                            expirationDate = "2023-12-30T19:00:00.000Z",
                            issuedRewardReference = "12344",
                            status = "YetToReward"
                        )
                    ),
                    name = "Bonnie and Clyde Style Promotion",
                    startDate = "2023-10-01T19:00:00.000Z",
                    status = "active",
                    timeoutDuration = 10,
                    type = "SpintheWheel"
                )
            ),
            status = true
        )
        TestCase.assertEquals(games.status, true)
        TestCase.assertEquals(games.gameDefinitions.size, 1)
        val gameDef = games.gameDefinitions.firstOrNull()
        TestCase.assertNotNull(gameDef)
        TestCase.assertEquals(gameDef?.gameDefinitionId, "1")
        TestCase.assertEquals(gameDef?.description, "Play Spin The Wheel to Get Amazing Rewards")
        TestCase.assertEquals(gameDef?.endDate, "2023-10-31T19:00:00.000Z")
        TestCase.assertEquals(gameDef?.name, "Bonnie and Clyde Style Promotion")
        TestCase.assertEquals(gameDef?.startDate, "2023-10-01T19:00:00.000Z")
        TestCase.assertEquals(gameDef?.status, "active")
        TestCase.assertEquals(gameDef?.timeoutDuration, 10)
        TestCase.assertEquals(gameDef?.type, "SpintheWheel")
        val gameReward = gameDef?.gameRewards?.firstOrNull()
        TestCase.assertEquals(gameReward?.gameRewardId, "501")
        TestCase.assertEquals(gameReward?.rewardDefinitionId, "101")
        TestCase.assertEquals(gameReward?.segColor, "01CD6C")
        TestCase.assertEquals(gameReward?.description, "Win\n $12 \n Off")
        TestCase.assertEquals(gameReward?.expirationDate, "2023-10-31T19:00:00.000Z")
        TestCase.assertEquals(gameReward?.imageUrl, null)
        TestCase.assertEquals(gameReward?.name, "10% Off")
        TestCase.assertEquals(gameReward?.rewardType, "Voucher")
        TestCase.assertEquals(gameReward?.rewardValue, null)
        val gameParticipantReward = gameDef?.participantGameRewards?.firstOrNull()
        TestCase.assertEquals(gameParticipantReward?.gameRewardId, "501")
        TestCase.assertEquals(gameParticipantReward?.gameParticipantRewardId, "201")
        TestCase.assertEquals(gameParticipantReward?.sourceActivityId, "201")
        TestCase.assertEquals(gameParticipantReward?.expirationDate, "2023-12-30T19:00:00.000Z")
        TestCase.assertEquals(gameParticipantReward?.issuedRewardReference, "12344")
        TestCase.assertEquals(gameParticipantReward?.status, "YetToReward")
    }

    @Test
    fun testGameRewardsResponse() {
        val gameRewardsResponse = GameRewardResponse(
            errorMessage = null,
            gameRewards = listOf(
                GameRewards(
                    gameRewardId = "501",
                    rewardDefinitionId = "101",
                    color = "01CD6C",
                    description = null,
                    expirationDate = "2024-01-04T20:00:00.000Z",
                    imageUrl = null,
                    name = "1000 Reward Points",
                    rewardType = "LoyaltyPoints",
                    rewardValue = "100.0",
                    issuedRewardReference = "3GRSB0000000Caf4AE"
                )
            ),

            status = true
        )
        TestCase.assertEquals(gameRewardsResponse.status, true)
        TestCase.assertEquals(gameRewardsResponse.gameRewards.size, 1)
        val reward = gameRewardsResponse?.gameRewards?.firstOrNull()
        TestCase.assertNotNull(reward)
        TestCase.assertEquals(reward?.gameRewardId, "501")
        TestCase.assertEquals(reward?.rewardDefinitionId, "101")
        TestCase.assertEquals(reward?.color, "01CD6C")
        TestCase.assertEquals(reward?.description, null)
        TestCase.assertEquals(reward?.expirationDate, "2024-01-04T20:00:00.000Z")
        TestCase.assertEquals(reward?.imageUrl, null)
        TestCase.assertEquals(reward?.name, "1000 Reward Points")
        TestCase.assertEquals(reward?.rewardType, "LoyaltyPoints")
        TestCase.assertEquals(reward?.rewardValue, "100.0")
        TestCase.assertEquals(reward?.issuedRewardReference, "3GRSB0000000Caf4AE")
    }
}