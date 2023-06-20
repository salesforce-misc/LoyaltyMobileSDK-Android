package com.salesforce.loyalty.mobile.myntorewards

import android.content.Context
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderAttributes
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderDetailsResponse
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.ShippingMethod
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_ADDRESS_DETAIL
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_APP_LOGO_HOME_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_BACK_BUTTON_CHECKOUT_PAYMENT
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_BENEFITS
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CHECKOUT_FLOW_CONTAINER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CHECKOUT_PROMO_DESCRIPTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CHECKOUT_PROMO_NAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CLOSE_POPUP
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CLOSE_POPUP_PROMOTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_COLLECTION_TYPE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_EMAIL_PHONE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_ENROLLMENT_CONGRATULATIONS
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_EXPIRATION_DATE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_HOME_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_HOME_SCREEN_CONTAINER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_JOIN_BUTTON
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_JOIN_UI
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_LOGIN_UI
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_ORDER_DESCRIPTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_ORDER_IMAGE_SELCTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PAYMENT_UI_CONTAINER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PRODUCT_PRICE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROFILE_ELEMENT_CONTAINER
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMOTION_CARD
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMO_DESCRIPTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMO_ITEM
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMO_LIST
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMO_NAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_QR_CODE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RATING
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_RECENT_TRANSACTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SELECT_COLOUR_ROW
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SELECT_QUANTITY_ROW
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SHIPPING_PAYMENT_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SHIPPING_PRICE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_SIZE_SELECTION_ROW
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_TRANSACTION_LIST
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_VOUCHER_ROW
import com.salesforce.loyalty.mobile.myntorewards.utilities.ViewPagerSupport
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.*
import com.salesforce.loyalty.mobile.myntorewards.views.*
import com.salesforce.loyalty.mobile.myntorewards.views.checkout.CheckOutFlowOrderSelectScreen
import com.salesforce.loyalty.mobile.sources.loyaltyModels.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*


@RunWith(AndroidJUnit4::class)
class SampleAppUnitTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun init() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.setContent {
            Navigation(getMembershipProfileViewModel(),
                getPromotionViewModel(),
                getVoucherViewModel(),
                getOnBoardingMockViewModel(),
                getBenefitViewModel(),
                getTransactionViewModel(),
                getCheckoutFlowViewModel()
            )
        }
   /*     composeTestRule.setContent {
            CheckOutFlowOrderSelectScreen(
                NavController(appContext)
            )
        }*/

       /* loginTestRule.setContent {
            LoginForm(NavController(appContext),   openPopup = {  }){

            }
        }*/

    }

    @Test
    fun login_app_flow() {
        composeTestRule.onNodeWithContentDescription("Application Logo").assertIsDisplayed()
        app_has_swipe_images()
        verify_login_button()
    }

   /* @Test
    fun join_app_flow()
    {
        verify_join_button()
    }*/


    @OptIn(ExperimentalTestApi::class)
    private fun verify_login_button()
    {
        verifyLoginTesting()
        display_home_ui_testing()
        display_promo_popup_testing()
        offer_tab_testing()
        profile_ui_testing()
        verify_more_tab_testing()
        home_screen_extensive_testing()
    }


 /*   @Test
   fun verify_login_then_join()
    {
        composeTestRule.onNodeWithText("Already a Member? Log In").performClick()
        Thread.sleep(3000)

        composeTestRule.onNodeWithText("Not a Member?Join Now").assertIsDisplayed()
        Thread.sleep(3000)
        composeTestRule.onNodeWithText("Not a Member?Join Now").performClick()
        composeTestRule.onNodeWithTag(TEST_TAG_JOIN_UI).assertIsDisplayed()
        Thread.sleep(3000)
        composeTestRule.onNodeWithTag(TEST_TAG_CLOSE_POPUP).performClick()
        Thread.sleep(2000)
    }*/


    private fun verifyLoginTesting()
    {
        composeTestRule.onNodeWithText("Already a Member? Log In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Already a Member? Log In").performClick()
        //Thread.sleep(1000)
        composeTestRule.onNodeWithTag(TEST_TAG_LOGIN_UI).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_EMAIL_PHONE).assertIsDisplayed()
        composeTestRule.onNodeWithTag("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Log In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Log In").assertIsNotEnabled()
        Thread.sleep(1000)
        composeTestRule.onNodeWithTag(TEST_TAG_EMAIL_PHONE).performTextInput("vasanthkumar.work@gmail.com")
        composeTestRule.onNodeWithTag("Password").performTextInput("test@321")
        composeTestRule.onNodeWithText("Log In").assertIsEnabled()
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("Log In").performClick()
        Thread.sleep(2000)
    }

    private fun verify_more_tab_testing()
    {

        composeTestRule.onNodeWithContentDescription("More").performClick()
        Thread.sleep(2000)
    }
    private fun offer_tab_testing()
    {

        composeTestRule.onNodeWithContentDescription("My Offers").performClick()
        composeTestRule.onNodeWithText("My Promotions").assertIsDisplayed()
        composeTestRule.onNodeWithText("All").assertIsDisplayed()
        composeTestRule.onNodeWithText("Active").assertIsDisplayed()
        composeTestRule.onNodeWithText("Unenrolled").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("search_icon").assertIsDisplayed()

        composeTestRule.onNodeWithText("All").performClick()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMO_LIST).assertIsDisplayed()
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_PROMO_ITEM)).assertCountEquals(3)


        Thread.sleep(2000)
           composeTestRule.onNodeWithText("Active").performClick()
           composeTestRule.onNodeWithTag(TEST_TAG_PROMO_LIST).assertIsDisplayed()

           composeTestRule.onAllNodes(hasTestTag(TEST_TAG_PROMO_ITEM)).assertCountEquals(3)



        Thread.sleep(5000)
         composeTestRule.onNodeWithText("Unenrolled").performClick()
         composeTestRule.onNodeWithTag(TEST_TAG_PROMO_LIST).assertIsDisplayed()

         composeTestRule.onAllNodes(hasTestTag(TEST_TAG_PROMO_ITEM)).assertCountEquals(2)

         composeTestRule.onNodeWithText("Active").performClick()

