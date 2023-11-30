package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import android.content.Context
import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GameRewardViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GamesViewState
import com.salesforce.loyalty.mobile.sources.loyaltyModels.GameRewardResponse
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Games

interface GameViewModelInterface {

    val rewardLiveData: LiveData<GameRewardResponse>

    val gamesViewState: LiveData<GamesViewState>
    fun getGameReward(gameParticipantRewardId: String, mock: Boolean)
    fun getGames(context: Context, mock: Boolean)
    val gamesLiveData: LiveData<Games>
    suspend fun getGameRewardResult(gameParticipantRewardId: String, mock: Boolean): Result<GameRewardResponse>
    val gameRewardsViewState: LiveData<GameRewardViewState>
}