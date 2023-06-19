package com.salesforce.loyalty.mobile.myntorewards

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MembershipBenefitViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.TransactionsViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.VoucherViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.BenefitViewStates
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.TransactionViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.VoucherViewState
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberBenefitsResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.TransactionsResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.VoucherResult
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class SampleAppViewModelTest
{
    @get:Rule
    val rule = MainDispatcherRule()


    private lateinit var benefitViewModel: MembershipBenefitViewModel
    private lateinit var transactionViewModel: TransactionsViewModel
    private lateinit var voucherViewModel: VoucherViewModel

    private val loyaltyAPIManager: LoyaltyAPIManager = mockk()
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

            Log.d("Akash", "state "+ it)
            voucherViewStates.add(it)
        }

    }

    @After
      fun tearDown() {
           Dispatchers.resetMain()
     }





    @Test
    fun `for benefit failure resource, data must be available`()
    {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every{context.getSharedPreferences(any(),any())}
            .returns(sharedPrefs)
        every{sharedPrefs.getString(any(), any())}
            .returns(mockResponse)

        val value = Result.failure<MemberBenefitsResponse>(Exception("HTTP 401 Unauthorized"))
        coEvery {
            loyaltyAPIManager.getMemberBenefits(any(), any())
        } returns value

        benefitViewModel.loadBenefits(context , true)

        coVerify {
            loyaltyAPIManager.getMemberBenefits(any(), any())
        }

        Assert.assertEquals(BenefitViewStates.BenefitFetchInProgress, viewStates[0])
        Assert.assertEquals(BenefitViewStates.BenefitFetchFailure, viewStates[1])
    }


    @Test
    fun `for benefit success resource, data must be available`()
    {

        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every{context.getSharedPreferences(any(),any())}
            .returns(sharedPrefs)
        every{sharedPrefs.getString(any(), any())}
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

        benefitViewModel.loadBenefits(context , true)

        coVerify {
            loyaltyAPIManager.getMemberBenefits(any(), any())
        }

       // coVerify{localManager.saveData<MemberBenefitsResponse>(any(), any(), any(), any())}

        Assert.assertEquals(BenefitViewStates.BenefitFetchInProgress, viewStates[0])
        Assert.assertEquals(BenefitViewStates.BenefitFetchSuccess, viewStates[1])
        Assert.assertEquals(benefitViewModel.membershipBenefitLiveData.value, mockMemberBenefitsResponse.memberBenefits)

       // verify(LocalFileManager).saveData<MemberBenefitsResponse>(context, mockMemberBenefitsResponse, membershipKey, LocalFileManager.DIRECTORY_BENEFITS)

//
//        verify {
//            LocalFileManager.saveData<MemberBenefitsResponse>(context, mockMemberBenefitsResponse, membershipKey, LocalFileManager.DIRECTORY_BENEFITS)
//        }
    }

    @Test
    fun `for benefit success resource, cache data must be available`()
    {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every{context.getSharedPreferences(any(),any())}
            .returns(sharedPrefs)
        every{sharedPrefs.getString(any(), any())}
            .returns(mockResponseInfo)

        val mockResponse = MockResponseFileReader("Benefits.json").content
        val mockMemberBenefitsResponse =
            Gson().fromJson(mockResponse, MemberBenefitsResponse::class.java)


        benefitViewModel.loadBenefits(context , false)

        Assert.assertEquals(BenefitViewStates.BenefitFetchInProgress, viewStates[0])
        Assert.assertEquals(BenefitViewStates.BenefitFetchSuccess, viewStates[1])
        Assert.assertEquals(benefitViewModel.membershipBenefitLiveData.value, mockMemberBenefitsResponse.memberBenefits)
    }

    @Test
    fun `for transaction failure resource, data must be available`()
    {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every{context.getSharedPreferences(any(),any())}
            .returns(sharedPrefs)
        every{sharedPrefs.getString(any(), any())}
            .returns(mockResponse)

        val value = Result.failure<TransactionsResponse>(Exception("HTTP 401 Unauthorized"))
        coEvery {
            loyaltyAPIManager.getTransactions(any(), any(), any(), any(), any(), any())
        } returns value

        transactionViewModel.loadTransactions(context , true)

        coVerify {
            loyaltyAPIManager.getTransactions(any(), any(), any(), any(), any(), any())
        }

        Assert.assertEquals(TransactionViewState.TransactionFetchInProgress, tranViewStates[0])
        Assert.assertEquals(TransactionViewState.TransactionFetchFailure, tranViewStates[1])
    }

    @Test
    fun `for voucher failure resource, data must be available`()
    {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponse = MockResponseFileReader("MemberInfo.json").content
        every{context.getSharedPreferences(any(),any())}
            .returns(sharedPrefs)
        every{sharedPrefs.getString(any(), any())}
            .returns(mockResponse)


        val value = Result.failure<VoucherResult>(Exception("HTTP 401 Unauthorized"))
        coEvery {
            loyaltyAPIManager.getVouchers(any(), any(), any(), any(), any(), any(), any())
        } returns value

        voucherViewModel.loadVoucher(context , true)

        coVerify {
            loyaltyAPIManager.getVouchers(any(), any(), any(), any(), any(), any(), any())
        }

        Assert.assertEquals(VoucherViewState.VoucherFetchInProgress, voucherViewStates[0])
        Assert.assertEquals(VoucherViewState.VoucherFetchFailure, voucherViewStates[1])
    }

    @Test
    fun `for voucher success resource, data must be available`()
    {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every{context.getSharedPreferences(any(),any())}
            .returns(sharedPrefs)
        every{sharedPrefs.getString(any(), any())}
            .returns(mockResponseInfo)

        val mockResponse =
            Gson().fromJson(MockResponseFileReader("GetVouchers.json").content, VoucherResult::class.java)

        coEvery {
            loyaltyAPIManager.getVouchers(any(), any(), any(), any(), any(), any(), any())
        } returns Result.success(mockResponse)

        voucherViewModel.loadVoucher(context , true)

        coVerify {
            loyaltyAPIManager.getVouchers(any(), any(), any(), any(), any(), any(), any())
        }

        Assert.assertEquals(VoucherViewState.VoucherFetchInProgress, voucherViewStates[0])
        Assert.assertEquals(VoucherViewState.VoucherFetchSuccess, voucherViewStates[1])
        Assert.assertEquals(voucherViewModel.voucherLiveData.value, mockResponse.voucherResponse)

    }

    @Test
    fun `for voucher success resource, cache data must be available`()
    {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every{context.getSharedPreferences(any(),any())}
            .returns(sharedPrefs)
        every{sharedPrefs.getString(any(), any())}
            .returns(mockResponseInfo)

        val mockResponse =
            Gson().fromJson(MockResponseFileReader("GetVouchers.json").content, VoucherResult::class.java)

        voucherViewModel.loadVoucher(context , false)

        Assert.assertEquals(VoucherViewState.VoucherFetchInProgress, voucherViewStates[0])
        Assert.assertEquals(VoucherViewState.VoucherFetchSuccess, voucherViewStates[1])
        Assert.assertEquals(voucherViewModel.voucherLiveData.value, mockResponse.voucherResponse)
    }


    @Test
    fun `for transaction success resource, data must be available`()
    {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every{context.getSharedPreferences(any(),any())}
            .returns(sharedPrefs)
        every{sharedPrefs.getString(any(), any())}
            .returns(mockResponseInfo)

        val mockResponse =
            Gson().fromJson(MockResponseFileReader("Transactions.json").content, TransactionsResponse::class.java)

        coEvery {
            loyaltyAPIManager.getTransactions(any(), any(), any(), any(), any(), any())
        } returns Result.success(mockResponse)

        transactionViewModel.loadTransactions(context , true)

        coVerify {
            loyaltyAPIManager.getTransactions(any(), any(), any(), any(), any(), any())

        }

        Assert.assertEquals(TransactionViewState.TransactionFetchInProgress, tranViewStates[0])
        Assert.assertEquals(TransactionViewState.TransactionFetchSuccess, tranViewStates[1])
        Assert.assertEquals(transactionViewModel.transactionsLiveData.value, mockResponse)

    }


    @Test
    fun `for transaction success resource, cache data must be available`()
    {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val mockResponseInfo = MockResponseFileReader("MemberInfo.json").content
        every{context.getSharedPreferences(any(),any())}
            .returns(sharedPrefs)
        every{sharedPrefs.getString(any(), any())}
            .returns(mockResponseInfo)

        val mockResponse =
            Gson().fromJson(MockResponseFileReader("Transactions.json").content, TransactionsResponse::class.java)


        transactionViewModel.loadTransactions(context , false)

        Assert.assertEquals(TransactionViewState.TransactionFetchInProgress, tranViewStates[0])
        Assert.assertEquals(TransactionViewState.TransactionFetchSuccess, tranViewStates[1])
        Assert.assertEquals(transactionViewModel.transactionsLiveData.value, mockResponse)
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