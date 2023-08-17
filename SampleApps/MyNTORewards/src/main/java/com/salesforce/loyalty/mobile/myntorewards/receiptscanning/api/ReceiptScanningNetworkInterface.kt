package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api

import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseRequest
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ReceiptListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ReceiptScanningNetworkInterface {

    @POST()
    suspend fun analyzeExpense(
        @Url url: String,
        @Body json: AnalyzeExpenseRequest
    ): Result<String>

    @GET()
    suspend fun receiptList(
        @Url url: String,
    ): Result<ReceiptListResponse>
}