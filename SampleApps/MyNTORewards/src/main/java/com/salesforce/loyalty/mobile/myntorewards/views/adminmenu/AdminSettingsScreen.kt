package com.salesforce.loyalty.mobile.myntorewards.views.adminmenu

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ConnectedAppViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.factory.ConnectedAppViewModelFactory

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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ConnectedAppSetting(navController: NavController, closeSheet: () -> Unit) {
    Scaffold(topBar = {
        SettingsAppBar(
            closeSheet
        )
    }) {

        Column(
            modifier = Modifier
                .background(MyProfileScreenBG)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            SettingListItem(title = R.string.label_connected_app) {
                navController.navigate(SettingsScreen.ConnectedAppSettings.name)
            }
            SettingListItem(title = R.string.menu_app_settings) {
                navController.navigate(SettingsScreen.AppSettings.name)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingListItem(title: Int, settingValue: String? = null, onClick: ()-> Unit){
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White, shape = RoundedCornerShape(6.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

    ) {
        Column(
            modifier = Modifier.weight(0.8f),
        ) {
            androidx.compose.material.Text(
                text = stringResource(title),
                modifier = Modifier
                    .padding(start = 4.dp),
                fontFamily = font_sf_pro,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                textAlign = TextAlign.Start,
                fontSize = 16.sp
            )
            settingValue?.let {
                androidx.compose.material.Text(
                    text = settingValue,
                    modifier = Modifier
                        .padding(start = 4.dp),
                    fontFamily = font_sf_pro,
                    fontWeight = FontWeight.Normal,
                    color = CardFieldText,
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp)
            }
        }
        Image(
            painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
            contentDescription = stringResource(title),
            colorFilter = ColorFilter.tint(TextGray),
            modifier = Modifier
                .wrapContentSize(Alignment.CenterEnd)
                .padding(vertical = 6.dp, horizontal = 4.dp)
                .weight(0.2f)
                .clickable(interactionSource = interactionSource, indication = null) {
                    onClick()
                }
        )
    }
}