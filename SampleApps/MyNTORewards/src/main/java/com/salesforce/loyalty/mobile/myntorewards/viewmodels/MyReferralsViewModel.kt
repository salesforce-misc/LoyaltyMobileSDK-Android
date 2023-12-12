package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.utilities.DatePeriodType
import com.salesforce.loyalty.mobile.myntorewards.utilities.DateUtils
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralScreenState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralsViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReferralItemState
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferralStatusType
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ReferralTabs
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import com.salesforce.referral_sdk.api.ApiResponse
import com.salesforce.referral_sdk.entities.ReferralEntity
import com.salesforce.referral_sdk.repository.ReferralsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
@HiltViewModel
class MyReferralsViewModel @Inject constructor(
    private val repository: ReferralsRepository
): BaseViewModel<MyReferralsViewState>() {

    init {
        fetchReferralsInfo()
    }

    private fun fetchReferralsInfo() {
        uiMutableState.postValue(MyReferralsViewState.MyReferralsFetchInProgress)
        viewModelScope.launch {
            when(val result = repository.fetchReferralsInfo()) {
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

    private fun successState(data: List<ReferralEntity>?): MyReferralScreenState {
        val (successStates, inProgressStates) = successAndInProgressItemStates(data)
        val sentCount = data?.size ?: 0
        val acceptedCount = inProgressStates.filter { it.purchaseStatus == ReferralStatusType.SIGNED_UP }.size
        return MyReferralScreenState(
            tabItems = listOf(ReferralTabs.Success.tabName, ReferralTabs.InProgress.tabName),
            completedStates = successStates,
            inProgressStates = inProgressStates,
            listOf(Pair(R.string.my_referral_sent_label, "$sentCount"),
                Pair(R.string.my_referrals_accepted_label, "$acceptedCount"),
                Pair(R.string.my_referrals_vouchers_earned_label, "${successStates.size}")),
            referralsRecentDuration = "90"
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
}

