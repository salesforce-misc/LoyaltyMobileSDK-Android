package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GameRewardViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GamesViewState
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Games

interface GameViewModelInterface {

    val rewardTextLiveData: LiveData<String>

    val gamesViewState: LiveData<GamesViewState>
    fun getGameReward(mock: Boolean)
    fun getGames(mock: Boolean)
    val gamesLiveData: LiveData<Games>
    val gameRewardsViewState: LiveData<GameRewardViewState>
}