//        composeTestRule.onNodeWithTag("promotion_popup").assertIsDisplayed()
//      composeTestRule.onNodeWithContentDescription("promotion popup image").assertIsDisplayed()
        composeTestRule.onAllNodes(hasTestTag(TEST_TAG_PROMO_ITEM)).onFirst().performClick()
        composeTestRule.onNodeWithTag(TEST_TAG_CLOSE_POPUP_PROMOTION).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMO_NAME).assertIsDisplayed()
//         composeTestRule.onNodeWithTag("detail_heading").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMO_DESCRIPTION).assertIsDisplayed()
        composeTestRule.onNodeWithTag("expiration_date").assertIsDisplayed()
        composeTestRule.onNodeWithTag("expiration_date").assertIsDisplayed()
        composeTestRule.onNodeWithText("Shop").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_CLOSE_POPUP_PROMOTION).performClick()
        //composeTestRule.onNodeWithContentDescription("close_button").performClick()


        Thread.sleep(2000)
    }
    private fun display_promo_popup_testing()
    {
        composeTestRule.onNodeWithTag(TEST_TAG_PROMOTION_CARD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMOTION_CARD).performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_CLOSE_POPUP_PROMOTION).assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_CLOSE_POPUP_PROMOTION).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMO_NAME).assertIsDisplayed()
        //   composeTestRule.onNodeWithTag("detail_heading").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMO_DESCRIPTION).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_EXPIRATION_DATE).assertIsDisplayed()
        composeTestRule.onNodeWithText("Shop").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_CLOSE_POPUP_PROMOTION).performClick()
    }
    @OptIn(ExperimentalTestApi::class)
    private fun display_home_ui_testing()
    {
        composeTestRule.onNodeWithTag(TEST_TAG_HOME_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_APP_LOGO_HOME_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMOTION_CARD).assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithContentDescription("Home").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("My Offers").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("My Profile").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("More").assertIsDisplayed()

    }
    @OptIn(ExperimentalTestApi::class)
    private fun home_screen_extensive_testing()
    {
        composeTestRule.onNodeWithContentDescription("Home").performClick()

        composeTestRule.onNodeWithText("5619.0 Points").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PROMOTION_CARD).assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_HOME_SCREEN_CONTAINER).performMouseInput { scroll(10F) }
        Thread.sleep(5000)
        composeTestRule.onNodeWithTag(TEST_TAG_VOUCHER_ROW).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_HOME_SCREEN_CONTAINER).performMouseInput { scroll(-10F) }
        checkout_ui_testing()
        Thread.sleep(5000)
    }

    @OptIn(ExperimentalTestApi::class)
    private fun checkout_ui_testing() {
        composeTestRule.onNodeWithTag(TEST_TAG_PROMOTION_CARD).performClick()
        composeTestRule.onNodeWithText("Shop").assertIsDisplayed()
        composeTestRule.onNodeWithText("Shop").performClick()

        //checkout flow
        composeTestRule.onNodeWithContentDescription("back button checkout order description screen").assertIsDisplayed()


        composeTestRule.onNodeWithTag(TEST_TAG_CHECKOUT_PROMO_NAME).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_CHECKOUT_PROMO_DESCRIPTION).assertIsDisplayed()
        composeTestRule.onNodeWithText("Details").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reviews").assertIsDisplayed()
        composeTestRule.onNodeWithText("TnC").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_RATING).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_COLLECTION_TYPE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_ORDER_DESCRIPTION).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_ORDER_IMAGE_SELCTION).assertIsDisplayed()


        composeTestRule.onNodeWithContentDescription("Order Image").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_CHECKOUT_FLOW_CONTAINER).performMouseInput { scroll(10F) }
        Thread.sleep(5000)
        composeTestRule.onNodeWithText("Select Size").assertIsDisplayed()
        composeTestRule.onNodeWithText("View Size Chart").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_SIZE_SELECTION_ROW).assertIsDisplayed()
        composeTestRule.onNodeWithText("Available Colors").assertIsDisplayed()
        composeTestRule.onNodeWithText("Quantity").assertIsDisplayed()

        composeTestRule.onNodeWithTag(TEST_TAG_SELECT_COLOUR_ROW).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_SELECT_QUANTITY_ROW).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PRODUCT_PRICE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_SHIPPING_PRICE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_CHECKOUT_FLOW_CONTAINER).performMouseInput { scroll(5F) }

        composeTestRule.onNodeWithText("Buy Now").assertIsDisplayed()
        composeTestRule.onNodeWithText("Add To Cart").assertIsDisplayed()
        composeTestRule.onNodeWithText("Buy Now").performClick()

        composeTestRule.onNodeWithTag(TEST_TAG_SHIPPING_PAYMENT_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("shipping payment back button").assertIsDisplayed()
        composeTestRule.onNodeWithText("Order Details").assertIsDisplayed()
        composeTestRule.onNodeWithText("1. Shipping").assertIsDisplayed()
        composeTestRule.onNodeWithText("2. Payment").assertIsDisplayed()
        composeTestRule.onNodeWithText("Shipping Address").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_ADDRESS_DETAIL).assertIsDisplayed()
        composeTestRule.onNodeWithText("Add New Address").assertIsDisplayed()
        composeTestRule.onNodeWithText("Edit Address").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete Address").assertIsDisplayed()
        composeTestRule.onNodeWithText("Deliver To This Address").assertIsDisplayed()
        composeTestRule.onNodeWithText("Deliver To This Address").performClick()

        composeTestRule.onNodeWithTag(TEST_TAG_PAYMENT_UI_CONTAINER).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_BACK_BUTTON_CHECKOUT_PAYMENT).performClick()
        composeTestRule.onNodeWithTag(TEST_TAG_BACK_BUTTON_CHECKOUT_PAYMENT).performClick()
        //composeTestRule.onNodeWithTag(TEST_TAG_CHECKOUT_FLOW_CONTAINER).performMouseInput { scroll(-5F) }
        //composeTestRule.onNodeWithTag(TEST_TAG_BACK_BUTTON_CHECKOUT_FIRST_SCREEN).performClick()
