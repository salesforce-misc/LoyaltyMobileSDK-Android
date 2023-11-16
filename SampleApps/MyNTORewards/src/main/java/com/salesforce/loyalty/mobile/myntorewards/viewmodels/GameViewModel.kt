package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.GameViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.VoucherViewState
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Games
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberBenefit
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(private val loyaltyAPIManager: LoyaltyAPIManager) : ViewModel(),
    GameViewModelInterface {
    private val TAG = GameViewModel::class.java.simpleName

    private var rewardTextMutableLiveData = MutableLiveData<String>("Loading...")

    override val rewardTextLiveData: LiveData<String>
        get() = rewardTextMutableLiveData

    override val gamesLiveData: LiveData<Games>
        get() = games

    private val games = MutableLiveData<Games>()


    override fun getGameReward(mock: Boolean) {
        viewModelScope.launch {
            val result = loyaltyAPIManager.getGameReward(true)
            result.onSuccess {
                Logger.d("Canvas", "API Result SUCCESS: ${it}")
                val reward: String? =
                    it?.gameRewards?.get(0)?.description
                delay(2000)
                reward?.let {
                    rewardTextMutableLiveData.postValue(it)
                }
            }.onFailure {
                Logger.d("Canvas", "API Result FAILURE: ${it}")
            }
        }
    }

    override fun getGames(mock: Boolean)
    {
        viewModelScope.launch {
            val result = loyaltyAPIManager.getGames(true)
            result.onSuccess {
                games.value= it
            }.onFailure {
                Logger.d("Canvas", "API Result FAILURE: ${it}")
            }
        }
    }

}
