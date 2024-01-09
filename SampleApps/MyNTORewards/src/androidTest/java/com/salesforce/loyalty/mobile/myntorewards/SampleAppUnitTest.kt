package com.salesforce.loyalty.mobile.myntorewards

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.SampleAppUnitTest.Companion.mockReceiptResponse
import com.salesforce.gamification.model.GameReward
import com.salesforce.gamification.model.GameRewardResponse
import com.salesforce.gamification.model.Games
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderAttributes
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderCreationResponse
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderDetailsResponse
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.ShippingMethod
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseResponse
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ReceiptListResponse
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.MY_PROFILE_FULL_SCREEN_HEADER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_ADDRESS_DETAIL
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_APP_LOGO_HOME_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_BACK_BUTTON_CHECKOUT_PAYMENT
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_BENEFITS
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CAMERA_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CHECKOUT_ADD_TO_CART
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CHECKOUT_DELIVER_TO_ADDRESS
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CHECKOUT_FLOW_CONTAINER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CHECKOUT_PROMO_DESCRIPTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CHECKOUT_PROMO_NAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CLOSE_POPUP_PROMOTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_COLLECTION_TYPE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CONGRATULATIONS_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_EMAIL_PHONE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_ENROLLMENT_CONGRATULATIONS
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_ERROR_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_EXPIRATION_DATE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_GAME_REWARD
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_GAME_ZONE_ITEM
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_GAME_ZONE_ITEM_EXPIRY
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_GAME_ZONE_ITEM_IMAGE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_GAME_ZONE_ITEM_TITLE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_GAME_ZONE_ITEM_TYPE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_HOME_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_HOME_SCREEN_CONTAINER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_JOIN_BUTTON
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_JOIN_UI
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_LOGIN_UI
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_MANUAL_RECEIPT_COMMENT
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_MANUAL_RECEIPT_DATE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_MANUAL_RECEIPT_NUMBER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_MANUAL_RECEIPT_POINT
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_MANUAL_REVIEW_CLOSE_POPUP_ICON
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_MANUAL_REVIEW_HEADER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_MANUAL_SUBMIT_BUTTON
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_OLD_TRANSACTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_ORDER_DESCRIPTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_ORDER_IMAGE_SELCTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PAYMENT_UI_CONTAINER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PLAYED_GAME_PUPUP
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PLAYED_GAME_PUPUP_HEADING
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PLAYED_GAME_PUPUP_MSG
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PRODUCT_PRICE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROFILE_ELEMENT_CONTAINER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMOTION_CARD
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMO_DESCRIPTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMO_ITEM
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMO_LIST
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMO_NAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_QR_CODE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RATING
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_DETAIL_BACK_BUTTON
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_DETAIL_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_FIRST_STEP_COMPLETED
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_LIST_ITEM
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_NUMBER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_SECOND_STEP_COMPLETED
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_STATUS
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_TABLE_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECEIPT_UPLOAD
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SELECT_COLOUR_ROW
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SELECT_QUANTITY_ROW
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SHIPPING_PAYMENT_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SHIPPING_PRICE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SIZE_SELECTION_ROW
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SPIN_WHEEL_BG
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SPIN_WHEEL_CIRCLE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SPIN_WHEEL_COLOUR_SEGMENT
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SPIN_WHEEL_CONTENT
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SPIN_WHEEL_FOOTER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SPIN_WHEEL_FRAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SPIN_WHEEL_HEADER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SPIN_WHEEL_POINTER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SPIN_WHEEL_TEXT
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SUBHEADER_CONGRATS_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_TRANSACTION_LIST
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_TRY_AGAIN_ERROR_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_VOUCHER_ROW
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_VOUCHER_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.ViewPagerSupport
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.*
import com.salesforce.loyalty.mobile.myntorewards.views.*
import com.salesforce.loyalty.mobile.sources.loyaltyModels.*
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import java.io.InputStreamReader
import java.lang.RuntimeException


@RunWith(AndroidJUnit4::class)
class SampleAppUnitTest {

    private lateinit var uiDevice: UiDevice

    @get:Rule
    val composeTestRule = createComposeRule()

    companion object {
        var mockReceiptResponse: String ? = null
    }
    @Before
    fun init() {
        //might needed in future
        //val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        composeTestRule.setContent {
            Navigation(
                getMembershipProfileViewModel(),
                getPromotionViewModel(),
                getVoucherViewModel(),
                getOnBoardingMockViewModel(),
                getBenefitViewModel(),
                getTransactionViewModel(),
                getCheckoutFlowViewModel(),
                getScanningViewModel(),
                getGameViewModel() as GameViewModel
            )
        }

    }

    @Test
    fun login_app_flow() {
        composeTestRule.onNodeWithContentDescription("Application Logo").assertIsDisplayed()
        verify_login_button()
    }

    private fun verify_login_button() {
        app_has_swipe_images()
        verifyLoginTesting()
        display_home_ui_testing()
        verify_gamification_testing()
        receipt_scanning_ui_testing()
        display_promo_popup_testing()
        offer_tab_testing()
        profile_ui_testing()
        verify_more_tab_testing()
        home_screen_extensive_testing()
    }

    @OptIn(ExperimentalTestApi::class)
    private fun verify_gamification_testing() {

        composeTestRule.onNodeWithText("More").performClick()
        composeTestRule.onNodeWithText("Game Zone").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Game Zone").assertIsDisplayed()
        composeTestRule.onNodeWithText("Game Zone").performClick()
        composeTestRule.onNodeWithText("Game Zone").assertIsDisplayed()
        composeTestRule.onNodeWithText("More").performClick()
        composeTestRule.onNodeWithText("Game Zone").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Game Zone").assertIsDisplayed()
        composeTestRule.onNodeWithText("Game Zone").performClick()
        composeTestRule.onNodeWithText("Game Zone").assertIsDisplayed()
        composeTestRule.onNodeWithText("Available").assertIsDisplayed()
        composeTestRule.onNodeWithText("Expired").assertIsDisplayed()
        composeTestRule.onNodeWithText("Played").assertIsDisplayed()
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM)).assertCountEquals(3)

        composeTestRule.onNodeWithText("Played").performClick()
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM)).assertCountEquals(2)
        composeTestRule.onAllNodes(hasText("Spin a Wheel")).assertCountEquals(2)
        composeTestRule.onNodeWithText("Played in the last 90 days").assertIsDisplayed()

        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_IMAGE), useUnmergedTree=true).assertCountEquals(2)
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_TITLE), useUnmergedTree=true).assertCountEquals(2)
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_TYPE),useUnmergedTree=true).assertCountEquals(2)
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_EXPIRY), useUnmergedTree=true).assertCountEquals(2)
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_REWARD), useUnmergedTree=true).assertCountEquals(2)
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_IMAGE), useUnmergedTree=true).onFirst().performClick()


        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_IMAGE), useUnmergedTree=true).onFirst().performClick()

        composeTestRule.onNodeWithText("Congratulations!").assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_PLAYED_GAME_PUPUP).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PLAYED_GAME_PUPUP_HEADING).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PLAYED_GAME_PUPUP_MSG).assertIsDisplayed()
        composeTestRule.onNodeWithText("Go to Vouchers").assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithContentDescription("played_game_popup_cd").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Close Popup").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Close Popup").performClick()
        composeTestRule.onNodeWithTag(TEST_TAG_PLAYED_GAME_PUPUP).assertIsNotDisplayed()

        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_IMAGE), useUnmergedTree=true).onFirst().performClick()
        composeTestRule.onNodeWithText("Back").assertIsDisplayed()

        composeTestRule.onNodeWithText("Back").performClick()
        composeTestRule.onNodeWithTag(TEST_TAG_PLAYED_GAME_PUPUP).assertIsNotDisplayed()
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_IMAGE), useUnmergedTree=true).onFirst().performClick()
        composeTestRule.onNodeWithText("Go to Vouchers").performClick()
        composeTestRule.onNodeWithText("Vouchers").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_VOUCHER_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Voucher_Back_Button").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Voucher_Back_Button").performClick()
        composeTestRule.onNodeWithText("Game Zone").assertIsDisplayed()
        composeTestRule.onNodeWithText("Available").assertIsDisplayed()
        composeTestRule.onNodeWithText("Played").performClick()

        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_IMAGE), useUnmergedTree=true).onLast().performClick()
        composeTestRule.onNodeWithTag(TEST_TAG_PLAYED_GAME_PUPUP).assertIsDisplayed()
        composeTestRule.onNodeWithText("Better luck next time!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Thank you for playing! Stay tuned for more offers.").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Close Popup").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Close Popup").performClick()
        composeTestRule.onNodeWithTag(TEST_TAG_PLAYED_GAME_PUPUP).assertIsNotDisplayed()

        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_IMAGE), useUnmergedTree=true).onLast().performClick()

        composeTestRule.onNodeWithText("Back").performClick()

        composeTestRule.onNodeWithText("Expired").performClick()
        composeTestRule.onNodeWithText("Expired in the last 90 days").assertIsDisplayed()
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM)).assertCountEquals(1)
        composeTestRule.onAllNodes(hasText("Spin a Wheel")).assertCountEquals(1)
        //composeTestRule.onAllNodes(hasText("Scratch a Card")).assertCountEquals(1)

        composeTestRule.onNodeWithText("Available").performClick()
        composeTestRule.onAllNodes(hasText("Spin a Wheel")).assertCountEquals(2)
        composeTestRule.onAllNodes(hasText("Scratch a Card")).assertCountEquals(1)
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_IMAGE), useUnmergedTree=true).assertCountEquals(3)
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_TITLE), useUnmergedTree=true).assertCountEquals(3)
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_TYPE),useUnmergedTree=true).assertCountEquals(3)
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_EXPIRY), useUnmergedTree=true).assertCountEquals(3)
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_IMAGE), useUnmergedTree=true).onFirst().performClick()
        composeTestRule.onNodeWithTag(TEST_TAG_SPIN_WHEEL_BG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_SPIN_WHEEL_HEADER).assertIsDisplayed()
        composeTestRule.onNodeWithText("Spin a Wheel").assertIsDisplayed()
        composeTestRule.onNodeWithText("Spin the wheel and unlock instant rewards!").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("game back button").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("game back button").performClick()
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_GAME_ZONE_SCREEN).assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_IMAGE), useUnmergedTree=true).onFirst().performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_SPIN_WHEEL_BG).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TEST_TAG_SPIN_WHEEL_CIRCLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_SPIN_WHEEL_FRAME).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_SPIN_WHEEL_COLOUR_SEGMENT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_SPIN_WHEEL_COLOUR_SEGMENT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_SPIN_WHEEL_CONTENT).assertIsDisplayed()
        Thread.sleep(2000)
