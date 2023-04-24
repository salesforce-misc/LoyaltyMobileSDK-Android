package com.salesforce.loyalty.mobile.myntorewards.views.adminmenu

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG

/**
 * enum values that represent the screens in the settings flow
 */
enum class SettingsScreen(@StringRes val title: Int) {
    OpenSettings(title = R.string.label_settings),
    ConnectedAppSettings(title = R.string.label_connected_app),
    OpenConnectedAppDetail(title = R.string.label_connected_app),
    NewConnectedApp(title = R.string.text_new_connectedapp_details)
}

@Composable
fun SettingsMain(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = SettingsScreen.valueOf(
        backStackEntry?.destination?.route ?: SettingsScreen.OpenSettings.name
    )

    Scaffold(
        modifier = Modifier.background(MyProfileScreenBG)
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = SettingsScreen.OpenSettings.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = SettingsScreen.OpenSettings.name) {
                ConnectedAppSetting(
                    navController
                )
            }
            composable(route = SettingsScreen.ConnectedAppSettings.name) {
                val context = LocalContext.current
                SelectedConnectedApp(navController)
            }
            composable(route = SettingsScreen.OpenConnectedAppDetail.name) {
                val context = LocalContext.current
                ConnectedAppDetails(navController, SettingsScreen.OpenConnectedAppDetail,  isNew = false)
            }
            composable(route = SettingsScreen.NewConnectedApp.name) {
                ConnectedAppDetails(navController, SettingsScreen.NewConnectedApp, isNew = true)
            }
        }
    }
}
