package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro


@Composable
fun CameraPermissionScreen(launchPermission: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(200.dp))
        Text(
            text = stringResource(id = R.string.permission_message),
            fontFamily = font_sf_pro,
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 32.dp, end = 32.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(200.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(), onClick = {
                launchPermission()
            },
            colors = ButtonDefaults.buttonColors(VibrantPurple40),
            shape = RoundedCornerShape(100.dp)

        ) {
            Text(
                text = stringResource(id = R.string.request_permission),
                fontFamily = font_sf_pro,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 3.dp, bottom = 3.dp)
            )
        }
        Spacer(modifier = Modifier.height(200.dp))
    }

}