//        composeTestRule.onNodeWithTag(TEST_TAG_PAYMENT_UI_CONTAINER).performMouseInput { scroll(10F) }


//        composeTestRule.onNodeWithText("Confirm Order").assertIsDisplayed()
        //composeTestRule.onNodeWithTag(TEST_TAG_CONFIRM_ORDER_BUTTON).performClick()


        Thread.sleep(5000)
    }

    @OptIn(ExperimentalTestApi::class)
    private fun profile_ui_testing()
    {

        //Profile Unit Testing
        composeTestRule.onNodeWithContentDescription("My Profile").performClick()
        composeTestRule.onNodeWithText("My Profile").assertIsDisplayed()

        composeTestRule.onNodeWithText("Harrold Williamson").assertIsDisplayed()
        composeTestRule.onNodeWithText("24345678").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gold").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Profile Card Image Content Logo").assertIsDisplayed()

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
        composeTestRule.onNodeWithText("Recent").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_RECENT_TRANSACTION).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("transaction_back_button").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_PROFILE_ELEMENT_CONTAINER).performMouseInput { scroll(10F) }
        composeTestRule.onNodeWithText("Vouchers").assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_PROFILE_ELEMENT_CONTAINER).performMouseInput { scroll(10F) }
        composeTestRule.onNodeWithText("My Benefits").assertIsDisplayed()


        composeTestRule.onNodeWithTag("My Benefits click").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithContentDescription("benefit_back_button").assertIsDisplayed()
        composeTestRule.onNodeWithText("My Benefits").assertIsDisplayed()

        composeTestRule.onNodeWithTag(TEST_TAG_BENEFITS).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("benefit_back_button").performClick()


        Thread.sleep(2000)

    }
    private fun verify_join_button()
    {
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Join").assertIsDisplayed()
        composeTestRule.onNodeWithText("Join").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithTag(TEST_TAG_JOIN_UI).assertIsDisplayed()
        Thread.sleep(1000)

        //composeTestRule.onNodeWithTag("closePopup").performClick()
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
        composeTestRule.onNodeWithTag("confirmPasswordOnBoarding", true).performTextInput("Akash123#")
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_JOIN_BUTTON).assertIsEnabled()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TEST_TAG_JOIN_BUTTON).performClick()
        Thread.sleep(2000)
       composeTestRule.onNodeWithTag(TEST_TAG_ENROLLMENT_CONGRATULATIONS).assertIsDisplayed()
        Thread.sleep(2000)
    }

    private fun app_has_swipe_images()
    {
        composeTestRule.onNodeWithTag(""+ViewPagerSupport.imageID(0)).assertIsDisplayed()
        Thread.sleep(1000)
        composeTestRule.onNodeWithTag(""+ViewPagerSupport.imageID(0)).performTouchInput { swipeLeft() }
        composeTestRule.onNodeWithTag(""+ViewPagerSupport.imageID(1)).assertIsDisplayed()
        Thread.sleep(1000)
        composeTestRule.onNodeWithTag(""+ViewPagerSupport.imageID(1)).performTouchInput { swipeLeft() }
        composeTestRule.onNodeWithTag(""+ ViewPagerSupport.imageID(2)).assertIsDisplayed()
    }

}

