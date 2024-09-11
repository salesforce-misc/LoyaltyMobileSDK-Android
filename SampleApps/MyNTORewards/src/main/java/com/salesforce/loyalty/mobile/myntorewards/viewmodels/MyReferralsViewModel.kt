package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.AppSettings
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralConfig.REFERRAL_DEFAULT_PROMOTION_CODE
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralConfig.REFERRAL_DURATION
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralConfig.REFERRAL_PROGRAM_NAME
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralsLocalRepository
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnrollmentInfo
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEntity
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralPromotionStatusAndPromoCode
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralsInfoEntity
import com.salesforce.loyalty.mobile.myntorewards.utilities.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.getMember
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.isEndDateExpired
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralsViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReferFriendViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReferralItemState
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralProgramType
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralStatusType
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ReferralTabs.Companion.sortedTabs
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.set
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import com.salesforce.referral.EnrollmentStatus
import com.salesforce.referral.api.ApiResponse
import com.salesforce.referral.entities.ReferralEnrollmentResponse
import com.salesforce.referral.repository.ReferralsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyReferralsViewModel @Inject constructor(
    private val repository: ReferralsRepository,
    private val localRepository: ReferralsLocalRepository,
    forceAuthManager: ForceAuthManager
): BaseViewModel<MyReferralsViewState>() {

    private val _programState: MutableLiveData<ReferralProgramType> = MutableLiveData()
    val programState: LiveData<ReferralProgramType> = _programState

    private val _viewState: MutableLiveData<ReferFriendViewState?> = MutableLiveData()
    val viewState: LiveData<ReferFriendViewState?> = _viewState

    var forceRefreshReferralsInfo = false
    private var promotionCode: String = REFERRAL_DEFAULT_PROMOTION_CODE

    private var emailsToSend: List<String> = mutableListOf()
    init {
        val communityBaseUrl = forceAuthManager.getCommunityUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.communityUrl
        repository.setInstanceUrl(communityBaseUrl)
    }

    fun onSignUpToReferClicked(email: String) {
        //NOTE: This UI state is never called as user is already logged in to this sample app to access Referral feature
        _programState.value = ReferralProgramType.JOIN_PROGRAM
    }

    fun startReferring() {
        _programState.value = ReferralProgramType.START_REFERRING
    }

    fun retryReferralMailEvent(context: Context) {
        sendReferralMail(context, emailsToSend)
    }

    fun sendReferralMail(context: Context, emails: List<String>) {
        emailsToSend = emails
        _viewState.value = ReferFriendViewState.ReferFriendInProgress
        viewModelScope.launch {
            when(val result = repository.sendReferrals(referralCode(context).orEmpty(), emails)) {
                is ApiResponse.Success -> {
                    _viewState.value = ReferFriendViewState.ReferFriendSendMailsSuccess
                    forceRefreshReferralsInfo = true
                    startReferring()
                }
                is ApiResponse.Error -> {
                    _programState.value = ReferralProgramType.ERROR(result.errorMessage)
                    _viewState.value = ReferFriendViewState.ReferFriendSendMailsFailed(result.errorMessage)
                }
                ApiResponse.NetworkError -> {
                    _programState.value = ReferralProgramType.ERROR()
                    _viewState.value = ReferFriendViewState.ReferFriendSendMailsFailed()
                }
            }
        }
    }

    fun resetViewState() {
        _viewState.value = null
    }

    fun fetchReferralProgramStatus(context: Context) {
        if (referralCode(context).isNullOrEmpty()) {
            fetchReferralAndPromotionCode(context)
        }
        uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchInProgress)
        viewModelScope.launch {
            val member = getMember(context)
            val memberId = member?.contactId.orEmpty()
            when(val result = localRepository.checkIfMemberEnrolled(promotionCode, memberId)) {
                is ApiResponse.Success -> {
                    val data: List<ReferralEnrollmentInfo> = result.data.records.orEmpty()
                    // If data is available that indicates, user is already enrolled to the given Promotion
                    updateReferralEnrollmentStatusInPreferences(context, isEnrolled = data.isNotEmpty())
                    if (data.isNotEmpty()) {
                        _programState.value = ReferralProgramType.START_REFERRING
                        uiMutableState.postValue(MyReferralsViewState.MyReferralsPromotionEnrolled)
                    } else {
                        _programState.value = ReferralProgramType.JOIN_PROGRAM
                        uiMutableState.postValue(MyReferralsViewState.MyReferralsPromotionNotEnrolled)
                    }
                }
                is ApiResponse.Error -> {
                    uiMutableState.postValue(MyReferralsViewState.MyReferralsPromotionStatusFailure(result.errorMessage))
                }
                ApiResponse.NetworkError -> uiMutableState.postValue(MyReferralsViewState.MyReferralsPromotionStatusFailure())
            }
        }
    }

    fun fetchReferralsInfo(context: Context) {
        uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchInProgress)
        viewModelScope.launch {
            val member = getMember(context)
            when(val result = localRepository.fetchReferralsInfo(member?.membershipNumber.orEmpty(), REFERRAL_DURATION)) {
                is ApiResponse.Success -> {
                    val data: ReferralsInfoEntity? = result.data
                    Logger.d("Success", "$data")
                    uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchSuccess(successState(data?.referralList)))
                    forceRefreshReferralsInfo = false
                }
                is ApiResponse.Error -> {
                    uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchFailure(result.errorMessage))
                }
                ApiResponse.NetworkError -> uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchFailure())
            }
        }
    }

    private fun fetchReferralAndPromotionCode(context: Context) {
        viewModelScope.launch {
            when (val result = localRepository.fetchMemberReferralCode(
                getMember(context)?.contactId.orEmpty(),
                getReferralProgramName(context)
            )) {
                is ApiResponse.Success -> {
                    result.data.records?.firstOrNull()?.referralCode?.let {
                        setReferralCode(context, it)
                    } ?: Logger.d("fetchReferralAndPromotionCode", "Failed while fetching referral code")
                }
                else -> Logger.d("fetchReferralAndPromotionCode", "Failed while fetching referral code")
            }
        }
    }


    fun enrollToReferralPromotion(context: Context, isTryAgain: Boolean) {
        if (isTryAgain){
            _programState.value = ReferralProgramType.EMPTY_STATE
        } else {
            _viewState.value = ReferFriendViewState.ReferFriendInProgress
        }
        viewModelScope.launch {
            val member = getMember(context)
            val result = when {
                member?.contactId != null -> {
                    repository.enrollExistingAdvocateToPromotionWithContactId(
                        getReferralProgramName(context), promotionCode, member.contactId
                    )
                }
                member?.membershipNumber != null -> {
                    repository.enrollExistingAdvocateToPromotionWithMembershipNumber(
                        getReferralProgramName(context), promotionCode, member.membershipNumber
                    )
                }
                else -> enrollNewCustomerAsAdvocateOfPromotion(
                    getReferralProgramName(context),
                    promotionCode,
                    member
                )
            }
            when(result) {
                is ApiResponse.Success -> {
                    val enrollmentResponse = result.data
                    val status = enrollmentResponse.transactionJournals.firstOrNull()?.status
                    /*
                    During the Referral Promotion enrolment process, there is a slight possibility that the enrolment request may not be processed immediately.
                    If the Transaction Journal status is not Processed, which means Canceled/Error, you may have to contact the Salesforce support centre to finish the enrolment process.
                    */
                    if (status == EnrollmentStatus.PROCESSED.status || status == EnrollmentStatus.PENDING.status) {
                        _programState.value = ReferralProgramType.START_REFERRING
                        val memberReferralCode = enrollmentResponse.promotionReferralCode.split("-").firstOrNull()
                        setReferralCode(context, memberReferralCode)
                        updateReferralEnrollmentStatusInPreferences(context, isEnrolled = true)
                    } else {
                        val error = context.getString(R.string.enrolment_not_processed_message)
                        _programState.value = ReferralProgramType.ERROR(error)
                    }
                    _viewState.value = ReferFriendViewState.EnrollmentTaskFinished
                }
                is ApiResponse.Error -> {
                    _programState.value = ReferralProgramType.ERROR(result.errorMessage)
                    _viewState.value = ReferFriendViewState.EnrollmentTaskFinished
                }
                ApiResponse.NetworkError -> {
                    _programState.value = ReferralProgramType.ERROR()
                    _viewState.value = ReferFriendViewState.EnrollmentTaskFinished
                }
            }
        }
    }

    private fun updateReferralEnrollmentStatusInPreferences(context: Context, isReferralPromotion: Boolean = true, isEnrolled: Boolean = false) {
        localRepository.saveReferralStatusInCache(promotionCode, isReferralPromotion = isReferralPromotion, isEnrolled = isEnrolled)
    }

     private suspend fun enrollNewCustomerAsAdvocateOfPromotion(
        promotionName: String, promotionCode: String, member: CommunityMemberModel?
    ): ApiResponse<ReferralEnrollmentResponse> {
        return repository.enrollNewCustomerAsAdvocateOfPromotion(
            firstName = member?.firstName.orEmpty(),
            lastName = member?.lastName.orEmpty(),
            email = member?.email.orEmpty(),
            promotionName  = promotionName,
            promotionCode  = promotionCode
        )
    }

    private fun successState(data: List<ReferralEntity>?): MyReferralScreenState {
        val (successStates, inProgressStates) = successAndInProgressItemStates(data)
        val sentCount = data?.size ?: 0
        val acceptedCount = inProgressStates.filter { it.purchaseStatus == ReferralStatusType.SIGNED_UP }.size
        return MyReferralScreenState(
            tabItems = sortedTabs(),
            completedStates = successStates,
            inProgressStates = inProgressStates,
            listOf(Pair(R.string.my_referral_sent_label, "$sentCount"),
                Pair(R.string.my_referrals_accepted_label, "$acceptedCount"),
                Pair(R.string.my_referrals_vouchers_earned_label, "${successStates.size}")),
            referralsRecentDuration = "$REFERRAL_DURATION"
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

    fun referralCode(context: Context): String? {
        val memberReferralCode =
            PrefHelper.customPrefs(context).getString(AppConstants.KEY_MEMBER_REFERRAL_CODE, null)
        return memberReferralCode?.let { "$it-$promotionCode" }
    }

    fun referralLink(promoPageUrl: String): String {
        return "$promoPageUrl?referralCode="
    }

    fun setReferralCode(context: Context, memberReferralCode: String?) {
        PrefHelper.customPrefs(context)
            .set(AppConstants.KEY_MEMBER_REFERRAL_CODE, memberReferralCode)
    }

    /**
     * Check if Promotion status is already loaded and available in cache other wise maki API call
     */
    fun checkIfGivenPromotionIsInCache(context: Context, promotionId: String) {
        uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchInProgress)
        val promoCodeFromCache = localRepository.getPromoCodeFromCache(promotionId)
        setPromoCode(promoCodeFromCache)

        // If promotion code is not available in cache, call API to get the promotion type and promotion code
        if (promoCodeFromCache != null) {
            val (isReferral, isEnrolled) = localRepository.getReferralStatusFromCache(promoCodeFromCache)
            if (isReferral) {
                if (isEnrolled) {
                    _programState.value = ReferralProgramType.START_REFERRING
                    uiMutableState.postValue(MyReferralsViewState.MyReferralsPromotionEnrolled)
                } else {
                    _programState.value = ReferralProgramType.JOIN_PROGRAM
                    uiMutableState.postValue(MyReferralsViewState.MyReferralsPromotionNotEnrolled)
                }
            } else {
                uiMutableState.postValue(MyReferralsViewState.PromotionStateNonReferral)
            }
        } else {
            checkIfGivenPromotionIsReferralAndEnrolled(context, promotionId)
        }
    }

    fun checkIfReferralIsEnabled(forceRefresh: Boolean = false) {
        if (forceRefresh) {
            ReferralsLocalRepository.clearReferralsData()
        }
        uiMutableState.value = null
        uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchInProgress)
        localRepository.isReferralFeatureEnabled()?.let {
            updateReferralEnableStatus(it)
            return
        }
        viewModelScope.launch {
            when(val result = localRepository.checkIfReferralIsEnabled()) {
                is ApiResponse.Success -> {
                    val referralFeatureEnabled = result.data.records?.firstOrNull()?.isReferralPromotion != null
                    updateReferralEnableStatus(referralFeatureEnabled)
                }
                else -> {
                    updateReferralEnableStatus(false)
                }
            }
        }
    }

    private fun updateReferralEnableStatus(referralEnabled: Boolean) {
        localRepository.setReferralFeatureEnabled(referralEnabled)
        if (referralEnabled) {
            uiMutableState.postValue(MyReferralsViewState.ReferralFeatureEnabled)
        } else {
            uiMutableState.postValue(MyReferralsViewState.ReferralFeatureNotEnabled)
        }
    }

    private fun checkIfGivenPromotionIsReferralAndEnrolled(context: Context, promotionId: String) {
        uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchInProgress)
        viewModelScope.launch {
            when(val result = localRepository.checkIfGivenPromotionIsReferralAndEnrolled(promotionId)) {
                is ApiResponse.Success -> {
                    val records = result.data.records
                    val promoDetails = records?.firstOrNull()
                    localRepository.savePromoCodeAndUrlInCache(promotionId, promoDetails)
                    setPromoCode(promoDetails?.promotionCode.orEmpty())
                    if (promoDetails?.isReferralPromotion == true) {
                        fetchReferralProgramStatus(context)
                    } else {
                        updateReferralEnrollmentStatusInPreferences(context, isReferralPromotion = false)
                        uiMutableState.postValue(MyReferralsViewState.PromotionStateNonReferral)
                    }
                }
                is ApiResponse.Error -> {
                    uiMutableState.postValue(MyReferralsViewState.PromotionReferralApiStatusFailure(result.errorMessage))
                }
                ApiResponse.NetworkError -> uiMutableState.postValue(MyReferralsViewState.PromotionReferralApiStatusFailure())
            }
        }
    }

    fun fetchReferralPromotionDetailsFromCache(context: Context, promotionId: String): ReferralPromotionStatusAndPromoCode? {
        return localRepository.getReferralPromotionDetails(promotionId)?.let {
            if (isEndDateExpired(it.endDate)) {
                _programState.value = ReferralProgramType.ERROR(context.getString(R.string.referral_promotion_expired_error_message))
            }
            it
        }
    }

    private fun setPromoCode(promoCode: String?) {
        promoCode?.let { promotionCode = it }
    }

    fun resetProgramTypeOnError() {
        val isEnrolled = localRepository.getReferralStatusFromCache(REFERRAL_DEFAULT_PROMOTION_CODE).second
        if (isEnrolled) {
            _programState.value = ReferralProgramType.START_REFERRING
        } else {
            _programState.value = ReferralProgramType.JOIN_PROGRAM
        }
    }

    private fun getReferralProgramName(context: Context): String {
        return Common.getLoyaltyProgramName(context)
    }
}