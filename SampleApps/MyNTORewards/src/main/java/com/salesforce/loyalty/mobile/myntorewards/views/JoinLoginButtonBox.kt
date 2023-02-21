package com.salesforce.loyalty.mobile.myntorewards.views

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
import com.salesforce.loyalty.mobile.myntorewards.utilities.PopupState

// Combine UI of Onboarding screen having buttons to open join Popup and Login Popup
@Composable
fun JoinLoginButtonBox(navController: NavController) {
    var popupControlLogin by remember { mutableStateOf(false) }
    var popupControlJoin by remember { mutableStateOf(false) }
    var popupControlCongratulations by remember { mutableStateOf(false) }


    Spacer(modifier = Modifier.height(24.dp))

    JoinButton { popupControlJoin = true }

    Spacer(modifier = Modifier.height(24.dp))

    AlreadyAMemberButton { popupControlLogin = true }

    //Popup Control Join
    if (popupControlJoin) {
        Popup(
            alignment = Alignment.Center,
            offset = IntOffset(0, 700),
            onDismissRequest = { popupControlJoin = false },
            properties = PopupProperties(focusable = true)
        ) {
            //Launch Login UI or close Join UI logic
            EnrollmentUI(navController) {
                popupControlJoin = (it == PopupState.POPUP_JOIN)
                popupControlLogin = (it == PopupState.POPUP_LOGIN)
                popupControlCongratulations = (it == PopupState.POPUP_CONGRATULATIONS)
            }
        }
    }

    //Popup Control Login
    if (popupControlLogin) {
        Popup(
            alignment = Alignment.Center,
            offset = IntOffset(0, 700),
            onDismissRequest = { popupControlLogin = false },
            properties = PopupProperties(focusable = true)
        ) {
            //Launch Join UI or close Login UI logic
            LoginUI(navController) {
                popupControlJoin = (it == PopupState.POPUP_JOIN)
                popupControlLogin = (it == PopupState.POPUP_LOGIN)
            }
            //Launch Join UI or close Login UI logic
        }
    }

    if (popupControlCongratulations) {
        Popup(
            alignment = Alignment.Center,
            offset = IntOffset(0, 700),
            onDismissRequest = { popupControlLogin = false },
            properties = PopupProperties(focusable = true)
        ) {
            //Launch Join UI or close Login UI logic
            EnrollmentCongratulationsView(navController = navController)
            {
                popupControlJoin = false
                popupControlLogin = false
                popupControlCongratulations = false
            }
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