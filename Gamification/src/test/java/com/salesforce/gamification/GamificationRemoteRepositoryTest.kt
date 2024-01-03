package com.salesforce.gamification

import com.google.gson.Gson
import com.salesforce.gamification.api.GameAPIClient
import com.salesforce.gamification.api.GameAPIInterface
import com.salesforce.gamification.api.GameAuthenticator
import com.salesforce.gamification.model.GameRewardResponse
import com.salesforce.gamification.model.Games
import com.salesforce.gamification.repository.GamificationRemoteRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GamificationRemoteRepositoryTest {
    private lateinit var gameRemoteRepository: GamificationRemoteRepository

    private lateinit var gameAPIClient: GameAPIClient

    private lateinit var gameAPIInterface: GameAPIInterface

    private lateinit var authenticator: GameAuthenticator

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        gameAPIClient = mockk<GameAPIClient>(relaxed = true)

        authenticator = mockk<GameAuthenticator>(relaxed = true)

        gameAPIInterface = mockk<GameAPIInterface>(relaxed = true)

        gameRemoteRepository =
            GamificationRemoteRepository(authenticator, "https://instanceUrl", gameAPIClient)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get Games success mock response`() {
        runBlocking {
            coEvery { gameAPIClient.getNetworkClient() } returns gameAPIInterface
            val mockResponse = MockFileReader("Games.json").content
            val mockGamesResponse =
                Gson().fromJson(mockResponse, Games::class.java)
            coEvery {
                gameAPIClient.getNetworkClient().getGames(any(), any())
            } returns Result.success(mockGamesResponse)

            val result = gameRemoteRepository.getGames(
                participantId = "MRI706",
                mockResponse = true
            )
            TestCase.assertEquals(result.isSuccess, true)
            result.onSuccess {
                TestCase.assertEquals(it.gameDefinitions.size , 6)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get Games success response`() {
        runBlocking {
            coEvery { gameAPIClient.getNetworkClient() } returns gameAPIInterface
            val mockResponse = MockFileReader("Games.json").content
            val mockGamesResponse =
                Gson().fromJson(mockResponse, Games::class.java)
            coEvery {
                gameAPIClient.getNetworkClient().getGames(any(), any())
            } returns Result.success(mockGamesResponse)

            val result = gameRemoteRepository.getGames(
                participantId = "MRI706",
                mockResponse = false
            )
            TestCase.assertEquals(result.isSuccess, true)
            result.onSuccess {
                TestCase.assertEquals(it.gameDefinitions.size , 6)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get Games failure response`() {
        runBlocking {
            coEvery { gameAPIClient.getNetworkClient() } returns gameAPIInterface
            coEvery {
                gameAPIClient.getNetworkClient().getGames(any(), any())
            } returns Result.failure(Exception("HTTP 401 Unauthorized"))

            val result = gameRemoteRepository.getGames(
                participantId = "MRI706",
                mockResponse = true
            )
            result.onFailure {
                TestCase.assertEquals(result.isFailure, true)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get Games Reward success mock response`() {
        runBlocking {
            coEvery { gameAPIClient.getNetworkClient() } returns gameAPIInterface
            val mockResponse = MockFileReader("GameRewards.json").content
            val mockGameRewardsResponse =
                Gson().fromJson(mockResponse, GameRewardResponse::class.java)
            coEvery {
                gameAPIClient.getNetworkClient().getGameReward(any())
            } returns Result.success(mockGameRewardsResponse)

            val result = gameRemoteRepository.getGameReward(
                gameParticipantRewardId = "MRI706",
                mockResponse = true
            )
            TestCase.assertEquals(result.isSuccess, true)
            result.onSuccess {
                TestCase.assertNotNull(it.gameRewards)
                TestCase.assertEquals(it.gameRewards.size , 1)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get Games Reward success response`() {
        runBlocking {
            coEvery { gameAPIClient.getNetworkClient() } returns gameAPIInterface
            val mockResponse = MockFileReader("GameRewards.json").content
            val mockGameRewardsResponse =
                Gson().fromJson(mockResponse, GameRewardResponse::class.java)
            coEvery {
                gameAPIClient.getNetworkClient().getGameReward(any())
            } returns Result.success(mockGameRewardsResponse)

            val result = gameRemoteRepository.getGameReward(
                gameParticipantRewardId = "MRI706",
                mockResponse = false
            )
            TestCase.assertEquals(result.isSuccess, true)
            result.onSuccess {
                TestCase.assertNotNull(it.gameRewards)
                TestCase.assertEquals(it.gameRewards.size , 1)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get Game Reward failure response`() {
        runBlocking {
            coEvery { gameAPIClient.getNetworkClient() } returns gameAPIInterface
            coEvery {
                gameAPIClient.getNetworkClient().getGameReward(any())
            } returns Result.failure(Exception("HTTP 401 Unauthorized"))

            val result = gameRemoteRepository.getGameReward(
                gameParticipantRewardId = "MRI706",
                mockResponse = true
            )
            result.onFailure {
                TestCase.assertEquals(result.isFailure, true)
            }
        }
    }
}