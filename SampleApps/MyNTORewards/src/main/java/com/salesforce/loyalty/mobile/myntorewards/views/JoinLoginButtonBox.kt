package com.salesforce.loyalty.mobile.myntorewards.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LightPurple
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.MyProfileScreenState
import com.salesforce.loyalty.mobile.myntorewards.utilities.PopupState

// Combine UI of Onboarding screen having buttons to open join Popup and Login Popup
@Composable
fun JoinLoginButtonBox(navController: NavController) {

    var currentPopupState by remember { mutableStateOf(PopupState.POPUP_NONE) }

    Spacer(modifier = Modifier.height(24.dp))

    JoinButton { currentPopupState = PopupState.POPUP_JOIN }

    Spacer(modifier = Modifier.height(24.dp))

    AlreadyAMemberButton { currentPopupState = PopupState.POPUP_LOGIN }

    when (currentPopupState) {
        PopupState.POPUP_JOIN -> EnrollmentPopup{
            currentPopupState= it
        }
        PopupState.POPUP_LOGIN -> LoginPopup(navController) {
            currentPopupState= it
        }
        PopupState.POPUP_CONGRATULATIONS -> EnrollmentCongratulationsPopup(navController){
            currentPopupState= it
        }
        PopupState.POPUP_NONE -> { Log.d("JoinLoginButtonBox", "No-Popup")
        }
    }
}
@Composable
fun JoinButton(openJoinPopup: () -> Unit) {

    Button(
        modifier = Modifier
            .fillMaxWidth(), onClick = {
            openJoinPopup()
        },
        colors = ButtonDefaults.buttonColors(LightPurple),
        shape = RoundedCornerShape(100.dp)

    ) {
        Text(
            text = stringResource(id = R.string.join_text),
            fontFamily = font_sf_pro,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 3.dp, bottom = 3.dp)
        )
    }
}