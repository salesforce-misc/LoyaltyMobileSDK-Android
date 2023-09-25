package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import android.content.Context
import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseResponse
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ReceiptListResponse
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.CreateTransactionJournalViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptScanningViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptStatusUpdateViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptViewState

interface ScanningViewModelInterface {

    fun analyzeExpense(context: Context, encodedImage: ByteArray): AnalyzeExpenseResponse?

    val receiptListLiveData: LiveData<ReceiptListResponse>
    val receiptListViewState: LiveData<ReceiptViewState>
    val scannedReceiptLiveData: LiveData<AnalyzeExpenseResponse>
    val receiptScanningViewStateLiveData: LiveData<ReceiptScanningViewState>
    val createTransactionJournalViewStateLiveData: LiveData<CreateTransactionJournalViewState>
    val receiptStatusUpdateViewStateLiveData: LiveData<ReceiptStatusUpdateViewState>
    fun getReceiptListsAPI(context: Context, membershipKey: String)
    fun getReceiptLists(context: Context, refreshRequired: Boolean = false)
    fun createTransactionalJournal(analyzeExpenseResponse: AnalyzeExpenseResponse)
    fun submitForManualReview(receiptId: String, comments: String?)
    fun submitForProcessing(receiptId: String)
}