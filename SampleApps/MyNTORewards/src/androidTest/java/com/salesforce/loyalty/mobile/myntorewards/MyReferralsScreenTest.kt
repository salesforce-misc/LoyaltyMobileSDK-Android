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
            MyReferralsListScreen {}
        }
    }

    @Test
    fun verifyReferralScreen() {
        composeTestRule.run {
            onNodeWithText("My Referrals").assertIsDisplayed()
            onNodeWithTag(CIRCULAR_PROGRESS_TEST_TAG).assertIsDisplayed()
            Thread.sleep(2000)
            onNodeWithTag(TEST_TAG_TITLE_VIEW)
                .assertTextEquals("My Referrals")
                .assertIsDisplayed()
            onNodeWithTag(TEST_TAG_REFERRAL_CARD).assertIsDisplayed()
            Thread.sleep(2000)
            onNodeWithText("Invitations Sent").assertIsDisplayed()
            onNodeWithText("Invitations Accepted").assertIsDisplayed()
            onNodeWithText("Vouchers Earned").assertIsDisplayed()
            onNodeWithText("Refer Now").assertIsDisplayed()

            onNodeWithTag(TEST_TAG_TAB_VIEW).assertIsDisplayed()
            // SUCCESS TAB content
            onNodeWithText("Completed").assertIsDisplayed().performClick()
            val successList = onNodeWithTag(TEST_TAG_REFERRALS_LIST).onChildren()
            successList.assertCountEquals(6)
            successList.onFirst().assert(hasText("Recent"))

            successList[1].assert(hasTestTag(TEST_TAG_REFERRALS_LIST_ITEM))
            val successListItem = successList[1].onChildren()
            successListItem.onFirst().assert(hasText("strawberry.sheikh@yahoo.com"))
            successListItem.onLast().assert(hasText("Purchase Completed"))

            // InProgress TAB content
            onNodeWithText("In Progress").assertIsDisplayed().performClick()
            val inProgressList = onNodeWithTag(TEST_TAG_REFERRALS_LIST).onChildren()
            inProgressList.assertCountEquals(6)
            inProgressList.onFirst().assert(hasText("Recent"))

            inProgressList[1].assert(hasTestTag(TEST_TAG_REFERRALS_LIST_ITEM))
            val inProgressListItem = successList[1].onChildren()
            inProgressListItem.onFirst().assert(hasText("strawberry.sheikh@yahoo.com"))
            successListItem.onLast().assert(hasText("Invitations Sent"))
        }
        Thread.sleep(500)
        verifyReferFriendScreen()
    }

    private fun verifyReferFriendScreen() {
        composeTestRule.run {
            onNodeWithText("Refer Now").assertIsDisplayed().performClick()
            Thread.sleep(500)
            onNodeWithTag(TEST_TAG_REFER_FRIEND_SCREEN).assertIsDisplayed()
            onNodeWithContentDescription(activity.getString(R.string.refer_friend_banner_content_description)).assertIsDisplayed()
            onNodeWithText("Start Referring").assertIsDisplayed()
            onNodeWithText("Your referral code is ready! Share the referral code with your friends and get rewarded when they place their first order.").assertIsDisplayed()

            // Test Email input Field
            onNodeWithText("Add a comma after each email address.").assertIsDisplayed()
            onNodeWithText("Enter the email addresses of your friends…")
                .performTextInput("abc@xyz.com, abc@xyz2.com")
            onNodeWithTag(TEST_TAG_TEXT_FIELD_RIGHT_ICON, true).assertIsDisplayed()
            // TODO: TEST Share EMail Functionality once API integration is done

            // Test Refer Code Row with Copy option
            onNodeWithText("845FFF907ZX6").assertIsDisplayed()
            onNodeWithText("Copy").assertIsDisplayed().performClick()
            assertEquals("845FFF907ZX6", clipboardManager.text)
            Thread.sleep(2000)

            // Test Social Media Share Icons
            onNodeWithText("Share Invite").assertIsDisplayed()
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