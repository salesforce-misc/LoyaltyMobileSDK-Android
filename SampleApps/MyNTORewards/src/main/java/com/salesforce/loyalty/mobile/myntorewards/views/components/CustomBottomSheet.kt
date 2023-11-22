package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun CustomBottomSheet(
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed,
            confirmValueChange = {
                // Prevent collapsing by swipe down gesture
                it != BottomSheetValue.Collapsed
            })
    )

    val openOrCloseBottomSheet = {
        coroutineScope.launch {
            val bottomSheetState = bottomSheetScaffoldState.bottomSheetState
            if (bottomSheetState.isCollapsed) {
                bottomSheetState.expand()
            } else {
                bottomSheetState.collapse()
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = { sheetContent },
        sheetShape = RoundedCornerShape(AppConstants.POPUP_ROUNDED_CORNER_SIZE),
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false
    ) {
        content
    }
}