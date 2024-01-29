package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralConfig.REFERRAL_DURATION
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralConfig.REFERRAL_PROGRAM_NAME
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralConfig.REFERRAL_PROMO_CODE
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralsLocalRepository
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEnrollmentInfo
import com.salesforce.loyalty.mobile.myntorewards.referrals.entity.ReferralEntity
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.DatePeriodType
import com.salesforce.loyalty.mobile.myntorewards.utilities.DateUtils
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
    instanceUrl: String
): BaseViewModel<MyReferralsViewState>() {

    private val _programState: MutableLiveData<ReferralProgramType> = MutableLiveData(null)
    val programState: LiveData<ReferralProgramType> = _programState

    private val _viewState: MutableLiveData<ReferFriendViewState?> = MutableLiveData(null)
    val viewState: LiveData<ReferFriendViewState?> = _viewState

    var forceRefreshReferralsInfo = false

    init {
        repository.setInstanceUrl(instanceUrl)
    }

    fun onSignUpToReferClicked(email: String) {
        // TODO: Signup to Referral Program with mail id
        _programState.value = ReferralProgramType.JOIN_PROGRAM
    }

    fun startReferring() {
        _programState.value = ReferralProgramType.START_REFERRING
    }

    fun sendReferralMail(context: Context, emails: List<String>) {
        _viewState.value = ReferFriendViewState.ReferFriendInProgress
        viewModelScope.launch {
            when(val result = repository.sendReferrals(referralCode(context), emails)) {
                is ApiResponse.Success -> {
                    _viewState.value = ReferFriendViewState.ReferFriendSendMailsSuccess
                    forceRefreshReferralsInfo = true
                }
                is ApiResponse.Error -> {
                    _viewState.value = ReferFriendViewState.ReferFriendSendMailsFailed(result.errorMessage)
                }
                ApiResponse.NetworkError -> _viewState.value = ReferFriendViewState.ReferFriendSendMailsFailed()
            }
        }
    }

    fun resetViewState() {
        _viewState.value = null
    }

    fun fetchReferralProgramStatus(context: Context) {
        uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchInProgress)
        viewModelScope.launch {
            val member = getMember(context)
            val memberId = member?.loyaltyProgramMemberId.orEmpty()
            when(val result = localRepository.checkIfMemberEnrolled(REFERRAL_PROMO_CODE, memberId)) {
                is ApiResponse.Success -> {
                    val data: List<ReferralEnrollmentInfo>? = result.data.records
                    if (data?.firstOrNull()?.loyaltyProgramMemberId == memberId){
                        updateReferralEnrollmentStatusInPreferences(context)
                        fetchReferralsInfo(context)
                    } else {
                        uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchSuccess(successState(null)))
                        _programState.value = ReferralProgramType.JOIN_PROGRAM
                    }
                }
                is ApiResponse.Error -> {
                    uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchFailure(result.errorMessage))
                }
                ApiResponse.NetworkError -> uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchFailure())
            }
        }
    }

    fun fetchReferralsInfo(context: Context) {
        if (referralCode(context).isEmpty()) {
            fetchReferralAndPromotionCode(context)
        }
        uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchInProgress)
        viewModelScope.launch {
            val member = getMember(context)
            when(val result = localRepository.fetchReferralsInfo(member?.contactId.orEmpty(), REFERRAL_PROMO_CODE, REFERRAL_DURATION)) {
                is ApiResponse.Success -> {
                    val data: List<ReferralEntity>? = result.data.records
                    Logger.d("Success", "$data")
                    uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchSuccess(successState(data)))
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
            when(val result = localRepository.fetchMemberReferralCode(getMember(context)?.contactId.orEmpty())) {
                is ApiResponse.Success -> {
                    result.data.records?.firstOrNull()?.referralCode?.let {
                        setReferralCode(context, "$it-$REFERRAL_PROMO_CODE")
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
                        REFERRAL_PROGRAM_NAME, REFERRAL_PROMO_CODE, member.contactId
                    )
                }
                member?.membershipNumber != null -> {
                    repository.enrollExistingAdvocateToPromotionWithMembershipNumber(
                        REFERRAL_PROGRAM_NAME, REFERRAL_PROMO_CODE, member.membershipNumber
                    )
                }
                else -> enrollNewCustomerAsAdvocateOfPromotion(REFERRAL_PROGRAM_NAME, REFERRAL_PROMO_CODE, member)
            }
            when(result) {
                is ApiResponse.Success -> {
                    val enrollmentResponse = result.data
                    val status = enrollmentResponse.transactionJournals.firstOrNull()?.status
                    // If enrolment is processed successfully, start referring friends else show error message
                    if (status == EnrollmentStatus.PROCESSED.status) {
                        _programState.value = ReferralProgramType.START_REFERRING
                        setReferralCode(context, enrollmentResponse.promotionReferralCode)
                        updateReferralEnrollmentStatusInPreferences(context)
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

    private fun updateReferralEnrollmentStatusInPreferences(context: Context) {
        PrefHelper.customPrefs(context)[AppConstants.REFERRAL_PROGRAM_JOINED] = true
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

    private fun getMember(context: Context): CommunityMemberModel? {
        val memberJson = PrefHelper.customPrefs(context).getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
        return if (memberJson != null) {
            Gson().fromJson(memberJson, CommunityMemberModel::class.java)
        } else {
            Logger.d("enrollToReferralProgram", "failed: member promotion Member details not present")
            null
        }
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

    fun referralCode(context: Context): String {
        return PrefHelper.customPrefs(context).getString(AppConstants.KEY_PROMOTION_REFERRAL_CODE, null).orEmpty()
    }

    private fun setReferralCode(context: Context, promotionReferralCode: String) {
        PrefHelper.customPrefs(context)
            .set(AppConstants.KEY_PROMOTION_REFERRAL_CODE, promotionReferralCode)
    }
}