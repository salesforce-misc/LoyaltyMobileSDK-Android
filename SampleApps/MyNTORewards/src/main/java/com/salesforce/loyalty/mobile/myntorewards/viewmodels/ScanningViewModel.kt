package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.ReceiptScanningManager
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ReceiptListResponse
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.ScanningViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.BenefitViewStates
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.PromotionViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptScanState
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import com.salesforce.loyalty.mobile.sources.loyaltyModels.PromotionsResponse
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



    override fun getReceiptLists() {
        viewModelScope.launch {
            viewState.postValue(ReceiptScanState.ReceiptListFetchInProgress)
            receiptScanningManager.receiptList().onSuccess {
                receiptList.value= it
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