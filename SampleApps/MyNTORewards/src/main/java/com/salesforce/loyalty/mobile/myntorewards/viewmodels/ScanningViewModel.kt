package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.ReceiptScanningManager
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ReceiptListResponse
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.ScanningViewModelInterface
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import kotlinx.coroutines.launch

class ScanningViewModel(private val receiptScanningManager: ReceiptScanningManager) : ViewModel(),
    ScanningViewModelInterface {
    private val TAG = ScanningViewModel::class.java.simpleName

    data class Receipt(
        val receiptNumber: String,
        val price: String,
        val date: String,
        val points: String
    )


    override fun getReceiptLists(): List<Receipt> {
        val receiptLists = mutableListOf<Receipt>()

        Logger.d(TAG, "ReceiptList")
        var result: ReceiptListResponse? = null
        viewModelScope.launch {
            receiptScanningManager.receiptList().onSuccess {
                Logger.d(TAG, "Akash Success : $it")
                result = it
                result?.let {
                    for(item in it.records){
                        receiptLists.add(Receipt(item.ReceiptId__c, item.TotalAmount__c.toString(), item.Purchase_Date__c, ""))
                    }
                }

            }
                .onFailure {
                    Logger.d(TAG, "Akash failed: ${it.message}")
                }
        }


        /*val receiptLists = listOf(
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
        )*/
        return receiptLists
    }

    override fun analyzeExpense(encodedImage: String): String? {
        Logger.d(TAG, "analyzeExpense")
        var result: String? = null
        viewModelScope.launch {

            receiptScanningManager.analyzeExpense(encodedImage).onSuccess {
                Logger.d(TAG, "analyzeExpense Success : $it")
                result = it
            }
                .onFailure {
                    Logger.d(TAG, "analyzeExpense failed: ${it.message}")
                }

        }
        return result
    }


}