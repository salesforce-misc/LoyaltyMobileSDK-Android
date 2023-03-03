package com.salesforce.loyalty.mobile.myntorewards.views.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LightPurple
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro

@Composable
fun JoinButtonProm(openJoinPopup: () -> Unit) {

    Button(
        modifier = Modifier
            .width(288.dp), onClick = {
            openJoinPopup()
        },
        colors = ButtonDefaults.buttonColors(LightPurple),
        shape = RoundedCornerShape(100.dp)

    ) {
        Text(
            text = stringResource(id = R.string.join_text),
            fontFamily = font_sf_pro,
            color = VibrantPurple40,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 3.dp, bottom = 3.dp)
        )
    }
}