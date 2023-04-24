package com.salesforce.loyalty.mobile.myntorewards.views.adminmenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
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
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
) {

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(start = 8.dp, end = 16.dp)
    ) {
        if (canNavigateBack) {
            Spacer(modifier = Modifier.height(15.dp))
            IconButton(onClick = navigateUp) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = stringResource(R.string.back_button)
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
fun ConnectedAppSetting(/*connectedApp: ConnectedApp*//*onOpenButtonClicked: () -> Unit*/navController: NavController) {
    var openConnectedAppList by remember { mutableStateOf(false) }
    Scaffold(topBar = {
        SettingsAppBar(
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = { navController.navigateUp() }
        )
    }) { innerPadding ->

        Column(
            modifier = Modifier
                .background(MyProfileScreenBG)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.White, shape = RoundedCornerShape(6.dp))
                    .padding(8.dp)

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
                        .weight(0.2f).clickable {
                            openConnectedAppList = true
                        }
                )
            }
        }
    }
    if (openConnectedAppList) {
        LaunchedEffect(Unit) {
            navController.navigate(SettingsScreen.ConnectedAppSettings.name)
        }
    }
}