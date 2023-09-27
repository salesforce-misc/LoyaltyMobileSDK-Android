package com.salesforce.loyalty.mobile.myntorewards.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.ReceiptScanningManager
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.api.ReceiptScanningConfig
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseResponse
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.ReceiptListResponse
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.RECEIPT_POINT_STATUS_PROCESSED
import com.salesforce.loyalty.mobile.myntorewards.utilities.CommunityMemberModel
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.ScanningViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.CreateTransactionJournalViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptScanningViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptStatusUpdateViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReceiptViewState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.UploadRecieptCancelledViewState
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import kotlinx.coroutines.delay
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

    override val createTransactionJournalViewStateLiveData: LiveData<CreateTransactionJournalViewState>
        get() = createTransactionJournalViewState

    private val createTransactionJournalViewState = MutableLiveData<CreateTransactionJournalViewState>()


    override val cancellingSubmissionLiveData: LiveData<UploadRecieptCancelledViewState>
        get() = cancellingSubmissionViewState

    private val cancellingSubmissionViewState = MutableLiveData<UploadRecieptCancelledViewState>()

    override val receiptStatusUpdateViewStateLiveData: LiveData<ReceiptStatusUpdateViewState>
        get() = receiptStatusUpdateViewState

    private val receiptStatusUpdateViewState = MutableLiveData<ReceiptStatusUpdateViewState>()

    override fun getReceiptLists(context: Context, refreshRequired: Boolean) {
        viewModelScope.launch {
            viewState.postValue(ReceiptViewState.ReceiptListFetchInProgressView)

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
                    viewState.postValue(ReceiptViewState.ReceiptListFetchSuccessView)
                }
            }

        }
    }

    override fun getReceiptListsAPI(context: Context, membershipKey: String) {
        viewModelScope.launch {

            receiptScanningManager.receiptList(membershipKey).onSuccess {
                receiptList.value = it
                LocalFileManager.saveData(
                    context,
                    it,
                    membershipKey,
                    LocalFileManager.DIRECTORY_RECEIPT_LIST
                )
                viewState.postValue(ReceiptViewState.ReceiptListFetchSuccessView)
            }.onFailure {
                Logger.d(TAG, "receipt call failed: ${it.message}")
                viewState.postValue(ReceiptViewState.ReceiptListFetchFailureView)
            }
        }
    }

    override fun analyzeExpense(context: Context, encodedImage: ByteArray): AnalyzeExpenseResponse? {
        Logger.d(TAG, "analyzeExpense")
        var result: AnalyzeExpenseResponse? = null
        viewModelScope.launch {
            receiptScanningViewState.postValue(ReceiptScanningViewState.ReceiptScanningInProgress)
            val memberJson =
                PrefHelper.customPrefs(context).getString(AppConstants.KEY_COMMUNITY_MEMBER, null)
            if (memberJson == null) {
                Logger.d(TAG, "failed: analyzeExpense Member details not present")
                return@launch
            }
            val member = Gson().fromJson(memberJson, CommunityMemberModel::class.java)

            var membershipKey = member.membershipNumber ?: ""

            receiptScanningManager.analyzeExpense(membershipKey, encodedImage).onSuccess {
                Logger.d(TAG, "analyzeExpense Success : $it")
                scannedReceipt.value= it
                result = it
                receiptScanningViewState.postValue(ReceiptScanningViewState.ReceiptScanningSuccess)
            }
                .onFailure {
                    Logger.d(TAG, "analyzeExpense failed: ${it.message}")
                    receiptScanningViewState.postValue(ReceiptScanningViewState.ReceiptScanningFailure(it.message))
                }

        }
        return result
    }

    override fun createTransactionalJournal(analyzeExpenseResponse: AnalyzeExpenseResponse) {
        Logger.d(TAG, "analyzeExpense")
        viewModelScope.launch {
            createTransactionJournalViewState.postValue(CreateTransactionJournalViewState.CreateTransactionJournalInProgress)

            receiptScanningManager.createTransactionJournal(analyzeExpenseResponse).onSuccess {
                Logger.d(TAG, "createTransactionalJournal Success : $it")
                createTransactionJournalViewState.postValue(CreateTransactionJournalViewState.CreateTransactionJournalSuccess)
            }
                .onFailure {
                    Logger.d(TAG, "createTransactionalJournal failed: ${it.message}")
                    createTransactionJournalViewState.postValue(CreateTransactionJournalViewState.CreateTransactionJournalFailure)
                }
        }
    }

    override fun submitForManualReview(receiptId: String, comments: String?) {
        Logger.d(TAG, "submitForManualReview")
        viewModelScope.launch {
            receiptStatusUpdateViewState.postValue(ReceiptStatusUpdateViewState.ReceiptStatusUpdateInProgress)

            receiptScanningManager.receiptStatusUpdate(
                receiptId = receiptId,
                status = ReceiptScanningConfig.RECEIPT_STATUS_MANUAL_REVIEW,
                comments = comments
            ).onSuccess {
                Logger.d(TAG, "submitForManualReview Success : $it")
                receiptStatusUpdateViewState.postValue(ReceiptStatusUpdateViewState.ReceiptStatusUpdateSuccess(null))
            }.onFailure {
                Logger.d(TAG, "submitForManualReview failed: ${it.message}")
                receiptStatusUpdateViewState.postValue(ReceiptStatusUpdateViewState.ReceiptStatusUpdateFailure)
            }
        }
    }

    override fun submitForProcessing(receiptId: String) {
        Logger.d(TAG, "submitForProcessing")
        viewModelScope.launch {
            createTransactionJournalViewState.postValue(CreateTransactionJournalViewState.CreateTransactionJournalInProgress)

            receiptScanningManager.receiptStatusUpdate(
                receiptId = receiptId,
                status = ReceiptScanningConfig.RECEIPT_STATUS_IN_PROGRESS,
                comments = null
            ).onSuccess {
                Logger.d(TAG, "submitForProcessing Success : $it")
                createTransactionJournalViewState.postValue(CreateTransactionJournalViewState.CreateTransactionJournalSuccess)
            }.onFailure {
                Logger.d(TAG, "submitForProcessing failed: ${it.message}")
                createTransactionJournalViewState.postValue(CreateTransactionJournalViewState.CreateTransactionJournalFailure)
            }
        }
    }

    override fun cancellingSubmission(receiptId: String) {
        Logger.d(TAG, "submitForCancelling")
        viewModelScope.launch {
            cancellingSubmissionViewState.postValue(UploadRecieptCancelledViewState.UploadRecieptCancelledInProgress)

            receiptScanningManager.receiptStatusUpdate(
                receiptId = receiptId,
                status = ReceiptScanningConfig.RECEIPT_STATUS_CANCELLED,
                comments = null
            ).onSuccess {
                Logger.d(TAG, "cancelSubmission Success : $it")
                cancellingSubmissionViewState.postValue(UploadRecieptCancelledViewState.UploadRecieptCancelledSuccess)
            }.onFailure {
                Logger.d(TAG, "cancelSubmission failed: ${it.message}")
                cancellingSubmissionViewState.postValue(UploadRecieptCancelledViewState.UploadRecieptCancelledFailure)
            }
        }
    }

    override fun getReceiptStatus(
        receiptId: String,
        membershipNumber: String,
        maxRetryCount: Int,
        delaySeconds: Long
    ) {
        Logger.d(TAG, "getReceiptStatus")
        viewModelScope.launch {
            receiptStatusUpdateViewState.postValue(ReceiptStatusUpdateViewState.ReceiptStatusUpdateInProgress)
            var retryCount = 1
            var points: String? = null
            do {
                delay(delaySeconds)
                var status: String? = null
                receiptScanningManager.getReceiptStatus(
                    receiptId = receiptId, membershipNumber = membershipNumber
                ).onSuccess {
                    Logger.d(TAG, "getReceiptStatus Success : $it")
                    val record = it.records.get(0)
                    status = record.receipt_status
                    points = record.total_points?.toString()
                }.onFailure {
                    Logger.d(TAG, "getReceiptStatus failed: ${it.message}")
                    receiptStatusUpdateViewState.postValue(ReceiptStatusUpdateViewState.ReceiptStatusUpdateFailure)
                }
                if (status?.equals(RECEIPT_POINT_STATUS_PROCESSED, ignoreCase = true) == true) {
                    receiptStatusUpdateViewState.postValue(
                        ReceiptStatusUpdateViewState.ReceiptStatusUpdateSuccess(
                            points
                        )
                    )
                    return@launch
                }
                retryCount += 1
            } while (retryCount <= maxRetryCount)
            receiptStatusUpdateViewState.postValue(
                ReceiptStatusUpdateViewState.ReceiptStatusUpdateSuccess(
                    points
                )
            )
        }
    }
}