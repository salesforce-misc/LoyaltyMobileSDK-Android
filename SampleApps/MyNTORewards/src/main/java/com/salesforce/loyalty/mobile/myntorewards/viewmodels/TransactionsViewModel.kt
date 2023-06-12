package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.AppSettings
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.TransactionViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.TransactionViewState
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.TransactionsResponse
import kotlinx.coroutines.launch

class TransactionsViewModel : ViewModel(), TransactionViewModelInterface {
    private val TAG = TransactionsViewModel::class.java.simpleName

    private val loyaltyAPIManager: LoyaltyAPIManager = LoyaltyAPIManager(
        ForceAuthManager.forceAuthManager,
        ForceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
    )
    //live data for transaction data
    override val transactionsLiveData: LiveData<TransactionsResponse>
        get() = transactions

    private val transactions = MutableLiveData<TransactionsResponse>()

    override val transactionViewState: LiveData<TransactionViewState>
        get() = viewState

    private val viewState = MutableLiveData<TransactionViewState>()

    override fun loadTransactions(context: Context, refreshRequired:Boolean) {
        viewState.postValue(TransactionViewState.TransactionFetchInProgress)
        viewModelScope.launch {
            val memberJson =
                PrefHelper.customPrefs(context).getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
            if (memberJson == null) {
                Log.d(TAG, "failed: member getTransactions Member details not present")
                return@launch
            }
            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)

            val membershipKey = member.membershipNumber ?: ""

            if(refreshRequired)
            {
                getTransactions(context, membershipKey)
            }
            else
            {
                val transactionCache = LocalFileManager.getData(
                    context,
                    membershipKey,
                    LocalFileManager.DIRECTORY_TRANSACTIONS,
                    TransactionsResponse::class.java
                )

                Log.d(TAG, "cache : $transactionCache")
                if (transactionCache == null) {
                    getTransactions(context, membershipKey)
                } else {
                    transactions.value = transactionCache!!
                    viewState.postValue(TransactionViewState.TransactionFetchSuccess)
                }
            }

        }
    }

    private fun getTransactions(context: Context, membershipNumber: String) {
        viewState.postValue(TransactionViewState.TransactionFetchInProgress)

        viewModelScope.launch {
            membershipNumber?.let { membershipNumber ->
                loyaltyAPIManager.getTransactions(membershipNumber, null, null, null, null, null)
                    .onSuccess {

                        var gson = Gson()
                        var jsonString:String = gson.toJson(it)
                        Log.d(TAG, "Akash*** Profile Cache"+ jsonString )

                        LocalFileManager.saveData(
                            context,
                            it,
                            membershipNumber,
                            LocalFileManager.DIRECTORY_TRANSACTIONS
                        )
                        viewState.postValue(TransactionViewState.TransactionFetchSuccess)
                        transactions.value = it
                        Log.d(TAG, "getTransactions success: $it")

                    }.onFailure {
                        Log.d(TAG, "getTransactions failed: ${it.message}")
                        viewState.postValue(TransactionViewState.TransactionFetchFailure)
                    }
            }
        }
    }
}