package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.salesforce.loyalty.mobile.myntorewards.views.components.HeaderText

/**
 * Common Title View, can be used across multiple screens
 */
@Composable
fun TitleView(titleText: String) {
    HeaderText(
        text = titleText,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 50.dp, bottom = 0.dp)
    )
}