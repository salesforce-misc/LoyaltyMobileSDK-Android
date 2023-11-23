package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val bottomSheetShape = RoundedCornerShape(22.dp, 22.dp, 0.dp, 0.dp)

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