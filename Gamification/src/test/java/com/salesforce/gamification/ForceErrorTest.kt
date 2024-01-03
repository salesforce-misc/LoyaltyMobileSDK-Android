package com.salesforce.gamification

import com.salesforce.gamification.api.GameAPIError
import junit.framework.TestCase.assertEquals
import org.junit.Test

class GameAPIErrorTest {

    @Test
    fun testGameAPIErrorDescriptions() {
        assertEquals(GameAPIError.AUTHENTICATION_NEEDED.customDescription, "Authentication needed")
        assertEquals(GameAPIError.REQUEST_FAILED.customDescription, "Request Failed Error")
        assertEquals(
            GameAPIError.JSON_CONVERSION_FAILURE.customDescription,
            "JSON Conversion Failure"
        )
        assertEquals(GameAPIError.INVALID_DATA.customDescription, "Invalid Data Error")
        assertEquals(
            GameAPIError.RESPONSE_UNSUCCESSFUL.customDescription,
            "Response Unsuccessful Error"
        )
        assertEquals(
            GameAPIError.JSON_PARSING_FAILURE.customDescription,
            "JSON Parsing Failure Error"
        )
        assertEquals(GameAPIError.NO_INTERNET.customDescription, "No internet connection")
        assertEquals(
            GameAPIError.FAILED_SERIALIZATION.customDescription,
            "Serialization print for debug failed."
        )
        assertEquals(
            GameAPIError.USER_IDENTITY_NEEDED.customDescription,
            "User Identity has not been set."
        )
        assertEquals(
            GameAPIError.AUTH_NOT_FOUND_IN_PREFERENCES.customDescription,
            "Cannot find the auth from Preferences."
        )

    }
}