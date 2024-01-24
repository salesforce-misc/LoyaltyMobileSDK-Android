package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.salesforce.loyalty.mobile.MyNTORewards.R

@Composable
fun EmptyView(header: String, description: String? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize().verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty_view),
            contentDescription = header
        )
        BodyTextBoldCentered(text = header,  modifier = Modifier.fillMaxWidth().padding(top = 4.dp))
        description?.let {
            BodyTextSmall(text = it, modifier = Modifier.padding(top = 4.dp), textAlign = TextAlign.Center)
        }
    }
}