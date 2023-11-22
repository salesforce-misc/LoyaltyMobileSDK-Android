package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetCustomState() = rememberBottomSheetScaffoldState(
    bottomSheetState = BottomSheetState(
        initialValue = BottomSheetValue.Collapsed,
        confirmValueChange = {
            // Prevent collapsing by swipe down gesture
            it != BottomSheetValue.Collapsed
        }
    )
)