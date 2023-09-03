package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.ReceiptScanningManager
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ReceiptListResponse
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.ScanningViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptScanState
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import kotlinx.coroutines.launch

class ScanningViewModel(private val receiptScanningManager: ReceiptScanningManager) : ViewModel(),
    ScanningViewModelInterface {
    private val TAG = ScanningViewModel::class.java.simpleName

    override val receiptListLiveData: LiveData<ReceiptListResponse>
        get() = receiptList

    private val receiptList = MutableLiveData<ReceiptListResponse>()

    override val receiptListViewState: LiveData<ReceiptScanState>
        get() = viewState

    private val viewState = MutableLiveData<ReceiptScanState>()


    override fun getReceiptLists(context: Context, refreshRequired: Boolean) {
        viewModelScope.launch {
            viewState.postValue(ReceiptScanState.ReceiptListFetchInProgress)

            val memberJson =
                PrefHelper.customPrefs(context)
                    .getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
            if (memberJson == null) {
                Logger.d(TAG, "failed: receipt list details not present")
                return@launch
            }
            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)
            val membershipKey = member.membershipNumber ?: ""

            if (refreshRequired) {
                getReceiptListsAPI(context, membershipKey)
            } else {
                val receiptListCache = LocalFileManager.getData(
                    context,
                    membershipKey,
                    LocalFileManager.DIRECTORY_RECEIPT_LIST,
                    ReceiptListResponse::class.java
                )

                if (receiptListCache == null) {
                    getReceiptListsAPI(context, membershipKey)
                } else {
                    receiptList.value =
                        receiptListCache!!   // this not null is assertion is needed else android studio gives compile error.
                    viewState.postValue(ReceiptScanState.ReceiptListFetchSuccess)
                }
            }

        }
    }

    override fun getReceiptListsAPI(context: Context, membershipKey: String) {
        viewModelScope.launch {

            receiptScanningManager.receiptList().onSuccess {
                receiptList.value = it
                LocalFileManager.saveData(
                    context,
                    it,
                    membershipKey,
                    LocalFileManager.DIRECTORY_RECEIPT_LIST
                )
                viewState.postValue(ReceiptScanState.ReceiptListFetchSuccess)
            }.onFailure {
                Logger.d(TAG, "receipt call failed: ${it.message}")
                viewState.postValue(ReceiptScanState.ReceiptListFetchFailure)
            }
        }
    }

    override fun analyzeExpense(encodedImage: String): String? {
        Logger.d(TAG, "analyzeExpense")
        var result: String? = null
        viewModelScope.launch {

            receiptScanningManager.analyzeExpense(encodedImage).onSuccess {
                Logger.d(TAG, "analyzeExpense Success : $it")
                result = it
            }
                .onFailure {
                    Logger.d(TAG, "analyzeExpense failed: ${it.message}")
                }

        }
        return result
    }


}