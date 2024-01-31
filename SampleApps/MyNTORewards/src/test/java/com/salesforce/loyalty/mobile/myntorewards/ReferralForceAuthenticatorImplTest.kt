package com.salesforce.loyalty.mobile.myntorewards

import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralForceAuthenticatorImpl
import com.salesforce.referral.api.ReferralForceAuthenticator
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class ReferralForceAuthenticatorImplTest {

    private val forceAuthManager: ForceAuthManager = mockk()
    private lateinit var referralForceAuthenticator: ReferralForceAuthenticator

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        referralForceAuthenticator = ReferralForceAuthenticatorImpl(forceAuthManager)
    }

    @Test
    fun `Given valid access token, when getting access token from force auth manager, then verify token is same`() {
        runBlocking {
            // given
            every { forceAuthManager.getAccessToken() } returns "ABCDEFGHIJK"

            // When
            val result = referralForceAuthenticator.getAccessToken()

            // Then
            TestCase.assertEquals("ABCDEFGHIJK", result)
        }
    }

    @Test
    fun `Given access token as null, when getting access token from force auth manager, then verify token is null`() {
        runBlocking {
            // given
            every { forceAuthManager.getAccessToken() } returns null

            // When
            val result = referralForceAuthenticator.getAccessToken()

            // Then
            TestCase.assertNull(result)
        }
    }

    @Test
    fun `Given valid access token, when granting access token from force auth manager, then verify token is same`() {
        runBlocking {
            // given
            coEvery { forceAuthManager.grantAccessToken() } returns "ABCDEFGHIJK"

            // When
            val result = referralForceAuthenticator.grantAccessToken()

            // Then
            TestCase.assertEquals("ABCDEFGHIJK", result)
        }
    }

    @Test
    fun `Given access token as null, when granting access token from force auth manager, then verify token is null`() {
        runBlocking {
            // given
            coEvery { forceAuthManager.grantAccessToken() } returns null

            // When
            val result = referralForceAuthenticator.grantAccessToken()

            // Then
            TestCase.assertNull(result)
        }
    }
}