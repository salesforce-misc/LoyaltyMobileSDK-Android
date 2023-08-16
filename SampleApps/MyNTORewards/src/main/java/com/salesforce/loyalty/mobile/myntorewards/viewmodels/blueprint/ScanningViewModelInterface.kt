package com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint

import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ScanningViewModel

interface ScanningViewModelInterface {

    fun analyzeExpense(encodedImage: String): String?

    fun getReceiptLists(): List<ScanningViewModel.Receipt>
}