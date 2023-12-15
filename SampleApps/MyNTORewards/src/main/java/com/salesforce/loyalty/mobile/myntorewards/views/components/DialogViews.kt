package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ShowErrorDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit,
    confirmButtonText: String,
    confirmButtonClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = description)
        },
        confirmButton = {
            TextButtonCustom(
                confirmButtonClick,
                confirmButtonText,
                modifier = Modifier.wrapContentSize()
            )
        }
    )
}