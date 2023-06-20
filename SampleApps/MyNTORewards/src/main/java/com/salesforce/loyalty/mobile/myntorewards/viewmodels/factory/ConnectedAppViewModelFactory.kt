package com.salesforce.loyalty.mobile.myntorewards.viewmodels.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ConnectedAppViewModel

class ConnectedAppViewModelFactory(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ConnectedAppViewModel(context) as T
    }
}