//        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_SPIN_WHEEL_TEXT), useUnmergedTree=true).assertCountEquals(7)
        composeTestRule.onNodeWithTag(TEST_TAG_SPIN_WHEEL_POINTER).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("spin_wheel_pointer").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_SPIN_WHEEL_FOOTER).assertIsDisplayed()
        composeTestRule.onNodeWithText("Grab this exclusive onetime offer and win some exciting rewards.").assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_SPIN_WHEEL_POINTER).performClick()

        composeTestRule.waitUntilExactlyOneExists(hasTestTag(TestTags.TEST_TAG_GAME_PLAYED_CONFIRMATION_SCREEN), 10000)
        composeTestRule.onNodeWithText("Congratulations!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Congratulations!").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_SUBHEADER_CONGRATS_SCREEN).assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("Play More").assertIsDisplayed()
        composeTestRule.onNodeWithText("Play More").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_GAME_ZONE_SCREEN).assertIsDisplayed()
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_IMAGE), useUnmergedTree=true).onLast().performClick()
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_SCRATCH_CARD_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithText("Scratch a Card and Win").assertIsDisplayed()
        composeTestRule.onNodeWithText("Unlock instant rewards!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Unlock instant rewards!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Grab this exclusive onetime offer and win some exciting rewards.").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("game_back_button").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("game_back_button").performClick()
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_GAME_ZONE_SCREEN).assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_GAME_ZONE_ITEM_IMAGE), useUnmergedTree=true).onLast().performClick()
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_SCRATCH_CARD_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_SCRATCH_CARD).assertIsDisplayed()
        Thread.sleep(1000)
        composeTestRule.onNodeWithContentDescription("game_back_button").performClick()
        composeTestRule.onNodeWithText("Home").performClick()

        //will need in future
     /*   composeTestRule.onNodeWithTag(TestTags.TEST_TAG_SCRATCH_CARD).performTouchInput {
            longClick()
            advanceEventTime(100)
            moveTo(0, percentOffset(10f, 10f), 2000)
        }*/


    }


    private fun verifyLoginTesting() {
        composeTestRule.onNodeWithText("Already a Member? Log In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Already a Member? Log In").performClick()
        //Thread.sleep(1000)
        composeTestRule.onNodeWithTag(TEST_TAG_LOGIN_UI).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_EMAIL_PHONE).assertIsDisplayed()
        composeTestRule.onNodeWithTag("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Log In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Log In").assertIsNotEnabled()
        Thread.sleep(1000)
        composeTestRule.onNodeWithTag(TEST_TAG_EMAIL_PHONE)
            .performTextInput("vasanthkumar.work@gmail.com")
        composeTestRule.onNodeWithTag("Password").performTextInput("test@321")
        composeTestRule.onNodeWithText("Log In").assertIsEnabled()
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("Log In").performClick()
        Thread.sleep(2000)
    }

    private fun verify_more_tab_testing() {

        composeTestRule.onNodeWithText("More").performClick()
        Thread.sleep(2000)
    }

    private fun offer_tab_testing() {

        composeTestRule.onAllNodes(hasText("My Promotions")).onLast().performClick()
        composeTestRule.onNodeWithTag(TestTags.MY_PROMOTION_FULL_SCREEN_HEADER).assertIsDisplayed()
        composeTestRule.onNodeWithText("All").assertIsDisplayed()
        composeTestRule.onNodeWithText("Opted In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Available to Opt In").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("search_icon").assertIsDisplayed()

        composeTestRule.onNodeWithText("All").performClick()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMO_LIST).assertIsDisplayed()
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_PROMO_ITEM)).assertCountEquals(2)


        Thread.sleep(2000)
        composeTestRule.onNodeWithText("Opted In").performClick()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMO_LIST).assertIsDisplayed()

        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_PROMO_ITEM)).assertCountEquals(2)



        Thread.sleep(5000)
        composeTestRule.onNodeWithText("Available to Opt In").performClick()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMO_LIST).assertIsDisplayed()


        composeTestRule.onNodeWithText("Opted In").performClick()

        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_PROMO_ITEM)).onFirst().performClick()
        composeTestRule.onNodeWithTag(TEST_TAG_CLOSE_POPUP_PROMOTION).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMO_NAME).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMO_DESCRIPTION).assertIsDisplayed()
        composeTestRule.onNodeWithTag("expiration_date").assertIsDisplayed()
        composeTestRule.onNodeWithTag("expiration_date").assertIsDisplayed()
        composeTestRule.onNodeWithText("Shop").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_CLOSE_POPUP_PROMOTION).performClick()


        Thread.sleep(2000)
    }

    private fun display_promo_popup_testing() {
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Home").performClick()

        composeTestRule.onNodeWithTag(TEST_TAG_PROMOTION_CARD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMOTION_CARD).performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_CLOSE_POPUP_PROMOTION).assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_CLOSE_POPUP_PROMOTION).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMO_NAME).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMO_DESCRIPTION).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_EXPIRATION_DATE).assertIsDisplayed()
        composeTestRule.onNodeWithText("Shop").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_CLOSE_POPUP_PROMOTION).performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    private fun display_home_ui_testing() {
        composeTestRule.onNodeWithTag(TEST_TAG_HOME_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_APP_LOGO_HOME_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMOTION_CARD).assertIsDisplayed()

        Thread.sleep(2000)


        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_HOME_SCREEN_SUBHEADER_PROMOTION).assertIsDisplayed()
        composeTestRule.onNodeWithText("My Profile").assertIsDisplayed()
        composeTestRule.onNodeWithText("More").assertIsDisplayed()

    }

    @OptIn(ExperimentalTestApi::class)
    private fun receipt_scanning_ui_testing() {
        //home screen receipt scanning button
        composeTestRule.onNodeWithContentDescription("Receipt Scanning").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Receipt Scanning").performClick()

        //verifying receipt list screen
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_RECEIPT_LIST_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_SEARCH_FIELD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_RECEIPT_LIST).assertIsDisplayed()
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_RECEIPT_LIST_ITEM)).assertCountEquals(5)


        //receipt list verify back button flow
        composeTestRule.onNodeWithText("More").assertIsDisplayed()

        composeTestRule.onNodeWithText("More").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Receipts").assertIsDisplayed()
        composeTestRule.onNodeWithText("Receipts").performClick()

        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Home").performClick()

        composeTestRule.onNodeWithContentDescription("Receipt Scanning").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("New").assertIsDisplayed()

        //start camera screen flow
        composeTestRule.onNodeWithText("New").performClick()

        Thread.sleep(2000)
        //verify camera screen
        composeTestRule.onNodeWithText("Request permission").assertIsDisplayed().performClick()
        Thread.sleep(1000)
        grantRuntimePermission()
        Thread.sleep(500)

        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_CAMERA_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_CAMERA).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("gallery icon").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("camera back button").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("shutter button").assertIsDisplayed()

        Thread.sleep(2000)

        //back from camera screen
        composeTestRule.onNodeWithContentDescription("camera back button").performClick()
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_RECEIPT_LIST_SCREEN).assertIsDisplayed()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("New").performClick()
        Thread.sleep(2000)

        //continue image preview screen
        composeTestRule.onNodeWithContentDescription("shutter button").performClick()
        Thread.sleep(2000)

        //verifying image preview screen
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_IMAGE_PREVIEW_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("image preview back button")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Captured photo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Upload").assertIsDisplayed()
        composeTestRule.onNodeWithText("Try Again").assertIsDisplayed()

        Thread.sleep(1000)

        //verifying image preview back button flow
        composeTestRule.onNodeWithContentDescription("image preview back button").performClick()
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_CAMERA_SCREEN).assertIsDisplayed()
        Thread.sleep(1000)
        composeTestRule.onNodeWithContentDescription("shutter button").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_IMAGE_PREVIEW_SCREEN).assertIsDisplayed()

        //verifying image preview try again  flow
        composeTestRule.onNodeWithText("Try Again").performClick()
        verifyReceiptUploadingProgressIndicatorWithApiFailure()
        /*composeTestRule.onNodeWithTag(TEST_TAG_TRY_AGAIN_ERROR_SCREEN).performClick()
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_CAMERA_SCREEN).assertIsDisplayed()
        Thread.sleep(1000)
        composeTestRule.onNodeWithContentDescription("shutter button").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_IMAGE_PREVIEW_SCREEN).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TEST_TAG_RECEIPT_UPLOAD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_RECEIPT_UPLOAD).performClick()


        composeTestRule.onNodeWithText("Generating preview…").assertIsDisplayed()


        composeTestRule.onNodeWithText("Generating preview…").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hang in there! This may take a minute.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()

        composeTestRule.waitUntilExactlyOneExists(hasText("Submit"), 10000)*/

        verifyConfidenceFailureScenario()

        verifyPartialConfidenceScenario()

        verifyReceiptSuccessNoEligibleItemsScenario()

        verifyReceiptSuccessSubmissionFlow()

        //verifying congratulations screen
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(TEST_TAG_CONGRATULATIONS_SCREEN), 5000)
        composeTestRule.onNodeWithContentDescription("Congratulations Screen Background")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Congrats Gift Icon").assertIsDisplayed()
        composeTestRule.onNodeWithText("Your receipt is uploaded!").assertIsDisplayed()
        composeTestRule.onNode(hasText("Scan Another Receipt"))
        composeTestRule.onNodeWithText("Done").assertIsDisplayed()

        //continue congratulations screen done flow
        composeTestRule.onNodeWithText("Done").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_RECEIPT_LIST_SCREEN).assertIsDisplayed()

        Thread.sleep(2000)
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_RECEIPT_LIST_ITEM)).onFirst().performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_RECEIPT_DETAIL_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithText("Receipt Details").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_RECEIPT_NUMBER).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_RECEIPT_NUMBER).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_RECEIPT_STATUS).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_RECEIPT_TABLE).assertIsDisplayed()
        composeTestRule.onNodeWithText("Receipt Items").assertIsDisplayed()
        composeTestRule.onNodeWithText("Receipt Image").assertIsDisplayed()

       // composeTestRule.onNodeWithText("79.9 Points").assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_RECEIPT_DETAIL_BACK_BUTTON)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(TEST_TAG_RECEIPT_DETAIL_BACK_BUTTON).performClick()
        Thread.sleep(2000)

        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_RECEIPT_LIST_SCREEN).assertIsDisplayed()
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_RECEIPT_LIST_ITEM)).onLast().performClick()
        composeTestRule.onNodeWithText("Request a Manual Review").assertIsDisplayed()

        composeTestRule.onNodeWithText("Request a Manual Review").performClick()

        composeTestRule.onNodeWithTag(TEST_TAG_MANUAL_REVIEW_CLOSE_POPUP_ICON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_MANUAL_REVIEW_CLOSE_POPUP_ICON).performClick()
        composeTestRule.onNodeWithText("Request a Manual Review").assertIsDisplayed()
        composeTestRule.onNodeWithText("Request a Manual Review").performClick()
        Thread.sleep(2000)

        composeTestRule.onNode(hasText("Back"))
        composeTestRule.onNode(hasText("Back")).performClick()

        composeTestRule.onNodeWithText("Request a Manual Review").assertIsDisplayed()
        composeTestRule.onNodeWithText("Request a Manual Review").performClick()


        composeTestRule.onNodeWithTag(TEST_TAG_MANUAL_REVIEW_HEADER).assertIsDisplayed()
        composeTestRule.onNodeWithText("Comments").assertIsDisplayed()

        composeTestRule.onNodeWithTag(TEST_TAG_MANUAL_RECEIPT_NUMBER).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_MANUAL_RECEIPT_DATE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_MANUAL_RECEIPT_POINT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_MANUAL_RECEIPT_COMMENT).assertIsDisplayed()


        composeTestRule.onNodeWithText("Enter the reason for manual request…").assertIsDisplayed()

        composeTestRule.onNodeWithTag(TEST_TAG_MANUAL_SUBMIT_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_MANUAL_SUBMIT_BUTTON).performClick()
        composeTestRule.onNodeWithTag("receipt_detail_screen").assertIsDisplayed()

    }

    private fun verifyReceiptUploadingProgressIndicatorWithApiFailure() {
        composeTestRule.onNodeWithTag(TEST_TAG_CAMERA_SCREEN).assertIsDisplayed()
        Thread.sleep(1000)
        composeTestRule.onNodeWithContentDescription("shutter button").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_IMAGE_PREVIEW_SCREEN).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TEST_TAG_RECEIPT_UPLOAD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_RECEIPT_UPLOAD).performClick()
        Thread.sleep(1000)

        composeTestRule.onNodeWithText("Reading receipt information…").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hang in there! This may take a minute.").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Reading receipt information…").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_RECEIPT_FIRST_STEP_COMPLETED).assertIsDisplayed()
        Thread.sleep(1000)

        composeTestRule.onNodeWithText("Processing receipt information…").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hang in there! This may take a minute.").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Processing receipt information…").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Receipt Scanning Process Completed").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_RECEIPT_SECOND_STEP_COMPLETED).assertIsDisplayed()
        Thread.sleep(2000)

        //verifying API Failure Scenario
        mockReceiptResponse = null
        composeTestRule.onNodeWithText("Something went wrong. Try again.").assertIsDisplayed()
        Thread.sleep(1000)
    }

    @OptIn(ExperimentalTestApi::class)
    private fun verifyConfidenceFailureScenario() {
        mockReceiptResponse = "SampleAnalyzeExpenseFailureScenario.json"
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_TRY_AGAIN_ERROR_SCREEN).assertIsDisplayed()
            .performClick()

        composeTestRule.waitUntilExactlyOneExists(hasTestTag(TEST_TAG_CAMERA_SCREEN), 5000)

        composeTestRule.onNodeWithContentDescription("shutter button").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Upload").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(TEST_TAG_ERROR_SCREEN), 10000)
        composeTestRule.onNodeWithText(
            "Oops! The required information in the receipt could not be read and processed.",
            substring = true
        ).assertIsDisplayed()
        Thread.sleep(1000)
    }

    @OptIn(ExperimentalTestApi::class)
    private fun verifyPartialConfidenceScenario() {
        mockReceiptResponse = "SampleAnalyzeExpensePartialScenario.json"
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_TRY_AGAIN_ERROR_SCREEN).assertIsDisplayed()
            .performClick()

        composeTestRule.waitUntilExactlyOneExists(hasTestTag(TEST_TAG_CAMERA_SCREEN), 5000)

        composeTestRule.onNodeWithContentDescription("shutter button").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Upload").performClick()

        composeTestRule.waitUntilExactlyOneExists(hasTestTag(TEST_TAG_RECEIPT_TABLE_SCREEN), 10000)
        composeTestRule.onNodeWithText(
            "Oops! Some items in the receipt could not be read and processed.",
            substring = true
        ).assertIsDisplayed().performClick()
        Thread.sleep(1000)
    }

    @OptIn(ExperimentalTestApi::class)
    private fun verifyReceiptSuccessNoEligibleItemsScenario() {
        mockReceiptResponse = "SampleAnalyzeExpenseNoEligibleItems.json"
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_TRY_AGAIN_SCANNED_RECEIPT)
            .assertIsDisplayed().performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(TEST_TAG_CAMERA_SCREEN), 5000)
        composeTestRule.onNodeWithContentDescription("shutter button").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Upload").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasText("No Eligible Items found in the Receipt!"), 10000)

        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_ROW_STORE_DETAILS).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_RECEIPT_TABLE).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_TRY_AGAIN_SCANNED_RECEIPT)
            .assertIsDisplayed().performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    private fun verifyReceiptSuccessSubmissionFlow() {
        mockReceiptResponse = "SampleAnalyzeExpense.json"
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_TRY_AGAIN_SCANNED_RECEIPT)
            .assertIsDisplayed().performClick()

        composeTestRule.waitUntilExactlyOneExists(hasTestTag(TEST_TAG_CAMERA_SCREEN), 5000)
        composeTestRule.onNodeWithContentDescription("shutter button").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Upload").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(TEST_TAG_RECEIPT_TABLE_SCREEN), 10000)

        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_ROW_STORE_DETAILS).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TEST_TAG_RECEIPT_TABLE).assertIsDisplayed()
        composeTestRule.onNodeWithText("Receipt", true).assertIsDisplayed()

        composeTestRule.waitUntilExactlyOneExists(hasText("Submit"), 10000)
        composeTestRule.onNodeWithTag(TEST_TAG_RECEIPT_TABLE_SCREEN).assertIsDisplayed()

        //continue scan receipt screen submit receipt flow
        composeTestRule.onNodeWithText("Submit").performClick()
    }


    @OptIn(ExperimentalTestApi::class)
    private fun home_screen_extensive_testing() {
        composeTestRule.onNodeWithText("Home").performClick()

        composeTestRule.onNodeWithText("5619.0 Points").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMOTION_CARD).assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_HOME_SCREEN_CONTAINER)
            .performMouseInput { scroll(10F) }
        Thread.sleep(5000)
        composeTestRule.onNodeWithTag(TEST_TAG_VOUCHER_ROW).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_HOME_SCREEN_CONTAINER)
            .performMouseInput { scroll(-10F) }
        checkout_ui_testing()
        Thread.sleep(5000)
    }

    @OptIn(ExperimentalTestApi::class)
    private fun checkout_ui_testing() {
        composeTestRule.onNodeWithTag(TEST_TAG_PROMOTION_CARD).performClick()
        composeTestRule.onNodeWithText("Shop").assertIsDisplayed()
        composeTestRule.onNodeWithText("Shop").performClick()

        //checkout flow
        composeTestRule.onNodeWithContentDescription("back button checkout order description screen")
            .assertIsDisplayed()


        composeTestRule.onNodeWithTag(TEST_TAG_CHECKOUT_PROMO_NAME).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_CHECKOUT_PROMO_DESCRIPTION).assertIsDisplayed()
        composeTestRule.onNodeWithText("Details").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reviews").assertIsDisplayed()
        composeTestRule.onNodeWithText("T&C").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_RATING).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_COLLECTION_TYPE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_ORDER_DESCRIPTION).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_ORDER_IMAGE_SELCTION).assertIsDisplayed()


        composeTestRule.onNodeWithContentDescription("Order Image").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_CHECKOUT_FLOW_CONTAINER)
            .performMouseInput { scroll(10F) }
        Thread.sleep(5000)
        composeTestRule.onNodeWithText("Size").assertIsDisplayed()
        composeTestRule.onNodeWithText("Size Chart").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_SIZE_SELECTION_ROW).assertIsDisplayed()
        composeTestRule.onNodeWithText("Colors").assertIsDisplayed()
        composeTestRule.onNodeWithText("Quantity").assertIsDisplayed()

        composeTestRule.onNodeWithTag(TEST_TAG_SELECT_COLOUR_ROW).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_SELECT_QUANTITY_ROW).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PRODUCT_PRICE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_SHIPPING_PRICE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_CHECKOUT_FLOW_CONTAINER)
            .performMouseInput { scroll(6F) }
        Thread.sleep(3000)
        composeTestRule.onNodeWithText("Buy").assertIsDisplayed()
        composeTestRule.onNode(hasTestTag(TEST_TAG_CHECKOUT_ADD_TO_CART))
        composeTestRule.onNodeWithText("Buy").performClick()

        composeTestRule.onNodeWithTag(TEST_TAG_SHIPPING_PAYMENT_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("shipping payment back button")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Order Details").assertIsDisplayed()
        composeTestRule.onNodeWithText("1. Shipping").assertIsDisplayed()
        composeTestRule.onNodeWithText("2. Payment").assertIsDisplayed()
        composeTestRule.onNodeWithText("Address").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_ADDRESS_DETAIL).assertIsDisplayed()
        composeTestRule.onNodeWithText("Add a New Address").assertIsDisplayed()
        composeTestRule.onNodeWithText("Edit Address").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete Address").assertIsDisplayed()
        composeTestRule.onNode(hasTestTag(TEST_TAG_CHECKOUT_DELIVER_TO_ADDRESS))
        composeTestRule.onNode(hasTestTag(TEST_TAG_CHECKOUT_DELIVER_TO_ADDRESS)).performClick()

        composeTestRule.onNodeWithTag(TEST_TAG_PAYMENT_UI_CONTAINER).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_BACK_BUTTON_CHECKOUT_PAYMENT).performClick()
        composeTestRule.onNodeWithTag(TEST_TAG_BACK_BUTTON_CHECKOUT_PAYMENT).performClick()
        Thread.sleep(5000)
    }

    @OptIn(ExperimentalTestApi::class)
    private fun profile_ui_testing() {

        //Profile Unit Testing
        composeTestRule.onNodeWithText("My Profile").performClick()
        composeTestRule.onNodeWithTag(MY_PROFILE_FULL_SCREEN_HEADER).assertIsDisplayed()

        composeTestRule.onNodeWithText("Harrold Williamson").assertIsDisplayed()
        composeTestRule.onNodeWithText("Membership Number: 24345678").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gold").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Profile Card Image Content Logo")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("5619.0").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reward Points").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_QR_CODE).assertIsDisplayed()

        composeTestRule.onNodeWithText("My Transactions").assertIsDisplayed()
        composeTestRule.onNodeWithTag("My Transactions click").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_TRANSACTION_LIST).assertIsDisplayed()

        composeTestRule.onNodeWithTag("My Transactions click").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithContentDescription("transaction_back_button").assertIsDisplayed()
        composeTestRule.onNodeWithText("My Transactions").assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("One month ago").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_OLD_TRANSACTION).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("transaction_back_button").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_PROFILE_ELEMENT_CONTAINER)
            .performMouseInput { scroll(10F) }
        composeTestRule.onNodeWithText("Vouchers").assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_PROFILE_ELEMENT_CONTAINER)
            .performMouseInput { scroll(10F) }
        composeTestRule.onNodeWithText("My Benefits").assertIsDisplayed()


        composeTestRule.onNodeWithTag("My Benefits click").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithContentDescription("benefit_back_button").assertIsDisplayed()
        composeTestRule.onNodeWithText("My Benefits").assertIsDisplayed()

        composeTestRule.onNodeWithTag(TEST_TAG_BENEFITS).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("benefit_back_button").performClick()


        Thread.sleep(2000)

    }

    private fun verify_join_button() {
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Join").assertIsDisplayed()
        composeTestRule.onNodeWithText("Join").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithTag(TEST_TAG_JOIN_UI).assertIsDisplayed()
        Thread.sleep(1000)

        Thread.sleep(1000)

        composeTestRule.onNodeWithTag(TEST_TAG_JOIN_BUTTON).assertIsNotEnabled()
        Thread.sleep(1000)
        composeTestRule.onNodeWithTag("firstName").performTextInput("Akash")
        composeTestRule.onNodeWithTag("lastName").performTextInput("Agarwal")
        composeTestRule.onNodeWithTag("mobileNumber").performTextInput("8090830397")

        composeTestRule.onNodeWithTag("email").performTextInput("akash.agrawal@salesforce.com")
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag("passwordOnBoarding", true).performTextInput("Akash123#")
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag("confirmPasswordOnBoarding", true)
            .performTextInput("Akash123#")
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_JOIN_BUTTON).assertIsEnabled()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_JOIN_BUTTON).performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_ENROLLMENT_CONGRATULATIONS).assertIsDisplayed()
        Thread.sleep(2000)
    }

    private fun app_has_swipe_images() {
        composeTestRule.onNodeWithTag("" + ViewPagerSupport.imageID(0)).assertIsDisplayed()
        Thread.sleep(1000)
        composeTestRule.onNodeWithTag("" + ViewPagerSupport.imageID(0))
            .performTouchInput { swipeLeft() }
        composeTestRule.onNodeWithTag("" + ViewPagerSupport.imageID(1)).assertIsDisplayed()
        Thread.sleep(1000)
        composeTestRule.onNodeWithTag("" + ViewPagerSupport.imageID(1))
            .performTouchInput { swipeLeft() }
        composeTestRule.onNodeWithTag("" + ViewPagerSupport.imageID(2)).assertIsDisplayed()
    }

}

