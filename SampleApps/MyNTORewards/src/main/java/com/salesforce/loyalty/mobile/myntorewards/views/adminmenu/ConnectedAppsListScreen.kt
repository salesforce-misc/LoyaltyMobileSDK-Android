package com.salesforce.loyalty.mobile.myntorewards.views.adminmenu

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ConnectedApp
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_OPEN_CONNECTED_APP_INSTANCE
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ConnectedAppViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ConnectedAppViewModelFactory

@Composable
fun ConnectedAppsListAppBar(
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
            text = stringResource(SettingsScreen.ConnectedAppSettings.title),
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
fun SelectedConnectedApp(navController: NavController) {
    var openedConnectedAppInstance by remember { mutableStateOf("") }
    Scaffold(topBar = {
        ConnectedAppsListAppBar(
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = { navController.navigateUp() }
        )
    }) { innerPadding ->

        val context: Context = LocalContext.current
        val model: ConnectedAppViewModel =
            viewModel(factory = ConnectedAppViewModelFactory(context))  //fetching reference of viewmodel
        // collecting livedata as state
        val connectedApps by model.savedAppsLiveData.observeAsState()
        val selectedInstanceUrl by model.selectedInstanceLiveData.observeAsState()

        LaunchedEffect(Unit) {
            model.retrieveAll(context)
        }
        Log.d("ConnectedApps", " connectedApps : $connectedApps")
        val selectedApp = connectedApps?.filter {
            it.instanceUrl?.let { instance ->
                selectedInstanceUrl.equals(instance)
            } == true
        }
//        val selectedApp = ForceAuthManager.forceAuthManager.getConnectedApp()
        Log.d("ConnectedApps", " selected : $selectedApp")
        var myConnectedApps: MutableList<ConnectedApp> = mutableListOf()
        if (connectedApps != null) {

            myConnectedApps.addAll(connectedApps!!)

            selectedApp?.get(0)?.let {
                myConnectedApps?.remove(it)
            }
        }
        Log.d("ConnectedApps", " myConnectedApps : $myConnectedApps")

        selectedApp?.get(0)?.let {
            Column(
                modifier = Modifier
                    .background(MyProfileScreenBG)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                val interactionSource = remember { MutableInteractionSource() }
                selectedApp?.get(0)?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )
                    {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.label_selected_connected_app).toUpperCase(
                                Locale.current
                            ),
                            modifier = Modifier
                                .padding(start = 18.dp, top = 8.dp)
                                .wrapContentSize(),
                            fontFamily = font_sf_pro,
                            fontWeight = FontWeight.Bold,
                            color = TextGray,
                            textAlign = TextAlign.Start,
                            fontSize = 15.sp
                        )

                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(6.dp))
                                .padding(4.dp)
                        ) {

                            androidx.compose.material3.Text(
                                text = it.name,
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .weight(0.8f)
                                    .align(Alignment.CenterVertically),
                                fontFamily = font_sf_pro,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                                textAlign = TextAlign.Start,
                                fontSize = 16.sp
                            )

                            Image(
                                painter = painterResource(id = R.drawable.baseline_info_24),
                                contentDescription = stringResource(R.string.label_selected_connected_app),
                                colorFilter = ColorFilter.tint(VibrantPurple40),
                                modifier = Modifier
                                    .wrapContentSize(Alignment.CenterEnd)
                                    .padding(4.dp)
                                    .weight(0.2f)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        openedConnectedAppInstance = it.instanceUrl
                                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                                            set(
                                                KEY_OPEN_CONNECTED_APP_INSTANCE,
                                                openedConnectedAppInstance
                                            )
                                        }
                                        navController.navigate(SettingsScreen.OpenConnectedAppDetail.name)
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.label_my_connected_apps).toUpperCase(
                                Locale.current
                            ),
                            modifier = Modifier
                                .padding(start = 18.dp)
                                .wrapContentSize(),
                            fontFamily = font_sf_pro,
                            fontWeight = FontWeight.Bold,
                            color = TextGray,
                            textAlign = TextAlign.Start,
                            fontSize = 15.sp
                        )
                    }
                }


                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color.White, shape = RoundedCornerShape(6.dp))
                ) {
                    if (myConnectedApps?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            items(myConnectedApps) {
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxWidth()
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            it.instanceUrl?.let { instance ->
                                                model.setSelectedApp(
                                                    instance
                                                )
                                            }
                                        }
                                ) {

                                    androidx.compose.material3.Text(
                                        text = it.name,
                                        modifier = Modifier
                                            .padding(start = 12.dp)
                                            .weight(0.8f)
                                            .align(Alignment.CenterVertically),
                                        fontFamily = font_sf_pro,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black,
                                        textAlign = TextAlign.Start,
                                        fontSize = 16.sp
                                    )

                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_info_24),
                                        contentDescription = stringResource(R.string.label_selected_connected_app),
                                        colorFilter = ColorFilter.tint(VibrantPurple40),
                                        modifier = Modifier
                                            .wrapContentSize(Alignment.CenterEnd)
                                            .padding(4.dp)
                                            .weight(0.2f)
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                openedConnectedAppInstance = it.instanceUrl
                                                navController.currentBackStackEntry?.savedStateHandle?.apply {
                                                    set(
                                                        KEY_OPEN_CONNECTED_APP_INSTANCE,
                                                        openedConnectedAppInstance
                                                    )
                                                }
                                                navController.navigate(SettingsScreen.OpenConnectedAppDetail.name)
                                            }
                                    )
                                }
                                Divider(color = Color.LightGray, thickness = 1.dp)
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .padding(top = 8.dp, start = 4.dp, end = 4.dp, bottom = 4.dp)
                            .fillMaxWidth()
                            .clickable(interactionSource = interactionSource, indication = null) {
                                navController.navigate(SettingsScreen.NewConnectedApp.name)
                            }
                    ) {

                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.text_others),
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .height(40.dp)
                                .align(Alignment.CenterVertically),
                            fontFamily = font_sf_pro,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
