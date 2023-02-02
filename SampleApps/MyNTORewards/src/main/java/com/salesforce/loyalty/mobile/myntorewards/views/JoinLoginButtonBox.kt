package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LightPurple
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro


// Combine UI of Onboarding screen having buttons to open join Popup and Login Popup
@Composable
fun JoinLoginButtonBox() {
    var popupControlLogin by remember { mutableStateOf(false) }
    var popupControlJoin by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp),
        verticalArrangement = Arrangement.Bottom,
    )
    {

        //Join Button
        Text(
            text = stringResource(id = R.string.join_text),
            fontFamily = font_sf_pro,
            color = VibrantPurple40,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .width(327.dp)
                .background(LightPurple, RoundedCornerShape(100.dp))
                .padding(top = 10.dp, bottom = 10.dp)
                .clickable {
                    popupControlJoin = true
                }
        )
    }

    //Already a member login clickable text
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom,
    )
    {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.White,
                        fontFamily = font_sf_pro,
                        fontSize = 16.sp
                    )
                ) {
                    append(stringResource(id = R.string.existing_member_text))
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = font_sf_pro,
                        fontSize = 16.sp
                    )
                ) {
                    append(stringResource(id = R.string.login_text))
                }
            }, modifier = Modifier
                .fillMaxWidth(1f)
                .clickable {
                    popupControlLogin = true
                },
            textAlign = TextAlign.Center

        )

        //Popup Control Join
        if (popupControlJoin) {
            Popup(alignment = Alignment.Center,
                offset = IntOffset(0, 700),
                onDismissRequest = { popupControlJoin = false },
                properties = PopupProperties(focusable = true)
            ) {

                //Launch Login UI or close Join UI logic
                EnrollmentUI {
                    when (it) {
                        "Join" -> {
                            popupControlLogin = false
                            popupControlJoin = true
                        }

                        "Login" -> {
                            popupControlLogin = true
                            popupControlJoin = false
                        }
                        "None" -> {
                            popupControlLogin = false
                            popupControlJoin = false
                        }
                        else -> {
                            popupControlLogin = false
                            popupControlJoin = false
                        }
                    }
                }
            }
        }
        //Popup Control Login
        if (popupControlLogin) {
            Popup(alignment = Alignment.Center,
                offset = IntOffset(0, 700),
                onDismissRequest = { popupControlLogin = false },
                properties = PopupProperties(focusable = true)
            ) {
                //Launch Join UI or close Login UI logic
                LoginUI {
                    when (it) {
                        "Join" -> {
                            popupControlLogin = false
                            popupControlJoin = true
                        }

                        "Login" -> {
                            popupControlLogin = true
                            popupControlJoin = false
                        }
                        "None" -> {
                            popupControlLogin = false
                            popupControlJoin = false
                        }
                        else -> {
                            popupControlLogin = false
                            popupControlJoin = false
                        }
                    }
                }
            }
        }
    }
}