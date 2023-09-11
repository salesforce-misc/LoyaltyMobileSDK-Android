package com.salesforce.loyalty.mobile.myntorewards.utilities

import android.content.Context
import android.content.SharedPreferences
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.PREF_MY_DATE

class DatePreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_MY_DATE, Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}