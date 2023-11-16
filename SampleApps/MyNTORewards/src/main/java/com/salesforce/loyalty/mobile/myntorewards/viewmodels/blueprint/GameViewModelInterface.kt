package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Games

interface GameViewModelInterface {

    val rewardTextLiveData: LiveData<String>
    fun getGameReward(mock: Boolean)
    fun getGames(mock: Boolean)
    val gamesLiveData: LiveData<Games>
}