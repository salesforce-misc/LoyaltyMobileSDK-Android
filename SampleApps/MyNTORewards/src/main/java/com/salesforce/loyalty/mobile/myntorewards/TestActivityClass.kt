package com.salesforce.loyalty.mobile.myntorewards

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.view.WindowCompat
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*

class TestActivityClass : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )


        setContent {
            setBoxView2()
            }

        }
    }

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun setBoxView2() {
    Box(modifier = Modifier.fillMaxSize())
    {

        val pagerState = rememberPagerState()
        var imageID = R.drawable.image_8

        HorizontalPager(count = 3, state = pagerState) { page ->
            when (page) {
                0 -> {
                    imageID = R.drawable.image_11
                }

                1 -> {
                    imageID = R.drawable.image_12
                }
                2 -> {
                    imageID = R.drawable.image_8
                }
            }
            Image(
                painter = painterResource(id = imageID),
                contentDescription = "Onboard Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth,
            )

        }
            Image(
                painter = painterResource(id = R.drawable.blur),
                contentDescription = "Onboard Image",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .align(alignment = Alignment.BottomEnd),
                verticalArrangement = Arrangement.Bottom,

                )
            {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .padding(start = 24.dp),
                    verticalArrangement = Arrangement.Bottom,
                )
                {


                    Image(
                        painter = painterResource(id = R.drawable.group_37156),
                        contentDescription = "Onboard Image"
                    )

                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        activeColor = Color.White,
                        inactiveColor = Color.Gray,
                        modifier = Modifier
                            .padding(16.dp)
                    )



                    var stringText = remember { mutableStateOf("Get personalized \noffers") }
                    LaunchedEffect(pagerState) {
                        // Collect from the pager state a snapshotFlow reading the currentPage
                        snapshotFlow { pagerState.currentPage }.collect { page ->
                            when (page) {
                                0 -> {
                                    stringText.value = "Get personalized \noffers"
                                }

                                1 -> {
                                    stringText.value = "Redeem your points for exciting vouchers"
                                }
                                2 -> {
                                    stringText.value = "Accumulate points and unlock new \n rewards"
                                }
                            }
                        }
                    }

                    Text(
                        text =stringText.value,
                        fontFamily = loyoaltyApp,
                        color = Color.White,
                        fontSize = 34.sp
                    )


                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                )
                {
                    JoinLoginButtonBox()
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f),
                    verticalArrangement = Arrangement.Bottom,
                )
                {}
            }
    }
    }


@Composable
fun JoinLoginButtonBox()
{

    var popupControlLogin by remember { mutableStateOf(false) }
    var popupControlJoin by remember { mutableStateOf(false) }

    var popupType="Login"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp),
        verticalArrangement = Arrangement.Bottom,
    )
    {

        Text(
            text ="Join",
            fontFamily = loyoaltyApp,
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
                        fontFamily = loyoaltyApp,
                        fontSize = 16.sp
                    )
                ) {
                    append("Already a Member?")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = loyoaltyApp,
                        fontSize = 16.sp
                    )
                ) {
                    append(" Log In")
                }
            }, modifier = Modifier
                .fillMaxWidth(1f)
                .clickable {
                    popupControlLogin = true
                },
            textAlign = TextAlign.Center

        )

        if (popupControlJoin) {
            Popup(alignment = Alignment.Center,
                offset = IntOffset(0, 700),
                onDismissRequest = { popupControlJoin = false }
                ,                     properties = PopupProperties(focusable = true)
            ) {
                EnrollmentUI{
                    popupControlJoin= false
                    popupControlLogin= true
                }
            }
        }
        if (popupControlLogin) {
            Popup(alignment = Alignment.Center,
                offset = IntOffset(0, 700),
                onDismissRequest = { popupControlLogin = false }
                ,                     properties = PopupProperties(focusable = true)
            ) {
                LoginUI{
                    popupControlJoin= true
                    popupControlLogin= false
                }
            }
        }
    }
}

@Composable
fun LoginUI(hideLoginOpenJoin: () -> Unit) {
    val cornerSize = 16.dp
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(Color.White, RoundedCornerShape(cornerSize))
    )
    {

        Column {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                )

                {
                    Text(
                        text = "Log In",
                        fontFamily = loyoaltyApp,
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(start = 10.dp),
                        textAlign = TextAlign.Start,
                    )

                    Image(
                        painter = painterResource(id = R.drawable.clearbasemobile),
                        contentDescription = "Onboard Image",
                        modifier = Modifier.width(16.dp).height(16.dp).clickable {
                            hideLoginOpenJoin()
                        },
                        contentScale = ContentScale.FillWidth,
                    )
                }


                Box(modifier = Modifier.fillMaxWidth()) {
                    Divider(color = Color.LightGray, thickness = 1.dp)

                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                var emailAddressPhoneNumberText by remember { mutableStateOf(TextFieldValue("")) }
                var passwordtext by remember { mutableStateOf(TextFieldValue("")) }

                outlineFieldText(emailAddressPhoneNumberText, "Email address or Phone number"){
                    emailAddressPhoneNumberText= it
                }
                outlineFieldText(passwordtext, "Password"){
                    passwordtext= it
                }

                Text(
                    text = "Forget Your Password?",
                    fontFamily = loyoaltyApp,
                    color = VibrantPurple40,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Log In",
                    fontFamily = loyoaltyApp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .width(327.dp)
                        .background(VibrantPurple40, RoundedCornerShape(100.dp))
                        .padding(top = 10.dp, bottom = 10.dp)
                )

                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontFamily = loyoaltyApp,
                                fontSize = 16.sp
                            )
                        ) {
                            append("Not a Member?")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = VibrantPurple40,
                                fontFamily = loyoaltyApp,
                                fontSize = 16.sp
                            )
                        ) {
                            append("Join Now")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .clickable {
                            hideLoginOpenJoin()
                        },
                    textAlign = TextAlign.Center
                )

            }
        }
    }
}





