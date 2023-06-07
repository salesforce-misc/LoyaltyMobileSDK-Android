package com.salesforce.loyalty.mobile.myntorewards

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.utilities.ViewPagerSupport
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.MembershipProfileViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.MyPromotionViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.OnBoardingViewModelAbstractInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.VoucherViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyProfileViewStates
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.PromotionViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.VoucherViewState
import com.salesforce.loyalty.mobile.myntorewards.views.*
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


  /*  @get:Rule
    val loginTestRule = createComposeRule()*/

    @Before
    fun init() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.setContent {
            Navigation(getOnBoardingMockViewModel(),
                getMembershipProfileViewModel(),
                getPromotionViewModel(),
                getVoucherViewModel()
            )
        }
       /* loginTestRule.setContent {
            LoginForm(NavController(appContext),   openPopup = {  }){

            }

        }*/



    }

    @Test
    fun app_consist_join_Button() {


    }
    @Test
    fun app_consist_login_Button() {

        //verify_join_button()

//        composeTestRule.onNodeWithContentDescription("Application Logo").assertIsDisplayed()



        verify_login_button()
        //verify_join_button()
       //app_has_swipe_images()

    }


    @OptIn(ExperimentalTestApi::class)
    private fun verify_login_button()
    {
        composeTestRule.onNodeWithText("Already a Member? Log In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Already a Member? Log In").performClick()
        //Thread.sleep(1000)
        composeTestRule.onNodeWithTag("LoginUI").assertIsDisplayed()
        composeTestRule.onNodeWithTag("EmailID_PhoneNumber").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Log In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Log In").assertIsNotEnabled()
        Thread.sleep(1000)
        composeTestRule.onNodeWithTag("EmailID_PhoneNumber").performTextInput("vasanthkumar.work@gmail.com")
        composeTestRule.onNodeWithTag("Password").performTextInput("test@321")
        composeTestRule.onNodeWithText("Log In").assertIsEnabled()
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("Log In").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()

        composeTestRule.onNodeWithText("5619.0 Points").assertIsDisplayed()
        composeTestRule.onNodeWithTag("PromotionCard").assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag("HomeScreenElementContainer").performMouseInput { scroll(10F) }

        composeTestRule.onNodeWithTag("VoucherRowView").assertIsDisplayed()
        Thread.sleep(10000)
        //composeTestRule.onNodeWithTag("closePopup").performClick()
        //Thread.sleep(1000)
    }

