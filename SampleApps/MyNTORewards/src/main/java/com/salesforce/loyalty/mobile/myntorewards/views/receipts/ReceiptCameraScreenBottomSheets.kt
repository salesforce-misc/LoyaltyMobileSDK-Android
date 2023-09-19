package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseResponse
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.utilities.ReceiptScanningBottomSheetType
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.ScanningViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens

@Composable
fun OpenReceiptBottomSheetContent(
    bottomSheetType: ReceiptScanningBottomSheetType,
    navController: NavHostController,
    scannedReceiptLiveData: AnalyzeExpenseResponse?,
    scanningViewModel: ScanningViewModelInterface,
    setBottomSheetState: (bottomSheetState: ReceiptScanningBottomSheetType) -> Unit,
    closeSheet: () -> Unit,
) {
    val context = LocalContext.current
    when (bottomSheetType) {
        ReceiptScanningBottomSheetType.POPUP_PROGRESS -> {
            ScanningProgress(
                navHostController = navController,
                closePopup = { closeSheet() })
        }

        ReceiptScanningBottomSheetType.POPUP_SCANNED_RECEIPT -> {
            ShowScannedReceiptScreen(
                navController,
                scanningViewModel,
                scannedReceiptLiveData,
                closePopup = {
                    closeSheet()
                },
                openCongratsPopup = {
                    setBottomSheetState(it)
                })
        }

        ReceiptScanningBottomSheetType.POPUP_CONGRATULATIONS -> {
            CongratulationsPopup(navController, closePopup = {
                closeSheet()

                //Invalidate receipt list cache
                LocalFileManager.clearFolder(context, LocalFileManager.DIRECTORY_RECEIPT_LIST)
                navController.popBackStack(MoreScreens.ReceiptListScreen.route, false)
            }, scanAnotherReceipt = {
                closeSheet()
                //Invalidate receipt list cache
                LocalFileManager.clearFolder(context, LocalFileManager.DIRECTORY_RECEIPT_LIST)
            })
        }

        ReceiptScanningBottomSheetType.POPUP_ERROR -> {
            ScanningErrorPopup(closePopup = {
                closeSheet()
                navController.popBackStack(MoreScreens.ReceiptListScreen.route, false)
            }, scanAnotherReceipt = { closeSheet() })
        }
    }
}