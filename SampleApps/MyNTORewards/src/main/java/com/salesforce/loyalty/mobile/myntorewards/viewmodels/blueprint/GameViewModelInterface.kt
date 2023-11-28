package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GameRewardViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GamesViewState
import com.salesforce.loyalty.mobile.sources.loyaltyModels.GameRewardResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Games

interface GameViewModelInterface {

    val rewardLiveData: LiveData<GameRewardResponse>

    val gamesViewState: LiveData<GamesViewState>
    fun getGameReward(mock: Boolean)
    fun getGames(mock: Boolean)
    val gamesLiveData: LiveData<Games>
    suspend fun getGameRewardResult(mock: Boolean): Result<GameRewardResponse>
    val gameRewardsViewState: LiveData<GameRewardViewState>
}