private fun getOnBoardingMockViewModel(): OnBoardingViewModelAbstractInterface {
    return object : OnBoardingViewModelAbstractInterface {
        override val loginStatusLiveData: LiveData<LoginState>
            get() = loginStatus
        private val loginStatus = MutableLiveData<LoginState>()
        override val enrollmentStatusLiveData: LiveData<EnrollmentState>
            get() = enrollmentStatus
        private val enrollmentStatus = MutableLiveData<EnrollmentState>()
        override val logoutStateLiveData: LiveData<LogoutState>
            get() = logoutStatus
        private val logoutStatus = MutableLiveData<LogoutState>()

        override fun resetEnrollmentStatusDefault() {
            enrollmentStatus.value = EnrollmentState.ENROLLMENT_DEFAULT_EMPTY
        }

        override fun resetLoginStatusDefault() {
            loginStatus.value = LoginState.LOGIN_DEFAULT_EMPTY
        }

        override fun loginUser(
            emailAddressText: String,
            passwordText: String,
            context: Context
        ) {

            loginStatus.value = LoginState.LOGIN_IN_PROGRESS
            loginStatus.value = LoginState.LOGIN_SUCCESS
        }

        fun enrollUser(
            firstNameText: String,
            lastNameText: String,
            mobileNumberText: String,
            emailAddressText: String,
            passwordText: String,
            confirmPasswordText: String,
            mailCheckedState: Boolean,
            tncCheckedState: Boolean,
            context: Context
        ) {
            enrollmentStatus.value = EnrollmentState.ENROLLMENT_SUCCESS
        }

        override fun logoutAndClearAllSettings(context: Context) {

        }

        override fun joinUser(email: String, context: Context) {
            enrollmentStatus.value = EnrollmentState.ENROLLMENT_SUCCESS
        }

        override fun getSelfRegisterUrl(): String {
            return ""
        }

        override fun getSelfRegisterRedirectUrl(): String {
            return ""
        }

        override fun resetLogOutDefault() {
        }

        override fun logoutAndClearAllSettingsAfterSessionExpiry(context: Context) {
            logoutStatus.value = LogoutState.LOGOUT_SUCCESS_AFTER_SESSION_EXPIRY
        }

    }
}

