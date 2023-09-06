package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api

import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseRequest
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseResponse
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.CreateTransactionalJournalResponse
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ReceiptListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface ReceiptScanningNetworkInterface {

    @POST()
    suspend fun analyzeExpense(
        @Url url: String,
        @Body json: AnalyzeExpenseRequest
    ): Result<AnalyzeExpenseResponse>

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
}