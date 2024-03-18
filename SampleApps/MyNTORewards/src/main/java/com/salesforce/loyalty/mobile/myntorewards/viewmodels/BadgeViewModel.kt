package com.salesforce.loyalty.mobile.myntorewards.viewmodels
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.salesforce.loyalty.mobile.myntorewards.badge.LoyaltyBadgeManager
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyBadgeList
import com.salesforce.loyalty.mobile.myntorewards.badge.models.RecordBadgeList
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.BadgeViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.BadgeViewState
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger


class BadgeViewModel(private val loyaltyBadgeManager: LoyaltyBadgeManager) : ViewModel(), BadgeViewModelInterface {

    private val TAG = BadgeViewModel::class.java.simpleName
    override val badgeLiveData: LiveData<LoyaltyBadgeList<RecordBadgeList>>
        get() = badges

    private val badges = MutableLiveData<LoyaltyBadgeList<RecordBadgeList>>()

    override val badgeViewState: LiveData<BadgeViewState>
        get() = viewState

    private val viewState = MutableLiveData<BadgeViewState>()


    override suspend fun loadBadge(context: Context) {
        viewState.postValue(BadgeViewState.BadgeFetchInProgress)
        loyaltyBadgeManager.fetchBadgeList("").onSuccess {
            viewState.postValue(BadgeViewState.BadgeFetchSuccess)
            badges.value = it
            Log.d(TAG, "**** Badge List *******"+it.toString())
        }.onFailure {
            Logger.d(TAG, "badge list call failed: ${it.message}")
            Log.d(TAG, "failed ***********"+it.toString())
        }
    }

    override suspend fun loadBadgeDetails(context: Context) {
        viewState.postValue(BadgeViewState.BadgeFetchInProgress)
        loyaltyBadgeManager.fetchBadgeDetails("").onSuccess {
            viewState.postValue(BadgeViewState.BadgeFetchSuccess)
            Log.d(TAG, "***Badge Detail********"+it.toString())
        }.onFailure {
            Logger.d(TAG, "badge detail call failed: ${it.message}")
            Log.d(TAG, "failed ***********"+it.toString())
            //viewState.postValue(ReceiptViewState.ReceiptListFetchFailureView)
        }
    }

}