/*    fun verify_login_then_join()
    {
        composeTestRule.onNodeWithText("Already a Member? Log In").performClick()
        Thread.sleep(1000)

        composeTestRule.onNodeWithText("Not a Member?Join Now").assertIsDisplayed()
        composeTestRule.onNodeWithText("Not a Member?Join Now").performClick()
        composeTestRule.onNodeWithTag("JoinUI").assertIsDisplayed()
        Thread.sleep(1000)
        composeTestRule.onNodeWithTag("closePopup").performClick()
        Thread.sleep(1000)
    }*/
    private fun verify_join_button()
    {
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Join").assertIsDisplayed()
        composeTestRule.onNodeWithText("Join").performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithTag("JoinUI").assertIsDisplayed()
        Thread.sleep(1000)

        //composeTestRule.onNodeWithTag("closePopup").performClick()
        Thread.sleep(1000)

        composeTestRule.onNodeWithTag("JoinButton").assertIsNotEnabled()
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
        composeTestRule.onNodeWithTag("JoinButton").assertIsEnabled()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag("JoinButton").performClick()
        Thread.sleep(2000)
       composeTestRule.onNodeWithTag("EnrollmentCongratsScreenTag").assertIsDisplayed()
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

   /* @Test
    fun app_consist_image() {
        composeTestRule.onNodeWithTag(""+ViewPagerSupport.imageID(0)).assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(""+ViewPagerSupport.imageID(0)).performTouchInput { swipeLeft() }
        composeTestRule.onNodeWithTag(""+ViewPagerSupport.imageID(1)).assertIsDisplayed()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(""+ViewPagerSupport.imageID(1)).performTouchInput { swipeLeft() }
        composeTestRule.onNodeWithTag(""+ViewPagerSupport.imageID(2)).assertIsDisplayed()
    }*/
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

        override fun enrollUser(
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
        override val membershipPromotionLiveData: LiveData<List<Results>>
            get() = membershipPromo
        private val membershipPromo = MutableLiveData<List<Results>>()
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
            val promotionResponse= gson.fromJson("{\"voucherCount\":6,\"vouchers\":[{\"effectiveDate\":\"2020-11-20\",\"expirationDate\":\"2021-01-20\",\"faceValue\":\"30.0\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"productCategoryId\":\"0ZGB00000009B13OAE\",\"status\":\"Expired\",\"type\":\"FixedValue\",\"voucherCode\":\"CRIS4000PROM\",\"voucherDefinition\":\"Christmas and New year voucher\",\"voucherNumber\":\"00000294\"},{\"discountPercent\":\"20\",\"effectiveDate\":\"2023-05-22\",\"expirationDate\":\"2023-04-05\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"status\":\"Expired\",\"type\":\"DiscountPercentage\",\"voucherCode\":\"HLDY2006SEAS\",\"voucherDefinition\":\"Holiday season voucher\",\"voucherImageUrl\":\"https://hutl.file.force.com/sfc/servlet.shepherd/version/download/068B000000FpN3k\",\"voucherNumber\":\"00000293\"},{\"discountPercent\":\"30\",\"effectiveDate\":\"2023-05-22\",\"expirationDate\":\"2023-06-22\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"productCategoryId\":\"0ZGB00000009B13OAE\",\"status\":\"Issued\",\"type\":\"DiscountPercentage\",\"voucherCode\":\"WIN2124JACK\",\"voucherDefinition\":\"Winter Jackets voucher\",\"voucherImageUrl\":\"https://unsplash.com/photos/oEoe-qfymZQ/download?ixid\\u003dM3wxMjA3fDB8MXxzZWFyY2h8Nzc4fHx3aW50ZXIlMjBqYWNrZXRzfGVufDB8fHx8MTY4NDI4OTAyNXww\\u0026amp;force\\u003dtrue\\u0026amp;w\\u003d640\",\"voucherNumber\":\"00000296\"},{\"discountPercent\":\"20\",\"effectiveDate\":\"2023-05-05\",\"expirationDate\":\"2023-11-05\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"status\":\"Issued\",\"type\":\"DiscountPercentage\",\"voucherCode\":\"HLDY2003SEAS\",\"voucherDefinition\":\"Holiday season voucher\",\"voucherImageUrl\":\"https://hutl.file.force.com/sfc/servlet.shepherd/version/download/068B000000FpN3k\",\"voucherNumber\":\"00000007\"},{\"discountPercent\":\"20\",\"effectiveDate\":\"2023-05-19\",\"expirationDate\":\"2023-11-19\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"status\":\"Redeemed\",\"type\":\"DiscountPercentage\",\"useDate\":\"2023-04-20\",\"voucherCode\":\"HLDY2004SEAS\",\"voucherDefinition\":\"Holiday season voucher\",\"voucherImageUrl\":\"https://hutl.file.force.com/sfc/servlet.shepherd/version/download/068B000000FpN3k\",\"voucherNumber\":\"00000273\"},{\"discountPercent\":\"20\",\"effectiveDate\":\"2023-05-22\",\"expirationDate\":\"2023-11-22\",\"isVoucherDefinitionActive\":true,\"isVoucherPartiallyRedeemable\":false,\"redeemedValue\":\"50.0\",\"remainingValue\":\"-50.0\",\"status\":\"Redeemed\",\"type\":\"DiscountPercentage\",\"useDate\":\"2023-05-12\",\"voucherCode\":\"HLDY2005SEAS\",\"voucherDefinition\":\"Holiday season voucher\",\"voucherImageUrl\":\"https://hutl.file.force.com/sfc/servlet.shepherd/version/download/068B000000FpN3k\",\"voucherNumber\":\"00000283\"}]}\n",
                PromotionsResponse::class.java)
            viewState.postValue(PromotionViewState.PromotionsFetchSuccess(promotionResponse))
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