private fun getOnBoardingMockViewModel(): OnBoardingViewModelAbstractInterface {
    return object: OnBoardingViewModelAbstractInterface {
        override val loginStatusLiveData: LiveData<LoginState>
            get() = loginStatus
        private val loginStatus = MutableLiveData<LoginState>()
        override val enrollmentStatusLiveData: LiveData<EnrollmentState>
            get() = enrollmentStatus
        private val enrollmentStatus = MutableLiveData<EnrollmentState>()
        override val logoutStateLiveData: LiveData<LogoutState>
            get() = MutableLiveData<LogoutState>()

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

    }
}

fun getMembershipProfileViewModel(): MembershipProfileViewModelInterface {
    return object: MembershipProfileViewModelInterface {
        override val membershipProfileLiveData: LiveData<MemberProfileResponse?>
            get() = membershipProfile
        private val membershipProfile = MutableLiveData<MemberProfileResponse?>()
        override val profileViewState: LiveData<MyProfileViewStates>
            get() = viewState
        private val viewState = MutableLiveData<MyProfileViewStates>()

        override fun loadProfile(context: Context, refreshRequired: Boolean) {
            viewState.postValue(MyProfileViewStates.MyProfileFetchInProgress)
            val gson = Gson()
            membershipProfile.value = gson.fromJson("{\"additionalLoyaltyProgramMemberFields\":{\"City__c\":\"Tucson\",\"Age__c\":49.0,\"Anniversary__c\":\"2015-08-22\",\"DateOfBirth__c\":\"1974-06-01\",\"Hobbies__c\":\"Watching TV\",\"Gender__c\":\"Male\",\"SeedData__c\":true,\"Income__c\":\"100001 to 150000\",\"State__c\":\"Arizona\"},\"associatedContact\":{\"contactId\":\"003B000000N5kgwIAB\",\"email\":\"haroldwilliason@sfloyaltymanagementdemo.org\",\"firstName\":\"Harrold\",\"lastName\":\"Williamson\"},\"canReceivePartnerPromotions\":false,\"canReceivePromotions\":false,\"enrollmentChannel\":\"POS\",\"enrollmentDate\":\"2021-02-25\",\"loyaltyProgramMemberId\":\"0lMB0000000TW41MAG\",\"loyaltyProgramName\":\"NTO Insider\",\"memberCurrencies\":[{\"additionalLoyaltyMemberCurrencyFields\":{},\"escrowPointsBalance\":0.0,\"expirablePoints\":0.0,\"lastAccrualProcessedDate\":\"2023-05-22T04:12:21.000Z\",\"loyaltyMemberCurrencyName\":\"Reward Points\",\"loyaltyProgramCurrencyId\":\"0lcB0000000TQlyIAG\",\"memberCurrencyId\":\"0lzB0000000TTBoIAO\",\"pointsBalance\":5619.0,\"qualifyingPointsBalanceBeforeReset\":0.0,\"totalEscrowPointsAccrued\":0.0,\"totalEscrowRolloverPoints\":0.0,\"totalPointsAccrued\":58739.0,\"totalPointsExpired\":0.0,\"totalPointsRedeemed\":53120.0},{\"additionalLoyaltyMemberCurrencyFields\":{},\"escrowPointsBalance\":0.0,\"expirablePoints\":0.0,\"lastAccrualProcessedDate\":\"2023-05-05T06:28:07.000Z\",\"loyaltyMemberCurrencyName\":\"Tier Points\",\"loyaltyProgramCurrencyId\":\"0lcB0000000TQlzIAG\",\"memberCurrencyId\":\"0lzB0000000TTBpIAO\",\"nextQualifyingPointsResetDate\":\"2024-02-25\",\"pointsBalance\":474.47,\"qualifyingPointsBalanceBeforeReset\":0.0,\"totalEscrowPointsAccrued\":0.0,\"totalEscrowRolloverPoints\":0.0,\"totalPointsAccrued\":474.47,\"totalPointsExpired\":0.0,\"totalPointsRedeemed\":0.0}],\"memberStatus\":\"Active\",\"memberTiers\":[{\"additionalLoyaltyMemberTierFields\":{},\"areTierBenefitsAssigned\":true,\"loyaltyMemberTierId\":\"0lyB0000000TQq3IAG\",\"loyaltyMemberTierName\":\"Gold\",\"tierChangeReason\":\"VIP\",\"tierChangeReasonType\":\"VIP Member\",\"tierEffectiveDate\":\"2023-05-19\",\"tierExpirationDate\":\"2024-05-31\",\"tierGroupId\":\"0ltB0000000TPMsIAO\",\"tierId\":\"0lgB00000008Rf2IAE\",\"tierSequenceNumber\":20}],\"memberType\":\"Individual\",\"membershipNumber\":\"24345678\",\"transactionJournalStatementFrequency\":\"Monthly\",\"transactionJournalStatementMethod\":\"Mail\"}\n", MemberProfileResponse::class.java)
            viewState.postValue(MyProfileViewStates.MyProfileFetchSuccess)
        }

    }
}

