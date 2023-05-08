package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.PrefHelper.set

object ForceAuthEncryptedPreference {

    private lateinit var secureSharedPref: SharedPreferences

    fun getSecureSharedPref(context: Context): SharedPreferences {
        if (!::secureSharedPref.isInitialized) {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

            secureSharedPref = EncryptedSharedPreferences.create(
                AppConstants.AUTH_PREF_NAME,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
        return secureSharedPref
    }

    fun saveAuth(context: Context, forceAuth: ForceAuth) {
        val jsonString = Gson().toJson(forceAuth, ForceAuth::class.java)
        getSecureSharedPref(context)
            .set(forceAuth.identityURL, jsonString)
    }

    fun getAuth(context: Context, identityUrl: String): ForceAuth {
        val forceAuthString = getSecureSharedPref(context)
            .get(identityUrl, "")
        return Gson().fromJson(forceAuthString, ForceAuth::class.java)
    }

    fun clearAll(context: Context) {
        val editor: SharedPreferences.Editor = getSecureSharedPref(
            context
        ).edit()
        editor.clear()
        editor.apply()
    }
}