fun getMembershipProfileViewModel(): MembershipProfileViewModelInterface {
    return object : MembershipProfileViewModelInterface {
        override val membershipProfileLiveData: LiveData<MemberProfileResponse?>
            get() = membershipProfile
        private val membershipProfile = MutableLiveData<MemberProfileResponse?>()
        override val profileViewState: LiveData<MyProfileViewStates>
            get() = viewState
        private val viewState = MutableLiveData<MyProfileViewStates>()

        override fun loadProfile(context: Context, refreshRequired: Boolean) {
            viewState.postValue(MyProfileViewStates.MyProfileFetchInProgress)
            val gson = Gson()
            membershipProfile.value = gson.fromJson(
                "{\"additionalLoyaltyProgramMemberFields\":{\"City__c\":\"Tucson\",\"Age__c\":49.0,\"Anniversary__c\":\"2015-08-22\",\"DateOfBirth__c\":\"1974-06-01\",\"Hobbies__c\":\"Watching TV\",\"Gender__c\":\"Male\",\"SeedData__c\":true,\"Income__c\":\"100001 to 150000\",\"State__c\":\"Arizona\"},\"associatedContact\":{\"contactId\":\"003B000000N5kgwIAB\",\"email\":\"haroldwilliason@sfloyaltymanagementdemo.org\",\"firstName\":\"Harrold\",\"lastName\":\"Williamson\"},\"canReceivePartnerPromotions\":false,\"canReceivePromotions\":false,\"enrollmentChannel\":\"POS\",\"enrollmentDate\":\"2021-02-25\",\"loyaltyProgramMemberId\":\"0lMB0000000TW41MAG\",\"loyaltyProgramName\":\"NTO Insider\",\"memberCurrencies\":[{\"additionalLoyaltyMemberCurrencyFields\":{},\"escrowPointsBalance\":0.0,\"expirablePoints\":0.0,\"lastAccrualProcessedDate\":\"2023-05-22T04:12:21.000Z\",\"loyaltyMemberCurrencyName\":\"Reward Points\",\"loyaltyProgramCurrencyId\":\"0lcB0000000TQlyIAG\",\"memberCurrencyId\":\"0lzB0000000TTBoIAO\",\"pointsBalance\":5619.0,\"qualifyingPointsBalanceBeforeReset\":0.0,\"totalEscrowPointsAccrued\":0.0,\"totalEscrowRolloverPoints\":0.0,\"totalPointsAccrued\":58739.0,\"totalPointsExpired\":0.0,\"totalPointsRedeemed\":53120.0},{\"additionalLoyaltyMemberCurrencyFields\":{},\"escrowPointsBalance\":0.0,\"expirablePoints\":0.0,\"lastAccrualProcessedDate\":\"2023-05-05T06:28:07.000Z\",\"loyaltyMemberCurrencyName\":\"Tier Points\",\"loyaltyProgramCurrencyId\":\"0lcB0000000TQlzIAG\",\"memberCurrencyId\":\"0lzB0000000TTBpIAO\",\"nextQualifyingPointsResetDate\":\"2024-02-25\",\"pointsBalance\":474.47,\"qualifyingPointsBalanceBeforeReset\":0.0,\"totalEscrowPointsAccrued\":0.0,\"totalEscrowRolloverPoints\":0.0,\"totalPointsAccrued\":474.47,\"totalPointsExpired\":0.0,\"totalPointsRedeemed\":0.0}],\"memberStatus\":\"Active\",\"memberTiers\":[{\"additionalLoyaltyMemberTierFields\":{},\"areTierBenefitsAssigned\":true,\"loyaltyMemberTierId\":\"0lyB0000000TQq3IAG\",\"loyaltyMemberTierName\":\"Gold\",\"tierChangeReason\":\"VIP\",\"tierChangeReasonType\":\"VIP Member\",\"tierEffectiveDate\":\"2023-05-19\",\"tierExpirationDate\":\"2024-05-31\",\"tierGroupId\":\"0ltB0000000TPMsIAO\",\"tierId\":\"0lgB00000008Rf2IAE\",\"tierSequenceNumber\":20}],\"memberType\":\"Individual\",\"membershipNumber\":\"24345678\",\"transactionJournalStatementFrequency\":\"Monthly\",\"transactionJournalStatementMethod\":\"Mail\"}\n",
                MemberProfileResponse::class.java
            )
            viewState.postValue(MyProfileViewStates.MyProfileFetchSuccess)
        }

    }
}

