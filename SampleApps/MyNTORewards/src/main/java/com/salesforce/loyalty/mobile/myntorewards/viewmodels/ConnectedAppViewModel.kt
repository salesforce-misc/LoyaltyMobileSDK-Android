package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.AppSettings
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ConnectedApp
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceConnectedAppEncryptedPreference
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.PrefHelper.set
import kotlinx.coroutines.launch

class ConnectedAppViewModel constructor(context: Context) : ViewModel() {
    private val TAG = ConnectedAppViewModel::class.java.simpleName
    var mContext: Context
    //live data for Saved connected apps
    val selectedAppLiveData: LiveData<String>
        get() = selectedConnectedAppName

    private val selectedConnectedAppName = MutableLiveData<String>()

    init {
        mContext = context
        selectedConnectedAppName.value =
            PrefHelper.customPrefs(mContext).get(AppConstants.KEY_SELECTED_CONNECTED_APP_NAME)
                ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.name!!
    }

    //live data for Saved connected apps
    val savedAppsLiveData: LiveData<List<ConnectedApp>>
        get() = savedApps

    private val savedApps = MutableLiveData<List<ConnectedApp>>()

    fun saveConnectedApp(context: Context, connectedApp: ConnectedApp) {
        viewModelScope.launch {
            ForceConnectedAppEncryptedPreference.saveConnectedApp(context, connectedApp)
        }
    }

    fun getConnectedApp(context: Context, name: String): ConnectedApp {
        Log.d(TAG, "getConnectedApp() name : $name")

        return ForceConnectedAppEncryptedPreference.getConnectedApp(context, name)
    }

    fun getAllConnectedApps(context: Context): List<ConnectedApp>? {
        return ForceConnectedAppEncryptedPreference.retrieveAll(context)
    }

    fun deleteConnectedApp(context: Context, name: String) {
        ForceConnectedAppEncryptedPreference.deleteConnectedApp(context, name)
    }

    fun retrieveAll(context: Context): List<ConnectedApp>? {
        var connectedApps: MutableList<ConnectedApp>?
        connectedApps = ForceConnectedAppEncryptedPreference.retrieveAll(context)?.toMutableList()
        if (connectedApps == null || connectedApps.isEmpty()) {
            Log.d("ConnectedApps", " connectedApps null or empty")
            connectedApps = mutableListOf()
            val initApp = AppSettings.DEFAULT_FORCE_CONNECTED_APP
            connectedApps.add(initApp)
            saveConnectedApp(context, initApp)
//            updateSavedApps(context)
            connectedApps?.let { it -> savedApps.value = it }
            return connectedApps
        } else {
            Log.d("ConnectedApps", " connectedApps $connectedApps")
            connectedApps?.let { it -> savedApps.value = it }
            return connectedApps
        }
    }

    private fun updateSavedApps(context: Context) {
        savedApps.value = ForceConnectedAppEncryptedPreference.retrieveAll(context)
    }

    fun setSelectedApp(name: String) {
        selectedConnectedAppName.value = name
        viewModelScope.launch {
            PrefHelper.customPrefs(mContext)
                .set(AppConstants.KEY_SELECTED_CONNECTED_APP_NAME, name)
        }
    }
}