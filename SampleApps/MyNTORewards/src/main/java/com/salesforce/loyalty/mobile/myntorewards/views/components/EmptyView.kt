package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_EMPTY_VIEW

@Composable
fun ErrorOrEmptyView(viewImageID:Int= R.drawable.ic_empty_view, header: String, description: String? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize().verticalScroll(rememberScrollState()).testTag(TEST_TAG_EMPTY_VIEW)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = viewImageID),
            contentDescription = header
        )
        BodyTextBold(text = header,  modifier = Modifier.padding(top = 4.dp), textAlign = TextAlign.Center)
        description?.let {
            BodyTextSmall(text = it, modifier = Modifier.padding(top = 4.dp), textAlign = TextAlign.Center)
        }
    }
}