package com.salesforce.loyalty.mobile.myntorewards.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.salesforce.gamification.repository.GamificationRemoteRepository
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.GameViewModel

class GameViewModelFactory(private val gamificationRemoteRepository: GamificationRemoteRepository): ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GameViewModel(gamificationRemoteRepository) as T
    }
}