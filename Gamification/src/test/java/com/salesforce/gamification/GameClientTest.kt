package com.salesforce.gamification

import com.salesforce.gamification.api.GameAPIClient
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class GameClientTest {

    companion object {
        const val INSTANCE_URL = "https://instanceUrl"
    }

    @Test
    fun testGameClient() {
        val gameClient = GameAPIClient(MockAuthenticator, INSTANCE_URL)
        assertNotNull(gameClient.getNetworkClient())
    }
}