@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EnrollmentUI(hideJoinOpenLogin: () -> Unit) {
    val cornerSize = 16.dp
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.92f)
            .background(Color.White, RoundedCornerShape(cornerSize))
    )
    {

        Column {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                )

                {
                    Text(
                        text = "Join",
                        fontFamily = loyoaltyApp,
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(start = 10.dp),
                        textAlign = TextAlign.Start,
                    )

                    Image(
                        painter = painterResource(id = R.drawable.clearbasemobile),
                        contentDescription = "Onboard Image",
                        modifier = Modifier.width(16.dp).height(16.dp).clickable {
                            hideJoinOpenLogin()
                        },
                        contentScale = ContentScale.FillWidth,
                    )
                }


                Box(modifier = Modifier.fillMaxWidth()) {
                    Divider(color = Color.LightGray, thickness = 1.dp)
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                var firstNameText by remember { mutableStateOf(TextFieldValue("")) }
                var lastNameText by remember { mutableStateOf(TextFieldValue("")) }
                var mobileNumberText by remember { mutableStateOf(TextFieldValue("")) }
                var emailAddressText by remember { mutableStateOf(TextFieldValue("")) }
                var passwordText by remember { mutableStateOf(TextFieldValue("")) }
                var confirmPasswordText by remember { mutableStateOf(TextFieldValue("")) }

                outlineFieldText(firstNameText, "First Name"){
                    firstNameText= it
                }
                outlineFieldText(lastNameText, "Last name"){
                    lastNameText= it
                }
                outlineFieldText(lastNameText, "Mobile number"){
                    mobileNumberText= it
                }
                outlineFieldText(lastNameText, "Email Address"){
                    emailAddressText= it
                }
                outlineFieldText(lastNameText, "Password"){
                    passwordText= it
                }
                outlineFieldText(lastNameText, "Confirm Password"){
                    confirmPasswordText= it
                }
                SimpleCheckboxComponent()

                Text(
                    text = "Join",
                    fontFamily = loyoaltyApp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .width(327.dp)
                        .background(VibrantPurple40, RoundedCornerShape(100.dp))
                        .padding(top = 10.dp, bottom = 10.dp)
                )

                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontFamily = loyoaltyApp,
                                fontSize = 16.sp
                            )
                        ) {
                            append("Already a Member?")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = VibrantPurple40,
                                fontFamily = loyoaltyApp,
                                fontSize = 16.sp
                            )
                        ) {
                            append("Log In")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .clickable {
                            hideJoinOpenLogin()
                        },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun SimpleCheckboxComponent() {
    val checkedStateTnC = remember { mutableStateOf(true) }
    val checkedStateMailing = remember { mutableStateOf(true) }
    Column(

        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(

                checked = checkedStateTnC.value,
                onCheckedChange = { checkedStateTnC.value = it },
                colors= myCheckBoxColors()
            )
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontFamily = loyoaltyApp,
                            fontSize = 14.sp
                        )
                    ) {
                        append("I agree to the ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = VibrantPurple40,
                            fontFamily = loyoaltyApp,
                            fontSize = 14.sp
                        )
                    ) {
                        append(" terms and conditions")
                    }
                }, modifier = Modifier
                    .fillMaxWidth(1f)
                    .clickable {
                    }
            )
        }

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(

                checked = checkedStateMailing.value,
                onCheckedChange = { checkedStateMailing.value = it },
                colors= myCheckBoxColors()
            )
            Text(text = "Add me to the loyalty program’s mailing list",   fontFamily = loyoaltyApp,
                color = Color.Black,
                fontSize = 14.sp)
        }
    }


}

@Composable
fun myCheckBoxColors(): CheckboxColors {
    return CheckboxDefaults.colors(
        checkedColor = VibrantPurple40

    )
}
@Composable
fun outlineFieldText(textfieldValue: TextFieldValue, placeholderText:String, hideLoginOpenJoin: (updatedValue:TextFieldValue) -> Unit){
    OutlinedTextField(
        value = textfieldValue,
        onValueChange = {
            hideLoginOpenJoin(it)
        },
        placeholder = { Text(text = "First name",    fontFamily = loyoaltyApp,
            color = TextGray,
            fontSize = 14.sp, modifier = Modifier.background(VeryLightPurple, RoundedCornerShape(16.dp)))},

        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, VibrantPurple40, RoundedCornerShape(16.dp)),


        enabled = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),

        textStyle=  TextStyle(fontFamily = loyoaltyApp,
            color = Color.Black,
            fontSize = 14.sp),

        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = VeryLightPurple,
            focusedIndicatorColor =  Color.Transparent, //hide the indicator
            unfocusedIndicatorColor = Color.Transparent),
        shape = RoundedCornerShape(16.dp),

        )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    setBoxView2()
}
