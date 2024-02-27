package com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates

sealed class CreateTransactionJournalViewState {
    object CreateTransactionJournalSuccess : CreateTransactionJournalViewState()
    object CreateTransactionJournalFailure : CreateTransactionJournalViewState()
    object CreateTransactionJournalInProgress : CreateTransactionJournalViewState()
}