package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<T> : ViewModel() {

    protected val _uiState: MutableLiveData<T> = MutableLiveData()
    val uiState: LiveData<T> = _uiState


}