fun getPromotionViewModel(): MyPromotionViewModelInterface {
    return object :MyPromotionViewModelInterface{
        override val membershipPromotionLiveData: LiveData<PromotionsResponse>
            get() = membershipPromo
        private val membershipPromo =MutableLiveData<PromotionsResponse>()
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
            val promotionResponse= gson.fromJson("{\"outputParameters\":{\"outputParameters\":{\"results\":[{\"description\":\"Double point promotion on member activities to promote NTO\",\"endDate\":\"2023-06-01\",\"fulfillmentAction\":\"CREDIT_POINTS\",\"loyaltyProgramCurrency\":\"0lcB0000000TQlyIAG\",\"loyaltyPromotionType\":\"STANDARD\",\"memberEligibilityCategory\":\"Eligible\",\"promotionEnrollmentRqr\":false,\"promotionId\":\"0c8B0000000CzEoIAK\",\"promotionImageUrl\":\"https://unsplash.com/photos/C2zhShTnl5I/download?ixid\\u003dMnwxMjA3fDB8MXxzZWFyY2h8MXx8ZXZlcnl3aGVyZXxlbnwwfHx8fDE2ODM2NjE0MTY\\u0026amp;force\\u003dtrue\\u0026amp;w\\u003d640\",\"promotionName\":\"NTO Always\",\"startDate\":\"2023-01-01\",\"totalPromotionRewardPointsVal\":0},{\"description\":\"Welcome to be a new customer and take 20% off on your first order.\",\"endDate\":\"2023-06-30\",\"loyaltyPromotionType\":\"STANDARD\",\"memberEligibilityCategory\":\"EligibleButNotEnrolled\",\"promEnrollmentStartDate\":\"2023-05-01\",\"promotionEnrollmentRqr\":true,\"promotionId\":\"0c8B0000000CwWpIAK\",\"promotionImageUrl\":\"https://unsplash.com/photos/OGND72jS-HE/download?ixid\\u003dMnwxMjA3fDB8MXxzZWFyY2h8M3x8d2VsY29tZXxlbnwwfHx8fDE2ODM2NzM3OTY\\u0026amp;force\\u003dtrue\\u0026amp;w\\u003d640\",\"promotionName\":\"Welcome Promotion\",\"startDate\":\"2023-05-01\"},{\"description\":\"Spend \$500 in a month and Earn a Stellar Media Voucher.\",\"endDate\":\"2023-07-31\",\"fulfillmentAction\":\"ISSUE_VOUCHER\",\"loyaltyPromotionType\":\"CUMULATIVE\",\"memberEligibilityCategory\":\"EligibleButNotEnrolled\",\"promEnrollmentStartDate\":\"2023-05-01\",\"promotionEnrollmentEndDate\":\"2023-07-31\",\"promotionEnrollmentRqr\":true,\"promotionId\":\"0c8B0000000Cx06IAC\",\"promotionImageUrl\":\"https://cdn.pixabay.com/photo/2018/05/10/11/34/concert-3387324_1280.jpg\",\"promotionName\":\"Your next concert experience is on us!\",\"startDate\":\"2023-05-01\"},{\"description\":\"Promotion to rejuvenate gold tier with 500 reward points for purchases during promotion period\",\"endDate\":\"2024-01-01\",\"fulfillmentAction\":\"CREDIT_POINTS\",\"loyaltyProgramCurrency\":\"0lcB0000000TQlyIAG\",\"loyaltyPromotionType\":\"STANDARD\",\"maximumPromotionRewardValue\":0,\"memberEligibilityCategory\":\"Eligible\",\"promotionEnrollmentRqr\":false,\"promotionId\":\"0c8B0000000CwVrIAK\",\"promotionImageUrl\":\"https://unsplash.com/photos/_Q0dP8xiUGA/download?ixid\\u003dMnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjgzNjYxODcy\\u0026amp;force\\u003dtrue\\u0026amp;w\\u003d640\",\"promotionName\":\"Gold Tier Rejuvenation\",\"startDate\":\"2023-01-01\",\"totalPromotionRewardPointsVal\":500},{\"description\":\"Promotion on Ironman watches\",\"endDate\":\"2024-03-30\",\"fulfillmentAction\":\"ISSUE_VOUCHER\",\"loyaltyPromotionType\":\"STANDARD\",\"maximumPromotionRewardValue\":0,\"memberEligibilityCategory\":\"Eligible\",\"promotionEnrollmentRqr\":false,\"promotionId\":\"0c8B0000000CwW5IAK\",\"promotionImageUrl\":\"https://unsplash.com/photos/rkaahInFlBg/download?ixid\\u003dMnwxMjA3fDB8MXxzZWFyY2h8Mnx8aXJvbiUyMG1hbnxlbnwwfHx8fDE2ODM2MDQzMDc\\u0026amp;force\\u003dtrue\\u0026amp;w\\u003d640\",\"promotionName\":\"Iron Man Promotion\",\"startDate\":\"2022-10-01\"},{\"description\":\"Thank you for members purchasing more than \$500 in a quarter and give a discount of 10% (Surprise and Delight)\",\"endDate\":\"2024-03-31\",\"fulfillmentAction\":\"CREDIT_POINTS\",\"loyaltyProgramCurrency\":\"0lcB0000000TQlyIAG\",\"loyaltyPromotionType\":\"STANDARD\",\"maximumPromotionRewardValue\":0,\"memberEligibilityCategory\":\"Eligible\",\"promotionEnrollmentRqr\":false,\"promotionId\":\"0c8B0000000CwW7IAK\",\"promotionImageUrl\":\"https://unsplash.com/photos/IPx7J1n_xUc/download?ixid\\u003dMnwxMjA3fDB8MXxzZWFyY2h8MTJ8fGdpZnR8ZW58MHx8fHwxNjgzNjQ3OTg5\\u0026amp;force\\u003dtrue\\u0026amp;w\\u003d640\",\"promotionName\":\"NTO Surprise and Delight\",\"startDate\":\"2023-01-01\",\"totalPromotionRewardPointsVal\":0}]}}}",
                PromotionsResponse::class.java)
            membershipPromo.value= promotionResponse
            viewState.postValue(PromotionViewState.PromotionsFetchSuccess)
        }

        override fun enrollInPromotions(context: Context, promotionName: String) {

        }

        override fun unEnrollInPromotions(context: Context, promotionName: String) {

        }

    }
}

