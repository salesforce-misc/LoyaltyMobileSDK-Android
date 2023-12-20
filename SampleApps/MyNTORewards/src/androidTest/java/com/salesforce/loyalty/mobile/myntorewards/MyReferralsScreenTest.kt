package com.salesforce.loyalty.mobile.myntorewards

import android.content.ClipboardManager
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.utilities.INSTAGRAM_APP_PACKAGE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TWITTER_APP_PACKAGE
import com.salesforce.loyalty.mobile.myntorewards.utilities.WHATSAPP_APP_PACKAGE
import com.salesforce.loyalty.mobile.myntorewards.views.TEST_TAG_TITLE_VIEW
import com.salesforce.loyalty.mobile.myntorewards.views.components.CIRCULAR_PROGRESS_TEST_TAG
import com.salesforce.loyalty.mobile.myntorewards.views.components.TEST_TAG_TAB_VIEW
import com.salesforce.loyalty.mobile.myntorewards.views.components.TEST_TAG_TEXT_FIELD_RIGHT_ICON
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.MyReferralsListScreen
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.TEST_TAG_REFERRALS_LIST
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.TEST_TAG_REFERRALS_LIST_ITEM
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.TEST_TAG_REFERRAL_CARD
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.TEST_TAG_REFER_FRIEND_SCREEN
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MyReferralsScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var uiDevice: UiDevice
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Launch the screen
        composeTestRule.setContent {
            MyReferralsListScreen(backAction = {true}) {}
        }
    }

    @Test
    fun verifyReferralScreen() {
        composeTestRule.run {
            onNodeWithText(activity.getString(R.string.header_label_my_referrals)).assertIsDisplayed()
            onNodeWithTag(CIRCULAR_PROGRESS_TEST_TAG).assertIsDisplayed()
            Thread.sleep(2000)
            onNodeWithTag(TEST_TAG_TITLE_VIEW)
                .assertTextEquals(activity.getString(R.string.header_label_my_referrals))
                .assertIsDisplayed()
            onNodeWithTag(TEST_TAG_REFERRAL_CARD).assertIsDisplayed()
            Thread.sleep(2000)
            onNodeWithText(activity.getString(R.string.my_referral_sent_label)).assertIsDisplayed()
            onNodeWithText(activity.getString(R.string.my_referrals_accepted_label)).assertIsDisplayed()
            onNodeWithText(activity.getString(R.string.my_referrals_vouchers_earned_label)).assertIsDisplayed()
            onNodeWithText(activity.getString(R.string.my_referrals_points_earned_label)).assertIsDisplayed()
            onNodeWithText(activity.getString(R.string.referral_card_button_text)).assertIsDisplayed()

            onNodeWithTag(TEST_TAG_TAB_VIEW).assertIsDisplayed()
            // SUCCESS TAB content
            onNodeWithText(activity.getString(R.string.referral_tab_success)).assertIsDisplayed().performClick()
            val successList = onNodeWithTag(TEST_TAG_REFERRALS_LIST).onChildren()
            successList.assertCountEquals(6)
            successList.onFirst().assert(hasText(activity.getString(R.string.recent_referrals_section_name)))

            successList[1].assert(hasTestTag(TEST_TAG_REFERRALS_LIST_ITEM))
            val successListItem = successList[1].onChildren()
            successListItem.onFirst().assert(hasText("strawberry.sheikh@yahoo.com"))
            successListItem.onLast().assert(hasText(activity.getString(R.string.purchase_status_completed)))

            // InProgress TAB content
            onNodeWithText(activity.getString(R.string.referral_tab_in_progress)).assertIsDisplayed().performClick()
            val inProgressList = onNodeWithTag(TEST_TAG_REFERRALS_LIST).onChildren()
            inProgressList.assertCountEquals(6)
            inProgressList.onFirst().assert(hasText(activity.getString(R.string.recent_referrals_section_name)))

            inProgressList[1].assert(hasTestTag(TEST_TAG_REFERRALS_LIST_ITEM))
            val inProgressListItem = successList[1].onChildren()
            inProgressListItem.onFirst().assert(hasText("strawberry.sheikh@yahoo.com"))
            successListItem.onLast().assert(hasText(activity.getString(R.string.purchase_status_pending)))
        }
        Thread.sleep(500)
        verifyReferFriendScreen()
    }

    private fun verifyReferFriendScreen() {
        composeTestRule.run {
            onNodeWithText(activity.getString(R.string.referral_card_button_text)).assertIsDisplayed().performClick()
            Thread.sleep(500)
            onNodeWithTag(TEST_TAG_REFER_FRIEND_SCREEN).assertIsDisplayed()
            onNodeWithContentDescription(activity.getString(R.string.refer_friend_banner_content_description)).assertIsDisplayed()
            onNodeWithText(activity.getString(R.string.refer_a_friend_and_earn_header)).assertIsDisplayed()
            onNodeWithText(activity.getString(R.string.refer_a_friend_and_earn_sub_header)).assertIsDisplayed()

            // Test Email input Field
            onNodeWithText(activity.getString(R.string.separate_emails_with_commas)).assertIsDisplayed()
            onNodeWithText(activity.getString(R.string.friends_email_address_placeholder))
                .performTextInput("abc@xyz.com, abc@xyz2.com")
            onNodeWithTag(TEST_TAG_TEXT_FIELD_RIGHT_ICON, true).assertIsDisplayed()
            // TODO: TEST Share EMail Functionality once API integration is done

            // Test Refer Code Row with Copy option
            onNodeWithText(activity.getString(R.string.share_referral_code_label)).assertIsDisplayed()
            onNodeWithText("845FFF907ZX6").assertIsDisplayed()
            onNodeWithText(activity.getString(R.string.tap_to_copy)).assertIsDisplayed().performClick()
            assertEquals("845FFF907ZX6", clipboardManager.text)
            Thread.sleep(2000)

            // Test Social Media Share Icons
            onNodeWithText(activity.getString(R.string.share_via)).assertIsDisplayed()
            onNodeWithContentDescription(activity.getString(R.string.share_via_facebook_icon_description))
                .assertIsDisplayed().performClick()
            Thread.sleep(1000)
            val facebook: UiObject2 = uiDevice.findObject(By.text("News Feed"))
            // Perform a click and wait until the app is opened.
            val facebookOpened: Boolean = facebook.clickAndWait(Until.newWindow(), 3000)
            assert(facebookOpened)
            uiDevice.pressBack()
            uiDevice.pressBack()
            uiDevice.pressBack()
            Thread.sleep(1000)

            onNodeWithContentDescription(activity.getString(R.string.share_via_twitter_icon_description))
                .assertIsDisplayed().performClick()
            Thread.sleep(1000)
            val twitter: UiObject2 = uiDevice.findObject(By.text("Post"))
            // Perform a click and wait until the app is opened.
            val twitterOpened: Boolean = twitter.clickAndWait(Until.newWindow(), 5000)
            assert(twitterOpened)
            assertEquals(TWITTER_APP_PACKAGE, uiDevice.currentPackageName)
            uiDevice.pressBack()
            uiDevice.pressBack()
            val twitterDelete: UiObject2 = uiDevice.findObject(By.text("Delete"))
            // Perform a click and wait until the app is opened.
            twitterDelete.click()
            Thread.sleep(1000)

            onNodeWithContentDescription(activity.getString(R.string.share_via_instagram_icon_description))
                .assertIsDisplayed().performClick()
            Thread.sleep(1000)
            val instagram: UiObject2 = uiDevice.findObject(By.text("Feed"))
            // Perform a click and wait until the app is opened.
            val opened: Boolean = instagram.clickAndWait(Until.newWindow(), 5000)
            assert(opened)
            assertEquals(INSTAGRAM_APP_PACKAGE, uiDevice.currentPackageName)
            uiDevice.pressBack()

            onNodeWithContentDescription(activity.getString(R.string.share_via_whatsapp_icon_description))
                .assertIsDisplayed().performClick()
            Thread.sleep(1000)
            assertEquals(WHATSAPP_APP_PACKAGE, uiDevice.currentPackageName)
            uiDevice.pressBack()
        }
    }
}