fun getPromotionViewModel(): MyPromotionViewModelInterface {
    return object : MyPromotionViewModelInterface {
        override val membershipPromotionLiveData: LiveData<PromotionsResponse>
            get() = membershipPromo
        private val membershipPromo = MutableLiveData<PromotionsResponse>()
        override val promEnrollmentStatusLiveData: LiveData<PromotionEnrollmentUpdateState>
            get() = promEnrollmentStatus
        private val promEnrollmentStatus = MutableLiveData<PromotionEnrollmentUpdateState>()
        override val promotionViewState: LiveData<PromotionViewState>
            get() = viewState
        private val viewState = MutableLiveData<PromotionViewState>()

        override fun resetPromEnrollmentStatusDefault() {
            promEnrollmentStatus.value =
                PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_DEFAULT_EMPTY
        }

        override fun loadPromotions(context: Context, refreshRequired: Boolean) {
            val gson = Gson()
            val promotionResponse = gson.fromJson(
                "{\"outputParameters\":{\"outputParameters\":{\"results\":[{\"description\":\"Double point promotion on member activities to promote NTO\",\"endDate\":\"2023-06-01\",\"fulfillmentAction\":\"CREDIT_POINTS\",\"loyaltyProgramCurrency\":\"0lcB0000000TQlyIAG\",\"loyaltyPromotionType\":\"STANDARD\",\"memberEligibilityCategory\":\"Eligible\",\"promotionEnrollmentRqr\":false,\"promotionId\":\"0c8B0000000CzEoIAK\",\"promotionImageUrl\":\"https://unsplash.com/photos/C2zhShTnl5I/download?ixid\\u003dMnwxMjA3fDB8MXxzZWFyY2h8MXx8ZXZlcnl3aGVyZXxlbnwwfHx8fDE2ODM2NjE0MTY\\u0026amp;force\\u003dtrue\\u0026amp;w\\u003d640\",\"promotionName\":\"NTO Always\",\"startDate\":\"2023-01-01\",\"totalPromotionRewardPointsVal\":0},{\"description\":\"Welcome to be a new customer and take 20% off on your first order.\",\"endDate\":\"2023-06-30\",\"loyaltyPromotionType\":\"STANDARD\",\"memberEligibilityCategory\":\"EligibleButNotEnrolled\",\"promEnrollmentStartDate\":\"2023-05-01\",\"promotionEnrollmentRqr\":true,\"promotionId\":\"0c8B0000000CwWpIAK\",\"promotionImageUrl\":\"https://unsplash.com/photos/OGND72jS-HE/download?ixid\\u003dMnwxMjA3fDB8MXxzZWFyY2h8M3x8d2VsY29tZXxlbnwwfHx8fDE2ODM2NzM3OTY\\u0026amp;force\\u003dtrue\\u0026amp;w\\u003d640\",\"promotionName\":\"Welcome Promotion\",\"startDate\":\"2023-05-01\"},{\"description\":\"Spend \$500 in a month and Earn a Stellar Media Voucher.\",\"endDate\":\"2023-07-31\",\"fulfillmentAction\":\"ISSUE_VOUCHER\",\"loyaltyPromotionType\":\"CUMULATIVE\",\"memberEligibilityCategory\":\"EligibleButNotEnrolled\",\"promEnrollmentStartDate\":\"2023-05-01\",\"promotionEnrollmentEndDate\":\"2023-07-31\",\"promotionEnrollmentRqr\":true,\"promotionId\":\"0c8B0000000Cx06IAC\",\"promotionImageUrl\":\"https://cdn.pixabay.com/photo/2018/05/10/11/34/concert-3387324_1280.jpg\",\"promotionName\":\"Your next concert experience is on us!\",\"startDate\":\"2023-05-01\"},{\"description\":\"Promotion to rejuvenate gold tier with 500 reward points for purchases during promotion period\",\"endDate\":\"2024-01-01\",\"fulfillmentAction\":\"CREDIT_POINTS\",\"loyaltyProgramCurrency\":\"0lcB0000000TQlyIAG\",\"loyaltyPromotionType\":\"STANDARD\",\"maximumPromotionRewardValue\":0,\"memberEligibilityCategory\":\"Eligible\",\"promotionEnrollmentRqr\":false,\"promotionId\":\"0c8B0000000CwVrIAK\",\"promotionImageUrl\":\"https://unsplash.com/photos/_Q0dP8xiUGA/download?ixid\\u003dMnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjgzNjYxODcy\\u0026amp;force\\u003dtrue\\u0026amp;w\\u003d640\",\"promotionName\":\"Gold Tier Rejuvenation\",\"startDate\":\"2023-01-01\",\"totalPromotionRewardPointsVal\":500},{\"description\":\"Promotion on Ironman watches\",\"endDate\":\"2024-03-30\",\"fulfillmentAction\":\"ISSUE_VOUCHER\",\"loyaltyPromotionType\":\"STANDARD\",\"maximumPromotionRewardValue\":0,\"memberEligibilityCategory\":\"Eligible\",\"promotionEnrollmentRqr\":false,\"promotionId\":\"0c8B0000000CwW5IAK\",\"promotionImageUrl\":\"https://unsplash.com/photos/rkaahInFlBg/download?ixid\\u003dMnwxMjA3fDB8MXxzZWFyY2h8Mnx8aXJvbiUyMG1hbnxlbnwwfHx8fDE2ODM2MDQzMDc\\u0026amp;force\\u003dtrue\\u0026amp;w\\u003d640\",\"promotionName\":\"Gold Tier Rejuvenation\",\"startDate\":\"2022-10-01\"},{\"description\":\"Thank you for members purchasing more than \$500 in a quarter and give a discount of 10% (Surprise and Delight)\",\"endDate\":\"2024-03-31\",\"fulfillmentAction\":\"CREDIT_POINTS\",\"loyaltyProgramCurrency\":\"0lcB0000000TQlyIAG\",\"loyaltyPromotionType\":\"STANDARD\",\"maximumPromotionRewardValue\":0,\"memberEligibilityCategory\":\"Eligible\",\"promotionEnrollmentRqr\":false,\"promotionId\":\"0c8B0000000CwW7IAK\",\"promotionImageUrl\":\"https://unsplash.com/photos/IPx7J1n_xUc/download?ixid\\u003dMnwxMjA3fDB8MXxzZWFyY2h8MTJ8fGdpZnR8ZW58MHx8fHwxNjgzNjQ3OTg5\\u0026amp;force\\u003dtrue\\u0026amp;w\\u003d640\",\"promotionName\":\"NTO Surprise and Delight\",\"startDate\":\"2023-01-01\",\"totalPromotionRewardPointsVal\":0}]}}}",
                PromotionsResponse::class.java
            )
            membershipPromo.value = promotionResponse
            viewState.postValue(PromotionViewState.PromotionsFetchSuccess)
        }

        override fun enrollInPromotions(context: Context, promotionName: String) {

        }

        override fun unEnrollInPromotions(context: Context, promotionName: String) {

        }

    }
}

