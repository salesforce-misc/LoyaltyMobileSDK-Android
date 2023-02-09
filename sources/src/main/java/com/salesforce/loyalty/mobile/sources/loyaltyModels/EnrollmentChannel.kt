package com.salesforce.loyalty.mobile.sources.loyaltyModels

enum class EnrollmentChannel(val channel: String) {
    POS("Pos"),
    WEB("Web"),
    EMAIL("Email"),
    CALL_CENTER("CallCenter"),
    SOCIAL("Social"),
    MOBILE("Mobile"),
    STORE("Store"),
    FRANCHISE("Franchise"),
    PARTNER("Partner"),
    PRINT("Print")
}