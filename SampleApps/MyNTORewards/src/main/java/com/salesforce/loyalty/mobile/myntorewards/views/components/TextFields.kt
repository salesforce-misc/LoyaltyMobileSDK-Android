package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextGray
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VeryLightPurple
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple65
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro

const val TEST_TAG_TEXT_FIELD_RIGHT_ICON = "TEST_TAG_TEXT_FIELD_RIGHT_ICON"

@Composable
fun TextFieldCustom(
    textFieldValue: TextFieldValue,
    placeholderText: String,
    keyboardType: KeyboardType = KeyboardType.Email,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    rightIconId: Int? = null,
    rightIconContentDescription: Int? = R.string.forward_arrow_content_description,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    borderColor: Color = VibrantPurple65,
    rightIconClick: (() -> Unit)? = null,
    updateTextField: (updatedValue: TextFieldValue) -> Unit
) {
    OutlinedTextField(
        value = textFieldValue,
        onValueChange = {
            updateTextField(it)
        },
        placeholder = {
            CommonText(
                text = placeholderText,
                color = TextGray,
                modifier = Modifier.background(VeryLightPurple, RoundedCornerShape(16.dp))
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
        enabled = true,
        textStyle = TextStyle(
            fontFamily = font_sf_pro,
            color = Color.Black,
            fontSize = 14.sp
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = VeryLightPurple,
            focusedIndicatorColor = Color.Transparent, //Hide the indicator
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
        trailingIcon = {
            rightIconId?.let {
                IconButton(onClick = { rightIconClick?.invoke() }) {
                    Icon(
                        painter = painterResource(id = rightIconId),
                        contentDescription = rightIconContentDescription?.let { stringResource(id = it) },
                        modifier = Modifier.testTag(TEST_TAG_TEXT_FIELD_RIGHT_ICON)
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        singleLine = singleLine
    )
}