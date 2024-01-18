package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import android.content.Context
import androidx.lifecycle.LiveData
import com.salesforce.gamification.model.GameDefinition
import com.salesforce.gamification.model.GameRewardResponse
import com.salesforce.gamification.model.Games
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GameRewardViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GamesViewState

interface GameViewModelInterface {

    val rewardLiveData: LiveData<GameRewardResponse>

    val gamesViewState: LiveData<GamesViewState>
    fun getGameReward(gameParticipantRewardId: String, mock: Boolean)
    fun getGames(context: Context, gameParticipantRewardId: String? = null, mock: Boolean)
    val gamesLiveData: LiveData<Games>
    suspend fun getGameRewardResult(gameParticipantRewardId: String, mock: Boolean): Result<GameRewardResponse>
    val gameRewardsViewState: LiveData<GameRewardViewState>

    fun getGameDefinitionFromGameParticipantRewardId(gameParticipantRewardId: String): GameDefinition?
}