package com.salesforce.gamification

import com.salesforce.gamification.api.NetworkClient
import com.salesforce.gamification.repository.GamificationRemoteRepository
import junit.framework.TestCase
import junit.framework.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.net.HttpURLConnection

class NetworkClientTest {

    private lateinit var gamificationRemoteRepository: GamificationRemoteRepository

    private lateinit var mockWebServer: MockWebServer

    private lateinit var gameClient: NetworkClient

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mockWebServer = MockWebServer()
        mockWebServer.start()
        gameClient = MockNetworkClient(MockAuthenticator, "https://instanceUrl", mockWebServer)
        gamificationRemoteRepository =
            GamificationRemoteRepository(MockAuthenticator, "https://instanceUrl", gameClient)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `read sample success json file`() {
        val reader = MockFileReader("Games.json")
        assertNotNull(reader.content)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGameAPIUnauthorized() {
        runBlocking {
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
            mockWebServer.enqueue(response)

            val actualResponse = gameClient.getNetworkClient().getGames(
                mockWebServer.url("/").toString(),
                gameParticipantRewardId = "MRI706",
            )
            mockWebServer.takeRequest()
            assertEquals(actualResponse.isFailure, true)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetGameAPI400Error() {
        runBlocking {
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody(MockFileReader("Games.json").content)
            mockWebServer.enqueue(response)

            val actualResponse = gameClient.getNetworkClient().getGames(
                mockWebServer.url("/").toString(),
                gameParticipantRewardId = "MRI706",
            )
            mockWebServer.takeRequest()
            actualResponse.onFailure {
                assertEquals(actualResponse.isFailure, true)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGameAPI() {
        runBlocking {
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockFileReader("Games.json").content)
            mockWebServer.enqueue(response)

            val actualResponse = gameClient.getNetworkClient().getGames(
                mockWebServer.url("/").toString(),
                gameParticipantRewardId = "MRI706"
            )
            mockWebServer.takeRequest()
            assertEquals(actualResponse.isSuccess, true)
            actualResponse.onSuccess {
                assertEquals(it.status, true)
                assertEquals(it.gameDefinitions.size, 6)
                val gameDef = it.gameDefinitions.firstOrNull()
                assertNotNull(gameDef)
                assertEquals(gameDef?.gameDefinitionId, "1")
                assertEquals(gameDef?.description, "Play Spin The Wheel to Get Amazing Rewards")
                assertEquals(gameDef?.endDate, "2023-10-31T19:00:00.000Z")
                assertEquals(gameDef?.name, "Bonnie and Clyde Style Promotion")
                assertEquals(gameDef?.startDate, "2023-10-01T19:00:00.000Z")
                assertEquals(gameDef?.status, "active")
                assertEquals(gameDef?.timeoutDuration, 10)
                assertEquals(gameDef?.type, "SpintheWheel")
                val gameReward = gameDef?.gameRewards?.firstOrNull()
                assertEquals(gameReward?.gameRewardId, "501")
                assertEquals(gameReward?.rewardDefinitionId, "101")
                assertEquals(gameReward?.segColor, "01CD6C")
                assertEquals(gameReward?.description, "Win\n $12 \n Off")
                assertEquals(gameReward?.expirationDate, "2023-10-31T19:00:00.000Z")
                assertEquals(gameReward?.imageUrl, null)
                assertEquals(gameReward?.name, "10% Off")
                assertEquals(gameReward?.rewardType, "Voucher")
                assertEquals(gameReward?.rewardValue, null)
                val gameParticipantReward = gameDef?.participantGameRewards?.firstOrNull()
                assertEquals(gameParticipantReward?.gameRewardId, "501")
                assertEquals(gameParticipantReward?.gameParticipantRewardId, "201")
                assertEquals(gameParticipantReward?.sourceActivityId, "201")
                assertEquals(gameParticipantReward?.expirationDate, "2023-11-30T19:00:00.000Z")
                assertEquals(gameParticipantReward?.issuedRewardReference, "12344")
                assertEquals(gameParticipantReward?.status, "active")
            }
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}