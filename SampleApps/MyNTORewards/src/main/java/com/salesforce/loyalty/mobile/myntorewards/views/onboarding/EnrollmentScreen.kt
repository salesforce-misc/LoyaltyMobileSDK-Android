package com.salesforce.loyalty.mobile.myntorewards.views.onboarding


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.EnrollmentState
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.OnBoardingViewModelAbstractInterface
import com.salesforce.loyalty.mobile.myntorewards.views.CheckBoxMailingList
import com.salesforce.loyalty.mobile.myntorewards.views.CheckBoxTnC
import com.salesforce.loyalty.mobile.myntorewards.views.PopupHeader
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.Screen
import com.salesforce.loyalty.mobile.sources.PrefHelper
import com.salesforce.loyalty.mobile.sources.PrefHelper.get

@Composable
fun JoinWithTermsAndConditions(
    closeSheet: () -> Unit,
    navController: NavController,
    model: OnBoardingViewModelAbstractInterface
) {
    var tncCheckedState by remember { mutableStateOf(false) }
    var mailCheckedState by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
            .fillMaxWidth()
            .fillMaxHeight(0.92f)
            .background(Color.White, RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PopupHeader(headingText = stringResource(id = R.string.join_text)) {
            closeSheet()
        }

        Box() {
            var isInProgress by remember { mutableStateOf(false) }
            var isJoinClicked by remember { mutableStateOf(false) }
            val context = LocalContext.current
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {

                //Observing the enrollment status live data as state. As per the Success or failure state will be changed
                val enrollmentStatusLiveData by model.enrollmentStatusLiveData.observeAsState(
                    EnrollmentState.ENROLLMENT_DEFAULT_EMPTY
                )

                //after enrollment state change to success
                if (enrollmentStatusLiveData == EnrollmentState.ENROLLMENT_SUCCESS) {
                    isInProgress = false
                    model.resetEnrollmentStatusDefault()
                    Toast.makeText(LocalContext.current, "Enrollment Success", Toast.LENGTH_SHORT)
                        .show()
                    closeSheet()
                    // routing to homescreen
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(0)
                    }
                } //after enrollment state change to failure
                else if (enrollmentStatusLiveData == EnrollmentState.ENROLLMENT_FAILURE) {
                    isInProgress = false
                    Toast.makeText(LocalContext.current, "Enrollment Failure", Toast.LENGTH_SHORT)
                        .show()
                    model.resetEnrollmentStatusDefault() //reset status of enrollment to default
                }
                Text(
                    text = stringResource(id = R.string.join_text_already_signed_in),
                    fontFamily = font_sf_pro,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {

                    CheckBoxTnC {
                        tncCheckedState = it
                    }
                    CheckBoxMailingList {
                        mailCheckedState = it

                    }
                }
                Button(
                    modifier = Modifier.fillMaxWidth(), onClick = {
                        isInProgress = true
                        isJoinClicked = true
                    }, enabled = isJoinWithTandCButtonEnabled(
                        tncCheckedState
                    ), colors = buttonColors(VibrantPurple40), shape = RoundedCornerShape(100.dp)

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
            if (isJoinClicked) {
                LaunchedEffect(true) {
                    val email =
                        PrefHelper.customPrefs(context).get<String>(AppConstants.KEY_EMAIL)
                    email?.let { model.joinUser(email, context) }
                    isJoinClicked = false
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
}

fun isJoinWithTandCButtonEnabled(
    tncAcceptance: Boolean
): Boolean {
    return (tncAcceptance)
}