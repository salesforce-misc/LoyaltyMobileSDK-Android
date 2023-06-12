package com.salesforce.loyalty.mobile.sources

import com.salesforce.loyalty.mobile.sources.forceUtils.ForceError
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ForceErrorTest {

    @Test
    fun testForceErrorDescriptions() {
        assertEquals(ForceError.AUTHENTICATION_NEEDED.customDescription, "Authentication needed")
        assertEquals(ForceError.REQUEST_FAILED.customDescription, "Request Failed Error")
        assertEquals(
            ForceError.JSON_CONVERSION_FAILURE.customDescription,
            "JSON Conversion Failure"
        )
        assertEquals(ForceError.INVALID_DATA.customDescription, "Invalid Data Error")
        assertEquals(
            ForceError.RESPONSE_UNSUCCESSFUL.customDescription,
            "Response Unsuccessful Error"
        )
        assertEquals(
            ForceError.JSON_PARSING_FAILURE.customDescription,
            "JSON Parsing Failure Error"
        )
        assertEquals(ForceError.NO_INTERNET.customDescription, "No internet connection")
        assertEquals(
            ForceError.FAILED_SERIALIZATION.customDescription,
            "Serialization print for debug failed."
        )
        assertEquals(
            ForceError.USER_IDENTITY_NEEDED.customDescription,
            "User Identity has not been set."
        )
        assertEquals(
            ForceError.AUTH_NOT_FOUND_IN_PREFERENCES.customDescription,
            "Cannot find the auth from Preferences."
        )

    }
}