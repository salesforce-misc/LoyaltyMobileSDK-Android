package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.TransactionsResponse
import kotlinx.coroutines.launch

class TransactionsViewModel : ViewModel() {
    private val TAG = TransactionsViewModel::class.java.simpleName

    //live data for login status
    val transactionsLiveData: LiveData<TransactionsResponse>
        get() = transactions

    private val transactions = MutableLiveData<TransactionsResponse>()

    fun getTransactions(context: Context) {
        viewModelScope.launch {
            var membershipKey =
                PrefHelper.customPrefs(context)[AppConstants.KEY_MEMBERSHIP_NUMBER, ""]
            membershipKey?.let { membershipNumber ->
                LoyaltyAPIManager.getTransactions("M0001", null, null, null, null, null)
                    .onSuccess {
                        transactions.value = it
                        Log.d(TAG, "getTransactions success: $it")
                    }.onFailure {
                        Log.d(TAG, "getTransactions failed: ${it.message}")
                    }
            }
        }
    }
}