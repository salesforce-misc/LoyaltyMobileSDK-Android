package com.salesforce.loyalty.mobile.sources

import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyClient
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class LoyaltyClientTest {

    companion object {
        const val INSTANCE_URL = "https://instanceUrl"
    }

    @Test
    fun testLoyaltyClient() {
        val loyaltyClient = LoyaltyClient(MockAuthenticator, INSTANCE_URL)
        assertNotNull(loyaltyClient.getNetworkClient())
    }
}