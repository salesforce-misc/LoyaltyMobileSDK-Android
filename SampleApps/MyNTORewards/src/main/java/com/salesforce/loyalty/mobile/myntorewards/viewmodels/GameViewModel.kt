package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.GameViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GameRewardViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GamesViewState
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.GameRewardResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Games
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(private val loyaltyAPIManager: LoyaltyAPIManager) : ViewModel(),
    GameViewModelInterface {
    private val TAG = GameViewModel::class.java.simpleName

    private var rewardMutableLiveData = MutableLiveData<GameRewardResponse>()

    override val rewardLiveData: LiveData<GameRewardResponse>
        get() = rewardMutableLiveData

    override val gamesLiveData: LiveData<Games>
        get() = games

    private val games = MutableLiveData<Games>()

    override val gamesViewState: LiveData<GamesViewState>
        get() = viewState

    private val viewState = MutableLiveData<GamesViewState>()

    override val gameRewardsViewState: LiveData<GameRewardViewState>
        get() = rewardViewState

    private val rewardViewState = MutableLiveData<GameRewardViewState>()

    override fun getGameReward(gameParticipantRewardId: String, mock: Boolean) {
        rewardViewState.postValue(GameRewardViewState.GameRewardFetchInProgress)
        viewModelScope.launch {
            val result = loyaltyAPIManager.getGameReward(gameParticipantRewardId, true)
            result.onSuccess {
                Logger.d(TAG, "API Result SUCCESS: ${it}")
                delay(2000)
                rewardMutableLiveData.postValue(it)
                rewardViewState.postValue(GameRewardViewState.GameRewardFetchSuccess)
            }.onFailure {
                Logger.d(TAG, "API Result FAILURE: ${it}")
                rewardViewState.postValue(GameRewardViewState.GameRewardFetchFailure)
            }
        }
    }

    override suspend fun getGameRewardResult(gameParticipantRewardId: String, mock: Boolean): Result<GameRewardResponse> {
        delay(2000)
        return loyaltyAPIManager.getGameReward(gameParticipantRewardId, true)
    }

    override fun getGames(context: Context, mock: Boolean) {
        viewState.postValue(GamesViewState.GamesFetchInProgress)
        viewModelScope.launch {
            val memberJson =
                PrefHelper.customPrefs(context)
                    .getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
            if (memberJson == null) {
                viewState.postValue(GamesViewState.GamesFetchFailure)
            }
            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)
            val membershipNumber = member.membershipNumber ?: ""
            val result = loyaltyAPIManager.getGames(membershipNumber, true)
            result.onSuccess {
                games.value = it
                viewState.postValue(GamesViewState.GamesFetchSuccess)
            }.onFailure {
                Logger.d(TAG, "API Result FAILURE: ${it}")
                viewState.postValue(GamesViewState.GamesFetchFailure)
            }
        }
    }

}
