package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.OnboardingScreenViewModel
import java.time.Duration

//Login UI. getting triggered from Onboarding Screen or from Join UI bottom link
@Composable
fun LoginUI(navController: NavController, openPopup: (popupStatus: String) -> Unit) {
    val cornerSize = 16.dp
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(Color.White, RoundedCornerShape(cornerSize))
    )
    {
        Column {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 16.dp,  end = 16.dp, start = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                PopupHeader(headingText = stringResource(id = R.string.login_text))
                {
                    openPopup(it)
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                LoginForm(navController){
                    openPopup(it)
                }

                NewMemberJoin{
                    openPopup(it)
                }
            }
        }
    }
}

@Composable
fun LoginForm(navController: NavController, openPopup: (popupStatus: String)->Unit) {
    var emailAddressPhoneNumberText by remember { mutableStateOf(TextFieldValue("")) }
    var passwordtext by remember { mutableStateOf(TextFieldValue("")) }

    OutlineFieldText(
        emailAddressPhoneNumberText,
        stringResource(id = R.string.email_address_phone_number_text)
    ) {
        emailAddressPhoneNumberText = it
    }
    OutlineFieldText(passwordtext, stringResource(id = R.string.form_password)) {
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


    val model: OnboardingScreenViewModel = viewModel()
    val loginStatus by model.loginStatusLiveData.observeAsState()
    /*   if(loginStatus=="Login Success")
       {
          Toast.makeText(LocalContext.current, "Login Success", Toast.LENGTH_LONG).show()
       }*/

    Text(
        text = stringResource(id = R.string.login_text_header),
        fontFamily = font_sf_pro,
        color = Color.White,
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .width(327.dp)
            .background(VibrantPurple40, RoundedCornerShape(100.dp))
            .padding(top = 10.dp, bottom = 10.dp)
            .clickable {
                model.invokeTokenGenerationApi(emailAddressPhoneNumberText.text, passwordtext.text)
                openPopup("None")
                navController.navigate(Screen.HomeScreen.route)
            }
    )
}

@Composable
fun NewMemberJoin(openPopup: (popupStatus: String) -> Unit) {
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
                openPopup("Join")
            },
        textAlign = TextAlign.Center
    )
}
