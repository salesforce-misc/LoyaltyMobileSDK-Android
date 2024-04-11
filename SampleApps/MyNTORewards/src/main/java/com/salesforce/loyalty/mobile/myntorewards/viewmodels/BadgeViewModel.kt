package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.badge.LoyaltyBadgeManager
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeList
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyProgramBadgeListRecord
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyProgramMemberBadgeListRecord
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.BadgeViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.BadgeViewState
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import kotlinx.coroutines.launch


class BadgeViewModel(private val loyaltyBadgeManager: LoyaltyBadgeManager) : ViewModel(),
    BadgeViewModelInterface {

    private val TAG = BadgeViewModel::class.java.simpleName
    override val programMemberBadgeLiveData: LiveData<LoyaltyBadgeList<LoyaltyProgramMemberBadgeListRecord>>
        get() = program_member_badges
    private val program_member_badges =
        MutableLiveData<LoyaltyBadgeList<LoyaltyProgramMemberBadgeListRecord>>()

    override val programBadgeLiveData: LiveData<LoyaltyBadgeList<LoyaltyProgramBadgeListRecord>>
        get() = programBadges

    private val programBadges = MutableLiveData<LoyaltyBadgeList<LoyaltyProgramBadgeListRecord>>()

    override val badgeProgramViewState: LiveData<BadgeViewState>
        get() = viewStateProgram

    private val viewStateProgram = MutableLiveData<BadgeViewState>()
    override val badgeProgramMemberViewState: LiveData<BadgeViewState>
        get() = viewStateProgramMember

    private val viewStateProgramMember = MutableLiveData<BadgeViewState>()






    override fun loadLoyaltyProgramMemberBadge(context: Context) {
        viewStateProgramMember.postValue(BadgeViewState.BadgeFetchInProgress)
        viewModelScope.launch {
            loyaltyBadgeManager.fetchLoyaltyProgramMemberBadge().onSuccess {
                viewStateProgramMember.postValue(BadgeViewState.BadgeFetchSuccess)
                program_member_badges.value = it
                Logger.d(TAG, "**** loadLoyaltyProgramMemberBadge *******" + it.toString())
            }.onFailure {
                Logger.d(TAG, "loadLoyaltyProgramMemberBadge failed: ${it.message}")
                viewStateProgramMember.postValue(BadgeViewState.BadgeFetchFailure(it.message))
                Logger.d(TAG, "failed ***********" + it.toString())
            }
        }

    }

    override fun loadLoyaltyProgramBadge(context: Context) {
        viewModelScope.launch {
            viewStateProgram.postValue(BadgeViewState.BadgeFetchInProgress)
            loyaltyBadgeManager.fetchLoyaltyProgramBadge().onSuccess {
                viewStateProgram.postValue(BadgeViewState.BadgeFetchSuccess)
                programBadges.value = it
                Logger.d(TAG, "loadLoyaltyProgramBadge" + it.toString())
            }.onFailure {
                Logger.d(TAG, "loadLoyaltyProgramBadge call failed: ${it.message}")
                viewStateProgram.postValue(BadgeViewState.BadgeFetchFailure(it.message))
                Logger.d(TAG, "failed ***********" + it.toString())
            }
        }
    }
}