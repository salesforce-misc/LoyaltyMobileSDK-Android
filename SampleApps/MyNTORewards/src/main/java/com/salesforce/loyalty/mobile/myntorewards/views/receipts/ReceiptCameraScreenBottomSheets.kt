package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.receiptscanning.models.AnalyzeExpenseResponse
import com.salesforce.loyalty.mobile.myntorewards.utilities.LocalFileManager
import com.salesforce.loyalty.mobile.myntorewards.utilities.ReceiptScanningBottomSheetType
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.ScanningViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens

@Composable
fun OpenReceiptBottomSheetContent(
    bottomSheetType: ReceiptScanningBottomSheetType,
    navController: NavHostController,
    errorMessage: String,
    currentProgress: String,
    closeSheet: () -> Unit,
) {
    when (bottomSheetType) {
        ReceiptScanningBottomSheetType.POPUP_PROGRESS -> {
            ReceiptProgressScreen(
                navHostController = navController,
                closePopup = { closeSheet() },
                currentProgress = currentProgress
            )
        }

        ReceiptScanningBottomSheetType.POPUP_ERROR -> {
            ScanningErrorPopup(errorMessage, closePopup = {
                closeSheet()
                navController.popBackStack(MoreScreens.ReceiptListScreen.route, false)
            }, scanAnotherReceipt = { closeSheet() })
        }

        ReceiptScanningBottomSheetType.POPUP_ERROR_IMAGEMORETHAN5MB -> {
            ScanningErrorPopup(stringResource(id = R.string.receipt_scanning_error_desc_image), closePopup = {
                closeSheet()
                navController.popBackStack(MoreScreens.ReceiptListScreen.route, false)
            }, scanAnotherReceipt = { closeSheet() })
        }
        else -> {}
    }
}