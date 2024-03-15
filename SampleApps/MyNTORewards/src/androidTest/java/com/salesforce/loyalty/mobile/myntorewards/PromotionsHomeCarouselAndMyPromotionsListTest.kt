package com.salesforce.loyalty.mobile.myntorewards

import android.content.ClipboardManager
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
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
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralsLocalRepository
import com.salesforce.loyalty.mobile.myntorewards.utilities.INSTAGRAM_APP_PACKAGE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TWITTER_APP_PACKAGE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.REFER_FRIEND_PROMOTION_DESC
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.REFER_FRIEND_PROMOTION_NAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMO_DESCRIPTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMO_ITEM
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMO_NAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.WHATSAPP_APP_PACKAGE
import com.salesforce.loyalty.mobile.myntorewards.utilities.isAppInstalled
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.GameViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
import com.salesforce.loyalty.mobile.myntorewards.views.HomeTabScreen
import com.salesforce.loyalty.mobile.myntorewards.views.components.CIRCULAR_PROGRESS_TEST_TAG
import com.salesforce.loyalty.mobile.myntorewards.views.components.TEST_TAG_TEXT_FIELD_RIGHT_ICON
import com.salesforce.referral.repository.ReferralsRepository
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PromotionsHomeCarouselAndMyPromotionsListTest {
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
    }

    private fun mockPromotionsDataAndLaunchScreen(isReferralPromotion: Boolean) {
        val repository = ReferralsRepository(getAPIService())
        val localRepository = ReferralsLocalRepository(getLocalAPIService(), "")
        localRepository.setPromotionType(isReferral = isReferralPromotion)
        var myReferralsViewModel = MyReferralsViewModel(repository, localRepository, "")

        // Launch the screen
        composeTestRule.setContent {
            HomeTabScreen(
                getMembershipProfileViewModel(),
                getPromotionViewModel(),
                getVoucherViewModel(),
                getOnBoardingMockViewModel(),
                getBenefitViewModel(),
                getTransactionViewModel(),
                getCheckoutFlowViewModel(),
                getScanningViewModel(),
                getGameViewModel() as GameViewModel,
                referralViewModel = myReferralsViewModel
            )
        }
    }

    @After
    fun tearDown() {
        ReferralsLocalRepository.clearReferralsData()
    }

    @Test
    fun verifyPromotionsCarouselScreenWithReferralPromotions() {
        mockPromotionsDataAndLaunchScreen(true)

        composeTestRule.run {
            onNodeWithText("Home").assertIsDisplayed().performClick()

            onNodeWithTag(TestTags.TEST_TAG_PROMOTION_CARD).assertIsDisplayed().performClick()
            Thread.sleep(500)
            onNodeWithTag(CIRCULAR_PROGRESS_TEST_TAG).assertIsDisplayed()

            Thread.sleep(2000)
            verifyStartReferringScreen()
            onNodeWithTag(TestTags.TEST_TAG_PROMOTION_CARD).assertIsDisplayed().performClick()
            Thread.sleep(500)
            verifyReferFriendScreen()
            Thread.sleep(1000)
        }
    }

    @Test
    fun testPromotionsListScreenWithReferralPromotions() {
        mockPromotionsDataAndLaunchScreen(true)

        composeTestRule.run {
            onNode(
                hasText("My Promotions")
                        and
                        hasClickAction()
            ).assertIsDisplayed().performClick()
            Thread.sleep(500)
            onNode(
                hasTestTag(TEST_TAG_PROMO_ITEM)
                        and
                        hasText("Default Referral Promotion")
            ).assertIsDisplayed().performClick()
            Thread.sleep(500)
            onNodeWithTag(CIRCULAR_PROGRESS_TEST_TAG).assertIsDisplayed()

            Thread.sleep(2000)
            verifyStartReferringScreen()
            onNode(
                hasTestTag(TEST_TAG_PROMO_ITEM)
                        and
                        hasText("Default Referral Promotion")
            ).assertIsDisplayed().performClick()
            Thread.sleep(500)
            verifyReferFriendScreen()
            Thread.sleep(1000)
        }
    }

    @Test
    fun verifyPromotionsCarouselScreenWithLoyaltyPromotions() {
        mockPromotionsDataAndLaunchScreen(false)
        composeTestRule.run {
            onNodeWithText("Home").assertIsDisplayed().performClick()
            onNodeWithText("Default Referral Promotion").assertIsDisplayed()
            onNodeWithTag(TestTags.TEST_TAG_PROMOTION_CARD).assertIsDisplayed().performClick()
            Thread.sleep(1000)
            onNode(
                hasTestTag(TEST_TAG_PROMO_NAME)
                        and
                        hasText("Default Referral Promotion")
            ).assertIsDisplayed()
            onNode(
                hasTestTag(TEST_TAG_PROMO_DESCRIPTION)
                        and
                        hasText("Invite your friends and get a voucher when they shop for the first time.")
            ).assertIsDisplayed()
            onNodeWithTag(TestTags.TEST_TAG_CLOSE_POPUP_PROMOTION).assertIsDisplayed().performClick()
            Thread.sleep(500)
            onNodeWithTag(TestTags.TEST_TAG_PROMOTION_CARD).assertIsDisplayed()
            Thread.sleep(1000)
        }
    }

    @Test
    fun testPromotionsListScreenWithLoyaltyPromotions() {
        mockPromotionsDataAndLaunchScreen(false)
        composeTestRule.run {
            onNode(
                hasText("My Promotions")
                        and
                        hasClickAction()
            ).assertIsDisplayed().performClick()
            Thread.sleep(500)
            onNode(
                hasTestTag(TEST_TAG_PROMO_ITEM)
                        and
                        hasText("Default Referral Promotion")
            ).assertIsDisplayed().performClick()
            Thread.sleep(1000)
            onNode(
                hasTestTag(TEST_TAG_PROMO_NAME)
                        and
                        hasText("Default Referral Promotion")
            ).assertIsDisplayed()
            onNode(
                hasTestTag(TEST_TAG_PROMO_DESCRIPTION)
                        and
                        hasText("Invite your friends and get a voucher when they shop for the first time.")
            ).assertIsDisplayed()

            onNodeWithTag(TestTags.TEST_TAG_CLOSE_POPUP_PROMOTION).assertIsDisplayed().performClick()
            Thread.sleep(500)
            onNode(
                hasTestTag(TEST_TAG_PROMO_ITEM)
                        and
                        hasText("Default Referral Promotion")
            ).assertIsDisplayed().performClick()
            Thread.sleep(1000)
        }
    }

    private fun verifyStartReferringScreen() {
        composeTestRule.run {
            onNode(
                hasText("Default Referral Promotion")
                        and
                        hasTestTag(REFER_FRIEND_PROMOTION_NAME)
            ).assertIsDisplayed()
            onNode(
                hasText("Invite your friends and get a voucher when they shop for the first time.")
                        and
                        hasTestTag(REFER_FRIEND_PROMOTION_DESC)
            ).assertIsDisplayed()
            onNodeWithText("Start Referring").assertIsDisplayed()
            onNodeWithText("Back").assertIsDisplayed()
            onNodeWithText("Start Referring").performClick()
            Thread.sleep(2000)

            onNodeWithText("Done").assertIsDisplayed()
        }
    }
    private fun verifyReferFriendScreen() {
        composeTestRule.run {
            onNode(
                hasText("Default Referral Promotion")
                        and
                        hasTestTag(REFER_FRIEND_PROMOTION_NAME)
            ).assertIsDisplayed()
            onNode(
                hasText("Invite your friends and get a voucher when they shop for the first time.")
                        and
                        hasTestTag(REFER_FRIEND_PROMOTION_DESC)
            ).assertIsDisplayed()

            onNodeWithText("https://rb.gy/wa6jw7?referralCode=9RCLSYJO--TEMPRP7", substring = true).assertIsDisplayed()

            Thread.sleep(2000)

            // Test Email input Field
            onNodeWithText("Add a comma after each email address.").assertIsDisplayed()
            onNodeWithText("Enter the email addresses of your friends…")
                .performTextInput("email@test.com")

            onNodeWithTag(TEST_TAG_TEXT_FIELD_RIGHT_ICON, true).assertIsDisplayed().performClick()
            Thread.sleep(2000)
            // Test Social Media Share Icons
            onNodeWithText("Share Invite").assertIsDisplayed()

            onNodeWithContentDescription(activity.getString(R.string.share_via_twitter_icon_description))
                .assertIsDisplayed().performClick()
            Thread.sleep(1000)
            if (context.isAppInstalled(TWITTER_APP_PACKAGE)) {
                val twitter: UiObject2 = uiDevice.findObject(By.text("Post"))
                // Perform a click and wait until the app is opened.
                val twitterOpened: Boolean = twitter.clickAndWait(Until.newWindow(), 15000)
                assert(twitterOpened)
                assertEquals(TWITTER_APP_PACKAGE, uiDevice.currentPackageName)
                Thread.sleep(2000)
                uiDevice.pressBack()
                Thread.sleep(2000)
                uiDevice.pressBack()
                Thread.sleep(2000)

                val twitterDelete: UiObject2 = uiDevice.findObject(By.text("Delete"))
                twitterDelete.click()
            } else {
                onAllNodesWithText("No apps can perform this action.")
                uiDevice.pressBack()
            }
            Thread.sleep(1000)

            if (context.isAppInstalled(INSTAGRAM_APP_PACKAGE)) {
                onNodeWithContentDescription(activity.getString(R.string.share_via_instagram_icon_description))
                    .assertIsDisplayed().performClick()
                Thread.sleep(2000)

                assertEquals(INSTAGRAM_APP_PACKAGE, uiDevice.currentPackageName)
            } else {
                onAllNodesWithText("No apps can perform this action.")
            }
            uiDevice.pressBack()

            if (context.isAppInstalled(WHATSAPP_APP_PACKAGE)) {
                onNodeWithContentDescription(activity.getString(R.string.share_via_whatsapp_icon_description))
                    .assertIsDisplayed().performClick()
                Thread.sleep(2000)
                assertEquals(WHATSAPP_APP_PACKAGE, uiDevice.currentPackageName)
            } else {
                onAllNodesWithText("No apps can perform this action.")
            }
            uiDevice.pressBack()

            if (context.isAppInstalled(WHATSAPP_APP_PACKAGE)) {
                onNodeWithContentDescription(activity.getString(R.string.share_via_facebook_icon_description))
                    .assertIsDisplayed().performClick()
                Thread.sleep(1000)
                val facebook: UiObject2 = uiDevice.findObject(By.text("News Feed"))

                val facebookOpened: Boolean = facebook.clickAndWait(Until.newWindow(), 10000)
                assert(facebookOpened)
            } else {
                onAllNodesWithText("No apps can perform this action.")
            }
            uiDevice.pressBack()

            onNodeWithText("Copy").assertIsDisplayed().performClick()
            assertEquals("https://rb.gy/wa6jw7?referralCode=9RCLSYJO-TEMPRP7", clipboardManager.text)
            uiDevice.pressBack()
        }
    }
}