fun getVoucherViewModel(): VoucherViewModelInterface {
    return object : VoucherViewModelInterface {
        override val voucherLiveData: LiveData<List<VoucherResponse>>
            get() = vouchers

        private val vouchers = MutableLiveData<List<VoucherResponse>>()
        override val voucherViewState: LiveData<VoucherViewState>
            get() = viewState
        private val viewState = MutableLiveData<VoucherViewState>()
        override fun loadVoucher(context: Context, refreshRequired: Boolean) {
            viewState.postValue(VoucherViewState.VoucherFetchInProgress)
            val gson = Gson()
            vouchers.value = gson.fromJson(
                "{\"voucherCount\":6,\"vouchers\":[{\"effectiveDate\":\"2020-11-20\",\"expirationDate\":\"2021-01-20\",\"faceValue\":\"30.0\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"productCategoryId\":\"0ZGB00000009B13OAE\",\"status\":\"Expired\",\"type\":\"FixedValue\",\"voucherCode\":\"CRIS4000PROM\",\"voucherDefinition\":\"Christmas and New year voucher\",\"voucherNumber\":\"00000294\"},{\"discountPercent\":\"20\",\"effectiveDate\":\"2023-05-22\",\"expirationDate\":\"2023-04-05\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"status\":\"Expired\",\"type\":\"DiscountPercentage\",\"voucherCode\":\"HLDY2006SEAS\",\"voucherDefinition\":\"Holiday season voucher\",\"voucherImageUrl\":\"https://hutl.file.force.com/sfc/servlet.shepherd/version/download/068B000000FpN3k\",\"voucherNumber\":\"00000293\"},{\"discountPercent\":\"30\",\"effectiveDate\":\"2023-05-22\",\"expirationDate\":\"2023-06-22\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"productCategoryId\":\"0ZGB00000009B13OAE\",\"status\":\"Issued\",\"type\":\"DiscountPercentage\",\"voucherCode\":\"WIN2124JACK\",\"voucherDefinition\":\"Winter Jackets voucher\",\"voucherImageUrl\":\"https://unsplash.com/photos/oEoe-qfymZQ/download?ixid\\u003dM3wxMjA3fDB8MXxzZWFyY2h8Nzc4fHx3aW50ZXIlMjBqYWNrZXRzfGVufDB8fHx8MTY4NDI4OTAyNXww\\u0026amp;force\\u003dtrue\\u0026amp;w\\u003d640\",\"voucherNumber\":\"00000296\"},{\"discountPercent\":\"20\",\"effectiveDate\":\"2023-05-05\",\"expirationDate\":\"2023-11-05\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"status\":\"Issued\",\"type\":\"DiscountPercentage\",\"voucherCode\":\"HLDY2003SEAS\",\"voucherDefinition\":\"Holiday season voucher\",\"voucherImageUrl\":\"https://hutl.file.force.com/sfc/servlet.shepherd/version/download/068B000000FpN3k\",\"voucherNumber\":\"00000007\"},{\"discountPercent\":\"20\",\"effectiveDate\":\"2023-05-19\",\"expirationDate\":\"2023-11-19\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"status\":\"Redeemed\",\"type\":\"DiscountPercentage\",\"useDate\":\"2023-04-20\",\"voucherCode\":\"HLDY2004SEAS\",\"voucherDefinition\":\"Holiday season voucher\",\"voucherImageUrl\":\"https://hutl.file.force.com/sfc/servlet.shepherd/version/download/068B000000FpN3k\",\"voucherNumber\":\"00000273\"},{\"discountPercent\":\"20\",\"effectiveDate\":\"2023-05-22\",\"expirationDate\":\"2023-11-22\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"redeemedValue\":\"50.0\",\"remainingValue\":\"-50.0\",\"status\":\"Redeemed\",\"type\":\"DiscountPercentage\",\"useDate\":\"2023-05-12\",\"voucherCode\":\"HLDY2005SEAS\",\"voucherDefinition\":\"Holiday season voucher\",\"voucherImageUrl\":\"https://hutl.file.force.com/sfc/servlet.shepherd/version/download/068B000000FpN3k\",\"voucherNumber\":\"00000283\"}]}\n",
                VoucherResult::class.java
            ).voucherResponse
            viewState.postValue(VoucherViewState.VoucherFetchSuccess)
        }

    }
}

