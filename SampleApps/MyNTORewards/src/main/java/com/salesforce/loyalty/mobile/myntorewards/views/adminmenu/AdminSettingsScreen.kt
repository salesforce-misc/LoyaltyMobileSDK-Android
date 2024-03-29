package com.salesforce.loyalty.mobile.myntorewards.views.adminmenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*

@Composable
fun SettingsAppBar(
    closeSheet: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(start = 8.dp, end = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),

            ) {
            Spacer(Modifier.weight(0.9f))
            IconButton(onClick = { closeSheet() }) {
                Icon(
                    painter = painterResource(id = R.drawable.close_popup_icon),
                    contentDescription = stringResource(R.string.cd_close_popup),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material.Text(
            text = stringResource(SettingsScreen.OpenSettings.title),
            fontFamily = font_archivo,
            fontWeight = FontWeight.Bold,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ConnectedAppSetting(navController: NavController, closeSheet: () -> Unit) {
    Scaffold(topBar = {
        SettingsAppBar(
            closeSheet
        )
    }) { innerPadding ->

        Column(
            modifier = Modifier
                .background(MyProfileScreenBG)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.White, shape = RoundedCornerShape(6.dp))
                    .padding(12.dp)

            ) {
                androidx.compose.material.Text(
                    text = stringResource(id = R.string.label_connected_app),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .weight(0.8f)
                        .align(Alignment.CenterVertically),
                    fontFamily = font_sf_pro,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp
                )
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = stringResource(R.string.label_selected_connected_app),
                    colorFilter = ColorFilter.tint(TextGray),
                    modifier = Modifier
                        .wrapContentSize(Alignment.CenterEnd)
                        .padding(vertical = 6.dp, horizontal = 4.dp)
                        .weight(0.2f)
                        .clickable(interactionSource = interactionSource, indication = null) {
                            navController.navigate(SettingsScreen.ConnectedAppSettings.name)
                        }
                )
            }
        }
    }
}