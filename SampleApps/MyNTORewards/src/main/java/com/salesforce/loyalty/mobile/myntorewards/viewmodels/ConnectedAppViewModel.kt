package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
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
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import kotlinx.coroutines.launch

class ConnectedAppViewModel constructor(context: Context) : ViewModel() {
    private val TAG = ConnectedAppViewModel::class.java.simpleName
    var mContext: Context
    //live data for Saved connected apps
    val selectedInstanceLiveData: LiveData<String>
        get() = selectedInstanceUrl

    private val selectedInstanceUrl = MutableLiveData<String>()

    init {
        mContext = context
        selectedInstanceUrl.value =
            PrefHelper.customPrefs(mContext).get(AppConstants.KEY_SELECTED_INSTANCE_URL)
                ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
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

    fun getConnectedApp(context: Context, instanceUrl: String): ConnectedApp {
        Logger.d(TAG, "getConnectedApp() instanceUrl : $instanceUrl")

        return ForceConnectedAppEncryptedPreference.getConnectedApp(context, instanceUrl)
    }

    fun getAllConnectedApps(context: Context): List<ConnectedApp>? {
        return ForceConnectedAppEncryptedPreference.retrieveAll(context)
    }

    fun deleteConnectedApp(context: Context, instanceUrl: String) {
        ForceConnectedAppEncryptedPreference.deleteConnectedApp(context, instanceUrl)
        updateSavedApps(context)
    }

    fun retrieveAll(context: Context): List<ConnectedApp>? {
        var connectedApps: MutableList<ConnectedApp>?
        connectedApps = ForceConnectedAppEncryptedPreference.retrieveAll(context)?.toMutableList()
        if (connectedApps == null || connectedApps.isEmpty()) {
            Logger.d("ConnectedApps", " connectedApps null or empty")
            connectedApps = mutableListOf()
            val initApp = AppSettings.DEFAULT_FORCE_CONNECTED_APP
            connectedApps.add(initApp)
            saveConnectedApp(context, initApp)
//            updateSavedApps(context)
            connectedApps?.let { it -> savedApps.value = it }
            return connectedApps
        } else {
            Logger.d("ConnectedApps", " connectedApps $connectedApps")
            connectedApps?.let { it -> savedApps.value = it }
            return connectedApps
        }
    }

    private fun updateSavedApps(context: Context) {
        savedApps.value = ForceConnectedAppEncryptedPreference.retrieveAll(context)
    }

    fun setSelectedApp(instanceUrl: String) {
        selectedInstanceUrl.value = instanceUrl
        viewModelScope.launch {
            PrefHelper.customPrefs(mContext)
                .set(AppConstants.KEY_SELECTED_INSTANCE_URL, instanceUrl)
        }
    }
}