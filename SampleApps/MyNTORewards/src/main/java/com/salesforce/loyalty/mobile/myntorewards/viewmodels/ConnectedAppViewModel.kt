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
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get
import com.salesforce.loyalty.mobile.sources.PrefHelper.set
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import kotlinx.coroutines.launch

class ConnectedAppViewModel : ViewModel() {
    private val TAG = ConnectedAppViewModel::class.java.simpleName

    //live data for Saved connected apps
    val selectedInstanceLiveData: LiveData<String>
        get() = selectedInstanceUrl

    private val selectedInstanceUrl = MutableLiveData<String>()

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

    fun setSelectedApp(context: Context, instanceUrl: String, communityUrl: String) {
        selectedInstanceUrl.value = instanceUrl
        PrefHelper.instancePrefs(context)
            .set(AppConstants.KEY_SELECTED_INSTANCE_URL, instanceUrl)
        PrefHelper.instancePrefs(context).set(AppConstants.KEY_SELECTED_COMMUNITY_URL, communityUrl)
    }

    fun getSelectedApp(context: Context): String {
        val instanceUrl =
            PrefHelper.instancePrefs(context).get<String>(AppConstants.KEY_SELECTED_INSTANCE_URL)
                ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
        selectedInstanceUrl.value = instanceUrl
        return instanceUrl
    }

    fun setLoyaltyProgramName(context: Context, programName: String) {
        PrefHelper.instancePrefs(context)
            .set(AppConstants.KEY_LOYALTY_PROGRAM_NAME, programName)
    }
    fun getLoyaltyProgramName(context: Context): String {
        return Common.getLoyaltyProgramName(context)
    }

    fun setRewardCurrencyName(context: Context, currencyName: String) {
        viewModelScope.launch {
            PrefHelper.instancePrefs(context)
                .set(AppConstants.KEY_REWARD_CURRENCY_NAME, currencyName)
        }
    }
    fun getRewardCurrencyName(context: Context): String {
        return Common.getRewardCurrencyName(context)
    }

    fun setRewardCurrencyNameShort(context: Context, currencyNameShort: String) {
        PrefHelper.instancePrefs(context)
            .set(AppConstants.KEY_REWARD_CURRENCY_NAME_SHORT, currencyNameShort)
    }
    fun getRewardCurrencyNameShort(context: Context): String {
        return Common.getRewardCurrencyShortName(context)
    }
    fun setTierCurrencyName(context: Context, tierCurrencyName: String) {
        PrefHelper.instancePrefs(context)
            .set(AppConstants.KEY_TIER_CURRENCY_NAME, tierCurrencyName)
    }
    fun getTierCurrencyName(context: Context): String {
        return Common.getTierCurrencyName(context)
    }
}