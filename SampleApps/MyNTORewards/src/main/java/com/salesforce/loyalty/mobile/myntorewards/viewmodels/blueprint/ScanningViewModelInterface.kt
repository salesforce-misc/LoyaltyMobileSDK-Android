package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseResponse
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ReceiptListResponse
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptScanningViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptViewState

interface ScanningViewModelInterface {

    fun analyzeExpense(encodedImage: String): AnalyzeExpenseResponse?

    fun getReceiptLists()
    val receiptListLiveData: LiveData<ReceiptListResponse>
    val receiptListViewState: LiveData<ReceiptViewState>
    val scannedReceiptLiveData: LiveData<AnalyzeExpenseResponse>
    val receiptScanningViewStateLiveData: LiveData<ReceiptScanningViewState>
}