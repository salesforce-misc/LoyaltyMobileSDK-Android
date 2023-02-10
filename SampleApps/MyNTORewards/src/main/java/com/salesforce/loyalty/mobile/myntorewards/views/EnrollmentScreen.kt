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
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.OnboardingScreenViewModel

//Enrollment Screen UI
@Composable
fun EnrollmentUI(openPopup: (popupStatus: String) -> Unit) {
    val cornerSize = 16.dp
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.92f)
            .background(Color.White, RoundedCornerShape(cornerSize))
    )
    {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            PopupHeader(headingText = stringResource(id = R.string.join_text)) {
                openPopup(it)
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                OnboardingForm {
                    openPopup(it)
                }
                LinkAlreadyAMember {
                    openPopup(it)
                }
            }
        }
    }
}


@Composable
fun OnboardingForm(openPopup: (popupStatus: String) -> Unit) {
    var firstNameText by remember { mutableStateOf(TextFieldValue("")) }
    var lastNameText by remember { mutableStateOf(TextFieldValue("")) }
    var mobileNumberText by remember { mutableStateOf(TextFieldValue("")) }
    var emailAddressText by remember { mutableStateOf(TextFieldValue("")) }
    var passwordText by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPasswordText by remember { mutableStateOf(TextFieldValue("")) }

    OutlineFieldText(firstNameText, stringResource(id = R.string.onboard_form_first_name)) {
        firstNameText = it
    }
    OutlineFieldText(lastNameText, stringResource(id = R.string.onboard_form_last_name)) {
        lastNameText = it
    }
    OutlineFieldText(mobileNumberText, stringResource(id = R.string.onboard_form_mobile_number)) {
        mobileNumberText = it
    }
    OutlineFieldText(emailAddressText, stringResource(id = R.string.onboard_form_email_address)) {
        emailAddressText = it
    }
    OutlineFieldText(passwordText, stringResource(id = R.string.form_password)) {
        passwordText = it
    }
    OutlineFieldText(
        confirmPasswordText,
        stringResource(id = R.string.onboard_form_confirm_password)
    ) {
        confirmPasswordText = it
    }

    //calling checkBox UI
    SimpleCheckboxComponent()

    val model: OnboardingScreenViewModel = viewModel()
    val enrollmentStatusLiveData by model.enrollmentStatusLiveData.observeAsState("Enrollment Flow")
/*
                if(enrollmentStatusLiveData=="Enrollment Success")
                {
                    Toast.makeText(LocalContext.current, "Enrollment Success", Toast.LENGTH_LONG).show()

                }*/

    //join button
    Text(
        text = stringResource(id = R.string.join_text),
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
                model.invokeEnrollmentApi(
                    firstNameText.text,
                    lastNameText.text,
                    mobileNumberText.text,
                    emailAddressText.text,
                    passwordText.text,
                    confirmPasswordText.text
                )
                openPopup("None")
            }
    )

}

@Composable
fun LinkAlreadyAMember(openPopup: (popupStatus: String) -> Unit) {
    //Text Already a member Login
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontFamily = font_sf_pro,
                    fontSize = 16.sp
                )
            ) {
                append(stringResource(id = R.string.existing_member_text))
            }
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = VibrantPurple40,
                    fontFamily = font_sf_pro,
                    fontSize = 16.sp
                )
            ) {
                append(stringResource(id = R.string.login_text))
            }
        },
        modifier = Modifier
            .fillMaxWidth(1f)
            .clickable {
                openPopup("Login")
            },
        textAlign = TextAlign.Center
    )
}