fun getVoucherViewModel(): VoucherViewModelInterface {
    return object : VoucherViewModelInterface{
        override val voucherLiveData: LiveData<List<VoucherResponse>>
            get() = vouchers

        private val vouchers = MutableLiveData<List<VoucherResponse>>()
        override val voucherViewState: LiveData<VoucherViewState>
            get() = viewState
        private val viewState = MutableLiveData<VoucherViewState>()
        override fun loadVoucher(context: Context, refreshRequired: Boolean) {
            viewState.postValue(VoucherViewState.VoucherFetchInProgress)
            val gson = Gson()
            vouchers.value= gson.fromJson("{\"voucherCount\":6,\"vouchers\":[{\"effectiveDate\":\"2020-11-20\",\"expirationDate\":\"2021-01-20\",\"faceValue\":\"30.0\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"productCategoryId\":\"0ZGB00000009B13OAE\",\"status\":\"Expired\",\"type\":\"FixedValue\",\"voucherCode\":\"CRIS4000PROM\",\"voucherDefinition\":\"Christmas and New year voucher\",\"voucherNumber\":\"00000294\"},{\"discountPercent\":\"20\",\"effectiveDate\":\"2023-05-22\",\"expirationDate\":\"2023-04-05\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"status\":\"Expired\",\"type\":\"DiscountPercentage\",\"voucherCode\":\"HLDY2006SEAS\",\"voucherDefinition\":\"Holiday season voucher\",\"voucherImageUrl\":\"https://hutl.file.force.com/sfc/servlet.shepherd/version/download/068B000000FpN3k\",\"voucherNumber\":\"00000293\"},{\"discountPercent\":\"30\",\"effectiveDate\":\"2023-05-22\",\"expirationDate\":\"2023-06-22\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"productCategoryId\":\"0ZGB00000009B13OAE\",\"status\":\"Issued\",\"type\":\"DiscountPercentage\",\"voucherCode\":\"WIN2124JACK\",\"voucherDefinition\":\"Winter Jackets voucher\",\"voucherImageUrl\":\"https://unsplash.com/photos/oEoe-qfymZQ/download?ixid\\u003dM3wxMjA3fDB8MXxzZWFyY2h8Nzc4fHx3aW50ZXIlMjBqYWNrZXRzfGVufDB8fHx8MTY4NDI4OTAyNXww\\u0026amp;force\\u003dtrue\\u0026amp;w\\u003d640\",\"voucherNumber\":\"00000296\"},{\"discountPercent\":\"20\",\"effectiveDate\":\"2023-05-05\",\"expirationDate\":\"2023-11-05\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"status\":\"Issued\",\"type\":\"DiscountPercentage\",\"voucherCode\":\"HLDY2003SEAS\",\"voucherDefinition\":\"Holiday season voucher\",\"voucherImageUrl\":\"https://hutl.file.force.com/sfc/servlet.shepherd/version/download/068B000000FpN3k\",\"voucherNumber\":\"00000007\"},{\"discountPercent\":\"20\",\"effectiveDate\":\"2023-05-19\",\"expirationDate\":\"2023-11-19\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"status\":\"Redeemed\",\"type\":\"DiscountPercentage\",\"useDate\":\"2023-04-20\",\"voucherCode\":\"HLDY2004SEAS\",\"voucherDefinition\":\"Holiday season voucher\",\"voucherImageUrl\":\"https://hutl.file.force.com/sfc/servlet.shepherd/version/download/068B000000FpN3k\",\"voucherNumber\":\"00000273\"},{\"discountPercent\":\"20\",\"effectiveDate\":\"2023-05-22\",\"expirationDate\":\"2023-11-22\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"redeemedValue\":\"50.0\",\"remainingValue\":\"-50.0\",\"status\":\"Redeemed\",\"type\":\"DiscountPercentage\",\"useDate\":\"2023-05-12\",\"voucherCode\":\"HLDY2005SEAS\",\"voucherDefinition\":\"Holiday season voucher\",\"voucherImageUrl\":\"https://hutl.file.force.com/sfc/servlet.shepherd/version/download/068B000000FpN3k\",\"voucherNumber\":\"00000283\"}]}\n",
                VoucherResult::class.java).voucherResponse
            viewState.postValue(VoucherViewState.VoucherFetchSuccess)
        }

    }
}
fun getBenefitViewModel(): BenefitViewModelInterface {
    return object :BenefitViewModelInterface
    {
        override val membershipBenefitLiveData: LiveData<List<MemberBenefit>>
            get() = membershipBenefit
        private val membershipBenefit = MutableLiveData<List<MemberBenefit>>()
        override val benefitViewState: LiveData<BenefitViewStates>
            get() = viewState
        private val viewState = MutableLiveData<BenefitViewStates>()

        override fun loadBenefits(context: Context, refreshRequired: Boolean) {
            viewState.postValue(BenefitViewStates.BenefitFetchInProgress)
            val gson = Gson()
            membershipBenefit.value=gson.fromJson("{\"memberBenefits\":[{\"benefitId\":\"0jiB0000000GoTlIAK\",\"benefitName\":\"Extended Returns (15 Days)\",\"benefitTypeName\":\"Extended Returns\",\"isActive\":true},{\"benefitId\":\"0jiB0000000GoTjIAK\",\"benefitName\":\"Free Shipping (\\u0026gt; \$200 Orders)\",\"benefitTypeName\":\"Free Shipping\",\"isActive\":true},{\"benefitId\":\"0jiB0000000GoTqIAK\",\"benefitName\":\"Exclusive Merchandise for Gold Members\",\"benefitTypeName\":\"Tier Exclusive Offers\",\"isActive\":true},{\"benefitId\":\"0jiB0000000GoTtIAK\",\"benefitName\":\"Priority Support for Gold Members\",\"benefitTypeName\":\"Customer Support\",\"isActive\":true},{\"benefitId\":\"0jiB0000000GoToIAK\",\"benefitName\":\"Complimentary Voucher for Gold Members\",\"benefitTypeName\":\"Complimentary Vouchers\",\"isActive\":true}]}\n" ,
                MemberBenefitsResponse::class.java).memberBenefits

            viewState.postValue(BenefitViewStates.BenefitFetchSuccess)
        }
    }
}

