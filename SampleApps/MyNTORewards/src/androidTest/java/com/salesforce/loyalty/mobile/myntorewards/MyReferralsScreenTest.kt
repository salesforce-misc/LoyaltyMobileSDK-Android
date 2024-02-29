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
import androidx.compose.ui.test.onAllNodesWithText
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
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralsLocalRepository
import com.salesforce.loyalty.mobile.myntorewards.referrals.api.ReferralsLocalApiService
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.QueryResult
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralCode
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnrollmentInfo
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEntity
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralPromotionStatusAndPromoCode
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.INSTAGRAM_APP_PACKAGE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TWITTER_APP_PACKAGE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CLOSE_REFER_POPUP
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_EMPTY_VIEW
import com.salesforce.loyalty.mobile.myntorewards.utilities.WHATSAPP_APP_PACKAGE
import com.salesforce.loyalty.mobile.myntorewards.utilities.isAppInstalled
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
import com.salesforce.loyalty.mobile.myntorewards.views.TEST_TAG_TITLE_VIEW
import com.salesforce.loyalty.mobile.myntorewards.views.components.CIRCULAR_PROGRESS_TEST_TAG
import com.salesforce.loyalty.mobile.myntorewards.views.components.TEST_TAG_TEXT_FIELD_RIGHT_ICON
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.MyReferralsListScreen
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.TEST_TAG_REFERRALS_LIST
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.TEST_TAG_REFERRALS_LIST_ITEM
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.TEST_TAG_REFERRAL_CARD
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.TEST_TAG_REFER_FRIEND_SCREEN
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.set
import com.salesforce.referral.api.ApiService
import com.salesforce.referral.entities.ReferralEnrollmentResponse
import com.salesforce.referral.entities.ReferralExistingEnrollmentRequest
import com.salesforce.referral.entities.ReferralNewEnrollmentRequestBody
import com.salesforce.referral.entities.referral_event.ReferralEventRequest
import com.salesforce.referral.entities.referral_event.ReferralEventResponse
import com.salesforce.referral.repository.ReferralsRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.delay
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class MyReferralsScreenTest {
    private lateinit var localRepository: ReferralsLocalRepository

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

        localRepository = ReferralsLocalRepository(getLocalAPIService(), "")
    }

    private fun mockReferralsDataLaunchScreen() {
        val repository = ReferralsRepository(getAPIService())
        var myReferralsViewModel = MyReferralsViewModel(repository, localRepository, "")

        // Launch the screen
        composeTestRule.setContent {
            MyReferralsListScreen(myReferralsViewModel, backAction = { true }) {}
        }
    }

    @After
    fun tearDown() {
        ReferralsLocalRepository.clearReferralsData()
    }

    @Test
    fun testReferralScreenSuccessFlow() {
        mockReferralsDataLaunchScreen()

        composeTestRule.run {
            PrefHelper.customPrefs(context)[AppConstants.REFERRAL_PROGRAM_JOINED] = false
            onNodeWithText("My Referrals").assertIsDisplayed()
            onNodeWithTag(CIRCULAR_PROGRESS_TEST_TAG).assertIsDisplayed()
            Thread.sleep(5000)
            onNodeWithTag(TEST_TAG_TITLE_VIEW)
                .assertTextEquals("My Referrals")
                .assertIsDisplayed()
            onNodeWithTag(TEST_TAG_REFERRAL_CARD).assertIsDisplayed()
            Thread.sleep(5000)
            onNodeWithText("Invitations Sent").assertIsDisplayed()
            onNodeWithText("Invitations Accepted").assertIsDisplayed()
            onNodeWithText("Vouchers Earned").assertIsDisplayed()
            onNodeWithText("Refer Now").assertIsDisplayed().performClick()

            onNodeWithTag(TEST_TAG_REFER_FRIEND_SCREEN).assertIsDisplayed()
            onNodeWithText("Default Referral Promotion").assertIsDisplayed()
            onNodeWithText("Invite your friends and get a voucher when they shop for the first time.").assertIsDisplayed()
            onNodeWithText("Start Referring").assertIsDisplayed()
            onNodeWithText("Back").assertIsDisplayed()

            onNodeWithText("Start Referring").performClick()
            Thread.sleep(2000)

            onNodeWithText("Done").assertIsDisplayed()

            onNodeWithText("Refer Now").assertIsDisplayed().performClick()

            onNodeWithText("Enter the email addresses of your friends…")
                .performTextInput("akash.agrawal@salesforce.com")

            onNodeWithTag(TEST_TAG_TEXT_FIELD_RIGHT_ICON, useUnmergedTree=true).performClick()

            onNodeWithText("Done").performClick()

            onNodeWithText("Completed").assertIsDisplayed()

            onNodeWithText("In Progress").assertIsDisplayed()

            onNodeWithText("Completed").assertIsDisplayed().performClick()

            onNodeWithText("After you refer a friend, you’ll see it here.").assertIsDisplayed()
            onNodeWithContentDescription("After you refer a friend, you’ll see it here.").assertIsDisplayed()
            onNodeWithTag(TEST_TAG_EMPTY_VIEW).assertIsDisplayed()

            onNodeWithText("In Progress").assertIsDisplayed().performClick()
            Thread.sleep(5000)
            onNodeWithText("In Progress").assertIsDisplayed()
            onNodeWithText("Recent").assertIsDisplayed()

            val inProgressList = onNodeWithTag(TEST_TAG_REFERRALS_LIST).onChildren()
            inProgressList.assertCountEquals(6)
            inProgressList.onFirst().assert(hasText("Recent"))

            inProgressList[1].assert(hasTestTag(TEST_TAG_REFERRALS_LIST_ITEM))
            val inProgressListItem = inProgressList[1].onChildren()
            inProgressListItem.onFirst().assert(hasText("sivap@xyz.com"))
            inProgressListItem.onLast().assert(hasText("Invitations Sent"))
        }
        Thread.sleep(500)
        verifyReferFriendScreen()
    }

    @Test
    fun testReferralScreenFetchReferralsAPiFailureFlow() {
        localRepository.setReferralsListApiSuccess(false)
        mockReferralsDataLaunchScreen()

        composeTestRule.onNodeWithTag(CIRCULAR_PROGRESS_TEST_TAG).assertIsDisplayed()
        Thread.sleep(5000)
        verifyErrorScreenDisplayed()
    }

    @Test
    fun testReferralEnrollmentApiFailureFlow() {
        localRepository.setReferralsListApiSuccess(true)
        localRepository.setReferralEnrollmentStatusApiSuccess(false)
        mockReferralsDataLaunchScreen()

        composeTestRule.onNodeWithTag(CIRCULAR_PROGRESS_TEST_TAG).assertIsDisplayed()
        Thread.sleep(5000)
        verifyErrorScreenDisplayed()
    }

    @Test
    fun testPromotionTypeAndCodeApiFailureFlow() {
        localRepository.setReferralsListApiSuccess(true)
        localRepository.setReferralEnrollmentStatusApiSuccess(true)
        localRepository.setPromotionTypeAndCodeApiSuccess(false)
        mockReferralsDataLaunchScreen()

        verifyErrorScreenDisplayed()
    }

    private fun verifyErrorScreenDisplayed() {
        composeTestRule.run {
            onNodeWithTag(TEST_TAG_TITLE_VIEW)
                .assertTextEquals("My Referrals")
                .assertIsDisplayed()
            onNodeWithTag(TestTags.TEST_TAG_ERROR_SCREEN).assertIsDisplayed()
            onNodeWithText("Something went wrong. Try again.").assertIsDisplayed()
            onNodeWithTag(TestTags.TEST_TAG_TRY_AGAIN_ERROR_SCREEN)
                .assertTextEquals("Try Again").assertIsDisplayed()
            Thread.sleep(1000)
        }
    }

    private fun verifyReferFriendScreen() {
        composeTestRule.run {
            onNodeWithText("Refer Now").assertIsDisplayed().performClick()
            Thread.sleep(500)
            onNodeWithTag(TEST_TAG_REFER_FRIEND_SCREEN).assertIsDisplayed()
            onNodeWithText("Default Referral Promotion").assertIsDisplayed()
            onNodeWithText("Invite your friends and get a voucher when they shop for the first time.").assertIsDisplayed()

            // Test Email input Field
            onNodeWithText("Add a comma after each email address.").assertIsDisplayed()

            onNodeWithTag(TEST_TAG_TEXT_FIELD_RIGHT_ICON, true).assertIsDisplayed()

            Thread.sleep(2000)
            onNodeWithText("https://rb.gy/wa6jw7?referralCode=9RCLSYJO-TEMPRP7", substring = true).assertIsDisplayed()

            onNodeWithTag(TEST_TAG_CLOSE_REFER_POPUP).performClick()
            Thread.sleep(2000)

            onNodeWithText("Refer Now").assertIsDisplayed().performClick()

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

            //below comment causing intermittent behaviour. Sometime clip board gets hanged and doesnt disappear and
            //overshadow the twitter or other click icon. Icons get blocked to click and test gets failed.
            //we can test this at faster or real test device so that things perform faster.

            onNodeWithText("Copy").assertIsDisplayed().performClick()
            assertEquals("https://rb.gy/wa6jw7?referralCode=9RCLSYJO-TEMPRP7", clipboardManager.text)
            uiDevice.pressBack()

        }
    }
}

