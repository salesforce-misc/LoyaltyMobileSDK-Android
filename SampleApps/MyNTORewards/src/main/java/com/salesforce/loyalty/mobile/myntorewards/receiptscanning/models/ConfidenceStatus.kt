package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models

enum class ConfidenceStatus(val status: String) {
    SUCCESS("Success"),
    PARTIAL("Partial"),
    FAILURE("Failure")
}