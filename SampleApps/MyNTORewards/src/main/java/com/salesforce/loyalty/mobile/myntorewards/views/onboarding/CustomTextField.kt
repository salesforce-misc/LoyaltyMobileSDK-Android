package com.salesforce.loyalty.mobile.myntorewards.views.onboarding

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextGray
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VeryLightPurple
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import java.util.regex.Pattern


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

@Composable
fun OutlineFieldTextWithError(
    textfieldValue: TextFieldValue,
    textFieldType: CustomTextField.SignUpTextFieldType,
    placeholderText: String,
    hideLoginOpenJoin: (updatedValue: TextFieldValue) -> Unit
) {
    var text by remember {
        mutableStateOf("")
    }
    var isInputInValid by remember {
        mutableStateOf(false)
    }
    var passwordVisibility by remember { mutableStateOf(false) }

    val icon = if (passwordVisibility)
        painterResource(id = R.drawable.ic_visibility_on)
    else
        painterResource(id = R.drawable.ic_visibility_off)

    OutlinedTextField(
        value = textfieldValue,
        onValueChange = {
            text = it.text
            hideLoginOpenJoin(it)
            isInputInValid = CustomTextField.validate(text, textFieldType)
        },
        isError = isInputInValid,
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
        keyboardOptions = KeyboardOptions(
            keyboardType = CustomTextField.getKeyboardType(
                textFieldType
            )
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // validate here
                isInputInValid = CustomTextField.validate(text, textFieldType)
            }
        ),

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
    if (isInputInValid) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(id = CustomTextField.getErrorMessage(textFieldType)),
            color = MaterialTheme.colors.error
        )
    }
}

object CustomTextField {
    enum class SignUpTextFieldType(val defaultLabel: String) {
        FIRSTNAME(""),
        LASTNAME(""),
        EMAIL(""),
        PHONE_NUMBER(""),
        PASSWORD(""),
        CONFIRM_PASSWORD("")
    }

    fun getKeyboardType(textFieldType: SignUpTextFieldType): KeyboardType {
        return when (textFieldType) {
            SignUpTextFieldType.PHONE_NUMBER -> KeyboardType.Phone
            SignUpTextFieldType.EMAIL -> KeyboardType.Email
            SignUpTextFieldType.PASSWORD, SignUpTextFieldType.CONFIRM_PASSWORD -> KeyboardType.Password
            else -> KeyboardType.Text
        }
    }

    fun validate(
        text: String,
        textFieldType: SignUpTextFieldType,
        password: String? = null
    ): Boolean {
        return when (textFieldType) {
            SignUpTextFieldType.FIRSTNAME, SignUpTextFieldType.LASTNAME -> {
                val pattern: Pattern = Pattern.compile("[a-zA-Z0-9]*")
                return !(text.isNotEmpty() && pattern.matcher(text).matches())
            }
            SignUpTextFieldType.EMAIL -> {
                return !Patterns.EMAIL_ADDRESS.matcher(text).matches()
            }
            SignUpTextFieldType.PHONE_NUMBER -> {
                return !(text.isNotEmpty() && text.length == 10)
            }
            SignUpTextFieldType.PASSWORD -> {
                return !(text.isNotEmpty() && text.length >= 6)
            }
            SignUpTextFieldType.CONFIRM_PASSWORD -> {
                !(password?.isNotEmpty() == true && text.isNotEmpty() && text == password)
            }
        }
    }

    fun getErrorMessage(textFieldType: SignUpTextFieldType): Int {
        return when (textFieldType) {
            SignUpTextFieldType.FIRSTNAME -> R.string.error_msg_invalid_first_name
            SignUpTextFieldType.LASTNAME -> R.string.error_msg_invalid_last_name
            SignUpTextFieldType.EMAIL -> R.string.error_msg_invalid_email
            SignUpTextFieldType.PHONE_NUMBER -> R.string.error_msg_invalid_phone_number
            SignUpTextFieldType.PASSWORD -> R.string.error_msg_invalid_password
            SignUpTextFieldType.CONFIRM_PASSWORD -> R.string.error_msg_invalid_confirm_password
        }
    }
}

@Composable
fun PasswordTextField(
    textFieldValue: TextFieldValue,
    placeholderText: String,
    hideLoginOpenJoin: (updatedValue: TextFieldValue) -> Unit
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    val icon = if (passwordVisibility)
        painterResource(id = R.drawable.ic_visibility_on)
    else
        painterResource(id = R.drawable.ic_visibility_off)

    OutlinedTextField(
        value = textFieldValue,
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

        shape = RoundedCornerShape(16.dp),

        trailingIcon = {
            IconButton(onClick = {
                passwordVisibility = !passwordVisibility
            }) {
                Icon(
                    painter = icon,
                    contentDescription = "Visibility Icon"
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = if (passwordVisibility) VisualTransformation.None
        else PasswordVisualTransformation()
    )
}

@Composable
fun PasswordTextFieldWithError(
    textFieldValue: TextFieldValue,
    textFieldType: CustomTextField.SignUpTextFieldType,
    password: String? = null,
    placeholderText: String,
    hideLoginOpenJoin: (updatedValue: TextFieldValue) -> Unit
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    val icon = if (passwordVisibility)
        painterResource(id = R.drawable.ic_visibility_on)
    else
        painterResource(id = R.drawable.ic_visibility_off)

    var text by remember {
        mutableStateOf("")
    }
    var isInputInValid by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = {
            text = it.text
            hideLoginOpenJoin(it)
            isInputInValid = CustomTextField.validate(text, textFieldType, password)
        },
        isError = isInputInValid,
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
        keyboardOptions = KeyboardOptions(
            keyboardType = CustomTextField.getKeyboardType(
                textFieldType
            )
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // validate here
                isInputInValid = CustomTextField.validate(text, textFieldType)
            }
        ),

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

        shape = RoundedCornerShape(16.dp),

        trailingIcon = {
            IconButton(onClick = {
                passwordVisibility = !passwordVisibility
            }) {
                Icon(
                    painter = icon,
                    contentDescription = "Visibility Icon"
                )
            }
        },
        visualTransformation =
        if (passwordVisibility) VisualTransformation.None
        else PasswordVisualTransformation()
    )
    if (isInputInValid) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(id = CustomTextField.getErrorMessage(textFieldType)),
            color = MaterialTheme.colors.error
        )
    }
}
