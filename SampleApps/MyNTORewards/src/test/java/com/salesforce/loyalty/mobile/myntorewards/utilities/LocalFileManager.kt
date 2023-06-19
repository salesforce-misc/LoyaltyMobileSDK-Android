package com.salesforce.loyalty.mobile.myntorewards.utilities

import android.content.Context
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.MockResponseFileReader
import com.salesforce.loyalty.mobile.sources.loyaltyModels.*

object LocalFileManager {

    const val TAG = "LocalFileManager"
    const val DIRECTORY_PROFILE = "Profile"
    const val DIRECTORY_BENEFITS = "Benefits"
    const val DIRECTORY_TRANSACTIONS = "Transactions"
    const val DIRECTORY_PROMOTIONS = "Promotions"
    const val DIRECTORY_VOUCHERS = "Vouchers"

    fun <T> saveData(context: Context, data: T, id: String, folderName: String) {
        println("DEBUG: $id: $folderName")
    }

    fun <T> getData(context: Context, id: String, folderName: String, type: Class<T>): T?{

        when (folderName) {
            DIRECTORY_BENEFITS -> {
                     return  Gson().fromJson(MockResponseFileReader("Benefits.json").content, MemberBenefitsResponse::class.java) as T
            }
             DIRECTORY_TRANSACTIONS -> {

                 return  Gson().fromJson(MockResponseFileReader("Transactions.json").content, TransactionsResponse::class.java) as T
             }
            DIRECTORY_VOUCHERS -> {
            return  Gson().fromJson(MockResponseFileReader("GetVouchers.json").content, VoucherResult::class.java) as T

             }
            DIRECTORY_PROFILE -> {
                return  Gson().fromJson(MockResponseFileReader("Profile.json").content, MemberProfileResponse::class.java) as T

            }
            DIRECTORY_PROMOTIONS -> {
                return  Gson().fromJson(MockResponseFileReader("Promotions.json").content, PromotionsResponse::class.java) as T

            }
            else -> {
                val mockResponse = MockResponseFileReader("Benefits.json").content
                val mockMemberBenefitsResponse =
                    Gson().fromJson(mockResponse, MemberBenefitsResponse::class.java)
                return mockMemberBenefitsResponse as T}
        }
        }

}