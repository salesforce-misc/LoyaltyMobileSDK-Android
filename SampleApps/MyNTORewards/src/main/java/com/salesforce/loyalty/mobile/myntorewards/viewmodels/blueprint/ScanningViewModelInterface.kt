package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import android.content.Context
import androidx.lifecycle.LiveData
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ReceiptListResponse
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptScanState

interface ScanningViewModelInterface {

    fun analyzeExpense(encodedImage: String): String?

    val receiptListLiveData: LiveData<ReceiptListResponse>
    val receiptListViewState: LiveData<ReceiptScanState>
    fun getReceiptListsAPI(context: Context, membershipKey: String)
    fun getReceiptLists(context: Context, refreshRequired: Boolean = false)
}