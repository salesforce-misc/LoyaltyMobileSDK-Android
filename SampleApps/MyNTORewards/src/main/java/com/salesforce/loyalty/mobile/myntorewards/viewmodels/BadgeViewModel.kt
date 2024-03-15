package com.salesforce.loyalty.mobile.myntorewards.viewmodels
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.salesforce.loyalty.mobile.myntorewards.badge.LoyaltyBadgeManager
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeList
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager.TAG
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.BadgeViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.BadgeViewState
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger


class BadgeViewModel(private val loyaltyBadgeManager: LoyaltyBadgeManager) : ViewModel(), BadgeViewModelInterface {
    override val badgeLiveData: LiveData<LoyaltyBadgeList>
        get() = badges

    private val badges = MutableLiveData<LoyaltyBadgeList>()

    override val badgeViewState: LiveData<BadgeViewState>
        get() = viewState

    private val viewState = MutableLiveData<BadgeViewState>()


    override suspend fun loadBadge(context: Context) {
        viewState.postValue(BadgeViewState.BadgeFetchInProgress)
        loyaltyBadgeManager.fetchBadgeList("").onSuccess {
            viewState.postValue(BadgeViewState.BadgeFetchSuccess)
            badges.value = it
            Log.d(TAG, "***********"+it.toString())
        }.onFailure {
            Logger.d(TAG, "badge call failed: ${it.message}")
            Log.d(TAG, "failed ***********"+it.toString())
        }
    }

    override suspend fun loadBadgeDetails(context: Context) {
        viewState.postValue(BadgeViewState.BadgeFetchInProgress)
        loyaltyBadgeManager.fetchBadgeDetails("").onSuccess {
            viewState.postValue(BadgeViewState.BadgeFetchSuccess)
            Log.d(TAG, "***********"+it.toString())
        }.onFailure {
            Logger.d(TAG, "badge call failed: ${it.message}")
            Log.d(TAG, "failed ***********"+it.toString())
            //viewState.postValue(ReceiptViewState.ReceiptListFetchFailureView)
        }
    }

}