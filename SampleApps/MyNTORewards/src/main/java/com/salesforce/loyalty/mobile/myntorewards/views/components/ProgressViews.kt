package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.testTag

const val CIRCULAR_PROGRESS_TEST_TAG = "CIRCULAR_PROGRESS_TEST_TAG"

@Composable
fun CircularProgress() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize(0.1f)
                .align(Alignment.Center)
                .testTag(CIRCULAR_PROGRESS_TEST_TAG)
        )
    }
}