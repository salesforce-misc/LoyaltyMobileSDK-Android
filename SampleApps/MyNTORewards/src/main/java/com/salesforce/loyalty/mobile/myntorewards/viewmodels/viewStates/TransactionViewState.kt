package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class TransactionViewState {
        object TransactionFetchSuccess : TransactionViewState()
        object TransactionFetchFailure: TransactionViewState()
        object TransactionFetchInProgress : TransactionViewState()
}