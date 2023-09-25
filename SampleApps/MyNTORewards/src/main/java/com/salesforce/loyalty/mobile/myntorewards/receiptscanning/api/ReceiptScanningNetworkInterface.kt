package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api

import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.*
import okhttp3.RequestBody
import retrofit2.http.*

interface ReceiptScanningNetworkInterface {

    @PUT()
    suspend fun analyzeExpense(
        @Url url: String,
        @Body requestBody: RequestBody,
        @Query("membershipnumber") membershipNumber: String
    ): AnalyzeExpenseResponse

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
}