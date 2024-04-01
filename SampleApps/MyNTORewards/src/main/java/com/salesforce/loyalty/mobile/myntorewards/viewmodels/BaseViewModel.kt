package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Base View Model class, which can be extended by child view models to reuse the ui State object
 */
open class BaseViewModel<T> : ViewModel() {
    protected val uiMutableState: MutableLiveData<T?> = MutableLiveData()
    val uiState: LiveData<T?> = uiMutableState
}