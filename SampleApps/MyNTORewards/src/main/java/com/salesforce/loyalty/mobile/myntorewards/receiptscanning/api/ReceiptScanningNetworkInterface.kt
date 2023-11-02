package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api

import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.*
import okhttp3.RequestBody
import retrofit2.http.*

interface ReceiptScanningNetworkInterface {

    @POST()
    suspend fun analyzeExpense(
        @Url url: String,
        @Query("filename") fileName: String,
        @Query("membershipnumber") membershipNumber: String
    ): AnalyzeExpenseResponse

    @PUT()
    suspend fun uploadReceipt(
        @Url url: String,
        @Body requestBody: RequestBody,
        @Query("membershipnumber") membershipNumber: String
    ): Result<UploadReceiptResponse>

    @GET()
    suspend fun receiptList(
        @Url url: String,
        @Query("q") query: String?
    ): Result<ReceiptListResponse>

    @POST()
    suspend fun createTransactionalJournal(
        @Url url: String,
        @Body json: AnalyzeExpenseResponse
    ): Result<List<CreateTransactionalJournalResponse>>

    @PUT()
    suspend fun receiptStatusUpdate(
        @Url url: String,
        @Body json: ReceiptStatusUpdateRequest
    ): Result<ReceiptStatusUpdateResponse>

    @GET()
    suspend fun getReceiptStatus(
        @Url url: String,
        @Query("q") query: String?
    ): Result<ReceiptListResponse>
}