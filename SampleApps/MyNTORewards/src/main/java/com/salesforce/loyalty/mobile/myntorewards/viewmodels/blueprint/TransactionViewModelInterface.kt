package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import android.content.Context
import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.TransactionViewState
import com.salesforce.loyalty.mobile.sources.loyaltyModels.TransactionsResponse

interface TransactionViewModelInterface {
    val transactionsLiveData: LiveData<TransactionsResponse>
    val transactionViewState: LiveData<TransactionViewState>
    fun loadTransactions(context: Context, refreshRequired:Boolean=false)
}