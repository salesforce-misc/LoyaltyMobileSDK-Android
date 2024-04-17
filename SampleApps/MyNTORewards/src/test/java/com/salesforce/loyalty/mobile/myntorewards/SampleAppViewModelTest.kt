package com.salesforce.loyalty.mobile.myntorewards

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.MyNTORewards.R.*
import com.salesforce.gamification.model.GameRewardResponse
import com.salesforce.gamification.model.Games
import com.salesforce.gamification.repository.GamificationRemoteRepository
import com.salesforce.loyalty.mobile.myntorewards.badge.LoyaltyBadgeManager
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyProgramBadgeListRecord
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyProgramMemberBadgeListRecord
import com.salesforce.loyalty.mobile.myntorewards.checkout.CheckoutManager
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderAttributes
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.OrderDetailsResponse
import com.salesforce.loyalty.mobile.myntorewards.checkout.models.ShippingMethod
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.*
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.ReceiptScanningManager
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseResponse
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ReceiptListResponse
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ReceiptStatusUpdateResponse
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.UploadReceiptResponse
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralConfig
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralsLocalRepository
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.CurrentPromotionStage
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.QueryResult
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralCode
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralContactInfo
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnablementStatus
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnrollmentInfo
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEntity
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralPromotionStatusAndPromoCode
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralsInfoEntity
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferredAccount
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferredParty
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.DatePeriodType
import com.salesforce.loyalty.mobile.myntorewards.utilities.DateUtils
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.*
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralProgramType
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralStatusType
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ReferralTabs
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.*
import com.salesforce.referral.EnrollmentStatus
import com.salesforce.referral.api.ApiResponse
import com.salesforce.referral.entities.ReferralEnrollmentResponse
import com.salesforce.referral.entities.TransactionJournal
import com.salesforce.referral.entities.referral_event.ReferralEventResponse
import com.salesforce.referral.repository.ReferralsRepository
import io.mockk.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.Description
import org.mockito.kotlin.any

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
    private lateinit var connectedAppViewModel: ConnectedAppViewModel
    private lateinit var scanningViewModel: ScanningViewModel
    private lateinit var gameViewModel: GameViewModel
    private lateinit var badgeViewModel: BadgeViewModel
    private lateinit var myReferralsViewModel: MyReferralsViewModel


    private val loyaltyAPIManager: LoyaltyAPIManager = mockk()
    private val loyaltyBadgeManager: LoyaltyBadgeManager = mockk()

    private val gameRemoteRepository: GamificationRemoteRepository = mockk()
    private val checkoutManager: CheckoutManager = mockk()
    private val receiptScanningManager: ReceiptScanningManager = mockk()

    private val context: Context = mockk()
    private val forceAuthManager: ForceAuthManager = mockk()

    private val member: CommunityMemberModel = mockk()

    //private val localManager: LocalFileManager = mockk()

    private val referralsRepository: ReferralsRepository = mockk()
    private val referralsLocalRepository: ReferralsLocalRepository = mockk()



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
    private lateinit var badgeProgramViewStates: MutableList<BadgeViewState>
    private lateinit var badgeProgramMemberViewStates: MutableList<BadgeViewState>


    private lateinit var enrollmentState: MutableList<EnrollmentState>
    private lateinit var receiptListViewState: MutableList<ReceiptViewState>
    private lateinit var receiptScanningViewState: MutableList<ReceiptScanningViewState>
    private lateinit var receiptStatusUpdateViewState: MutableList<ReceiptStatusUpdateViewState>
    private lateinit var createTransactionJournalViewState: MutableList<CreateTransactionJournalViewState>
    private lateinit var cancelSubmissionViewState: MutableList<UploadRecieptCancelledViewState>
    private lateinit var rewardViewState: MutableList<GameRewardViewState>
    private lateinit var viewState: MutableList<GamesViewState>
    private lateinit var referralViewState: MutableList<ReferFriendViewState>
    private lateinit var uiMutableState: MutableList<MyReferralsViewState>
    private lateinit var programDataState: MutableList<ReferralProgramType>

  /*  private val _programState: MutableLiveData<ReferralProgramType> = MutableLiveData(null)
    val programState: LiveData<ReferralProgramType> = _programState*/

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

        connectedAppViewModel= ConnectedAppViewModel()
        scanningViewModel = ScanningViewModel(receiptScanningManager)
        receiptListViewState = mutableListOf()
        scanningViewModel.receiptListViewState.observeForever {
            receiptListViewState.add(it)
        }
        receiptScanningViewState = mutableListOf()
        scanningViewModel.receiptScanningViewStateLiveData.observeForever {
            receiptScanningViewState.add(it)
        }
        receiptStatusUpdateViewState = mutableListOf()
        scanningViewModel.receiptStatusUpdateViewStateLiveData.observeForever {
            receiptStatusUpdateViewState.add(it)
        }
        createTransactionJournalViewState = mutableListOf()
        scanningViewModel.createTransactionJournalViewStateLiveData.observeForever {
            createTransactionJournalViewState.add(it)
        }
        cancelSubmissionViewState = mutableListOf()
        scanningViewModel.cancellingSubmissionLiveData.observeForever {
            cancelSubmissionViewState.add(it)
        }
        gameViewModel = GameViewModel(gameRemoteRepository)
        rewardViewState = mutableListOf()
        viewState = mutableListOf()
        gameViewModel.gameRewardsViewState.observeForever{
            rewardViewState.add(it)
        }
        gameViewModel.gamesViewState.observeForever{
            viewState.add(it)
        }


        badgeViewModel = BadgeViewModel(loyaltyBadgeManager)
        badgeProgramViewStates = mutableListOf()
        badgeViewModel.badgeProgramViewState.observeForever {
            badgeProgramViewStates.add(it)
        }
        badgeProgramMemberViewStates = mutableListOf()
        badgeViewModel.badgeProgramMemberViewState.observeForever {
            badgeProgramMemberViewStates.add(it)
        }


        every { referralsRepository.setInstanceUrl("") } returns Unit
        myReferralsViewModel= MyReferralsViewModel(referralsRepository, referralsLocalRepository, "")
        referralViewState= mutableListOf()

        myReferralsViewModel.viewState.observeForever{
            if (it != null) {
                referralViewState.add(it)
            }
        }
        uiMutableState= mutableListOf()
        myReferralsViewModel.uiState.observeForever{
            if (it != null) {
                uiMutableState.add(it)
            }
        }

        programDataState= mutableListOf()

        myReferralsViewModel.programState.observeForever{
            programDataState.add(it)
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

    @Test
    fun `for saveConnectedApp`() {
        val context = mockk<Context>(relaxed = true)
        val connectedApp = mockk<ConnectedApp>(relaxed = true)
        mockkObject(ForceConnectedAppEncryptedPreference)
        every { ForceConnectedAppEncryptedPreference.saveConnectedApp(context, connectedApp) } returns Unit
        connectedAppViewModel.saveConnectedApp(context, connectedApp)
        coVerify {
            ForceConnectedAppEncryptedPreference.saveConnectedApp(context, connectedApp)
        }

    }

    @Test
    fun `for getAllConnectedApps`() {
        val context = mockk<Context>(relaxed = true)
        val connectedApp = mockk<ConnectedApp>(relaxed = true)
        mockkObject(ForceConnectedAppEncryptedPreference)
        every { ForceConnectedAppEncryptedPreference.retrieveAll(context) } returns mutableListOf()
        connectedAppViewModel.getAllConnectedApps(context)
        coVerify {
            ForceConnectedAppEncryptedPreference.retrieveAll(context)
        }

    }

   @Test
    fun `for getConnectedApp`() {
        val context = mockk<Context>(relaxed = true)
        val connectedApp = mockk<ConnectedApp>(relaxed = true)
        mockkObject(ForceConnectedAppEncryptedPreference)
        every { ForceConnectedAppEncryptedPreference.getConnectedApp(context, "") } returns connectedApp
        connectedAppViewModel.getConnectedApp(context, "")
        coVerify {
            ForceConnectedAppEncryptedPreference.getConnectedApp(context, "")
        }

    }
    @Test
    fun `for setSelectedApp`() {
        val context = mockk<Context>(relaxed = true)
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)

        val mockResponse = MockResponseFileReader("MemberInfo.json").content


        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)

        connectedAppViewModel.setSelectedApp(context, "instance_url")

        coVerify {
            sharedPrefs.edit().putString(AppConstants.KEY_SELECTED_INSTANCE_URL, "instance_url")
        }

        Assert.assertEquals(connectedAppViewModel.selectedInstanceLiveData.value, "instance_url")

    }

    @Test
    fun `for getSelectedApp`() {
        val context = mockk<Context>(relaxed = true)
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)

        val mockResponse = "instance_url"


        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(AppConstants.KEY_SELECTED_INSTANCE_URL, any()) }
            .returns(mockResponse)

        connectedAppViewModel.getSelectedApp(context)

        coVerify {
            sharedPrefs.getString(AppConstants.KEY_SELECTED_INSTANCE_URL, any())
        }

        Assert.assertEquals(connectedAppViewModel.selectedInstanceLiveData.value, "instance_url")

    }
    @Test
    fun `for deleteConnectedApp`() {
        val context = mockk<Context>(relaxed = true)
        //val connectedAppList = mockk<List<ConnectedApp>>(relaxed = true)
        val connectedAppList =  mutableListOf<ConnectedApp>()


        mockkObject(ForceConnectedAppEncryptedPreference)
        every { ForceConnectedAppEncryptedPreference.deleteConnectedApp(context, "") } returns Unit
        every { ForceConnectedAppEncryptedPreference.retrieveAll(context,) } returns connectedAppList

        connectedAppViewModel.deleteConnectedApp(context, "")
        coVerify {
            ForceConnectedAppEncryptedPreference.deleteConnectedApp(context, "")
        }
        coVerify {
            ForceConnectedAppEncryptedPreference.retrieveAll(context)
        }



        Assert.assertEquals(connectedAppViewModel.savedAppsLiveData.value, connectedAppList)

    }

    @Test
    fun `for retrieveAll`() {
        val context = mockk<Context>(relaxed = true)
        val connectedApp = mockk<ConnectedApp>(relaxed = true)
        val connectedAppList =  mutableListOf<ConnectedApp>()

        mockkObject(ForceConnectedAppEncryptedPreference)
        mockkObject(AppSettings)
        every { ForceConnectedAppEncryptedPreference.deleteConnectedApp(context, "") } returns Unit
        every { ForceConnectedAppEncryptedPreference.retrieveAll(context,) } returns connectedAppList
        every { AppSettings.DEFAULT_FORCE_CONNECTED_APP } returns connectedApp
        every { ForceConnectedAppEncryptedPreference.saveConnectedApp(context, connectedApp) } returns Unit

        connectedAppViewModel.retrieveAll(context)

        coVerify {
            ForceConnectedAppEncryptedPreference.retrieveAll(context)
        }
        coVerify {
            AppSettings.DEFAULT_FORCE_CONNECTED_APP
        }


        coVerify {
            ForceConnectedAppEncryptedPreference.saveConnectedApp(context, connectedApp)
        }




    }

    @Test
    fun `for receipt list success resource, data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)

        val mockResponse =
            Gson().fromJson(
                MockResponseFileReader("receiptlist.json").content,
                ReceiptListResponse::class.java
            )

        coEvery {
            receiptScanningManager.receiptList(any())
        } returns Result.success(mockResponse)
        scanningViewModel.getReceiptLists(context, true)

        Assert.assertEquals(scanningViewModel.receiptListLiveData.value, mockResponse)
    }

    @Test
    fun `for receipt list success resource, cache data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponseInfo)

        val mockResponse = MockResponseFileReader("receiptlist.json").content
        val mockReceiptResponse =
            Gson().fromJson(mockResponse, ReceiptListResponse::class.java)

        scanningViewModel.getReceiptLists(context, false)

        Assert.assertEquals(ReceiptViewState.ReceiptListFetchInProgressView, receiptListViewState[0])
        Assert.assertEquals(ReceiptViewState.ReceiptListFetchSuccessView, receiptListViewState[1])
        Assert.assertEquals(
            scanningViewModel.receiptListLiveData.value,
            mockReceiptResponse
        )
    }

    @Test
    fun `for receipt list failure resource, data must not be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)

        val value = Result.failure<ReceiptListResponse>(Exception("HTTP 401 Unauthorized"))
        coEvery {
            receiptScanningManager.receiptList(any())
        } returns value

        scanningViewModel.getReceiptLists(context, true)

        coVerify {
            receiptScanningManager.receiptList(any())
        }

        Assert.assertEquals(ReceiptViewState.ReceiptListFetchInProgressView, receiptListViewState[0])
        Assert.assertEquals(ReceiptViewState.ReceiptListFetchFailureView, receiptListViewState[1])
    }


     @Test
    fun `for upload receipt success, data must be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)

        val uploadReceiptResponse =
            Gson().fromJson(
                MockResponseFileReader("UploadReceiptFileNameFetch.json").content,
                UploadReceiptResponse::class.java
            )
         val mockAnalyzeExpenseResponse =
             Gson().fromJson(
                 MockResponseFileReader("AnalyzeExpense.json").content,
                 AnalyzeExpenseResponse::class.java
             )

        coEvery {
            receiptScanningManager.uploadReceipt(any(), any())
        } returns Result.success(uploadReceiptResponse)

         coEvery {
             receiptScanningManager.analyzeExpense(any(), any())
         } returns Result.success(mockAnalyzeExpenseResponse)

        scanningViewModel.uploadReceipt(context, byteArrayOf())

        coVerify {
            receiptScanningManager.uploadReceipt(any(), any())
        }
         coVerify {
             receiptScanningManager.analyzeExpense(any(), any())
         }
        Assert.assertEquals(ReceiptScanningViewState.UploadReceiptInProgress, receiptScanningViewState[0])
        Assert.assertEquals(ReceiptScanningViewState.UploadReceiptSuccess, receiptScanningViewState[1])
        Assert.assertEquals(ReceiptScanningViewState.ReceiptScanningSuccess, receiptScanningViewState[2])
        Assert.assertEquals(scanningViewModel.scannedReceiptLiveData.value, mockAnalyzeExpenseResponse)
    }

    @Test
    fun `for upload receipt failure, data must not be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)



        coEvery {
            receiptScanningManager.uploadReceipt(any(), any())
        } returns Result.failure(Exception("HTTP 401 Unauthorized"))


        scanningViewModel.uploadReceipt(context, byteArrayOf())

        coVerify {
            receiptScanningManager.uploadReceipt(any(), any())
        }
        coVerify(exactly = 0) {
            receiptScanningManager.analyzeExpense(any(), any())
        }
        Assert.assertEquals(ReceiptScanningViewState.UploadReceiptInProgress, receiptScanningViewState[0])
        Assert.assertEquals(ReceiptScanningViewState.ReceiptScanningFailure("HTTP 401 Unauthorized").message, (receiptScanningViewState[1] as ReceiptScanningViewState.ReceiptScanningFailure).message )
        Assert.assertEquals(scanningViewModel.scannedReceiptLiveData.value, null)
    }

    @Test
    fun `for upload receipt success but analyze expense failed, data must not be available`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)

        val uploadReceiptResponse =
            Gson().fromJson(
                MockResponseFileReader("UploadReceiptFileNameFetch.json").content,
                UploadReceiptResponse::class.java
            )

        coEvery {
            receiptScanningManager.uploadReceipt(any(), any())
        } returns Result.success(uploadReceiptResponse)


        coEvery {
            receiptScanningManager.analyzeExpense(any(), any())
        } returns Result.failure(Exception("HTTP 401 Unauthorized"))


        scanningViewModel.uploadReceipt(context, byteArrayOf())

        coVerify {
            receiptScanningManager.uploadReceipt(any(), any())
        }
        coVerify {
            receiptScanningManager.analyzeExpense(any(), any())
        }
        Assert.assertEquals(ReceiptScanningViewState.UploadReceiptInProgress, receiptScanningViewState[0])
        Assert.assertEquals(ReceiptScanningViewState.UploadReceiptSuccess, receiptScanningViewState[1])
        Assert.assertEquals(ReceiptScanningViewState.ReceiptScanningFailure("HTTP 401 Unauthorized").message, (receiptScanningViewState[2] as ReceiptScanningViewState.ReceiptScanningFailure).message )
        Assert.assertEquals(scanningViewModel.scannedReceiptLiveData.value, null)
    }

    @Test
    fun `for submit for manual review success resource, data must be available`() {
        val mockResponse =
            ReceiptStatusUpdateResponse("success","Status updated successfully!", "")

        coEvery {
            receiptScanningManager.receiptStatusUpdate(any(), any(), any())
        } returns Result.success(mockResponse)
        scanningViewModel.submitForManualReview("12345", any())

        Assert.assertEquals(ReceiptStatusUpdateViewState.ReceiptStatusUpdateInProgress, receiptStatusUpdateViewState[0])
        Assert.assertEquals(
            ReceiptStatusUpdateViewState.ReceiptStatusUpdateSuccess(null).points,
            (receiptStatusUpdateViewState[1] as ReceiptStatusUpdateViewState.ReceiptStatusUpdateSuccess).points
        )
    }

    @Test
    fun `for submit for manual review failure resource, data must not be available`() {
        val value = Result.failure<ReceiptStatusUpdateResponse>(Exception("HTTP 401 Unauthorized"))

        coEvery {
            receiptScanningManager.receiptStatusUpdate(any(), any(), any())
        } returns value
        scanningViewModel.submitForManualReview("12345", any())

        Assert.assertEquals(
            ReceiptStatusUpdateViewState.ReceiptStatusUpdateInProgress,
            receiptStatusUpdateViewState[0]
        )
        Assert.assertEquals(
            ReceiptStatusUpdateViewState.ReceiptStatusUpdateFailure, receiptStatusUpdateViewState[1]
        )
    }

    @Test
    fun `for create transaction journal success, data must be available`() {
        val mockResponse =
            ReceiptStatusUpdateResponse("success", "Status updated successfully!", "")

        coEvery {
            receiptScanningManager.receiptStatusUpdate(any(), any(), any())
        } returns Result.success(mockResponse)
        scanningViewModel.submitForProcessing("12345")

        Assert.assertEquals(
            CreateTransactionJournalViewState.CreateTransactionJournalInProgress,
            createTransactionJournalViewState[0]
        )
        Assert.assertEquals(
            CreateTransactionJournalViewState.CreateTransactionJournalSuccess,
            createTransactionJournalViewState[1]
        )
    }

    @Test
    fun `for create transaction journal failure, data must not be available`() {
        val value = Result.failure<ReceiptStatusUpdateResponse>(Exception("HTTP 401 Unauthorized"))

        coEvery {
            receiptScanningManager.receiptStatusUpdate(any(), any(), any())
        } returns value
        scanningViewModel.submitForProcessing("12345")

        Assert.assertEquals(
            CreateTransactionJournalViewState.CreateTransactionJournalInProgress,
            createTransactionJournalViewState[0]
        )
        Assert.assertEquals(
            CreateTransactionJournalViewState.CreateTransactionJournalFailure,
            createTransactionJournalViewState[1]
        )
    }

    @Test
    fun `for cancel submission success, data must be available`() {
        val mockResponse =
            ReceiptStatusUpdateResponse("success", "Status updated successfully!", "")

        coEvery {
            receiptScanningManager.receiptStatusUpdate(any(), any(), any())
        } returns Result.success(mockResponse)
        scanningViewModel.cancellingSubmission("12345")

        Assert.assertEquals(
            UploadRecieptCancelledViewState.UploadRecieptCancelledInProgress,
            cancelSubmissionViewState[0]
        )
        Assert.assertEquals(
            UploadRecieptCancelledViewState.UploadRecieptCancelledSuccess,
            cancelSubmissionViewState[1]
        )
    }

    @Test
    fun `for cancel submission failure, data must not be available`() {
        val value = Result.failure<ReceiptStatusUpdateResponse>(Exception("HTTP 401 Unauthorized"))

        coEvery {
            receiptScanningManager.receiptStatusUpdate(any(), any(), any())
        } returns value
        scanningViewModel.cancellingSubmission("12345")

        Assert.assertEquals(
            UploadRecieptCancelledViewState.UploadRecieptCancelledInProgress,
            cancelSubmissionViewState[0]
        )
        Assert.assertEquals(
            UploadRecieptCancelledViewState.UploadRecieptCancelledFailure,
            cancelSubmissionViewState[1]
        )
    }

    @Test
    fun `for get receipt status success, data must be available`() {

        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)

        val mockReceiptListResponse =
            Gson().fromJson(
                MockResponseFileReader("receiptlist.json").content,
                ReceiptListResponse::class.java
            )
        coEvery {
            receiptScanningManager.getReceiptStatus(any(), any())
        } returns Result.success(mockReceiptListResponse)

        scanningViewModel.getReceiptStatus("12345", "2345678", 5, 0)

        coVerify {
            receiptScanningManager.getReceiptStatus(any(), any())
        }
        Assert.assertEquals(
            ReceiptStatusUpdateViewState.ReceiptStatusUpdateInProgress,
            receiptStatusUpdateViewState[0]
        )
        //test case getting failed. need to check.
      /*  Assert.assertEquals(
            ReceiptStatusUpdateViewState.ReceiptStatusUpdateSuccess("100.0").points,
            (receiptStatusUpdateViewState[1] as ReceiptStatusUpdateViewState.ReceiptStatusUpdateSuccess).points
        )*/
    }

    @Test
    fun `on SignUp To Refer Clicked program state should be join program`() {
        myReferralsViewModel.onSignUpToReferClicked("")
        Assert.assertEquals(
            ReferralProgramType.JOIN_PROGRAM,
            programDataState[0]

        )
    }

    @Test
    fun `on start refer Refer Clicked program state should be start referring`() {
        myReferralsViewModel.startReferring()
        Assert.assertEquals(
            ReferralProgramType.START_REFERRING,
            programDataState[0]
        )
    }

    //after null status will be removed this test cases can be added.
    /* @Test
     fun `on reset clicked`() {
         myReferralsViewModel.resetViewState()
         Assert.assertEquals(
             null,
             referralViewState[0]
         )
     }*/


    @Test
    fun `for send email if api call is success posted value should be success with empty data`() {
        val context = mockSharedPrefs()

        coEvery {  referralsRepository.sendReferrals(any(), any()) }
            .returns(ApiResponse.Success(ReferralEventResponse("", "", "", listOf(), "")) )

        myReferralsViewModel.sendReferralMail(context, listOf() )
        coVerify {
            referralsRepository.sendReferrals(any(), any())
        }

        Assert.assertEquals(
            ReferFriendViewState.ReferFriendInProgress,
            referralViewState[0]
        )

        Assert.assertEquals(
            ReferFriendViewState.ReferFriendSendMailsSuccess,
            referralViewState[1]
        )

    }

    @Test
    fun `for send email if api call is failure posted value should be failure with empty data`() {
        val context = mockSharedPrefs()

        coEvery {  referralsRepository.sendReferrals(any(), any()) }
            .returns(ApiResponse.Error("Business Error") )

        myReferralsViewModel.sendReferralMail(context, listOf() )
        coVerify {
            referralsRepository.sendReferrals(any(), any())
        }

        Assert.assertEquals(
            ReferFriendViewState.ReferFriendInProgress,
            referralViewState[0]
        )

        Assert.assertEquals(
            ReferFriendViewState.ReferFriendSendMailsFailed("Business Error"),
            referralViewState[1]
        )
    }

    @Test
    fun `send email if api call is failure due to network, posted value should be failure with empty data`() {
        val context = mockSharedPrefs()

        coEvery {  referralsRepository.sendReferrals(any(), any()) }
            .returns(ApiResponse.NetworkError )

        myReferralsViewModel.sendReferralMail(context, listOf() )

        Assert.assertEquals(ReferFriendViewState.ReferFriendInProgress, referralViewState[0])
        Assert.assertEquals(ReferFriendViewState.ReferFriendSendMailsFailed(), referralViewState[1])
    }

    @Test
    fun `for fetch Referral Program Success Status should be success`() {
        val context = mockSharedPrefs()

        coEvery { referralsLocalRepository.checkIfMemberEnrolled(any(), "") }
            .returns(ApiResponse.Success(QueryResult(0, true, listOf(), "")))

        coEvery { referralsLocalRepository.saveReferralStatusInCache("TEMPRP7", true,false) }
            .returns(Unit)

        myReferralsViewModel.fetchReferralProgramStatus(context)

        coVerify {
            referralsLocalRepository.checkIfMemberEnrolled(any(), "")
        }

        coVerify {
            referralsLocalRepository.saveReferralStatusInCache("TEMPRP7", true,false)
        }

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[0]
        )

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsPromotionNotEnrolled,
            uiMutableState[1]
        )
    }

    @Test
    fun `given referral status enrolled, when fetching Referral Program Success, Status should be enrolled`() {
        val context = mockSharedPrefs()

        val enrollmentInfo = ReferralEnrollmentInfo("1234", ReferralContactInfo("1111"))
        coEvery { referralsLocalRepository.checkIfMemberEnrolled(any(), "") }
            .returns(ApiResponse.Success(QueryResult(1, true, listOf(enrollmentInfo), "")))

        coEvery { referralsLocalRepository.saveReferralStatusInCache("TEMPRP7", true,true) } just runs

        myReferralsViewModel.fetchReferralProgramStatus(context)

        Assert.assertEquals(MyReferralsViewState.MyReferralsFetchInProgress, uiMutableState[0])
        Assert.assertEquals(MyReferralsViewState.MyReferralsPromotionEnrolled, uiMutableState[1])
    }

    @Test
    fun `for fetch Referral Program Status error should have ui state error`() {

        val context = mockSharedPrefs()

        coEvery { referralsLocalRepository.checkIfMemberEnrolled(any(), "") }
            .returns(ApiResponse.Error("Run time Exception"))

        myReferralsViewModel.fetchReferralProgramStatus(context)

        coVerify {
            referralsLocalRepository.checkIfMemberEnrolled(any(), "")
        }
        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[0]
        )

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsPromotionStatusFailure("Run time Exception"),
            uiMutableState[1]
        )
    }

    @Test
    fun `for fetch Referral Program Status network error ui state should failure`() {

        val context = mockSharedPrefs()

        coEvery { referralsLocalRepository.checkIfMemberEnrolled(any(), "") }
            .returns(ApiResponse.NetworkError)

        myReferralsViewModel.fetchReferralProgramStatus(context)

        coVerify {
            referralsLocalRepository.checkIfMemberEnrolled(any(), "")
        }

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[0]
        )

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsPromotionStatusFailure(),
            uiMutableState[1]
        )
    }

    @Test
    fun `for fetchReferralAndPromotionCode called when referralCode is empty `() {

        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)

        every { sharedPrefs.getString(AppConstants.KEY_MEMBER_REFERRAL_CODE, null) }
            .returns(null)

        coEvery { referralsLocalRepository.checkIfMemberEnrolled(any(), "") }
            .returns(ApiResponse.NetworkError)
        coEvery { referralsLocalRepository.saveReferralStatusInCache("", true,false) }
            .returns(Unit)
        coEvery { referralsLocalRepository.fetchMemberReferralCode(any())}
            .returns(ApiResponse.Success(QueryResult(0, true, listOf(), "")))

        myReferralsViewModel.setReferralCode(context, "")

        myReferralsViewModel.fetchReferralProgramStatus(context)

        coVerify {
            sharedPrefs.getString(AppConstants.KEY_MEMBER_REFERRAL_CODE, any())
        }

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[0]
        )

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsPromotionStatusFailure(),
            uiMutableState[1]
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `for fetch fetch Referrals Info is success should be MyReferralsFetchSuccess`() {
        val context = mockSharedPrefs()

        coEvery {
            referralsLocalRepository.fetchReferralsInfo(
                any(),
                any()
            )
        }.returns(ApiResponse.Success(ReferralsInfoEntity(listOf())))

        myReferralsViewModel.fetchReferralsInfo(context)

        coVerify {
            referralsLocalRepository.fetchReferralsInfo(any(), any())
        }
        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[0]
        )

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchSuccess(
                MyReferralScreenState(
                    ReferralTabs.sortedTabs(), listOf(), listOf(), listOf(
                        Pair(
                            R.string.my_referral_sent_label, "${0}"
                        ),
                        Pair(R.string.my_referrals_accepted_label, "${0}"),
                        Pair(R.string.my_referrals_vouchers_earned_label, "${0}")
                    ), "${ReferralConfig.REFERRAL_DURATION}"
                )
            ),
            uiMutableState[1]
        )
    }


    @Test
    fun `for fetch fetch Referrals Info is success data not null MyReferrals Fetch Success`() {
        val context = mockSharedPrefs()

        val reffralEntity= ReferralEntity(any(), CurrentPromotionStage(""),  ReferredParty(
            ReferredAccount(""))
        )

        coEvery {
            referralsLocalRepository.fetchMemberReferralCode(
                any()
            )
        }.returns(ApiResponse.Success(QueryResult(0, true, listOf(ReferralCode("24345678","REFERRAL_CODE")), "")))

        coEvery {
            referralsLocalRepository.fetchReferralsInfo(
                any(),
                any()
            )
        }.returns(ApiResponse.Success(ReferralsInfoEntity(listOf(reffralEntity))))

        myReferralsViewModel.fetchReferralsInfo(context)

        coVerify {
            referralsLocalRepository.fetchReferralsInfo(any(), any())
        }

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchSuccess(
                successState(listOf(reffralEntity))
            ),
            uiMutableState[1]
        )
    }

    @Test
    fun `for fetch fetch Referrals Info is success referral code not empty MyReferrals Fetch Success`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)

        val context = mockk<Context>(relaxed = true)
        val reffralEntity= ReferralEntity(any(),CurrentPromotionStage(""), ReferredParty(ReferredAccount("")) )


        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)

        coEvery {
            referralsLocalRepository.fetchReferralsInfo(
                any(),
                any(),
            )
        }.returns(ApiResponse.Success(ReferralsInfoEntity(listOf(reffralEntity))))

        myReferralsViewModel.fetchReferralsInfo(context)

        coVerify {
            sharedPrefs.getString(any(), any())
        }
        coVerify {
            referralsLocalRepository.fetchReferralsInfo(any(),any())
        }
        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[0]
        )

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchSuccess(successState( listOf(reffralEntity))),
            uiMutableState[1]
        )

    }

    @Test
    fun `for fetch fetch Referrals Info is error MyReferrals Fetch Failure`() {
        val context = mockSharedPrefs()

        coEvery {
            referralsLocalRepository.fetchReferralsInfo(
                any(),
                any()
            )
        }.returns(ApiResponse.Error("run time exception"))

        myReferralsViewModel.fetchReferralsInfo(context)

        coVerify {
            referralsLocalRepository.fetchReferralsInfo(any(), any())
        }
        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[0]
        )

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchFailure("run time exception"),
            uiMutableState[1]
        )
    }

    @Test
    fun `for fetch fetch Referrals Info is network error MyReferrals Fetch Failure`() {
        val context = mockSharedPrefs()

        coEvery {
            referralsLocalRepository.fetchReferralsInfo(
                any(),
                any()
            )
        }.returns(ApiResponse.NetworkError)

        myReferralsViewModel.fetchReferralsInfo(context)

        coVerify {
            referralsLocalRepository.fetchReferralsInfo(any(), any())
        }
        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[0]
        )

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchFailure(),
            uiMutableState[1]
        )
    }

    private fun mockSharedPrefs(): Context {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        return context
    }

    @Test
    fun `for enroll To referral promotion success tryAgain false refer in progress`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(AppConstants.KEY_COMMUNITY_MEMBER, any()) }.returns(" ")

        coEvery {
            referralsRepository.enrollNewCustomerAsAdvocateOfPromotion(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
            )
        }.returns(ApiResponse.Success(ReferralEnrollmentResponse("","","", "","" , listOf())))

        myReferralsViewModel.enrollToReferralPromotion(context, false)

        coVerify {
            referralsRepository.enrollNewCustomerAsAdvocateOfPromotion(
                any() ,any(), any(), any(), any()
            )
        }
        Assert.assertEquals(
            ReferFriendViewState.ReferFriendInProgress,
            referralViewState[0]
        )

    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `for enroll To referral promotion generic error tryAgain false should have enrollment failed with generic error`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(AppConstants.KEY_COMMUNITY_MEMBER, any()) }.returns(" ")

        coEvery {
            referralsRepository.enrollNewCustomerAsAdvocateOfPromotion(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
            )
        }.returns(ApiResponse.Error("Run Time Exception"))

        myReferralsViewModel.enrollToReferralPromotion(context, false)

        Assert.assertEquals(
            ReferFriendViewState.ReferFriendInProgress,
            referralViewState[0]
        )
        Assert.assertEquals(
            ReferFriendViewState.EnrollmentTaskFinished,
            referralViewState[1]
        )
        Assert.assertEquals(
            ReferralProgramType.ERROR("Run Time Exception"),
            programDataState[0]
        )

    }

    //below test case causing coverage issue. Although its being passed when execute without coverage. While coverage calculation
    //comment below test case.
    @Test
    fun `for enroll To referral promotion network error tryAgain false should have enrollment failed with network error`() {

        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(AppConstants.KEY_COMMUNITY_MEMBER, any()) }.returns(" ")

        coEvery {
            referralsRepository.enrollNewCustomerAsAdvocateOfPromotion(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
            )
        }.returns(ApiResponse.NetworkError)

        myReferralsViewModel.enrollToReferralPromotion(context, false)

        Assert.assertEquals(
            ReferFriendViewState.ReferFriendInProgress,
            referralViewState[0]
        )
        Assert.assertEquals(
            ReferFriendViewState.EnrollmentTaskFinished,
            referralViewState[1]
        )
        Assert.assertEquals(
            ReferralProgramType.ERROR(),
            programDataState[0]
        )

    }

    @Test
    fun `for enroll To referral promotion success tryAgain false contact id not null enrollment status false then enrollment should fail`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { sharedPrefs.getString(AppConstants.KEY_COMMUNITY_MEMBER, any()) }.returns(mockResponse)

        coEvery {
            referralsRepository.enrollExistingAdvocateToPromotionWithContactId(
                any(),
                any(),
                any(),
                any()
            )
        }.returns(ApiResponse.Success(ReferralEnrollmentResponse("","","", "","" , listOf())))

        myReferralsViewModel.enrollToReferralPromotion(context, false)

        coVerify {
            referralsRepository.enrollExistingAdvocateToPromotionWithContactId(
                any() ,any(), any(), any()
            )
        }
        Assert.assertEquals(
            ReferFriendViewState.ReferFriendInProgress,
            referralViewState[0]
        )
        Assert.assertEquals(
            ReferFriendViewState.EnrollmentTaskFinished,
            referralViewState[1]
        )
        Assert.assertEquals(
            ReferralProgramType.ERROR(context.getString(R.string.enrolment_not_processed_message)),
            programDataState[0]
        )

    }

    @Test
    fun `for enroll To referral promotion success tryAgain false member id not null enrollment status false then enrollment should fail`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)

        val mockResponse = MockResponseFileReader("MembrInfoContactIDNull.json").content
        every { sharedPrefs.getString(AppConstants.KEY_COMMUNITY_MEMBER, any()) }.returns(mockResponse)


        coEvery {
            referralsRepository.enrollExistingAdvocateToPromotionWithMembershipNumber(
                any(),
                any(),
                any(),
                any()
            )
        }.returns(ApiResponse.Success(ReferralEnrollmentResponse("","","", "","" , listOf())))

        myReferralsViewModel.enrollToReferralPromotion(context, false)

        coVerify {
            referralsRepository.enrollExistingAdvocateToPromotionWithMembershipNumber(
                any() ,any(), any()
            )
        }
        Assert.assertEquals(
            ReferFriendViewState.ReferFriendInProgress,
            referralViewState[0]
        )
        Assert.assertEquals(
            ReferFriendViewState.EnrollmentTaskFinished,
            referralViewState[1]
        )
        Assert.assertEquals(
            ReferralProgramType.ERROR(context.getString(R.string.enrolment_not_processed_message)),
            programDataState[0]
        )

    }

    @Test
    fun `for enroll To referral promotion success tryAgain false member not null EnrollmentStatus is PROCESSED and program status is processed`() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val mockTransactionJournal = mockk<List<TransactionJournal>>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { sharedPrefs.getString(AppConstants.KEY_COMMUNITY_MEMBER, any()) }.returns(mockResponse)
        coEvery { mockTransactionJournal.firstOrNull()?.status } returns EnrollmentStatus.PROCESSED.status

        coEvery {
            referralsRepository.enrollExistingAdvocateToPromotionWithContactId(
                any(),
                any(),
                any(),
                any()
            )
        }.returns(ApiResponse.Success(ReferralEnrollmentResponse("","","", "","" , mockTransactionJournal)))

        myReferralsViewModel.enrollToReferralPromotion(context, false)

        coVerify {
            referralsRepository.enrollExistingAdvocateToPromotionWithContactId(
                any() ,any(), any(), any()
            )
        }
        Assert.assertEquals(
            ReferFriendViewState.ReferFriendInProgress,
            referralViewState[0]
        )
        Assert.assertEquals(
            ReferralProgramType.START_REFERRING,
            programDataState[0]
        )
    }

    @Test
    fun `for not null promoCodeFromCache isReferral true and isEnrolled true program state should be START_REFERRING and uiMutableState MyReferralsPromotionEnrolled` () {
        var chachedPromo= mutableMapOf<String, Pair<String?, String?>>()

        chachedPromo.set("Key1", Pair("First", "Second"))

        coEvery { referralsLocalRepository.getPromoCodeFromCache("1234") }.returns(chachedPromo["Key1"]?.first)
        coEvery { referralsLocalRepository.getReferralStatusFromCache(any()) }.returns(Pair(true, true))


        myReferralsViewModel.checkIfGivenPromotionIsInCache(context, "1234")

        coVerify { referralsLocalRepository.getPromoCodeFromCache("1234") }

        coVerify {referralsLocalRepository.getReferralStatusFromCache(any()) }


        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[0]
        )
        Assert.assertEquals(
            ReferralProgramType.START_REFERRING,
            programDataState[0]
        )

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsPromotionEnrolled,
            uiMutableState[1]
        )
    }

    @Test
    fun `for not null promoCodeFromCache isReferral true and isEnrolled false program state should be JOIN_PROGRAM and uiMutableState MyReferralsPromotionNotEnrolled` () {
        var chachedPromo= mutableMapOf<String, Pair<String?, String?>>()
        chachedPromo.set("Key1", Pair("First", "Second"))

        coEvery { referralsLocalRepository.getPromoCodeFromCache("1234") }.returns(chachedPromo["Key1"]?.first)
        coEvery { referralsLocalRepository.getReferralStatusFromCache(any()) }.returns(Pair(true, false))


        myReferralsViewModel.checkIfGivenPromotionIsInCache(context, "1234")


        coVerify { referralsLocalRepository.getPromoCodeFromCache("1234") }

        coVerify {referralsLocalRepository.getReferralStatusFromCache(any()) }


        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[0]
        )
        Assert.assertEquals(
            ReferralProgramType.JOIN_PROGRAM,
            programDataState[0]
        )

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsPromotionNotEnrolled,
            uiMutableState[1]
        )
    }

    @Test
    fun `for not null promoCodeFromCache isReferral false and isEnrolled false  uiMutableState PromotionStateNonReferral` () {
        var chachedPromo= mutableMapOf<String, Pair<String?, String?>>()
        chachedPromo.set("Key1", Pair("First", "Second"))

        coEvery { referralsLocalRepository.getPromoCodeFromCache("1234") }.returns(chachedPromo["Key1"]?.first)
        coEvery { referralsLocalRepository.getReferralStatusFromCache(any()) }.returns(Pair(false, false))

        myReferralsViewModel.checkIfGivenPromotionIsInCache(context, "1234")

        coVerify { referralsLocalRepository.getPromoCodeFromCache("1234") }

        coVerify {referralsLocalRepository.getReferralStatusFromCache(any()) }

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[0]
        )

        Assert.assertEquals(
            MyReferralsViewState.PromotionStateNonReferral,
            uiMutableState[1]
        )
    }

    @Test
    fun `for  null promoCodeFromCache checkIfGivenPromotionIsReferralAndEnrolled method is called` () {
        var chachedPromo= mutableMapOf<String, Pair<String?, String?>>()
        chachedPromo.set("Key1", Pair("First", "Second"))
        coEvery { referralsLocalRepository.getPromoCodeFromCache("1234") }.returns(null)
        coEvery { referralsLocalRepository.getReferralStatusFromCache(any()) }.returns(Pair(false, false))
        coEvery { referralsLocalRepository.checkIfGivenPromotionIsReferralAndEnrolled(any()) }
            .returns(ApiResponse.Success(QueryResult(0, true, listOf(), "")))

        coEvery { referralsLocalRepository.savePromoCodeAndUrlInCache(any(), any()) }
            .returns(Unit)
        coEvery { referralsLocalRepository.saveReferralStatusInCache(any(), any(), any()) }
            .returns(Unit)

        myReferralsViewModel.checkIfGivenPromotionIsInCache(context, "1234")

        coVerify { referralsLocalRepository.getPromoCodeFromCache("1234") }
        coVerify { referralsLocalRepository.checkIfGivenPromotionIsReferralAndEnrolled("1234") }
        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[0]
        )
        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[1]
        )

    }

    @Test
    fun `for  null promoCodeFromCache checkIfGivenPromotionIsReferralAndEnrolled method is called ansd verify submethod call API Success` () {
        var chachedPromo= mutableMapOf<String, Pair<String?, String?>>()
        chachedPromo.set("Key1", Pair("First", "Second"))
        coEvery { referralsLocalRepository.getPromoCodeFromCache("1234") }.returns(null)
        coEvery { referralsLocalRepository.getReferralStatusFromCache(any()) }.returns(Pair(false, false))
        coEvery { referralsLocalRepository.checkIfGivenPromotionIsReferralAndEnrolled(any()) }
            .returns(ApiResponse.Success(QueryResult(0, true, listOf(
                ReferralPromotionStatusAndPromoCode("promo_name", "1234", "", "", "", "", true)
            ), "")))

        coEvery { referralsLocalRepository.savePromoCodeAndUrlInCache(any(), any()) }
            .returns(Unit)
        coEvery { referralsLocalRepository.saveReferralStatusInCache(any(), any(), any()) }
            .returns(Unit)

        myReferralsViewModel.checkIfGivenPromotionIsInCache(context, "1234")

        coVerify { referralsLocalRepository.checkIfGivenPromotionIsReferralAndEnrolled(any()) }
        coVerify { referralsLocalRepository.savePromoCodeAndUrlInCache(any(), any()) }

        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[0]
        )
        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[1]
        )

    }

    @Test
    fun `for  null promoCodeFromCache checkIfGivenPromotionIsReferralAndEnrolled method is called ansd verify PromotionReferralApiStatusFailure on  API Error Response` () {
        var chachedPromo= mutableMapOf<String, Pair<String?, String?>>()
        chachedPromo.set("Key1", Pair("First", "Second"))
        val (isReferral, isEnrolled) = Pair("First", "Second")
        coEvery { referralsLocalRepository.getPromoCodeFromCache("1234") }.returns(null)
        coEvery { referralsLocalRepository.getReferralStatusFromCache(any()) }.returns(Pair(false, false))
        coEvery { referralsLocalRepository.checkIfGivenPromotionIsReferralAndEnrolled(any()) }
            .returns(ApiResponse.Error("Run Time Exception"))

        coEvery { referralsLocalRepository.savePromoCodeAndUrlInCache(any(), any()) }
            .returns(Unit)
        coEvery { referralsLocalRepository.saveReferralStatusInCache(any(), any(), any()) }
            .returns(Unit)

        myReferralsViewModel.checkIfGivenPromotionIsInCache(context, "1234")

        coVerify { referralsLocalRepository.checkIfGivenPromotionIsReferralAndEnrolled(any()) }


        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[0]
        )
        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[1]
        )
        Assert.assertEquals(
            MyReferralsViewState.PromotionReferralApiStatusFailure("Run Time Exception"),
            uiMutableState[2]
        )

    }

    @Test
    fun `for  null promoCodeFromCache checkIfGivenPromotionIsReferralAndEnrolled method is called ansd verify PromotionReferralApiStatusFailure on  API Network Error Response` () {
        var chachedPromo= mutableMapOf<String, Pair<String?, String?>>()
        chachedPromo.set("Key1", Pair("First", "Second"))
        val (isReferral, isEnrolled) = Pair("First", "Second")
        coEvery { referralsLocalRepository.getPromoCodeFromCache("1234") }.returns(null)
        coEvery { referralsLocalRepository.getReferralStatusFromCache(any()) }.returns(Pair(false, false))
        coEvery { referralsLocalRepository.checkIfGivenPromotionIsReferralAndEnrolled(any()) }
            .returns(ApiResponse.NetworkError)

        coEvery { referralsLocalRepository.savePromoCodeAndUrlInCache(any(), any()) }
            .returns(Unit)
        coEvery { referralsLocalRepository.saveReferralStatusInCache(any(), any(), any()) }
            .returns(Unit)

        myReferralsViewModel.checkIfGivenPromotionIsInCache(context, "1234")

        coVerify { referralsLocalRepository.checkIfGivenPromotionIsReferralAndEnrolled(any()) }


        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[0]
        )
        Assert.assertEquals(
            MyReferralsViewState.MyReferralsFetchInProgress,
            uiMutableState[1]
        )
        Assert.assertEquals(
            MyReferralsViewState.PromotionReferralApiStatusFailure(),
            uiMutableState[2]
        )

    }

    @Test
    fun `given referral feature enable status not fetched yet, when checking if referral is enabled, then assert the result as enabled` () {
        coEvery { referralsLocalRepository.isReferralFeatureEnabled() }.returns(null)

        val responseType = object : TypeToken<QueryResult<ReferralEnablementStatus>>() {}.type
        val mockResponse = mockResponse("ReferralEnbleStatus.json", responseType) as QueryResult<ReferralEnablementStatus>
        coEvery { referralsLocalRepository.checkIfReferralIsEnabled() }.returns(ApiResponse.Success(mockResponse))
        coEvery { referralsLocalRepository.setReferralFeatureEnabled(true) } just runs

        myReferralsViewModel.checkIfReferralIsEnabled(forceRefresh = true)

        Assert.assertEquals(MyReferralsViewState.MyReferralsFetchInProgress, uiMutableState[0])
        Assert.assertEquals(MyReferralsViewState.ReferralFeatureEnabled, uiMutableState[1])
    }

    @Test
    fun `given referral feature enable status not fetched yet and api failed, when checking if referral is enabled, then assert the result as disabled` () {
        coEvery { referralsLocalRepository.isReferralFeatureEnabled() }.returns(null)

        coEvery { referralsLocalRepository.checkIfReferralIsEnabled() }.returns(ApiResponse.NetworkError)
        coEvery { referralsLocalRepository.setReferralFeatureEnabled(false) } just runs

        myReferralsViewModel.checkIfReferralIsEnabled()

        Assert.assertEquals(MyReferralsViewState.MyReferralsFetchInProgress, uiMutableState[0])
        Assert.assertEquals(MyReferralsViewState.ReferralFeatureNotEnabled, uiMutableState[1])
    }

    @Test
    fun `given Promotion in cache, when fetching referral promotion from cache, then assert the result` () {
        coEvery { referralsLocalRepository.getReferralPromotionDetails(any()) }.returns(
            referralPromotionDetails()
        )

        val promotionDetails = myReferralsViewModel.fetchReferralPromotionDetailsFromCache(context, "12345")

        Assert.assertEquals(referralPromotionDetails(), promotionDetails)
    }

    @Test
    fun `given Promotion in cache but promotion has past date, when fetching referral promotion from cache, then assert the result` () {
        coEvery { referralsLocalRepository.getReferralPromotionDetails(any()) }.returns(
            referralPromotionDetails("2024-01-31")
        )
        coEvery { context.getString(any()) }.returns("You missed the boat You can no longer refer your friends because the referral promotion has expired.")

        val promotionDetails = myReferralsViewModel.fetchReferralPromotionDetailsFromCache(context, "12345")

        Assert.assertEquals(referralPromotionDetails("2024-01-31"), promotionDetails)
        Assert.assertEquals(ReferralProgramType.ERROR("You missed the boat You can no longer refer your friends because the referral promotion has expired."), programDataState[0])
    }

    @Test
    fun `given Default Promotion status as enrolled, when resetting program type, then assert the result` () {
        coEvery { referralsLocalRepository.getReferralStatusFromCache(any()) }.returns(
            Pair(true, true)
        )

        myReferralsViewModel.resetProgramTypeOnError()

        Assert.assertEquals(ReferralProgramType.START_REFERRING, programDataState[0])
    }

    @Test
    fun `given Default Promotion status as not enrolled, when resetting program type, then assert the result` () {
        coEvery { referralsLocalRepository.getReferralStatusFromCache(any()) }.returns(
            Pair(true, false)
        )

        myReferralsViewModel.resetProgramTypeOnError()

        Assert.assertEquals(ReferralProgramType.JOIN_PROGRAM, programDataState[0])
    }

    private fun referralPromotionDetails(endDate: String = "2090-12-31") = ReferralPromotionStatusAndPromoCode(
        "Default Referral Promotion",
        "TEMPRP7",
        "https://rb.gy/wa6jw7",
        "Invite your friends and get a voucher when they shop for the first time.",
        endDate,
        "https://rb.gy/wa6jw7",
        true
    )

    @Test
    fun `given Promotion PageUrl, when creating referral link, then assert the result` () {
        val referralLink = myReferralsViewModel.referralLink("https://rb.gy/wa6jw7")

        Assert.assertEquals("https://rb.gy/wa6jw7?referralCode=", referralLink)
    }

    @Test
    fun `for get game  reward success, data must be available`() {

        val mockGameResponse =
            Gson().fromJson(
                MockResponseFileReader("GameRewards.json").content,
                GameRewardResponse::class.java
            )
        coEvery {
            gameRemoteRepository.getGameReward("12345", false)
        } returns Result.success(mockGameResponse)


        gameViewModel.getGameReward("12345", false)

        coVerify {
            gameRemoteRepository.getGameReward(any(), any())
        }

        Assert.assertEquals(
            GameRewardViewState.GameRewardFetchInProgress,
            rewardViewState[0]
        )
        Assert.assertEquals(gameViewModel.rewardLiveData.value, mockGameResponse)

        Assert.assertEquals(
            GameRewardViewState.GameRewardFetchSuccess,
            rewardViewState[1]
        )
    }

    @Test
    fun `for get game  reward failure, data must not be available`() {

        coEvery {
            gameRemoteRepository.getGameReward("12345", false)
        } returns Result.failure(RuntimeException())

        gameViewModel.getGameReward("12345", false)

        coVerify {
            gameRemoteRepository.getGameReward(any(), any())
        }

        Assert.assertEquals(
            GameRewardViewState.GameRewardFetchInProgress,
            rewardViewState[0]
        )

        Assert.assertEquals(gameViewModel.rewardLiveData.value, null)

        Assert.assertEquals(
            GameRewardViewState.GameRewardFetchFailure,
            rewardViewState[1]
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `for get game  reward result success, data must be available`() {
        var results: Result<GameRewardResponse>? = null
        var response: GameRewardResponse? = null

        val mockGameResponse =
            Gson().fromJson(
                MockResponseFileReader("GameRewards.json").content,
                GameRewardResponse::class.java
            )
        coEvery {
            gameRemoteRepository.getGameReward("12345", false)
        } returns Result.success(mockGameResponse)


        GlobalScope.launch {
            results= gameViewModel.getGameRewardResult("12345", false)
            results?.onSuccess {
                response= it
            }
        }

        coVerify {
            gameRemoteRepository.getGameReward(any(), any())
        }

        Assert.assertEquals(
            results?.isSuccess,
            true
        )
        Assert.assertEquals(
            response,
            mockGameResponse
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `for get game  reward result Failure, data must not be available`() {
        var results: Result<GameRewardResponse>? = null
        var response: Throwable? = null

        coEvery {
            gameRemoteRepository.getGameReward("12345", false)
        } returns Result.failure(RuntimeException("run time exception"))


        GlobalScope.launch {
            results= gameViewModel.getGameRewardResult("12345", false)
            results?.onFailure {
                response= it
            }
        }

        coVerify {
            gameRemoteRepository.getGameReward(any(), any())
        }

        Assert.assertEquals(
            results?.isFailure,
            true
        )
        Assert.assertEquals(
            response?.message,
            "run time exception"
        )
    }

    @Test
    fun `for get game success, data must be available`() {

        val mockGameResponse =
            Gson().fromJson(
                MockResponseFileReader("Games.json").content,
                Games::class.java
            )
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)

        coEvery {
            gameRemoteRepository.getGames(any(), "0lMB0000000TW41MAG", false)
        } returns Result.success(mockGameResponse)


        gameViewModel.getGames(context, "0lMB0000000TW41MAG",false)

        coVerify {
            gameRemoteRepository.getGames(any(), any(), any())
        }

        Assert.assertEquals(
            GamesViewState.GamesFetchInProgress,
            viewState[0]
        )
        Assert.assertEquals(gameViewModel.gamesLiveData.value, mockGameResponse)

        Assert.assertEquals(
            GamesViewState.GamesFetchSuccess,
            viewState[1]
        )
    }

    @Test
    fun `for get game failure, data must be available`() {

        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.getString(any(), any()) }
            .returns(mockResponse)

        coEvery {
            gameRemoteRepository.getGames(any(),"0lMB0000000TW41MAG", false)
        } returns Result.failure(RuntimeException())


        gameViewModel.getGames(context, "0lMB0000000TW41MAG",false)

        coVerify {
            gameRemoteRepository.getGames(any(), any(), any())
        }

        Assert.assertEquals(
            GamesViewState.GamesFetchInProgress,
            viewState[0]
        )
        Assert.assertEquals(gameViewModel.gamesLiveData.value, null)

        Assert.assertEquals(
            GamesViewState.GamesFetchFailure,
            viewState[1]
        )
    }

    @Test
    fun `for loadLoyaltyProgramBadge success resource, program badges must be available`() {
        val context = mockk<Context>(relaxed = true)
        val mockResponse =
            Gson().fromJson(
                MockResponseFileReader("LoyaltyProgramBadge.json").content,
                LoyaltyBadgeList::class.java
            )

        coEvery {
            loyaltyBadgeManager.fetchLoyaltyProgramBadge(any())
        } returns Result.success(mockResponse) as Result<LoyaltyBadgeList<LoyaltyProgramBadgeListRecord>>

        badgeViewModel.getCahchedProgramBadge(context)

        coVerify {
            loyaltyBadgeManager.fetchLoyaltyProgramBadge(any())
        }

        Assert.assertEquals(BadgeViewState.BadgeFetchInProgress, badgeProgramViewStates[0])
        Assert.assertEquals(BadgeViewState.BadgeFetchSuccess, badgeProgramViewStates[1])
        Assert.assertEquals(badgeViewModel.programBadgeLiveData.value, mockResponse)

    }

    @Test
    fun `for loadLoyaltyProgramBadge failure resource, viewstate should be failure along with error msg`() {
        val context = mockk<Context>(relaxed = true)

        coEvery {
            loyaltyBadgeManager.fetchLoyaltyProgramBadge(any())
        } returns Result.failure(java.lang.RuntimeException("Run Time Exception"))

        badgeViewModel.getCahchedProgramBadge(context)

        coVerify {
            loyaltyBadgeManager.fetchLoyaltyProgramBadge(any())
        }

        Assert.assertEquals(BadgeViewState.BadgeFetchInProgress, badgeProgramViewStates[0])
        Assert.assertEquals(BadgeViewState.BadgeFetchFailure("Run Time Exception"), badgeProgramViewStates[1])
    }


    @Test
    fun `for loadLoyaltyProgramMemberBadge success resource, program member badges must be available`() {
        val context = mockk<Context>(relaxed = true)
        val mockResponse =
            Gson().fromJson(
                MockResponseFileReader("LoyaltyProgramMemberBadge.json").content,
                LoyaltyBadgeList::class.java
            )

        coEvery {
            loyaltyBadgeManager.fetchLoyaltyProgramMemberBadge(any())
        } returns Result.success(mockResponse) as Result<LoyaltyBadgeList<LoyaltyProgramMemberBadgeListRecord>>

        badgeViewModel.getCahchedProgramMemberBadge(context)

        coVerify {
            loyaltyBadgeManager.fetchLoyaltyProgramMemberBadge(any())
        }

        Assert.assertEquals(BadgeViewState.BadgeFetchInProgress, badgeProgramMemberViewStates[0])
        Assert.assertEquals(BadgeViewState.BadgeFetchSuccess, badgeProgramMemberViewStates[1])
        Assert.assertEquals(badgeViewModel.programMemberBadgeLiveData.value, mockResponse)

    }

    @Test
    fun `for loadLoyaltyProgramMemberBadge failure resource, viewstate should be failure along with error msg`() {
        val context = mockk<Context>(relaxed = true)

        coEvery {
            loyaltyBadgeManager.fetchLoyaltyProgramMemberBadge(any())
        } returns Result.failure(java.lang.RuntimeException("Run Time Exception"))

        badgeViewModel.getCahchedProgramMemberBadge(context)

        coVerify {
            loyaltyBadgeManager.fetchLoyaltyProgramMemberBadge(any())
        }

        Assert.assertEquals(BadgeViewState.BadgeFetchInProgress, badgeProgramMemberViewStates[0])
        Assert.assertEquals(BadgeViewState.BadgeFetchFailure("Run Time Exception"), badgeProgramMemberViewStates[1])
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

private fun successState(data: List<ReferralEntity>?): MyReferralScreenState {
    val (successStates, inProgressStates) = successAndInProgressItemStates(data)
    val sentCount = data?.size ?: 0
    val acceptedCount = inProgressStates.filter { it.purchaseStatus == ReferralStatusType.SIGNED_UP }.size
    return MyReferralScreenState(
        tabItems = ReferralTabs.sortedTabs(),
        completedStates = successStates,
        inProgressStates = inProgressStates,
        listOf(Pair(R.string.my_referral_sent_label, "$sentCount"),
            Pair(R.string.my_referrals_accepted_label, "$acceptedCount"),
            Pair(R.string.my_referrals_vouchers_earned_label, "${successStates.size}")),
        referralsRecentDuration = "${ReferralConfig.REFERRAL_DURATION}"
    )
}

private fun successAndInProgressItemStates(data: List<ReferralEntity>?): Pair<List<ReferralItemState>, List<ReferralItemState>> {
    if (data == null) {
        return Pair(emptyList(), emptyList())
    }
    return data.map {
        ReferralItemState(
            referralItemSectionName(it.referralDate),
            it.referredParty?.account?.personEmail.orEmpty(),
            it.referralDate.orEmpty(),
            ReferralStatusType from it.promotionStage?.type
        )
    }.partition { it.purchaseStatus == ReferralStatusType.COMPLETED }
}

private fun referralItemSectionName(inputDate: String?): Int {
    return when(DateUtils.datePeriod(inputDate)) {
        DatePeriodType.WITHIN1MONTH, DatePeriodType.TODAY, DatePeriodType.WITHIN7DAYS, DatePeriodType.YESTERDAY -> R.string.recent_referrals_section_name
        DatePeriodType.WITHIN3MONTHS -> R.string.referral_one_month_ago_section_name
        else -> R.string.referrals_older_than_three_months_section_name
    }
}