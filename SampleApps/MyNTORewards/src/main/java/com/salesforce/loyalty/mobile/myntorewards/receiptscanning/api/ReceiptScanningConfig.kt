package com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api

object ReceiptScanningConfig {

    const val RECEIPT_ANALYZE_EXPENSE = "/services/apexrest/AnalizeExpence/"
    const val RECEIPT_LIST = "/services/data/v59.0/query/?q=select%20Id,Purchase_Date__c,ReceiptId__c,Name,Status__c,StoreName__c,Total_Points__c,TotalAmount__c%20from%20Receipts__c%20Order%20by%20CreatedDate%20DESC"

}