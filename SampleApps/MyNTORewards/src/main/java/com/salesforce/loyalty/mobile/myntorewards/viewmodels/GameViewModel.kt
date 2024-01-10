package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salesforce.gamification.model.GameReward
import com.salesforce.gamification.model.GameRewardResponse
import com.salesforce.gamification.model.Games
import com.salesforce.gamification.repository.GamificationRemoteRepository
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.GameViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GameRewardViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GamesViewState
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class GameViewModel @Inject constructor(
    private val repository: GamificationRemoteRepository) : ViewModel(), GameViewModelInterface {
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
        viewModelScope.launch {
            rewardViewState.postValue(GameRewardViewState.GameRewardFetchInProgress)
            val result = repository.getGameReward(gameParticipantRewardId, mock)
            result.onSuccess {
                Logger.d(TAG, "API Result SUCCESS: ${it}")
                rewardMutableLiveData.postValue(it)
                rewardViewState.postValue(GameRewardViewState.GameRewardFetchSuccess)
            }.onFailure {
                Logger.d(TAG, "API Result FAILURE: ${it}")
                rewardViewState.postValue(GameRewardViewState.GameRewardFetchFailure)
            }
        }
    }

    override suspend fun getGameRewardResult(gameParticipantRewardId: String, mock: Boolean): Result<GameRewardResponse> {
        return repository.getGameReward(gameParticipantRewardId, mock)
    }

    override fun getGames(context: Context, gameParticipantRewardId: String?, mock: Boolean) {
        viewState.value = GamesViewState.GamesFetchInProgress
        viewModelScope.launch {
            val memberJson =
                PrefHelper.customPrefs(context)
                    .getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
            if (memberJson == null) {
                viewState.postValue(GamesViewState.GamesFetchFailure)
                return@launch
            }
            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)

            val loyaltyProgramMemberId = member.loyaltyProgramMemberId ?: ""
            val result = repository.getGames(
                participantId = loyaltyProgramMemberId,
                gameParticipantRewardId = gameParticipantRewardId,
                mockResponse = mock
            )

            result.onSuccess {
                games.value = it
                viewState.postValue(GamesViewState.GamesFetchSuccess)
            }.onFailure {
                Logger.d(TAG, "API Result FAILURE: ${it}")
                viewState.postValue(GamesViewState.GamesFetchFailure)
            }
        }
    }

    override fun getGameRewardsFromGameParticipantRewardId(gameParticipantRewardId: String): List<GameReward> {
        return gamesLiveData.value?.gameDefinitions?.firstOrNull { gameDefinition ->
            gameDefinition.participantGameRewards.any { partGameReward ->
                partGameReward.gameParticipantRewardId == gameParticipantRewardId
            }
        }?.gameRewards ?: emptyList()
    }
}
