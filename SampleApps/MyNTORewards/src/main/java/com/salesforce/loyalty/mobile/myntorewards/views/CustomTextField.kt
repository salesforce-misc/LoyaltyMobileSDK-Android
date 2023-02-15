package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextGray
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VeryLightPurple
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro

//Custom textfield to render the form field values in login and join form. Login and Join UI is using this custom field
@Composable
fun OutlineFieldText(
    textfieldValue: TextFieldValue,
    placeholderText: String,
    hideLoginOpenJoin: (updatedValue: TextFieldValue) -> Unit
) {
    OutlinedTextField(
        value = textfieldValue,
        onValueChange = {
            hideLoginOpenJoin(it)
        },

        placeholder = {
            Text(
                text = placeholderText,
                fontFamily = font_sf_pro,
                color = TextGray,
                fontSize = 14.sp,
                modifier = Modifier.background(VeryLightPurple, RoundedCornerShape(16.dp))
            )
        },

        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, VibrantPurple40, RoundedCornerShape(16.dp)),

        enabled = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),

        textStyle = TextStyle(
            fontFamily = font_sf_pro,
            color = Color.Black,
            fontSize = 14.sp
        ),

        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = VeryLightPurple,
            focusedIndicatorColor = Color.Transparent, //hide the indicator
            unfocusedIndicatorColor = Color.Transparent
        ),

        shape = RoundedCornerShape(16.dp)
    )
}