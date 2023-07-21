package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import androidx.lifecycle.ViewModel

class ScanningViewModel(/*private val loyaltyAPIManager: LoyaltyAPIManager*/) : ViewModel() {
    private val TAG = ScanningViewModel::class.java.simpleName

    data class Receipt(
        val receiptNumber: String,
        val price: String,
        val date: String,
        val points: String
    )


    fun getReceiptLists(): List<Receipt> {
        val receiptLists = listOf(
            Receipt("98765", "INR 32392", "13-07-2023", "434"),
            Receipt("12345", "INR 32392", "13-07-2023", "434"),
            Receipt("23545", "INR 32392", "13-07-2023", "434"),
            Receipt("45676", "INR 32392", "13-07-2023", "434"),
            Receipt("34567", "INR 32392", "13-07-2023", "434"),
            Receipt("23456", "INR 32392", "13-07-2023", "434"),
            Receipt("123789", "INR 32392", "13-07-2023", "434"),
            Receipt("345567", "INR 32392", "13-07-2023", "434"),
            Receipt("234509", "INR 32392", "13-07-2023", "434"),
            Receipt("567845", "INR 32392", "13-07-2023", "434")
        )
        return receiptLists
    }
}