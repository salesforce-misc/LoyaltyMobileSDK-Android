package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import androidx.lifecycle.LiveData

interface GameViewModelInterface {

    val rewardTextLiveData: LiveData<String>
    fun getGameReward(mock: Boolean)
}