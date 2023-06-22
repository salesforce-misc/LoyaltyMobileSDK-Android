package com.salesforce.loyalty.mobile.myntorewards

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.checkout.CheckoutManager
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderAttributes
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderDetailsResponse
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.ShippingMethod
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.*
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class SampleAppViewModelTest {
    @get:Rule
    val rule = MainDispatcherRule()


    private lateinit var benefitViewModel: MembershipBenefitViewModel
    private lateinit var transactionViewModel: TransactionsViewModel
    private lateinit var voucherViewModel: VoucherViewModel
    private lateinit var profileViewModel: MembershipProfileViewModel
    private lateinit var myPromotionViewModel: MyPromotionViewModel
    private lateinit var checkOutFlowViewModel: CheckOutFlowViewModel
    private lateinit var onboardingScreenViewModel: OnboardingScreenViewModel

    private val loyaltyAPIManager: LoyaltyAPIManager = mockk()
    private val checkoutManager: CheckoutManager = mockk()

    private val context: Context = mockk()
    private val forceAuthManager: ForceAuthManager = mockk()

    private val member: CommunityMemberModel = mockk()

    //private val localManager: LocalFileManager = mockk()


    private val memberID = "username"
    private val memberKey = "password"
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewStates: MutableList<BenefitViewStates>
    private lateinit var tranViewStates: MutableList<TransactionViewState>
    private lateinit var voucherViewStates: MutableList<VoucherViewState>
    private lateinit var profileViewStates: MutableList<MyProfileViewStates>
    private lateinit var promoViewStates: MutableList<PromotionViewState>
    private lateinit var orderPlacedState: MutableList<OrderPlacedState>
    private lateinit var loginState: MutableList<LoginState>
    private lateinit var logoutState: MutableList<LogoutState>
    private lateinit var enrollmentState: MutableList<EnrollmentState>

    /*  @get:Rule
    val rule = InstantTaskExecutorRule()*/


    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        benefitViewModel = MembershipBenefitViewModel(loyaltyAPIManager)
        viewStates = mutableListOf()
        benefitViewModel.benefitViewState.observeForever {

            viewStates.add(it)
        }

        transactionViewModel = TransactionsViewModel(loyaltyAPIManager)
        tranViewStates = mutableListOf()
        transactionViewModel.transactionViewState.observeForever {
            tranViewStates.add(it)
        }

        voucherViewModel = VoucherViewModel(loyaltyAPIManager)
        voucherViewStates = mutableListOf()
        voucherViewModel.voucherViewState.observeForever {
            voucherViewStates.add(it)
        }

        profileViewModel = MembershipProfileViewModel(loyaltyAPIManager)
        profileViewStates = mutableListOf()
        profileViewModel.profileViewState.observeForever {
            profileViewStates.add(it)
        }
        myPromotionViewModel = MyPromotionViewModel(loyaltyAPIManager)
        promoViewStates = mutableListOf()
        myPromotionViewModel.promotionViewState.observeForever {
            promoViewStates.add(it)
        }

        checkOutFlowViewModel = CheckOutFlowViewModel(checkoutManager)
        orderPlacedState = mutableListOf()
        checkOutFlowViewModel.orderPlacedStatusLiveData.observeForever {
            orderPlacedState.add(it)
        }

        onboardingScreenViewModel = OnboardingScreenViewModel(loyaltyAPIManager, forceAuthManager)
        loginState = mutableListOf()
        onboardingScreenViewModel.loginStatusLiveData.observeForever {
            loginState.add(it)

        }
        logoutState = mutableListOf()
        onboardingScreenViewModel.logoutStateLiveData.observeForever {
            logoutState.add(it)
        }

        enrollmentState = mutableListOf()
        onboardingScreenViewModel.enrollmentStatusLiveData.observeForever {
            enrollmentState.add(it)
        }

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `for benefit failure resource, data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)

        val value = Result.failure<MemberBenefitsResponse>(Exception("HTTP 401 Unauthorized"))
        coEvery {
            loyaltyAPIManager.getMemberBenefits(any(), any())
        } returns value

        benefitViewModel.loadBenefits(context, true)

        coVerify {
            loyaltyAPIManager.getMemberBenefits(any(), any())
        }

        Assert.assertEquals(BenefitViewStates.BenefitFetchInProgress, viewStates[0])
        Assert.assertEquals(BenefitViewStates.BenefitFetchFailure, viewStates[1])
    }


    @Test
    fun `for benefit success resource, data must be available`() {

        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)


        val mockResponse = MockResponseFileReader("Benefits.json").content
        val mockMemberBenefitsResponse =
            Gson().fromJson(mockResponse, MemberBenefitsResponse::class.java)

        coEvery {
            loyaltyAPIManager.getMemberBenefits(any(), any())
        } returns Result.success(mockMemberBenefitsResponse)

        /*coEvery {
            localManager.saveData<MemberBenefitsResponse>(any(), any(), any(), any())
        } returns*/

        benefitViewModel.loadBenefits(context, true)

        coVerify {
            loyaltyAPIManager.getMemberBenefits(any(), any())
        }

        // coVerify{localManager.saveData<MemberBenefitsResponse>(any(), any(), any(), any())}

        Assert.assertEquals(BenefitViewStates.BenefitFetchInProgress, viewStates[0])
        Assert.assertEquals(BenefitViewStates.BenefitFetchSuccess, viewStates[1])
        Assert.assertEquals(
            benefitViewModel.membershipBenefitLiveData.value,
            mockMemberBenefitsResponse.memberBenefits
        )

        // verify(LocalFileManager).saveData<MemberBenefitsResponse>(context, mockMemberBenefitsResponse, membershipKey, LocalFileManager.DIRECTORY_BENEFITS)

//
//        verify {
//            LocalFileManager.saveData<MemberBenefitsResponse>(context, mockMemberBenefitsResponse, membershipKey, LocalFileManager.DIRECTORY_BENEFITS)
//        }
    }

    @Test
    fun `for benefit success resource, cache data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)

        val mockResponse = MockResponseFileReader("Benefits.json").content
        val mockMemberBenefitsResponse =
            Gson().fromJson(mockResponse, MemberBenefitsResponse::class.java)


        benefitViewModel.loadBenefits(context, false)

        Assert.assertEquals(BenefitViewStates.BenefitFetchInProgress, viewStates[0])
        Assert.assertEquals(BenefitViewStates.BenefitFetchSuccess, viewStates[1])
        Assert.assertEquals(
            benefitViewModel.membershipBenefitLiveData.value,
            mockMemberBenefitsResponse.memberBenefits
        )
    }

    @Test
    fun `for transaction failure resource, data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)

        val value = Result.failure<TransactionsResponse>(Exception("HTTP 401 Unauthorized"))
        coEvery {
            loyaltyAPIManager.getTransactions(any(), any(), any(), any(), any(), any())
        } returns value

        transactionViewModel.loadTransactions(context, true)

        coVerify {
            loyaltyAPIManager.getTransactions(any(), any(), any(), any(), any(), any())
        }

        Assert.assertEquals(TransactionViewState.TransactionFetchInProgress, tranViewStates[0])
        Assert.assertEquals(TransactionViewState.TransactionFetchFailure, tranViewStates[1])
    }

    @Test
    fun `for transaction success resource, data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)

        val mockResponse =
            Gson().fromJson(
                MockResponseFileReader("Transactions.json").content,
                TransactionsResponse::class.java
            )

        coEvery {
            loyaltyAPIManager.getTransactions(any(), any(), any(), any(), any(), any())
        } returns Result.success(mockResponse)

        transactionViewModel.loadTransactions(context, true)

        coVerify {
            loyaltyAPIManager.getTransactions(any(), any(), any(), any(), any(), any())

        }

        Assert.assertEquals(TransactionViewState.TransactionFetchInProgress, tranViewStates[0])
        Assert.assertEquals(TransactionViewState.TransactionFetchSuccess, tranViewStates[1])
        Assert.assertEquals(transactionViewModel.transactionsLiveData.value, mockResponse)

    }


    @Test
    fun `for transaction success resource, cache data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)

        val mockResponse =
            Gson().fromJson(
                MockResponseFileReader("Transactions.json").content,
                TransactionsResponse::class.java
            )


        transactionViewModel.loadTransactions(context, false)

        Assert.assertEquals(TransactionViewState.TransactionFetchInProgress, tranViewStates[0])
        Assert.assertEquals(TransactionViewState.TransactionFetchSuccess, tranViewStates[1])
        Assert.assertEquals(transactionViewModel.transactionsLiveData.value, mockResponse)
    }

    @Test
    fun `for voucher failure resource, data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)


        val value = Result.failure<VoucherResult>(Exception("HTTP 401 Unauthorized"))
        coEvery {
            loyaltyAPIManager.getVouchers(any(), any(), any(), any(), any(), any(), any())
        } returns value

        voucherViewModel.loadVoucher(context, true)

        coVerify {
            loyaltyAPIManager.getVouchers(any(), any(), any(), any(), any(), any(), any())
        }

        Assert.assertEquals(VoucherViewState.VoucherFetchInProgress, voucherViewStates[0])
        Assert.assertEquals(VoucherViewState.VoucherFetchFailure, voucherViewStates[1])
    }


    @Test
    fun `for voucher success resource, data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)

        val mockResponse =
            Gson().fromJson(
                MockResponseFileReader("GetVouchers.json").content,
                VoucherResult::class.java
            )

        coEvery {
            loyaltyAPIManager.getVouchers(any(), any(), any(), any(), any(), any(), any())
        } returns Result.success(mockResponse)

        voucherViewModel.loadVoucher(context, true)

        coVerify {
            loyaltyAPIManager.getVouchers(any(), any(), any(), any(), any(), any(), any())
        }

        Assert.assertEquals(VoucherViewState.VoucherFetchInProgress, voucherViewStates[0])
        Assert.assertEquals(VoucherViewState.VoucherFetchSuccess, voucherViewStates[1])
        Assert.assertEquals(voucherViewModel.voucherLiveData.value, mockResponse.voucherResponse)

    }

    @Test
    fun `for voucher success resource, cache data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)

        val mockResponse =
            Gson().fromJson(
                MockResponseFileReader("GetVouchers.json").content,
                VoucherResult::class.java
            )

        voucherViewModel.loadVoucher(context, false)

        Assert.assertEquals(VoucherViewState.VoucherFetchInProgress, voucherViewStates[0])
        Assert.assertEquals(VoucherViewState.VoucherFetchSuccess, voucherViewStates[1])
        Assert.assertEquals(voucherViewModel.voucherLiveData.value, mockResponse.voucherResponse)
    }


    @Test
    fun `for promotion failure resource, data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)


        val value = Result.failure<PromotionsResponse>(Exception("HTTP 401 Unauthorized"))
        coEvery {
            loyaltyAPIManager.getEligiblePromotions(any(), any())
        } returns value

        myPromotionViewModel.loadPromotions(context, true)

        coVerify {
            loyaltyAPIManager.getEligiblePromotions(any(), any())
        }

        Assert.assertEquals(PromotionViewState.PromotionFetchInProgress, promoViewStates[0])
        Assert.assertEquals(PromotionViewState.PromotionsFetchFailure, promoViewStates[1])
    }

    @Test
    fun `for promotion success resource, data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)

        val mockResponse =
            Gson().fromJson(
                MockResponseFileReader("Promotions.json").content,
                PromotionsResponse::class.java
            )

        coEvery {
            loyaltyAPIManager.getEligiblePromotions(any(), any())
        } returns Result.success(mockResponse)

        myPromotionViewModel.loadPromotions(context, true)

        coVerify {
            loyaltyAPIManager.getEligiblePromotions(any(), any())
        }


        Assert.assertEquals(PromotionViewState.PromotionFetchInProgress, promoViewStates[0])
        Assert.assertEquals(PromotionViewState.PromotionsFetchSuccess, promoViewStates[1])
        Assert.assertEquals(myPromotionViewModel.membershipPromotionLiveData.value, mockResponse)

    }

    @Test
    fun `for promotion success resource, cache data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)

        val mockResponse =
            Gson().fromJson(
                MockResponseFileReader("Promotions.json").content,
                PromotionsResponse::class.java
            )

        myPromotionViewModel.loadPromotions(context, false)

        Assert.assertEquals(PromotionViewState.PromotionFetchInProgress, promoViewStates[0])
        Assert.assertEquals(PromotionViewState.PromotionsFetchSuccess, promoViewStates[1])
        Assert.assertEquals(myPromotionViewModel.membershipPromotionLiveData.value, mockResponse)
    }


    @Test
    fun `for promotion enroll success`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)

        val mockResponse =
            Gson().fromJson(
                MockResponseFileReader("EnrollmentPromotion.json").content,
                EnrollPromotionsResponse::class.java
            )

        coEvery {
            loyaltyAPIManager.enrollInPromotions(any(), any())
        } returns Result.success(mockResponse)

        val mockPromoResponse =
            Gson().fromJson(
                MockResponseFileReader("Promotions.json").content,
                PromotionsResponse::class.java
            )

        coEvery {
            loyaltyAPIManager.getEligiblePromotions(any(), any())
        } returns Result.success(mockPromoResponse)



        myPromotionViewModel.enrollInPromotions(context, "")

        coVerify {
            loyaltyAPIManager.enrollInPromotions(any(), any())
        }
        coVerify {
            loyaltyAPIManager.getEligiblePromotions(any(), any())
        }


        Assert.assertEquals(
            PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_SUCCESS,
            myPromotionViewModel.promEnrollmentStatusLiveData.value
        )

        Assert.assertEquals(PromotionViewState.PromotionFetchInProgress, promoViewStates[0])
        Assert.assertEquals(PromotionViewState.PromotionsFetchSuccess, promoViewStates[1])

        Assert.assertEquals(
            myPromotionViewModel.membershipPromotionLiveData.value,
            mockPromoResponse
        )
    }


    @Test
    fun `for promotion enroll failure`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)

        val value = Result.failure<EnrollPromotionsResponse>(Exception("HTTP 401 Unauthorized"))

        coEvery {
            loyaltyAPIManager.enrollInPromotions(any(), any())
        } returns value

        myPromotionViewModel.enrollInPromotions(context, "")

        coVerify {
            loyaltyAPIManager.enrollInPromotions(any(), any())
        }

        Assert.assertEquals(
            PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_FAILURE,
            myPromotionViewModel.promEnrollmentStatusLiveData.value
        )

    }

    @Test
    fun `for promotion unEnroll failure`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)

        val value = Result.failure<UnenrollPromotionResponse>(Exception("HTTP 401 Unauthorized"))

        coEvery {
            loyaltyAPIManager.unEnrollPromotion(any(), any())
        } returns value

        myPromotionViewModel.unEnrollInPromotions(context, "")

        coVerify {
            loyaltyAPIManager.unEnrollPromotion(any(), any())
        }

        Assert.assertEquals(
            PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_FAILURE,
            myPromotionViewModel.promEnrollmentStatusLiveData.value
        )

    }

    @Test
    fun `for promotion UnEnroll success`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)

        val mockResponse =
            Gson().fromJson(
                MockResponseFileReader("UnenrollmentPromotion.json").content,
                UnenrollPromotionResponse::class.java
            )

        coEvery {
            loyaltyAPIManager.unEnrollPromotion(any(), any())
        } returns Result.success(mockResponse)

        val mockPromoResponse =
            Gson().fromJson(
                MockResponseFileReader("Promotions.json").content,
                PromotionsResponse::class.java
            )

        coEvery {
            loyaltyAPIManager.getEligiblePromotions(any(), any())
        } returns Result.success(mockPromoResponse)


        myPromotionViewModel.unEnrollInPromotions(context, "")

        coVerify {
            loyaltyAPIManager.unEnrollPromotion(any(), any())
        }
        coVerify {
            loyaltyAPIManager.getEligiblePromotions(any(), any())
        }


        Assert.assertEquals(
            PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_SUCCESS,
            myPromotionViewModel.promEnrollmentStatusLiveData.value
        )

        Assert.assertEquals(PromotionViewState.PromotionFetchInProgress, promoViewStates[0])
        Assert.assertEquals(PromotionViewState.PromotionsFetchSuccess, promoViewStates[1])

        Assert.assertEquals(
            myPromotionViewModel.membershipPromotionLiveData.value,
            mockPromoResponse
        )
    }


    @Test
    fun `for profile failure resource, data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)


        val value = Result.failure<MemberProfileResponse>(Exception("HTTP 401 Unauthorized"))
        coEvery {
            loyaltyAPIManager.getMemberProfile(any(), any(), any())
        } returns value

        profileViewModel.loadProfile(context, true)

        coVerify {
            loyaltyAPIManager.getMemberProfile(any(), any(), any())
        }

        Assert.assertEquals(MyProfileViewStates.MyProfileFetchInProgress, profileViewStates[0])
        Assert.assertEquals(MyProfileViewStates.MyProfileFetchFailure, profileViewStates[1])
    }


    @Test
    fun `for profile success resource, data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)

        val mockResponse =
            Gson().fromJson(
                MockResponseFileReader("Profile.json").content,
                MemberProfileResponse::class.java
            )

        coEvery {
            loyaltyAPIManager.getMemberProfile(any(), any(), any())
        } returns Result.success(mockResponse)

        profileViewModel.loadProfile(context, true)

        coVerify {
            loyaltyAPIManager.getMemberProfile(any(), any(), any())
        }

        Assert.assertEquals(MyProfileViewStates.MyProfileFetchInProgress, profileViewStates[0])
        Assert.assertEquals(MyProfileViewStates.MyProfileFetchSuccess, profileViewStates[1])
        Assert.assertEquals(profileViewModel.membershipProfileLiveData.value, mockResponse)

    }

    @Test
    fun `for profile success resource, cache data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)

        val mockResponse =
            Gson().fromJson(
                MockResponseFileReader("Profile.json").content,
                MemberProfileResponse::class.java
            )

        profileViewModel.loadProfile(context, false)

        Assert.assertEquals(MyProfileViewStates.MyProfileFetchInProgress, profileViewStates[0])
        Assert.assertEquals(MyProfileViewStates.MyProfileFetchSuccess, profileViewStates[1])
        Assert.assertEquals(profileViewModel.membershipProfileLiveData.value, mockResponse)
    }

    @Test
    fun `for checkout flow order place failure`() {
        val value = Result.failure<String>(Exception("HTTP 401 Unauthorized"))
        coEvery {
            checkoutManager.createOrder()
        } returns value

        checkOutFlowViewModel.placeOrder()

        coVerify {
            checkoutManager.createOrder()
        }

        Assert.assertEquals(OrderPlacedState.ORDER_PLACED_FAILURE, orderPlacedState[0])
    }

    @Test
    fun `for checkout flow order place success`() {
        coEvery {
            checkoutManager.createOrder()
        } returns Result.success("MockOrderID")

        checkOutFlowViewModel.placeOrder()

        coVerify {
            checkoutManager.createOrder()
        }

        Assert.assertEquals(OrderPlacedState.ORDER_PLACED_SUCCESS, orderPlacedState[0])
        Assert.assertEquals(checkOutFlowViewModel.orderIDLiveData.value, "MockOrderID")
    }

    @Test
    fun `for checkout flow fetch order detail success`() {
        coEvery {
            checkoutManager.getOrderDetails(any())
        } returns Result.success(
            OrderDetailsResponse(
                OrderAttributes("test", "test"),
                "1234",
                "1233",
                ""
            )
        )

        checkOutFlowViewModel.fetchOrderDetails("MockOrderID")

        coVerify {
            checkoutManager.getOrderDetails(any())
        }

        Assert.assertEquals(
            checkOutFlowViewModel.orderDetailLiveData.value,
            OrderDetailsResponse(OrderAttributes("test", "test"), "1234", "1233", "")
        )
    }

    @Test
    fun `for checkout flow fetch order detail failure`() {
        val value = Result.failure<OrderDetailsResponse>(Exception("HTTP 401 Unauthorized"))
        coEvery {
            checkoutManager.getOrderDetails(any())
        } returns value

        checkOutFlowViewModel.fetchOrderDetails("MockOrderID")

        coVerify {
            checkoutManager.getOrderDetails(any())
        }
    }

    @Test
    fun `for checkout flow fetch shipping detail success`() {
        val shippingMethod: List<ShippingMethod> = mutableListOf()
        coEvery {
            checkoutManager.getShippingMethods()
        } returns Result.success(shippingMethod)
        checkOutFlowViewModel.fetchShippingDetails()
        coVerify {
            checkoutManager.getShippingMethods()
        }
        Assert.assertEquals(checkOutFlowViewModel.shippingDetailsLiveData.value, shippingMethod)
    }

    @Test
    fun `for login failed access token null`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)


        val value = "dummyAccessToken"

        coEvery {
            forceAuthManager.getConnectedApp()
        } returns ConnectedApp("", "", "", "", "", "", "", "")

        coEvery {
            forceAuthManager.authenticate(any(), any(), any(), any(), any())
        } returns null

        onboardingScreenViewModel.loginUser("", "", context)

        coVerify {
            forceAuthManager.authenticate(any(), any(), any(), any(), any())
        }

        Assert.assertEquals(LoginState.LOGIN_IN_PROGRESS, loginState[0])
        Assert.assertEquals(LoginState.LOGIN_FAILURE, loginState[1])
    }

    @Test
    fun `for login failed member profile fetch error`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)


        val value = "dummyAccessToken"

        coEvery {
            forceAuthManager.getConnectedApp()
        } returns ConnectedApp("", "", "", "", "", "", "", "")

        coEvery {
            forceAuthManager.authenticate(any(), any(), any(), any(), any())
        } returns value


        val profileResponseValue =
            Result.failure<MemberProfileResponse>(Exception("HTTP 401 Unauthorized"))
        coEvery {
            loyaltyAPIManager.getMemberProfile(any(), any(), any())
        } returns profileResponseValue

        onboardingScreenViewModel.loginUser("", "", context)

        coVerify {
            forceAuthManager.authenticate(any(), any(), any(), any(), any())
        }


        coVerify {
            loyaltyAPIManager.getMemberProfile(any(), any(), any())
        }

        Assert.assertEquals(LoginState.LOGIN_IN_PROGRESS, loginState[0])
        Assert.assertEquals(LoginState.LOGIN_FAILURE, loginState[1])
    }

    @Test
    fun `for login failed member enrollment required error`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)


        val value = "dummyAccessToken"

        coEvery {
            forceAuthManager.getConnectedApp()
        } returns ConnectedApp("", "", "", "", "", "", "", "")

        coEvery {
            forceAuthManager.authenticate(any(), any(), any(), any(), any())
        } returns value


        val profileResponseValue =
            Result.failure<MemberProfileResponse>(Exception("HTTP 500 Internal Server Error"))
        coEvery {
            loyaltyAPIManager.getMemberProfile(any(), any(), any())
        } returns profileResponseValue

        onboardingScreenViewModel.loginUser("", "", context)

        coVerify {
            forceAuthManager.authenticate(any(), any(), any(), any(), any())
        }


        coVerify {
            loyaltyAPIManager.getMemberProfile(any(), any(), any())
        }

        Assert.assertEquals(LoginState.LOGIN_IN_PROGRESS, loginState[0])
        Assert.assertEquals(LoginState.LOGIN_SUCCESS_ENROLLMENT_REQUIRED, loginState[1])
    }


    @Test
    fun `for login success`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content


        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)


        val value = "dummyAccessToken"

        coEvery {
            forceAuthManager.getConnectedApp()
        } returns ConnectedApp("", "", "", "", "", "", "", "")

        coEvery {
            forceAuthManager.authenticate(any(), any(), any(), any(), any())
        } returns value

        val profileResponseValue = Result.success(
            Gson().fromJson(
                MockResponseFileReader("Profile.json").content,
                MemberProfileResponse::class.java
            )
        )

        coEvery {
            loyaltyAPIManager.getMemberProfile(any(), any(), any())
        } returns profileResponseValue

        onboardingScreenViewModel.loginUser("", "", context)

        coVerify {
            forceAuthManager.authenticate(any(), any(), any(), any(), any())
        }

        coVerify {
            sharedPrefs.edit().putString(AppConstants.KEY_COMMUNITY_MEMBER, any())
        }

        coVerify {
            loyaltyAPIManager.getMemberProfile(any(), any(), any())
        }

        Assert.assertEquals(LoginState.LOGIN_IN_PROGRESS, loginState[0])
        Assert.assertEquals(LoginState.LOGIN_SUCCESS, loginState[1])
    }


    @Test
    fun `for logout success`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content

        mockkObject(ForceAuthEncryptedPreference)
        mockkObject(ForceConnectedAppEncryptedPreference)
        every { ForceAuthEncryptedPreference.clearAll(context) } returns Unit
        every { ForceConnectedAppEncryptedPreference.clearAll(context) } returns Unit

        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)
        coEvery {
            forceAuthManager.revokeAccessToken()
        } returns Unit

        onboardingScreenViewModel.logoutAndClearAllSettings(context)

        coVerify {
            forceAuthManager.revokeAccessToken()
        }

        coVerify {
            ForceAuthEncryptedPreference.clearAll(context)
        }
        coVerify {
            ForceConnectedAppEncryptedPreference.clearAll(context)
        }

        Assert.assertEquals(LogoutState.LOGOUT_IN_PROGRESS, logoutState[0])
        Assert.assertEquals(LogoutState.LOGOUT_SUCCESS, logoutState[1])


    }

    @Test
    fun `for join user failure enrollment failed`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content


        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)
        coEvery {
            forceAuthManager.getFirstNameLastNameFromContactEmail(any())
        } returns ContactRecord(Attributes1("", ""), "", "", "")

        coEvery {
            loyaltyAPIManager.postEnrollment(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Result.failure(Exception("HTTP 401 Unauthorized"))

        onboardingScreenViewModel.joinUser("", context)

        coVerify {
            forceAuthManager.getFirstNameLastNameFromContactEmail(any())
        }

        coVerify {
            loyaltyAPIManager.postEnrollment(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }


        Assert.assertEquals(EnrollmentState.ENROLLMENT_IN_PROGRESS, enrollmentState[0])
        Assert.assertEquals(EnrollmentState.ENROLLMENT_FAILURE, enrollmentState[1])


    }

    @Test
    fun `for join user failure enrollment passed profile failure`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content


        val mockResponseEnroll =
            Gson().fromJson(
                MockResponseFileReader("Enrollment.json").content,
                EnrollmentResponse::class.java
            )


        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)
        coEvery {
            forceAuthManager.getFirstNameLastNameFromContactEmail(any())
        } returns ContactRecord(Attributes1("", ""), "", "", "")

        coEvery {
            loyaltyAPIManager.postEnrollment(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Result.success(mockResponseEnroll)


        val value = Result.failure<MemberProfileResponse>(Exception("HTTP 401 Unauthorized"))
        coEvery {
            loyaltyAPIManager.getMemberProfile(any(), any(), any())
        } returns value

        onboardingScreenViewModel.joinUser("", context)

        coVerify {
            forceAuthManager.getFirstNameLastNameFromContactEmail(any())
        }

        coVerify {
            loyaltyAPIManager.postEnrollment(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }

        coVerify {
            loyaltyAPIManager.getMemberProfile(any(), any(), any())
        }

        Assert.assertEquals(EnrollmentState.ENROLLMENT_IN_PROGRESS, enrollmentState[0])
        Assert.assertEquals(EnrollmentState.ENROLLMENT_FAILURE, enrollmentState[1])


    }

    @Test
    fun `for join user success enrollment passed profile passed`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content


        val mockResponseEnroll =
            Gson().fromJson(
                MockResponseFileReader("Enrollment.json").content,
                EnrollmentResponse::class.java
            )


        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)
        coEvery {
            forceAuthManager.getFirstNameLastNameFromContactEmail(any())
        } returns ContactRecord(Attributes1("", ""), "", "", "")

        coEvery {
            loyaltyAPIManager.postEnrollment(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Result.success(mockResponseEnroll)




        coEvery {
            loyaltyAPIManager.getMemberProfile(any(), any(), any())
        } returns Result.success(
            Gson().fromJson(
                MockResponseFileReader("Profile.json").content,
                MemberProfileResponse::class.java
            )
        )

        onboardingScreenViewModel.joinUser("", context)

        coVerify {
            forceAuthManager.getFirstNameLastNameFromContactEmail(any())
        }

        coVerify {
            loyaltyAPIManager.postEnrollment(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }

        coVerify {
            loyaltyAPIManager.getMemberProfile(any(), any(), any())
        }

        coVerify {
            sharedPrefs.edit().putString(AppConstants.KEY_COMMUNITY_MEMBER, any())
        }


        Assert.assertEquals(EnrollmentState.ENROLLMENT_IN_PROGRESS, enrollmentState[0])
        Assert.assertEquals(EnrollmentState.ENROLLMENT_SUCCESS, enrollmentState[1])

    }

    @Test
    fun `for reset login status`() {
        onboardingScreenViewModel.resetLoginStatusDefault()
        Assert.assertEquals(LoginState.LOGIN_DEFAULT_EMPTY, loginState[0])

    }
    @Test
    fun `for reset enrollment status`() {
        onboardingScreenViewModel.resetEnrollmentStatusDefault()
        Assert.assertEquals(EnrollmentState.ENROLLMENT_DEFAULT_EMPTY, enrollmentState[0])

    }
    @Test
    fun `for getSelfRegisterUrl`() {

        coEvery {
            forceAuthManager.getConnectedApp().selfRegisterUrl
        } returns "url"

        Assert.assertEquals(onboardingScreenViewModel.getSelfRegisterUrl(), "url")
    }

    @Test
    fun `for getSelfRegisterRedirectUrl`() {

        coEvery {
            forceAuthManager.getConnectedApp().communityUrl
        } returns "url"

        onboardingScreenViewModel.getSelfRegisterRedirectUrl()

        coVerify {
            forceAuthManager.getConnectedApp().communityUrl
        }
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : InstantTaskExecutorRule() {
    override fun starting(description: Description) {
        super.starting(description)

        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)

        Dispatchers.resetMain()
    }
}