fun getBenefitViewModel(): BenefitViewModelInterface {
    return object : BenefitViewModelInterface {
        override val membershipBenefitLiveData: LiveData<List<MemberBenefit>>
            get() = membershipBenefit
        private val membershipBenefit = MutableLiveData<List<MemberBenefit>>()
        override val benefitViewState: LiveData<BenefitViewStates>
            get() = viewState
        private val viewState = MutableLiveData<BenefitViewStates>()

        override fun loadBenefits(context: Context, refreshRequired: Boolean) {
            viewState.postValue(BenefitViewStates.BenefitFetchInProgress)
            val gson = Gson()
            membershipBenefit.value = gson.fromJson(
                "{\"memberBenefits\":[{\"benefitId\":\"0jiB0000000GoTlIAK\",\"benefitName\":\"Extended Returns (15 Days)\",\"benefitTypeName\":\"Extended Returns\",\"isActive\":true},{\"benefitId\":\"0jiB0000000GoTjIAK\",\"benefitName\":\"Free Shipping (\\u0026gt; \$200 Orders)\",\"benefitTypeName\":\"Free Shipping\",\"isActive\":true},{\"benefitId\":\"0jiB0000000GoTqIAK\",\"benefitName\":\"Exclusive Merchandise for Gold Members\",\"benefitTypeName\":\"Tier Exclusive Offers\",\"isActive\":true},{\"benefitId\":\"0jiB0000000GoTtIAK\",\"benefitName\":\"Priority Support for Gold Members\",\"benefitTypeName\":\"Customer Support\",\"isActive\":true},{\"benefitId\":\"0jiB0000000GoToIAK\",\"benefitName\":\"Complimentary Voucher for Gold Members\",\"benefitTypeName\":\"Complimentary Vouchers\",\"isActive\":true}]}\n",
                MemberBenefitsResponse::class.java
            ).memberBenefits

            viewState.postValue(BenefitViewStates.BenefitFetchSuccess)
        }
    }
}

fun getTransactionViewModel(): TransactionViewModelInterface {

    return object : TransactionViewModelInterface {
        override val transactionsLiveData: LiveData<TransactionsResponse>
            get() = transactions
        private val transactions = MutableLiveData<TransactionsResponse>()
        override val transactionViewState: LiveData<TransactionViewState>
            get() = viewState
        private val viewState = MutableLiveData<TransactionViewState>()

        override fun loadTransactions(context: Context, refreshRequired: Boolean) {
            viewState.postValue(TransactionViewState.TransactionFetchInProgress)
            val gson = Gson()
            transactions.value = gson.fromJson(
                "{\"status\":true,\"transactionJournalCount\":275,\"transactionJournals\":[{\"activityDate\":\"2023-05-22T04:19:54.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalTypeName\":\"Manual Points Adjustment\",\"pointsChange\":[{\"changeInPoints\":-20.0,\"loyaltyMemberCurrency\":\"Reward Points\"}],\"transactionJournalId\":\"0lVB0000004Yrl6MAC\",\"transactionJournalNumber\":\"00001123\"},{\"activityDate\":\"2023-05-22T04:12:19.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Purchase\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[{\"changeInPoints\":500.0,\"loyaltyMemberCurrency\":\"Reward Points\"}],\"transactionAmount\":\"207.0\",\"transactionJournalId\":\"0lVB0000004YrlUMAS\",\"transactionJournalNumber\":\"00001122\"},{\"activityDate\":\"2023-05-22T04:12:19.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Engagement Activity\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[],\"transactionAmount\":\"0.0\",\"transactionJournalId\":\"0lVB0000004YrlTMAS\",\"transactionJournalNumber\":\"00001121\"},{\"activityDate\":\"2023-05-22T04:11:53.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Purchase\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[{\"changeInPoints\":500.0,\"loyaltyMemberCurrency\":\"Reward Points\"}],\"transactionAmount\":\"207.0\",\"transactionJournalId\":\"0lVB0000004YrlPMAS\",\"transactionJournalNumber\":\"00001120\"},{\"activityDate\":\"2023-05-22T04:11:53.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Engagement Activity\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[],\"transactionAmount\":\"0.0\",\"transactionJournalId\":\"0lVB0000004YrlOMAS\",\"transactionJournalNumber\":\"00001119\"},{\"activityDate\":\"2023-05-22T04:10:43.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalTypeName\":\"Manual Points Adjustment\",\"pointsChange\":[{\"changeInPoints\":-10.0,\"loyaltyMemberCurrency\":\"Reward Points\"}],\"transactionJournalId\":\"0lVB0000004Yrl5MAC\",\"transactionJournalNumber\":\"00001118\"},{\"activityDate\":\"2023-05-22T04:10:11.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Purchase\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[{\"changeInPoints\":500.0,\"loyaltyMemberCurrency\":\"Reward Points\"}],\"transactionAmount\":\"207.0\",\"transactionJournalId\":\"0lVB0000004YrlKMAS\",\"transactionJournalNumber\":\"00001117\"},{\"activityDate\":\"2023-05-22T04:10:11.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Engagement Activity\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[],\"transactionAmount\":\"0.0\",\"transactionJournalId\":\"0lVB0000004YrlJMAS\",\"transactionJournalNumber\":\"00001116\"},{\"activityDate\":\"2023-05-22T04:09:42.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Engagement Activity\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[],\"transactionAmount\":\"0.0\",\"transactionJournalId\":\"0lVB0000004YrlFMAS\",\"transactionJournalNumber\":\"00001115\"},{\"activityDate\":\"2023-05-22T04:09:42.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Purchase\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[{\"changeInPoints\":500.0,\"loyaltyMemberCurrency\":\"Reward Points\"}],\"transactionAmount\":\"207.0\",\"transactionJournalId\":\"0lVB0000004YrlEMAS\",\"transactionJournalNumber\":\"00001114\"},{\"activityDate\":\"2023-05-22T04:09:08.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Engagement Activity\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[],\"transactionAmount\":\"0.0\",\"transactionJournalId\":\"0lVB0000004YrlAMAS\",\"transactionJournalNumber\":\"00001113\"},{\"activityDate\":\"2023-05-22T04:09:08.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Purchase\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[{\"changeInPoints\":500.0,\"loyaltyMemberCurrency\":\"Reward Points\"}],\"transactionAmount\":\"207.0\",\"transactionJournalId\":\"0lVB0000004Yrl9MAC\",\"transactionJournalNumber\":\"00001112\"}]}\n",
                TransactionsResponse::class.java
            )
            viewState.postValue(TransactionViewState.TransactionFetchSuccess)
        }

    }
}

