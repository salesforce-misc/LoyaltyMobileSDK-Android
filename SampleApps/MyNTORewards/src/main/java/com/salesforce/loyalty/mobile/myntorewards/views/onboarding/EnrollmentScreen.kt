package com.salesforce.loyalty.mobile.myntorewards.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*


import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.PopupState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.EnrollmentState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.OnboardingScreenViewModel
import com.salesforce.loyalty.mobile.myntorewards.views.onboarding.CustomTextField
import com.salesforce.loyalty.mobile.myntorewards.views.onboarding.OutlineFieldTextWithError
import com.salesforce.loyalty.mobile.myntorewards.views.onboarding.PasswordTextFieldWithError

//Enrollment Screen UI

@Composable
fun EnrollmentPopup(openPopup: (popupStatus: PopupState) -> Unit)
{
    Popup(
        alignment = Center,
        offset = IntOffset(0, 700),
        onDismissRequest = { openPopup(PopupState.POPUP_NONE)},
        properties = PopupProperties(focusable = true)
    ) {
        EnrollmentUI{ openPopup(it)
        }
    }
}


@Composable
fun EnrollmentUI(openPopup: (popupStatus: PopupState) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight(0.92f)
            .background(Color.White, RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        PopupHeader(headingText = stringResource(id = R.string.join_text)) {
            openPopup(it)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .verticalScroll(
                    rememberScrollState()
                )
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

@Composable
fun OnboardingForm(openPopup: (popupStatus: PopupState) -> Unit) {

    Box() {
        var isInProgress by remember { mutableStateOf(false) }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            var firstNameText by remember { mutableStateOf(TextFieldValue("")) }
            var lastNameText by remember { mutableStateOf(TextFieldValue("")) }
            var mobileNumberText by remember { mutableStateOf(TextFieldValue("")) }
            var emailAddressText by remember { mutableStateOf(TextFieldValue("")) }
            var passwordText by remember { mutableStateOf(TextFieldValue("")) }
            var confirmPasswordText by remember { mutableStateOf(TextFieldValue("")) }
            var mailCheckedState by remember { mutableStateOf(true) }
            var tncCheckedState by remember { mutableStateOf(true) }

            OutlineFieldTextWithError(firstNameText, CustomTextField.SignUpTextFieldType.FIRSTNAME, stringResource(id = R.string.onboard_form_first_name)) {
                firstNameText = it
            }
            OutlineFieldTextWithError(lastNameText, CustomTextField.SignUpTextFieldType.LASTNAME, stringResource(id = R.string.onboard_form_last_name)) {
                lastNameText = it
            }
            OutlineFieldTextWithError(mobileNumberText, CustomTextField.SignUpTextFieldType.PHONE_NUMBER, stringResource(id = R.string.onboard_form_mobile_number)) {
                mobileNumberText = it
            }
            OutlineFieldTextWithError(emailAddressText, CustomTextField.SignUpTextFieldType.EMAIL, stringResource(id = R.string.onboard_form_email_address)) {
                emailAddressText = it
            }
            PasswordTextFieldWithError(passwordText, CustomTextField.SignUpTextFieldType.PASSWORD, placeholderText = stringResource(id = R.string.form_password)) {
                passwordText = it
            }
            PasswordTextFieldWithError(
                confirmPasswordText,
                CustomTextField.SignUpTextFieldType.CONFIRM_PASSWORD,
                password = passwordText.text,
                placeholderText = stringResource(id = R.string.onboard_form_confirm_password)
            ) {
                confirmPasswordText = it
            }



            //calling checkBox UI
            Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {

                CheckBoxTnC {
                    tncCheckedState = it
                }
                CheckBoxMailingList {
                    mailCheckedState = it

                }

            }


            val model: OnboardingScreenViewModel = viewModel() // fetching view mode reference

            //Observing the enrollment status live data as state. As per the Success or failure state will be changed
            val enrollmentStatusLiveData by model.enrollmentStatusLiveData.observeAsState(EnrollmentState.ENROLLMENT_DEFAULT_EMPTY)


            //after enrollment state change to success
            if (enrollmentStatusLiveData == EnrollmentState.ENROLLMENT_SUCCESS) {
                isInProgress= false
                Toast.makeText(LocalContext.current, "Enrollment Success", Toast.LENGTH_LONG)
                    .show()
                openPopup(PopupState.POPUP_CONGRATULATIONS)
                //closing the popup
                // navController.navigate(Screen.HomeScreen.route) // routing to homescreen
                model.resetEnrollmentStatusDefault()
            } //after enrollment state change to failure
            else if (enrollmentStatusLiveData == EnrollmentState.ENROLLMENT_FAILURE) {
                isInProgress= false
                Toast.makeText(LocalContext.current, "Enrollment Failure", Toast.LENGTH_LONG)
                    .show()
                model.resetEnrollmentStatusDefault() //reset status of enrollment to default
            }
            val context = LocalContext.current

            Button(  modifier = Modifier
                .fillMaxWidth(), onClick = {

                isInProgress= true
                model.enrollUser(
                    firstNameText.text,
                    lastNameText.text,
                    mobileNumberText.text,
                    emailAddressText.text,
                    passwordText.text,
                    confirmPasswordText.text,
                    mailCheckedState,
                    tncCheckedState,
                    context
                )},
                enabled =
                isJoinButtonEnabled(
                    firstNameText.text,
                    lastNameText.text,
                    emailAddressText.text,
                    passwordText.text,
                    confirmPasswordText.text
                ),
                colors = buttonColors(VibrantPurple40),
                shape = RoundedCornerShape(100.dp)

            ) {
                Text(
                    text = stringResource(id = R.string.join_text),
                    fontFamily = font_sf_pro,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 3.dp, bottom = 3.dp)
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

@Composable
fun LinkAlreadyAMember(openPopup: (popupStatus: PopupState) -> Unit) {
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
                openPopup(PopupState.POPUP_LOGIN)
            },
        textAlign = TextAlign.Center
    )
}

fun isJoinButtonEnabled(
    firstNameText: String,
    lastNameText: String,
    emailAddressText: String,
    passwordText: String,
    confirmPasswordText: String
): Boolean {
    val firstNameNotEmpty = firstNameText.isNotEmpty()
    val lastNameNotEmpty = lastNameText.isNotEmpty()
    val emailNotEmpty = emailAddressText.isNotEmpty()
    val passwordNotEmpty = passwordText.isNotEmpty()
    val confirmPasswordNotEmpty = confirmPasswordText.isNotEmpty()
    return (firstNameNotEmpty && lastNameNotEmpty && emailNotEmpty && passwordNotEmpty && confirmPasswordNotEmpty
            && passwordText.equals(confirmPasswordText))
}