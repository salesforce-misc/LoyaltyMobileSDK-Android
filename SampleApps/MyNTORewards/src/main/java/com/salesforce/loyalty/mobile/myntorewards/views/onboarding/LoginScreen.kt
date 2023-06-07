package com.salesforce.loyalty.mobile.myntorewards.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.BottomSheetType
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.LoginState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.OnBoardingViewModelAbstractInterface
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.Screen
import com.salesforce.loyalty.mobile.myntorewards.views.onboarding.OutlineFieldText
import com.salesforce.loyalty.mobile.myntorewards.views.onboarding.PasswordTextField

//Login UI. getting triggered from Onboarding Screen or from Join UI bottom link

@Composable
fun LoginUI(
    navController: NavController,
    openPopup: (popupStatus: BottomSheetType) -> Unit,
    closeSheet: () -> Unit,
    model: OnBoardingViewModelAbstractInterface
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .testTag("LoginUI")
            .background(Color.White, RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        PopupHeader(headingText = stringResource(id = R.string.login_text))
        {
            closeSheet()
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        )
        {
            //val model: OnboardingScreenViewModel = viewModel()
            LoginForm(navController, openPopup, model) {
                closeSheet()
            }
            LinkNewMemberJoin {
                openPopup(it)
            }
        }
    }
}

@Composable
fun LoginForm(
    navController: NavController,
    openPopup: (popupStatus: BottomSheetType) -> Unit,
    model: OnBoardingViewModelAbstractInterface,
    closeSheet: () -> Unit
) {
    Box() {
        val context = LocalContext.current
        var isInProgress by remember { mutableStateOf(false) }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            var emailAddressPhoneNumberText by remember { mutableStateOf(TextFieldValue("")) }
            var passwordtext by remember { mutableStateOf(TextFieldValue("")) }

            OutlineFieldText(
                emailAddressPhoneNumberText,
                stringResource(id = R.string.email_address_phone_number_text)
            ) {
                emailAddressPhoneNumberText = it
            }
            PasswordTextField(
                passwordtext,
                placeholderText = stringResource(id = R.string.form_password)
            ) {
                passwordtext = it
            }

            //Form Fields

            Text(
                text = stringResource(id = R.string.forget_your_password_text),
                fontFamily = font_sf_pro,
                color = VibrantPurple40,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )


          //fetching reference of viewmodel
            val loginStatus by model.loginStatusLiveData.observeAsState(LoginState.LOGIN_DEFAULT_EMPTY) // collecting livedata as state

            //loginStatus state being change to Success after token fetch
           when (loginStatus) {
                LoginState.LOGIN_SUCCESS -> {
                    isInProgress = false
                    Toast.makeText(LocalContext.current, "Login Success", Toast.LENGTH_LONG).show()
                    closeSheet()  // closing popup
                    model.resetLoginStatusDefault()
                    navController.navigate(Screen.HomeScreen.route){
                        popUpTo(0)
                    } //navigate to home screen
                }
                LoginState.LOGIN_FAILURE -> {
                    isInProgress = false
                    Toast.makeText(LocalContext.current, "Login Failed", Toast.LENGTH_LONG).show()
                    model.resetLoginStatusDefault()//reset login status to default
                }
                LoginState.LOGIN_IN_PROGRESS -> {
                    isInProgress = true
                }
                LoginState.LOGIN_SUCCESS_ENROLLMENT_REQUIRED -> {
                    isInProgress = false
                    model.resetLoginStatusDefault()
                    openPopup(BottomSheetType.POPUP_JOIN)
                }
                else -> {}
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth(), onClick = {
                    model.loginUser(emailAddressPhoneNumberText.text, passwordtext.text, context)
                },
                enabled =
                isLoginButtonEnabled(
                    emailAddressPhoneNumberText.text,
                    passwordtext.text,
                ),
                colors = ButtonDefaults.buttonColors(VibrantPurple40),
                shape = RoundedCornerShape(100.dp)

            ) {
                Text(
                    text = stringResource(id = R.string.login_text_header),
                    fontFamily = font_sf_pro,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 3.dp, bottom = 3.dp)
                )
            }
        }
        if (isInProgress) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(0.1f)
                    .align(Alignment.Center)
            )
        }
    }
}

fun isLoginButtonEnabled(
    emailAddressText: String,
    passwordText: String,
): Boolean {
    val emailNotEmpty = emailAddressText.isNotEmpty()
    val passwordNotEmpty = passwordText.isNotEmpty()
    return (emailNotEmpty && passwordNotEmpty)
}

@Composable
fun LinkNewMemberJoin(openPopup: (popupStatus: BottomSheetType) -> Unit) {
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontFamily = font_sf_pro,
                    fontSize = 16.sp
                )
            ) {
                append(stringResource(id = R.string.not_a_member_text))
            }
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = VibrantPurple40,
                    fontFamily = font_sf_pro,
                    fontSize = 16.sp
                )
            ) {
                append(stringResource(id = R.string.join_now_text))
            }
        },
        modifier = Modifier
            .fillMaxWidth(1f)
            .clickable {
                openPopup(BottomSheetType.POPUP_JOIN)
            },
        textAlign = TextAlign.Center
    )
}