fun getTransactionViewModel(): TransactionViewModelInterface{

    return object :TransactionViewModelInterface{
        override val transactionsLiveData: LiveData<TransactionsResponse>
            get() = transactions
        private val transactions = MutableLiveData<TransactionsResponse>()
        override val transactionViewState: LiveData<TransactionViewState>
            get() = viewState
        private val viewState = MutableLiveData<TransactionViewState>()

        override fun loadTransactions(context: Context, refreshRequired: Boolean) {
            viewState.postValue(TransactionViewState.TransactionFetchInProgress)
            val gson = Gson()
            transactions.value=gson.fromJson("{\"status\":true,\"transactionJournalCount\":275,\"transactionJournals\":[{\"activityDate\":\"2023-05-22T04:19:54.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalTypeName\":\"Manual Points Adjustment\",\"pointsChange\":[{\"changeInPoints\":-20.0,\"loyaltyMemberCurrency\":\"Reward Points\"}],\"transactionJournalId\":\"0lVB0000004Yrl6MAC\",\"transactionJournalNumber\":\"00001123\"},{\"activityDate\":\"2023-05-22T04:12:19.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Purchase\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[{\"changeInPoints\":500.0,\"loyaltyMemberCurrency\":\"Reward Points\"}],\"transactionAmount\":\"207.0\",\"transactionJournalId\":\"0lVB0000004YrlUMAS\",\"transactionJournalNumber\":\"00001122\"},{\"activityDate\":\"2023-05-22T04:12:19.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Engagement Activity\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[],\"transactionAmount\":\"0.0\",\"transactionJournalId\":\"0lVB0000004YrlTMAS\",\"transactionJournalNumber\":\"00001121\"},{\"activityDate\":\"2023-05-22T04:11:53.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Purchase\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[{\"changeInPoints\":500.0,\"loyaltyMemberCurrency\":\"Reward Points\"}],\"transactionAmount\":\"207.0\",\"transactionJournalId\":\"0lVB0000004YrlPMAS\",\"transactionJournalNumber\":\"00001120\"},{\"activityDate\":\"2023-05-22T04:11:53.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Engagement Activity\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[],\"transactionAmount\":\"0.0\",\"transactionJournalId\":\"0lVB0000004YrlOMAS\",\"transactionJournalNumber\":\"00001119\"},{\"activityDate\":\"2023-05-22T04:10:43.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalTypeName\":\"Manual Points Adjustment\",\"pointsChange\":[{\"changeInPoints\":-10.0,\"loyaltyMemberCurrency\":\"Reward Points\"}],\"transactionJournalId\":\"0lVB0000004Yrl5MAC\",\"transactionJournalNumber\":\"00001118\"},{\"activityDate\":\"2023-05-22T04:10:11.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Purchase\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[{\"changeInPoints\":500.0,\"loyaltyMemberCurrency\":\"Reward Points\"}],\"transactionAmount\":\"207.0\",\"transactionJournalId\":\"0lVB0000004YrlKMAS\",\"transactionJournalNumber\":\"00001117\"},{\"activityDate\":\"2023-05-22T04:10:11.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Engagement Activity\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[],\"transactionAmount\":\"0.0\",\"transactionJournalId\":\"0lVB0000004YrlJMAS\",\"transactionJournalNumber\":\"00001116\"},{\"activityDate\":\"2023-05-22T04:09:42.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Engagement Activity\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[],\"transactionAmount\":\"0.0\",\"transactionJournalId\":\"0lVB0000004YrlFMAS\",\"transactionJournalNumber\":\"00001115\"},{\"activityDate\":\"2023-05-22T04:09:42.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Purchase\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[{\"changeInPoints\":500.0,\"loyaltyMemberCurrency\":\"Reward Points\"}],\"transactionAmount\":\"207.0\",\"transactionJournalId\":\"0lVB0000004YrlEMAS\",\"transactionJournalNumber\":\"00001114\"},{\"activityDate\":\"2023-05-22T04:09:08.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Engagement Activity\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[],\"transactionAmount\":\"0.0\",\"transactionJournalId\":\"0lVB0000004YrlAMAS\",\"transactionJournalNumber\":\"00001113\"},{\"activityDate\":\"2023-05-22T04:09:08.000Z\",\"additionalTransactionJournalAttributes\":[],\"journalSubTypeName\":\"Purchase\",\"journalTypeName\":\"Accrual\",\"pointsChange\":[{\"changeInPoints\":500.0,\"loyaltyMemberCurrency\":\"Reward Points\"}],\"transactionAmount\":\"207.0\",\"transactionJournalId\":\"0lVB0000004Yrl9MAC\",\"transactionJournalNumber\":\"00001112\"}]}\n",
                TransactionsResponse::class.java)
            viewState.postValue(TransactionViewState.TransactionFetchSuccess)
        }

    }
}

fun getCheckoutFlowViewModel(): CheckOutFlowViewModelInterface{

    return object :CheckOutFlowViewModelInterface{
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

        private val shippingDetails = MutableLiveData<List<ShippingMethod>>()


        override fun placeOrder() {
            orderID.value = "1234"
            orderPlacedStatus.value = OrderPlacedState.ORDER_PLACED_SUCCESS

        }

        override fun fetchOrderDetails(orderID: String) {
            orderDetails.value = OrderDetailsResponse(OrderAttributes("test", "test"),"1234", "1233", "")
        }

        override fun fetchShippingDetails() {

        }


    }
}
