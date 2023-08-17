package com.salesforce.loyalty.mobile.myntorewards.receiptscanning

import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.NetworkClient
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseRequest
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ReceiptListResponse
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger

class ReceiptScanningManager constructor(auth: ForceAuthenticator, instanceUrl: String) {
    companion object {
        private const val TAG = "ReceiptScanningManager"
    }

    private val authenticator: ForceAuthenticator

    private val receiptClient: NetworkClient

    private val mInstanceUrl: String

    init {
        authenticator = auth
        mInstanceUrl = instanceUrl
        receiptClient = NetworkClient(auth, instanceUrl)
    }

    suspend fun analyzeExpense(
        encodedImage: String
    ): Result<String> {
        Logger.d(TAG, "analyzeExpense()")


        val requestBody = AnalyzeExpenseRequest(encodedImage)
        return receiptClient.receiptApi.analyzeExpense(
            getAnalyzeExpenseUrl(),
            requestBody
        )
    }
    suspend fun receiptList(
    ): Result<ReceiptListResponse> {
        Logger.d(TAG, "analyzeExpense()")

        return receiptClient.receiptApi.receiptList(
            getReceiptListUrl()
        )
    }

    private fun getAnalyzeExpenseUrl(): String {
        return mInstanceUrl + ReceiptScanningConfig.RECEIPT_ANALYZE_EXPENSE
    }
    private fun getReceiptListUrl(): String {
        return "https://hutl.my.salesforce.com/services/data/v59.0/query/?q=select%20Id,Purchase_Date__c,ReceiptId__c,Name,Status__c,StoreName__c,Total_Points__c,TotalAmount__c%20from%20Receipts__c%20Order%20by%20CreatedDate%20DESC"
    }

}