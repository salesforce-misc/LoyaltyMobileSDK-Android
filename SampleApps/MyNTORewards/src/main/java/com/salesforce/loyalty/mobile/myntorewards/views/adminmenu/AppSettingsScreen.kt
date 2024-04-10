package com.salesforce.loyalty.mobile.myntorewards.views.adminmenu

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ConnectedAppViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.factory.ConnectedAppViewModelFactory


@Composable
fun AppSettingAppBar(
    title: Int,
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
        Text(
            text = stringResource(title),
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
fun AppSettings(navController: NavController) {
    val viewModel: ConnectedAppViewModel =
        viewModel(factory = ConnectedAppViewModelFactory())
    val context = LocalContext.current
    val loyaltyProgramName: String? = viewModel.getLoyaltyProgramName(context)
    Scaffold(topBar = {
        AppSettingAppBar(
            title = SettingsScreen.AppSettings.title,
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = { navController.navigateUp() }
        )
    }) {
        Column(
            modifier = Modifier
                .background(MyProfileScreenBG)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            SettingListItem(R.string.mnu_loyalty_program_name, settingValue = loyaltyProgramName) {
                navController.navigate(SettingsScreen.LoyaltyProgramNameSettings.name)
            }
            SettingListItem(R.string.menu_reward_currency) {
                navController.navigate(SettingsScreen.CurrencySettings.name)
            }
        }
    }
}

@Composable
fun LoyaltyProgramNameSettings(navController: NavController) {
    ProgramSetting(AppConstants.KEY_LOYALTY_PROGRAM_NAME, navController)
}


@Composable
fun RewardCurrencyNameSettings(navController: NavController) {
    ProgramSetting(AppConstants.KEY_REWARD_CURRENCY_NAME, navController)
}

@Composable
fun RewardCurrencyNameShortSettings(navController: NavController) {
    ProgramSetting(AppConstants.KEY_REWARD_CURRENCY_NAME_SHORT, navController)
}

@Composable
fun TierCurrencyNameSettings(navController: NavController) {
    ProgramSetting(AppConstants.KEY_TIER_CURRENCY_NAME, navController)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CurrencySettings(navController: NavController) {
    val viewModel: ConnectedAppViewModel =
        viewModel(factory = ConnectedAppViewModelFactory())
    val context = LocalContext.current
    val rewardCurrencyName: String = viewModel.getRewardCurrencyName(context)
    val rewardCurrencyNameShort: String = viewModel.getRewardCurrencyNameShort(context)
    val tierCurrencyName: String = viewModel.getTierCurrencyName(context)

    Scaffold(topBar = {
        AppSettingAppBar(
            title = SettingsScreen.CurrencySettings.title,
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = { navController.navigateUp() }
        )
    }) {
        Column(
            modifier = Modifier
                .background(MyProfileScreenBG)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            SettingListItem(title = R.string.label_currency_name, rewardCurrencyName) {
                navController.navigate(SettingsScreen.RewardCurrencyNameSettings.name)
            }
            SettingListItem(title = R.string.label_currency_name_short, rewardCurrencyNameShort) {
                navController.navigate(SettingsScreen.RewardCurrencyNameShortSettings.name)
            }
            SettingListItem(R.string.label_tier_currency, tierCurrencyName) {
                navController.navigate(SettingsScreen.TierCurrencyNameSettings.name)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Setting(
    navController: NavController,
    title: Int,
    settingValue: String,
    onClose: (newSettingValue: String) -> Unit
) {
    var name by remember {
        mutableStateOf(
            TextFieldValue(
                settingValue
            )
        )
    }
    val context = LocalContext.current
    Scaffold(topBar = {
        AppSettingAppBar(
            title = title,
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = {
                val value = name.text
                if (value.isNullOrEmpty()) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_app_setting_no_value),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onClose(value)
                }
            }
        )
    }) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(MyProfileScreenBG)
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 8.dp, end = 8.dp)

        ) {

            Spacer(modifier = Modifier.height(8.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(start = 8.dp, end = 8.dp)
                    .background(Color.White, shape = RoundedCornerShape(6.dp))
            ) {
                TextField(
                    value = name,
                    onValueChange = { newText ->
                        name = newText
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedTextColor = CardFieldText,
                        unfocusedTextColor = CardFieldText,
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    enabled = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = stringResource(id = R.string.close_text),
                            modifier = Modifier
                                .clickable {
                                    name = TextFieldValue("")
                                }
                        )
                    }
                )

            }
        }
    }
}

@Composable
fun ProgramSetting(settingKey: String, navController: NavController) {
    val viewModel: ConnectedAppViewModel =
        viewModel(factory = ConnectedAppViewModelFactory())
    val context = LocalContext.current
    when (settingKey) {
        AppConstants.KEY_LOYALTY_PROGRAM_NAME -> {
            val loyaltyProgramName: String = viewModel.getLoyaltyProgramName(context)
            Setting(navController, R.string.mnu_loyalty_program_name, loyaltyProgramName) {
                viewModel.setLoyaltyProgramName(context, it)
                navController.navigateUp()
            }
        }
        AppConstants.KEY_REWARD_CURRENCY_NAME -> {
            val rewardCurrencyName: String = viewModel.getRewardCurrencyName(context)
            Setting(navController, R.string.label_currency_name, rewardCurrencyName) {
                viewModel.setRewardCurrencyName(context, it)
                navController.navigateUp()
            }
        }
        AppConstants.KEY_REWARD_CURRENCY_NAME_SHORT -> {
            val rewardCurrencyName: String = viewModel.getRewardCurrencyNameShort(context)
            Setting(navController, R.string.label_currency_name_short, rewardCurrencyName) {
                viewModel.setRewardCurrencyNameShort(context, it)
                navController.navigateUp()
            }
        }
        AppConstants.KEY_TIER_CURRENCY_NAME -> {
            val tierCurrencyName: String = viewModel.getTierCurrencyName(context)
            Setting(navController, R.string.label_tier_currency, tierCurrencyName) {
                viewModel.setTierCurrencyName(context, it)
                navController.navigateUp()
            }
        }
    }
}
