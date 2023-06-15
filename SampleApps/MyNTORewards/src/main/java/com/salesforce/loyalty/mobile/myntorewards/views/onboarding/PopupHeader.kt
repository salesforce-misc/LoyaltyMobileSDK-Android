package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CLOSE_POPUP

//header Component being used in Popup
@Composable
fun PopupHeader(headingText: String, closeSheet: () -> Unit) {

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 17.dp, bottom = 17.dp)
    )

    {
        Text(
            text = headingText,
            fontFamily = font_sf_pro,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(start = 10.dp),
            textAlign = TextAlign.Start,
        )

        Image(
            painter = painterResource(id = R.drawable.close_popup_icon),
            contentDescription = stringResource(id = R.string.cd_close_popup),
            modifier = Modifier
                .width(16.dp)
                .height(16.dp)
                .testTag(TEST_TAG_CLOSE_POPUP)
                .clickable {
                    closeSheet()
                },
            contentScale = ContentScale.FillWidth,
        )
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}