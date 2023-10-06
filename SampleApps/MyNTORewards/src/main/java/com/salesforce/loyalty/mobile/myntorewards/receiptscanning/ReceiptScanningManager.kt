package com.salesforce.loyalty.mobile.myntorewards.receiptscanning

import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.NetworkClient
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig.QUERY
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig.SOQL_QUERY_PATH
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig.SOQL_QUERY_VERSION
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.*
import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

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
        encodedImage: ByteArray
    ): Result<AnalyzeExpenseResponse> {
        Logger.d(TAG, "analyzeExpense()")

        /*val requestBody =
            AnalyzeExpenseRequest(membershipNumber = membershipNumber, base64image = encodedImage)*/
        var requestBody: RequestBody =
            encodedImage.toRequestBody("image/jpg".toMediaTypeOrNull(), 0, encodedImage.size)
        try {
            val success = receiptClient.receiptApi.analyzeExpense(
                getAnalyzeExpenseUrl(),
                requestBody, membershipNumber
            )
            return Result.success(success)
        } catch (e: HttpException) {
            val responseBody = e.response()?.errorBody()?.string()
            Logger.d(TAG, "Analyze Expense exception : $responseBody")
            val errorMessageBody = Gson().fromJson(responseBody, AnalyzeExpenseErrorResponse::class.java)
            errorMessageBody?.message?.let{
                return Result.failure(Throwable(it))
            }
            return Result.failure(Throwable(""))
        }
    }

    suspend fun receiptList(membershipKey: String
    ): Result<ReceiptListResponse> {
        Logger.d(TAG, "fetchReceiptList")

        return receiptClient.receiptApi.receiptList(
            getReceiptListURLSOQLUrl(), fetchReceiptListSOQLQuery(membershipKey)
        )
    }

    suspend fun receiptStatusUpdate(
        receiptId: String,
        status: String,
        comments: String?
    ): Result<ReceiptStatusUpdateResponse> {
        Logger.d(TAG, "receiptStatusUpdate()")

        val body =
            ReceiptStatusUpdateRequest(receiptId = receiptId, status = status, comments = comments)
        return receiptClient.receiptApi.receiptStatusUpdate(
            getStatusUpdateUrl(),
            body
        )
    }

    suspend fun getReceiptStatus(
        receiptId: String,
        membershipNumber: String
    ): Result<ReceiptListResponse> {
        Logger.d(TAG, "receiptStatusUpdate()")


        return receiptClient.receiptApi.getReceiptStatus(
            getReceiptListURLSOQLUrl(), fetchReceiptStatusSOQLQuery(membershipNumber, receiptId)
        )
    }

    private fun getAnalyzeExpenseUrl(): String {
        return mInstanceUrl + ReceiptScanningConfig.RECEIPT_ANALYZE_EXPENSE
    }

    private fun getReceiptListURLSOQLUrl(): String {
        return mInstanceUrl + SOQL_QUERY_PATH + SOQL_QUERY_VERSION + QUERY
    }

    private fun fetchReceiptListSOQLQuery(membershipKey: String): String {
        return "select Id,PurchaseDate__c,ReceiptID__c,Name,Status__c,StoreName__c,TotalRewardPoints__c,TotalAmount__c,APIResponse__c,ImageUrl__c from Receipts__c WHERE LoyaltyProgramMember__r.MembershipNumber = '${membershipKey}' Order by CreatedDate DESC"
    }

    private fun getCreateTransactionUrl(): String {
        return mInstanceUrl + ReceiptScanningConfig.RECEIPT_CREATE_TRANSACTION_PATH
    }

    private fun getStatusUpdateUrl(): String {
        return mInstanceUrl + ReceiptScanningConfig.RECEIPT_STATUS_UPDATE_URL
    }

    private fun fetchReceiptStatusSOQLQuery(membershipKey: String, receiptId: String): String {
        return "select Id,Status__c,TotalRewardPoints__c from Receipts__c WHERE LoyaltyProgramMember__r.MembershipNumber = '${membershipKey}' AND Id = '${receiptId}'"
    }
}