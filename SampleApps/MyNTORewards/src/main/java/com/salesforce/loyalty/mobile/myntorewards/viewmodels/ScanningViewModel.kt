package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.ReceiptScanningManager
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseResponse
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ReceiptListResponse
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.ScanningViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptScanningViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptViewState
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import kotlinx.coroutines.launch

class ScanningViewModel(private val receiptScanningManager: ReceiptScanningManager) : ViewModel(),
    ScanningViewModelInterface {
    private val TAG = ScanningViewModel::class.java.simpleName

    override val receiptListLiveData: LiveData<ReceiptListResponse>
        get() = receiptList

    private val receiptList = MutableLiveData<ReceiptListResponse>()

    override val receiptListViewState: LiveData<ReceiptViewState>
        get() = viewState

    private val viewState = MutableLiveData<ReceiptViewState>()

    override val scannedReceiptLiveData: LiveData<AnalyzeExpenseResponse>
        get() = scannedReceipt

    private val scannedReceipt = MutableLiveData<AnalyzeExpenseResponse>()

    override val receiptScanningViewStateLiveData: LiveData<ReceiptScanningViewState>
        get() = receiptScanningViewState

    private val receiptScanningViewState = MutableLiveData<ReceiptScanningViewState>()

    override fun getReceiptLists() {
        viewModelScope.launch {
            viewState.postValue(ReceiptViewState.ReceiptListFetchInProgressView)
            receiptScanningManager.receiptList().onSuccess {
                receiptList.value= it
                viewState.postValue(ReceiptViewState.ReceiptListFetchSuccessView)
            }.onFailure {
                    Logger.d(TAG, "receipt call failed: ${it.message}")
                    viewState.postValue(ReceiptViewState.ReceiptListFetchFailureView)
                }
        }
    }

    override fun analyzeExpense(encodedImage: String): AnalyzeExpenseResponse? {
        Logger.d(TAG, "analyzeExpense")
        var result: AnalyzeExpenseResponse? = null
        viewModelScope.launch {
            receiptScanningViewState.postValue(ReceiptScanningViewState.ReceiptScanningInProgress)
            receiptScanningManager.analyzeExpense(encodedImage).onSuccess {
                Logger.d(TAG, "analyzeExpense Success : $it")
                scannedReceipt.value= it
                result = it
                receiptScanningViewState.postValue(ReceiptScanningViewState.ReceiptScanningSuccess)
            }
                .onFailure {
                    Logger.d(TAG, "analyzeExpense failed: ${it.message}")
                    receiptScanningViewState.postValue(ReceiptScanningViewState.ReceiptScanningFailure)
                }

        }
        return result
    }

}