fun getCheckoutFlowViewModel(): CheckOutFlowViewModelInterface {

    return object : CheckOutFlowViewModelInterface {
        override val orderPlacedStatusLiveData: LiveData<OrderPlacedState>
            get() = orderPlacedStatus

        private val orderPlacedStatus = MutableLiveData<OrderPlacedState>()
        override fun resetOrderPlacedStatusDefault() {
            orderPlacedStatus.value = OrderPlacedState.ORDER_PLACED_DEFAULT_EMPTY
        }

        override val orderIDLiveData: LiveData<String>
            get() = orderID

        private var orderID = MutableLiveData<String>()

        override val orderDetailLiveData: LiveData<OrderDetailsResponse>
            get() = orderDetails

        private val orderDetails = MutableLiveData<OrderDetailsResponse>()

        override val shippingDetailsLiveData: LiveData<List<ShippingMethod>>
            get() = shippingDetails
        override val orderCreationResponseLiveData: LiveData<OrderCreationResponse>
            get() = orderCreationResponse

        private val orderCreationResponse = MutableLiveData<OrderCreationResponse>()

        private val shippingDetails = MutableLiveData<List<ShippingMethod>>()


        override fun placeOrder() {
            orderID.value = "1234"
            orderPlacedStatus.value = OrderPlacedState.ORDER_PLACED_SUCCESS

        }

        override fun fetchOrderDetails(orderID: String) {
            orderDetails.value =
                OrderDetailsResponse(OrderAttributes("test", "test"), "1234", "1233", "")
        }

        override fun fetchShippingDetails() {

        }

        override fun placeOrderAndGetParticipantReward() {
            orderCreationResponse.value = OrderCreationResponse(orderId = "1234", gameParticipantRewardId = "1234abcd")
            orderPlacedStatus.value = OrderPlacedState.ORDER_PLACED_SUCCESS
        }


    }
}

    fun getScanningViewModel(): ScanningViewModelInterface {
        return object : ScanningViewModelInterface {

            override val receiptListLiveData: LiveData<ReceiptListResponse>
                get() = receiptList

            private val receiptList = MutableLiveData<ReceiptListResponse>()

            override val receiptListViewState: LiveData<ReceiptViewState>
                get() = viewState

            private val viewState = MutableLiveData<ReceiptViewState>()
            override val scannedReceiptLiveData: LiveData<AnalyzeExpenseResponse>
                get() = scannedReceipt

            private val scannedReceipt = MutableLiveData<AnalyzeExpenseResponse>()
            override val receiptScanningViewStateLiveData: LiveData<ReceiptScanningViewState>
                get() = receiptScanningViewState

            private val receiptScanningViewState = MutableLiveData<ReceiptScanningViewState>()

            override val createTransactionJournalViewStateLiveData: LiveData<CreateTransactionJournalViewState>
                get() = createTransactionJournalViewState

            private val createTransactionJournalViewState = MutableLiveData<CreateTransactionJournalViewState>()
            override val receiptStatusUpdateViewStateLiveData: LiveData<ReceiptStatusUpdateViewState>
                get() = receiptStatusUpdateViewState

            private val receiptStatusUpdateViewState = MutableLiveData<ReceiptStatusUpdateViewState>()


            override fun getReceiptListsAPI(context: Context, membershipKey: String) {
                viewState.postValue(ReceiptViewState.ReceiptListFetchInProgressView)
                val gson = Gson()
                val mockResponse = MockResponseFileReader("SampleReceiptlist.json").content

                receiptList.value = gson.fromJson(
                    mockResponse,
                    ReceiptListResponse::class.java
                )

                viewState.postValue(ReceiptViewState.ReceiptListFetchSuccessView)
            }



            override fun getReceiptLists(context: Context, refreshRequired: Boolean) {
                getReceiptListsAPI(context, "")
            }

            override fun submitForManualReview(receiptId: String, comments: String?) {
                receiptStatusUpdateViewState.postValue(ReceiptStatusUpdateViewState.ReceiptStatusUpdateInProgress)
                Handler(getMainLooper()).postDelayed({
                    receiptStatusUpdateViewState.postValue(ReceiptStatusUpdateViewState.ReceiptStatusUpdateSuccess(null))
                }, 1000)

            }

            override fun submitForProcessing(receiptId: String) {
                createTransactionJournalViewState.postValue(CreateTransactionJournalViewState.CreateTransactionJournalInProgress)

                Handler(getMainLooper()).postDelayed({
                    createTransactionJournalViewState.postValue(CreateTransactionJournalViewState.CreateTransactionJournalSuccess)
                }, 2000)

            }

            override fun getReceiptStatus(
                receiptId: String,
                membershipNumber: String,
                maxRetryCount: Int,
                delaySeconds: Long
            ) {
                receiptStatusUpdateViewState.postValue(ReceiptStatusUpdateViewState.ReceiptStatusUpdateInProgress)

                Handler(getMainLooper()).postDelayed({
                    receiptStatusUpdateViewState.postValue(
                        ReceiptStatusUpdateViewState.ReceiptStatusUpdateSuccess(
                            "79.9"
                        )
                    )
                }, 1000)

            }

            override fun cancellingSubmission(receiptId: String) {
                cancellingSubmissionViewState.postValue(UploadRecieptCancelledViewState.UploadRecieptCancelledInProgress)

                Handler(getMainLooper()).postDelayed({
                    cancellingSubmissionViewState.postValue(UploadRecieptCancelledViewState.UploadRecieptCancelledSuccess)
                }, 2000)

            }

            override val cancellingSubmissionLiveData: LiveData<UploadRecieptCancelledViewState>
                get() = cancellingSubmissionViewState

            override fun uploadReceipt(context: Context, encodedImage: ByteArray): String? {
                receiptScanningViewState.postValue(ReceiptScanningViewState.UploadReceiptInProgress)

                Handler(getMainLooper()).postDelayed({
                    receiptScanningViewState.postValue(ReceiptScanningViewState.UploadReceiptSuccess)
                }, 1000)

                Handler(getMainLooper()).postDelayed({
                    receiptScanningViewState.postValue(ReceiptScanningViewState.ReceiptScanningInProgress)
                }, 2000)

                Handler(getMainLooper()).postDelayed({
                    mockReceiptResponse?.let {
                        val mockResponse = MockResponseFileReader(it).content
                        scannedReceipt.value = Gson().fromJson(mockResponse, AnalyzeExpenseResponse::class.java)
                        receiptScanningViewState.postValue(ReceiptScanningViewState.ReceiptScanningSuccess)
                    } ?: receiptScanningViewState.postValue(ReceiptScanningViewState.ReceiptScanningFailure(null))
                }, 3000)

                return "Upload and Scan Success"
            }

            private val cancellingSubmissionViewState = MutableLiveData<UploadRecieptCancelledViewState>()

        }
}


    fun getGameViewModel(): GameViewModelInterface{
        return object : GameViewModelInterface{

            private var rewardMutableLiveData = MutableLiveData<GameRewardResponse>()
            override val rewardLiveData: LiveData<GameRewardResponse>
                get() = rewardMutableLiveData


            private val viewState = MutableLiveData<GamesViewState>()
            override val gamesViewState: LiveData<GamesViewState>
                get() = viewState

            private val games = MutableLiveData<Games>()
            override val gamesLiveData: LiveData<Games>
                get() = games
            override val gameRewardsViewState: LiveData<GameRewardViewState>
                get() = rewardViewState

            override fun getGameRewardsFromGameParticipantRewardId(gameParticipantRewardId: String): List<GameReward> {
                return gamesLiveData.value?.gameDefinitions?.firstOrNull { gameDefinition ->
                    gameDefinition.participantGameRewards.any { partGameReward ->
                        partGameReward.gameParticipantRewardId == gameParticipantRewardId
                    }
                }?.gameRewards ?: emptyList()
            }

            private val rewardViewState = MutableLiveData<GameRewardViewState>()

            override fun getGameReward(gameParticipantRewardId: String, mock: Boolean) {

                rewardViewState.value = GameRewardViewState.GameRewardFetchInProgress
                viewState.value = GamesViewState.GamesFetchInProgress
                val gson = Gson()
                val mockResponse = MockResponseFileReader("GameRewards.json").content
                rewardMutableLiveData.postValue(gson.fromJson(
                    mockResponse,
                    GameRewardResponse::class.java
                ))

                viewState.postValue(GamesViewState.GamesFetchSuccess)
            }

            override fun getGames(context: Context, gameParticipantRewardId: String?, mock: Boolean) {
                viewState.value = GamesViewState.GamesFetchInProgress
                val gson = Gson()
                val mockResponse = MockResponseFileReader("Games.json").content
                games.value = gson.fromJson(
                    mockResponse,
                    Games::class.java
                )
                viewState.postValue(GamesViewState.GamesFetchSuccess)
            }



            override suspend fun getGameRewardResult(
                gameParticipantRewardId: String,
                mock: Boolean
            ): Result<GameRewardResponse> {
                val mockResponse= MockResponseFileReader("GameRewards.json").content
                val response =
                    Gson().fromJson(mockResponse, GameRewardResponse::class.java)

                return Result.success(response)
            }

        }
    }

fun grantRuntimePermission() {
    val instrumentation = InstrumentationRegistry.getInstrumentation()
    val allowPermission = UiDevice.getInstance(instrumentation).findObject(
        UiSelector().text(
            when {
                Build.VERSION.SDK_INT <= 28 -> "ALLOW"
                Build.VERSION.SDK_INT == 29 -> "Allow only while using the app"
                else -> "Only this time"
            }
        )
    )
    if (allowPermission.exists()) {
        allowPermission.click()
    }
}
