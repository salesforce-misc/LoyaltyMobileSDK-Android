package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40

const val CIRCULAR_PROGRESS_TEST_TAG = "CIRCULAR_PROGRESS_TEST_TAG"

@Composable
fun CircularProgress(modifier: Modifier = Modifier.fillMaxSize(), color: Color = VibrantPurple40) {
    Box(modifier = modifier) {
        CircularProgressIndicator(
            color = color,
            modifier = Modifier
                .fillMaxSize(0.1f)
                .align(Alignment.Center)
                .testTag(CIRCULAR_PROGRESS_TEST_TAG)
        )
    }
}

@Composable
fun ProgressDialogComposable(color: Color = VibrantPurple40, onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        CircularProgress(color = color)
    }
}