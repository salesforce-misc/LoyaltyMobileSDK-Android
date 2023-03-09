package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro

//Check Box UI being used in Enrollment UI

@Composable
fun CheckBoxMailingList(mailCheckBox: (mailCheckBox: Boolean) -> Unit) {
    val checkedStateMailing = remember { mutableStateOf(true) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(

            checked = checkedStateMailing.value,
            onCheckedChange = {
                checkedStateMailing.value = it
                mailCheckBox(checkedStateMailing.value)
            },
            colors = myCheckBoxColors()
        )
        Text(
            text = stringResource(id = R.string.onboard_form_checkbox_text_mailing_list),
            fontFamily = font_sf_pro,
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}

@Composable
fun CheckBoxTnC(tncCheckBox: (tncCheckBox: Boolean) -> Unit) {
    val checkedStateTnC = remember { mutableStateOf(true) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedStateTnC.value,
            onCheckedChange = {
                checkedStateTnC.value = it
                tncCheckBox(checkedStateTnC.value)
            },
            colors = myCheckBoxColors()
        )
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontFamily = font_sf_pro,
                        fontSize = 14.sp
                    )
                ) {
                    append(stringResource(id = R.string.onboard_form_checkbox_text_i_agree))
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = VibrantPurple40,
                        fontFamily = font_sf_pro,
                        fontSize = 14.sp
                    )
                ) {
                    append(stringResource(id = R.string.onboard_form_checkbox_text_TnC))
                }
            }, modifier = Modifier
                .fillMaxWidth(1f)
                .clickable {
                }
        )
    }
}

@Composable
fun myCheckBoxColors(): CheckboxColors {
    return CheckboxDefaults.colors(
        checkedColor = VibrantPurple40

    )
}