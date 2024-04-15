package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.badge.LoyaltyBadgeManager
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeListProgramMember
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeProgramList
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.BadgeViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.BadgeViewState
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import kotlinx.coroutines.launch


class BadgeViewModel(private val loyaltyBadgeManager: LoyaltyBadgeManager) : ViewModel(),
    BadgeViewModelInterface {

    private val TAG = BadgeViewModel::class.java.simpleName
    override val programMemberBadgeLiveData: LiveData<LoyaltyBadgeListProgramMember>
        get() = program_member_badges
    private val program_member_badges =
        MutableLiveData<LoyaltyBadgeListProgramMember>()

    override val programBadgeLiveData: LiveData<LoyaltyBadgeProgramList>
        get() = programBadges

    private val programBadges = MutableLiveData<LoyaltyBadgeProgramList>()

    override val badgeProgramViewState: LiveData<BadgeViewState>
        get() = viewStateProgram

    private val viewStateProgram = MutableLiveData<BadgeViewState>()
    override val badgeProgramMemberViewState: LiveData<BadgeViewState>
        get() = viewStateProgramMember

    private val viewStateProgramMember = MutableLiveData<BadgeViewState>()





    override fun getCahchedProgramMemberBadge(context: Context, refreshRequired:Boolean) {
        viewStateProgramMember.postValue(BadgeViewState.BadgeFetchInProgress)
        viewModelScope.launch {
            val memberJson =
                PrefHelper.customPrefs(context).getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
            if (memberJson == null) {
                Logger.d(TAG, "failed: member getBadge Member details not present")
                return@launch
            }
            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)
            val membershipKey = member.membershipNumber ?: ""
            if(refreshRequired)
            {
                loadLoyaltyProgramMemberBadge(context, membershipKey)
            }
            else{
                val badgeCache = LocalFileManager.getData(
                    context,
                    membershipKey,
                    LocalFileManager.DIRECTORY_PROGRAM_MEMBER_BADGES,
                    LoyaltyBadgeListProgramMember::class.java
                )

                Logger.d(TAG, "cache : $badgeCache")
                if (badgeCache == null) {
                    loadLoyaltyProgramMemberBadge(context, membershipKey)
                } else {
                     program_member_badges.value = badgeCache!!
                    viewStateProgramMember.postValue(BadgeViewState.BadgeFetchSuccess)
                }
            }

        }
    }


    override fun loadLoyaltyProgramMemberBadge(context: Context, membershipKey:String) {
        viewStateProgramMember.postValue(BadgeViewState.BadgeFetchInProgress)
        viewModelScope.launch {
            loyaltyBadgeManager.fetchLoyaltyProgramMemberBadge().onSuccess {
                program_member_badges.value = it
                LocalFileManager.saveData(
                    context,
                    it,
                    membershipKey,
                    LocalFileManager.DIRECTORY_PROGRAM_MEMBER_BADGES
                )

                viewStateProgramMember.postValue(BadgeViewState.BadgeFetchSuccess)

                Logger.d(TAG, "**** loadLoyaltyProgramMemberBadge *******" + it.toString())
            }.onFailure {
                Logger.d(TAG, "loadLoyaltyProgramMemberBadge failed: ${it.message}")
                viewStateProgramMember.postValue(BadgeViewState.BadgeFetchFailure(it.message))
                Logger.d(TAG, "failed ***********" + it.toString())
            }
        }

    }


    override fun getCahchedProgramBadge(context: Context, refreshRequired:Boolean) {
        viewStateProgram.postValue(BadgeViewState.BadgeFetchInProgress)
        viewModelScope.launch {
            val memberJson =
                PrefHelper.customPrefs(context).getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
            if (memberJson == null) {
                Logger.d(TAG, "failed: member getBadge Member details not present")
                return@launch
            }
            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)
            val membershipKey = member.membershipNumber ?: ""

            if(refreshRequired)
            {
                loadLoyaltyProgramBadge(context, membershipKey)
            }
            else{
                var badgeCache = LocalFileManager.getData(
                    context,
                    membershipKey,
                    LocalFileManager.DIRECTORY_PROGRAM_BADGES,
                    LoyaltyBadgeProgramList::class.java
                )

                Logger.d(TAG, "cache : $badgeCache")
                if (badgeCache == null || badgeCache.records == null) {
                    loadLoyaltyProgramBadge(context, membershipKey)
                } else {
                    programBadges.value = badgeCache!!
                    viewStateProgram.postValue(BadgeViewState.BadgeFetchSuccess)
                }
            }

        }
    }


    override fun loadLoyaltyProgramBadge(context: Context, membershipKey:String) {
        viewModelScope.launch {
            viewStateProgram.postValue(BadgeViewState.BadgeFetchInProgress)
            loyaltyBadgeManager.fetchLoyaltyProgramBadge().onSuccess {

                programBadges.value = it

                LocalFileManager.saveData(
                    context,
                    it,
                    membershipKey,
                    LocalFileManager.DIRECTORY_PROGRAM_BADGES
                )

                viewStateProgram.postValue(BadgeViewState.BadgeFetchSuccess)

                Logger.d(TAG, "loadLoyaltyProgramBadge" + it.toString())
            }.onFailure {
                Logger.d(TAG, "loadLoyaltyProgramBadge call failed: ${it.message}")
                viewStateProgram.postValue(BadgeViewState.BadgeFetchFailure(it.message))
                Logger.d(TAG, "failed ***********" + it.toString())
            }
        }
    }
}