package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.referrals.ReferralsLocalRepository
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.DatePeriodType
import com.salesforce.loyalty.mobile.myntorewards.utilities.DateUtils
import com.salesforce.loyalty.mobile.myntorewards.utilities.showToast
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
import com.salesforce.referral_sdk.EnrollmentStatus
import com.salesforce.referral_sdk.api.ApiResponse
import com.salesforce.referral_sdk.api.ReferralAPIConfig.REFERRAL_PROGRAM_NAME
import com.salesforce.referral_sdk.entities.ReferralEnrollmentResponse
import com.salesforce.referral_sdk.entities.ReferralEntity
import com.salesforce.referral_sdk.entities.referral_event.ReferralEventResponse
import com.salesforce.referral_sdk.repository.ReferralsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private val _viewState: MutableLiveData<ReferFriendViewState> = MutableLiveData()
    val viewState: LiveData<ReferFriendViewState> = _viewState

    var showDefaultPopup = true

    companion object {
        private const val REFERRAL_DURATION = 90
        // TODO: Check if we need to remove hard coded and use dynamically instead
        private const val PROMO_CODE = "Promo100"
    }

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
        /*if (emails.isEmpty() || emails.any { !isValidEmail(it.trim()) }) {
            uiMutableState.value = ReferFriendViewState.ShowSignupInvalidEmailMessage
//            Toast.makeText(this, getString(R.string.emails_warning_message_multiple), Toast.LENGTH_LONG).show()
            return
        }*/
        _viewState.value = ReferFriendViewState.ReferFriendInProgress
        viewModelScope.launch {
            when(val result = repository.sendReferrals(referralCode(context), emails)) {
                is ApiResponse.Success -> {
                    val data: ReferralEventResponse = result.data
                    Logger.d("sendReferralMail Success", "$data")
                    context.showToast("Emails sent Successfully!")
                    _viewState.postValue(ReferFriendViewState.ReferFriendSendMailsSuccess(""))
                }
                is ApiResponse.Error -> {
                    context.showToast("Failed, Try again!")
                    Logger.d("sendReferralMail Error", "${result.errorMessage}")
                    _viewState.postValue(ReferFriendViewState.ReferFriendSendMailsSuccess("Failed, Try again!"))
                }
                ApiResponse.NetworkError -> {
                    context.showToast("Failed, Try again!")
                    _viewState.postValue(ReferFriendViewState.ReferFriendSendMailsSuccess("Failed, Try again!"))
                }
            }
        }
    }

    fun fetchReferralProgramStatus() {
        uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchInProgress)
        viewModelScope.launch {
            delay(2000)
            uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchSuccess(successState(null)))
            _programState.value = ReferralProgramType.JOIN_PROGRAM
        }
    }

    fun fetchReferralsInfo(context: Context) {
        if (referralCode(context).isEmpty()) {
            fetchReferralAndPromotionCode(context)
        }
        uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchInProgress)
        viewModelScope.launch {
            val accessToken = ForceAuthManager(context).getForceAuth()?.accessToken ?: ""
            when(val result = localRepository.fetchReferralsInfo(accessToken, REFERRAL_DURATION)) {
                is ApiResponse.Success -> {
                    val data: List<ReferralEntity>? = result.data.records
                    Logger.d("Success", "$data")
                    uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchSuccess(successState(data)))
                }
                is ApiResponse.Error -> {
                    uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchFailure(result.errorMessage))
                }
                ApiResponse.NetworkError -> uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchFailure())
            }
        }
    }

    fun fetchReferralAndPromotionCode(context: Context) {
        val accessToken = ForceAuthManager(context).getForceAuth()?.accessToken ?: ""
        viewModelScope.launch {
            when(val result = localRepository.fetchMemberReferralCode(accessToken, getMember(context)?.membershipNumber.orEmpty())) {
                is ApiResponse.Success -> {
                    result.data.records?.firstOrNull()?.referralCode?.let {
                        setReferralCode(context, "$it-$PROMO_CODE")
                    } ?: Logger.d("fetchReferralAndPromotionCode", "Failed while fetching referral code")
                }
                else -> Logger.d("fetchReferralAndPromotionCode", "Failed while fetching referral code")
            }
        }
    }


    fun enrollToReferralPromotion(context: Context) {
        _viewState.value = ReferFriendViewState.ReferFriendInProgress
        viewModelScope.launch {
            val member = getMember(context)
            val result = when {
                member?.membershipNumber != null -> {
                    repository.enrollExistingAdvocateToPromotionWithMembershipNumber(
                        REFERRAL_PROGRAM_NAME, PROMO_CODE, member.membershipNumber
                    )
                }
                member?.contactId != null -> {
                    repository.enrollExistingAdvocateToPromotionWithContactId(
                        REFERRAL_PROGRAM_NAME, PROMO_CODE, member.contactId
                    )
                }
                else -> enrollNewCustomerAsAdvocateOfPromotion(REFERRAL_PROGRAM_NAME, PROMO_CODE, member)
            }
            when(result) {
                is ApiResponse.Success -> {
                    val enrollmentResponse = result.data
                    val status = enrollmentResponse.transactionJournals.firstOrNull()?.status
                    _programState.value = ReferralProgramType.START_REFERRING
                    setReferralCode(context, enrollmentResponse.promotionReferralCode)
                    PrefHelper.customPrefs(context)[AppConstants.REFERRAL_PROGRAM_JOINED] = true

                    if (status == EnrollmentStatus.PROCESSED.status) {

                    } else {
                        // TODO: What to do if enrollment is not processed
                    }
                    context.showToast("Enrollment $status!")

                    _viewState.value = ReferFriendViewState.ReferFriendSendMailsSuccess("Enrollment $status!")
                    Logger.d("enrollToReferralProgram Success", "$enrollmentResponse")
                }
                is ApiResponse.Error -> {
                    // TODO: REMOVE this line
                    _programState.value = ReferralProgramType.START_REFERRING

//                    _programState.value = ReferralProgramType.ERROR(result.errorMessage)
                    _viewState.value = ReferFriendViewState.EnrollmentFailed(result.errorMessage)

                    Logger.d("enrollToReferralProgram Error", "${result.errorMessage}")
                }
                ApiResponse.NetworkError -> {
                    _programState.value = ReferralProgramType.ERROR()
                    _viewState.value = ReferFriendViewState.EnrollmentFailed()
                    Logger.d("enrollToReferralProgram Error", "N/W error")
                }
            }
        }
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
                it.clientEmail.orEmpty(),
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