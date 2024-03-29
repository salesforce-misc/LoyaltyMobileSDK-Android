package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.TransactionViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.TransactionViewState
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.TransactionsResponse
import kotlinx.coroutines.launch

class TransactionsViewModel(private val loyaltyAPIManager: LoyaltyAPIManager) : ViewModel(), TransactionViewModelInterface {
    private val TAG = TransactionsViewModel::class.java.simpleName

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
                Logger.d(TAG, "failed: member getTransactions Member details not present")
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

                Logger.d(TAG, "cache : $transactionCache")
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
        viewModelScope.launch {
            membershipNumber?.let { membershipNumber ->
                loyaltyAPIManager.getTransactions(membershipNumber, null, null, null, null, null)
                    .onSuccess {
                        LocalFileManager.saveData(
                            context,
                            it,
                            membershipNumber,
                            LocalFileManager.DIRECTORY_TRANSACTIONS
                        )
                        viewState.postValue(TransactionViewState.TransactionFetchSuccess)
                        transactions.value = it
                        Logger.d(TAG, "getTransactions success: $it")

                    }.onFailure {
                        Logger.d(TAG, "getTransactions failed: ${it.message}")
                        viewState.postValue(TransactionViewState.TransactionFetchFailure)
                    }
            }
        }
    }
}