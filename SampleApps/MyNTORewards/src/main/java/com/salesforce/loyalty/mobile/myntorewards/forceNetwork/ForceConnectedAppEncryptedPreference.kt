package com.salesforce.loyalty.mobile.myntorewards.forceNetwork

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.PrefHelper.set

object ForceConnectedAppEncryptedPreference {

    private lateinit var secureSharedPref: SharedPreferences

    fun getSecureSharedPref(context: Context): SharedPreferences {
        if (!::secureSharedPref.isInitialized) {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

            secureSharedPref = EncryptedSharedPreferences.create(
                AppConstants.CONNECTED_APP_PREF_NAME,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
        return secureSharedPref
    }

    fun saveConnectedApp(context: Context, connectedApp: ConnectedApp) {
        val jsonString = Gson().toJson(connectedApp, ConnectedApp::class.java)
        getSecureSharedPref(context)
            .set(connectedApp.instanceUrl, jsonString)
    }

    fun getConnectedApp(context: Context, instanceUrl: String): ConnectedApp {
        val connectedAppString = getSecureSharedPref(context)
            .get(instanceUrl, "")
        return Gson().fromJson(connectedAppString, ConnectedApp::class.java)
    }

    fun deleteConnectedApp(context: Context, instanceUrl: String) {
        val editor: SharedPreferences.Editor = getSecureSharedPref(context).edit()
        editor.remove(instanceUrl)
        editor.apply()
    }

    fun retrieveAll(context: Context): List<ConnectedApp>? {
        var connectedApps: MutableList<ConnectedApp> = mutableListOf()
        val allValues = getSecureSharedPref(context).all
        if (allValues.isEmpty()) {
            return null
        }
        allValues.forEach { entry ->
            val value = entry.value as String
            connectedApps.add(Gson().fromJson<ConnectedApp>(value, ConnectedApp::class.java))
        }
        return connectedApps.toList()
    }
    fun clearAll(context: Context) {
        val editor: SharedPreferences.Editor = getSecureSharedPref(
            context
        ).edit()
        editor.clear()
        editor.apply()
    }
}