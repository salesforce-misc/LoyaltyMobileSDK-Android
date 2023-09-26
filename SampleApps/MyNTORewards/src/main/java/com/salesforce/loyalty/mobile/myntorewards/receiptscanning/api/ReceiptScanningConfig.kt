package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api

object ReceiptScanningConfig {

    const val RECEIPT_ANALYZE_EXPENSE = "/services/apexrest/AnalizeExpence/"

    const val RECEIPT_CREATE_TRANSACTION_PATH = "/services/apexrest/CreateAndProcessTransactionJournal"

    const val RECEIPT_STATUS_UPDATE_URL = "/services/apexrest/ReceiptStatusUpdate/"

    const val RECEIPT_STATUS_MANUAL_REVIEW = "Manual Review"

    const val RECEIPT_STATUS_IN_PROGRESS = "In Progress"

    const val SOQL_QUERY_PATH = "/services/data/v"
    const val SOQL_QUERY_VERSION = "59.0"
    const val QUERY = "/query/"

    const val RECEIPT_STATUS_MAX_RETRY_COUNT = 3
    const val RECEIPT_STATUS_FETCH_DELAY = 2000 // 2 seconds

}