fun getAPIService(): ApiService{
    return object : ApiService {
        override suspend fun enrollNewCustomerAsAdvocateOfPromotion(
            url: String,
            requestBody: ReferralNewEnrollmentRequestBody
        ): Response<ReferralEnrollmentResponse> {
          return Response.success("" as ReferralEnrollmentResponse)

        }

        override suspend fun enrollExistingAdvocateToPromotion(
            url: String,
            requestBody: ReferralExistingEnrollmentRequest
        ): Response<ReferralEnrollmentResponse> {
            return Response.success("" as ReferralEnrollmentResponse)
        }

        override suspend fun sendReferrals(
            url: String,
            requestBody: ReferralEventRequest
        ): Response<ReferralEventResponse> {
            return Response.success("" as ReferralEventResponse)
        }

    }
}

fun getLocalAPIService(): ReferralsLocalApiService {
    return object : ReferralsLocalApiService {
        override suspend fun fetchReferralsInfo(
            url: String,
            query: String?
        ): Response<QueryResult<ReferralEntity>> {
            return Response.success("" as QueryResult<ReferralEntity>)
        }

        override suspend fun fetchMemberReferralId(
            url: String,
            query: String?
        ): Response<QueryResult<ReferralCode>> {
            return Response.success("" as QueryResult<ReferralCode>)
        }

        override suspend fun checkIfMemberEnrolled(
            url: String,
            query: String?
        ): Response<QueryResult<ReferralEnrollmentInfo>> {

            delay(2000)
            val mockResponse = MockResponseFileReader("ReferralEnrollmentInfo.json").content
            val gson = Gson()
            var response= gson.fromJson(
                mockResponse,
                 QueryResult::class.java
            ) as QueryResult<ReferralEnrollmentInfo>

            return Response.success(response)
        }

        override suspend fun checkIfGivenPromotionIsReferralAndEnrolled(
            url: String,
            query: String?
        ): Response<QueryResult<ReferralPromotionStatusAndPromoCode>> {
            return Response.success("" as QueryResult<ReferralPromotionStatusAndPromoCode>)
        }
    }
}