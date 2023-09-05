package com.salesforce.loyalty.mobile.myntorewards.receiptscanning

import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.NetworkClient
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig.QUERY
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig.SOQL_QUERY_PATH
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig.SOQL_QUERY_VERSION
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseRequest
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseResponse
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
        membershipNumber: String,
        encodedImage: String
    ): Result<AnalyzeExpenseResponse> {
        Logger.d(TAG, "analyzeExpense()")


        val requestBody =
            AnalyzeExpenseRequest(membershipNumber = membershipNumber, base64image = encodedImage)
        return receiptClient.receiptApi.analyzeExpense(
            getAnalyzeExpenseUrl(),
            requestBody
        )
    }

    suspend fun receiptList(
    ): Result<ReceiptListResponse> {
        Logger.d(TAG, "fetchReceiptList")

        return receiptClient.receiptApi.receiptList(
            getReceiptListURLSOQLUrl(), fetchReceiptListSOQLQuery()
        )
    }

    private fun getAnalyzeExpenseUrl(): String {
        return mInstanceUrl + ReceiptScanningConfig.RECEIPT_ANALYZE_EXPENSE
    }

    private fun getReceiptListURLSOQLUrl(): String {
        return mInstanceUrl + SOQL_QUERY_PATH + SOQL_QUERY_VERSION + QUERY
    }

    private fun fetchReceiptListSOQLQuery(): String {
        return "select Id,Purchase_Date__c,ReceiptId__c,Name,Status__c,StoreName__c,Total_Points__c,TotalAmount__c,Processed_AWS_Response__c from Receipts__c Order by CreatedDate DESC"
    }

}