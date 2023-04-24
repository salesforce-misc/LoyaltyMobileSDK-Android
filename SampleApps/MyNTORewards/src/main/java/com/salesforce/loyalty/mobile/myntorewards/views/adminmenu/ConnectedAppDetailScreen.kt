package com.salesforce.loyalty.mobile.myntorewards.views.adminmenu

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ConnectedApp
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ConnectedAppViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ConnectedAppViewModelFactory

@Composable
fun ConnectedAppBar(
    currentScreen: SettingsScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    isNew: Boolean,
    setIsEditing: (isEditing: Boolean) -> Unit,
    saveConnectedApp: () -> Unit
) {
    var saveNeeded by remember {
        mutableStateOf(isNew)
    }
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(start = 8.dp, end = 16.dp)
    ) {
        if (canNavigateBack) {
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            ) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = stringResource(R.string.back_button),
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            painter = painterResource(
                                id = if (saveNeeded) {
                                    R.drawable.ic_save
                                } else {
                                    android.R.drawable.ic_menu_edit
                                }
                            ),
                            contentDescription = stringResource(R.string.back_button),
                            modifier = Modifier
                                .padding(15.dp)
                                .width(24.dp)
                                .height(24.dp)
                                .clickable {
                                    if (!saveNeeded) {
                                        setIsEditing(true)
                                        saveNeeded = true
                                    } else {
                                        // save in sp
                                        saveConnectedApp()
                                    }
                                },
                            tint = Black1TextOrderScreen
                        )
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material.Text(
            text = stringResource(currentScreen.title),
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


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectedAppDetails(
    navController: NavController,
    currentScreen: SettingsScreen,
    isNew: Boolean
) {
    val connectedAppName =
        navController.previousBackStackEntry?.savedStateHandle?.get<String>(AppConstants.KEY_OPEN_CONNECTED_APP_NAME)
    navController.previousBackStackEntry?.savedStateHandle?.set(
        AppConstants.KEY_OPEN_CONNECTED_APP_NAME,
        null
    )
    val context = LocalContext.current
    val viewModel: ConnectedAppViewModel =
        viewModel(factory = ConnectedAppViewModelFactory(context))

    val connectedApp = connectedAppName?.let { viewModel.getConnectedApp(context, it) }
    var isEditing by remember { mutableStateOf(false) }
    var name by remember {
        mutableStateOf(
            TextFieldValue(
                connectedApp?.name ?: ""
            )
        )
    }
    var consumerKey by remember {
        mutableStateOf(
            TextFieldValue(
                connectedApp?.consumerKey ?: ""
            )
        )
    }
    var consumerSecret by remember {
        mutableStateOf(
            TextFieldValue(
                connectedApp?.consumerSecret ?: ""
            )
        )
    }
    var callbackUrl by remember {
        mutableStateOf(
            TextFieldValue(
                connectedApp?.callbackUrl ?: ""
            )
        )
    }
    var baseUrl by remember { mutableStateOf(TextFieldValue(connectedApp?.baseUrl ?: "")) }
    var instanceUrl by remember { mutableStateOf(TextFieldValue(connectedApp?.instanceUrl ?: "")) }
    var communityUrl by remember {
        mutableStateOf(
            TextFieldValue(
                connectedApp?.communityUrl ?: ""
            )
        )
    }

    Scaffold(
        topBar = {
            ConnectedAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                isNew = isNew,
                setIsEditing = {
                    isEditing = true
                },
                saveConnectedApp = {
                    if (name.text.isEmpty() ||
                        consumerKey.text.isEmpty() ||
                        consumerSecret.text.isEmpty() ||
                        callbackUrl.text.isEmpty() ||
                        baseUrl.text.isEmpty() ||
                        instanceUrl.text.isEmpty() ||
                        communityUrl.text.isEmpty()
                    ) {
                        Toast.makeText(
                            context,
                            "Some fields are missing",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        viewModel.saveConnectedApp(
                            context,
                            ConnectedApp(
                                name.text,
                                consumerKey.text,
                                consumerSecret.text,
                                callbackUrl.text,
                                baseUrl.text,
                                instanceUrl.text,
                                communityUrl.text
                            )
                        )
                        navController.navigateUp()
                    }
                },
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .background(MyProfileScreenBG)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // Require to go at the next input from the keyboard.
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextLabel(stringId = R.string.label_name)
                        TextField(
                            value = name,
                            onValueChange = { newText ->
                                name = newText
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = CardFieldText,
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            enabled = (isNew || isEditing)
                        )
                    }
                }


                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(start = 8.dp, end = 8.dp)
                        .background(Color.White, shape = RoundedCornerShape(6.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextLabel(stringId = R.string.label_consumer_key)
                        TextField(
                            value = consumerKey,
                            onValueChange = { newText ->
                                consumerKey = newText
                            }, colors = TextFieldDefaults.textFieldColors(
                                textColor = CardFieldText,
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            enabled = (isNew || isEditing)
                        )
                    }
                    Divider(color = Color.LightGray, thickness = 1.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextLabel(stringId = R.string.label_consumer_secret)
                        TextField(
                            value = consumerSecret,
                            onValueChange = { newText ->
                                consumerSecret = newText
                            }, colors = TextFieldDefaults.textFieldColors(
                                textColor = CardFieldText,
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            enabled = (isNew || isEditing)
                        )
                    }
                    Divider(color = Color.LightGray, thickness = 1.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextLabel(stringId = R.string.label_callback_url)
                        TextField(
                            value = callbackUrl,
                            onValueChange = { newText ->
                                callbackUrl = newText
                            }, colors = TextFieldDefaults.textFieldColors(
                                textColor = CardFieldText,
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            enabled = (isNew || isEditing)
                        )
                    }

                    Divider(color = Color.LightGray, thickness = 1.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextLabel(stringId = R.string.label_base_url)
                        TextField(
                            value = baseUrl,
                            onValueChange = { newText ->
                                baseUrl = newText
                            }, colors = TextFieldDefaults.textFieldColors(
                                textColor = CardFieldText,
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            enabled = (isNew || isEditing)
                        )
                    }
                    Divider(color = Color.LightGray, thickness = 1.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextLabel(stringId = R.string.label_instance_url)
                        TextField(
                            value = instanceUrl,
                            onValueChange = { newText ->
                                instanceUrl = newText
                            }, colors = TextFieldDefaults.textFieldColors(
                                textColor = CardFieldText,
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            enabled = (isNew || isEditing)
                        )
                    }
                    Divider(color = Color.LightGray, thickness = 1.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextLabel(stringId = R.string.label_community_url)
                        TextField(
                            value = communityUrl,
                            onValueChange = { newText ->
                                communityUrl = newText
                            }, colors = TextFieldDefaults.textFieldColors(
                                textColor = CardFieldText,
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            enabled = (isNew || isEditing)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TextLabel(@StringRes stringId: Int) {
    Text(
        text = stringResource(id = stringId),
        modifier = Modifier
            .width(90.dp)
            .padding(top = 8.dp, start = 8.dp),
        fontFamily = font_sf_pro,
        fontWeight = FontWeight.Normal,
        color = Color.Black,
        textAlign = TextAlign.Start,
        fontSize = 15.sp,
    )
}
