package com.salesforce.loyalty.mobile.sources

import com.salesforce.loyalty.mobile.sources.forceUtils.DateUtils
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.junit.Test

class DateUtilsTest {

    @Test
    fun testGetCurrentDateAndTimeSuccess() {
        val currentDateTime = DateUtils.getCurrentDateTime(DateUtils.DATE_FORMAT_YYYYMMDDTHHMMSS)
        assertNotNull(currentDateTime)
    }

    @Test
    fun testGetCurrentDateAndTimeFailure() {
        val currentDateTime = DateUtils.getCurrentDateTime("abcd")
        assertNull(currentDateTime)
    }
}