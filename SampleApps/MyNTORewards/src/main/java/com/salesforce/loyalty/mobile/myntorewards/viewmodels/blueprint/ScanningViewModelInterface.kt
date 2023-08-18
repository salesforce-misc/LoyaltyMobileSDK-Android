package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ReceiptListResponse
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ScanningViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptScanState

interface ScanningViewModelInterface {

    fun analyzeExpense(encodedImage: String): String?

    fun getReceiptLists()
    val receiptListLiveData: LiveData<ReceiptListResponse>
    val receiptListViewState: LiveData<ReceiptScanState>
}