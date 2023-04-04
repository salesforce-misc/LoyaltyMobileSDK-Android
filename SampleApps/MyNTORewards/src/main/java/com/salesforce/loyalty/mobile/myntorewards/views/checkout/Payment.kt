package com.salesforce.loyalty.mobile.myntorewards.views.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.HomeScreenState

@Composable
fun PaymentsUI(openHomeScreen: (homeScreenState: HomeScreenState) -> Unit) {

    Column(modifier = Modifier.padding(start = 16.dp, end=25.dp)) {

        Spacer(modifier = Modifier.height(23.dp))
        VoucherRow()
        Spacer(modifier = Modifier.height(24.dp))
        PointsRow()
        AmountPaybleRow()
        Spacer(modifier = Modifier.height(24.dp))

        CardNumberRow()
        Spacer(modifier = Modifier.height(24.dp))
        ConfirmOrderButton{
            openHomeScreen(it)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun VoucherRow(){
    Column(modifier = Modifier.background(Color.White, RoundedCornerShape(20.dp))) {

        Text(
            text = "Vouchers",
            fontFamily = font_archivo,
            fontWeight = FontWeight.Medium,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 13.sp,
            modifier = Modifier
                .padding(start = 11.dp, top = 17.dp, end = 45.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        VoucherMenuBox()
        Spacer(modifier = Modifier.height(16.dp))

        
    }

}

@Composable
fun AmountPaybleRow(){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(start = 11.dp, end = 11.dp, bottom = 16.dp, top = 22.dp)) {

        Text(
            text = "Amount Payable:",
            fontFamily = font_archivo,
            fontWeight = FontWeight.Medium,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 13.sp,
            modifier = Modifier
        )
        Text(
            text = "$154",
            fontFamily = font_archivo,
            fontWeight = FontWeight.ExtraBold,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            modifier = Modifier
        )

    }
}

@Composable
fun PointsRow(){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 11.dp, end = 11.dp, bottom = 16.dp, top = 22.dp)) {

        Text(
            text = "Use my Points",
            fontFamily = font_archivo,
            fontWeight = FontWeight.SemiBold,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 13.sp,
            modifier = Modifier
        )
        Text(
            text = "Points Available: 430",
            fontFamily = font_archivo,
            fontWeight = FontWeight.SemiBold,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 13.sp,
            modifier = Modifier
        )

    }
}


@Composable
fun CardNumberRow(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(start = 11.dp, end = 11.dp, bottom = 16.dp, top = 22.dp))
    {

        Text(
            text = "Card Number",
            fontFamily = font_archivo,
            fontWeight = FontWeight.Medium,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 11.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 11.dp, end = 11.dp, bottom = 10.dp, top = 16.dp)) {

            CardInputField("0092")
            CardInputField("0230")
            CardInputField("0935")
            CardInputField("2800")

        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 11.dp, end = 11.dp, top = 16.dp)) {

            Text(
                text = "Expiry Date",
                fontFamily = font_archivo,
                fontWeight = FontWeight.Medium,
                color = LighterBlack,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                modifier = Modifier
            )
            Text(
                text = "Security Code",
                fontFamily = font_archivo,
                fontWeight = FontWeight.Medium,
                color = LighterBlack,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                modifier = Modifier
            )

        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 11.dp, bottom = 10.dp, top = 10.dp)) {



            MonthDropDownBox()
            YearDropDownBox()

            CVVInputField("298")

        }


    }
}

@Composable
fun CardInputField(text:String)
{
    TextField(
        value = text,
        onValueChange = {

        },
        modifier = Modifier
            .width(70.dp).width(44.dp),

        enabled = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),

        textStyle = TextStyle(
            fontFamily = font_archivo,
            color = CardFieldText,
            fontSize = 12.sp,
        ),

        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = DropDownMenuBG,
            focusedIndicatorColor = Color.Transparent, //hide the indicator
            unfocusedIndicatorColor = Color.Transparent
        ),

        shape = RoundedCornerShape(16.dp),
    )
}

@Composable
fun CVVInputField(text:String)
{
    TextField(
        value = text,
        onValueChange = {

        },
        modifier = Modifier
            .width(111.dp).width(44.dp),

        enabled = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),

        textStyle = TextStyle(
            fontFamily = font_archivo,
            color = CardFieldText,
            fontSize = 12.sp,
        ),

        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = DropDownMenuBG,
            focusedIndicatorColor = Color.Transparent, //hide the indicator
            unfocusedIndicatorColor = Color.Transparent
        ),

        shape = RoundedCornerShape(16.dp),
    )
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VoucherMenuBox() {
    val options = listOf("Trendy Wear at $25", "Trendy Wear 2 at $25", "Trendy Wear 3 at $25")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier
            .padding(start = 11.dp, end = 11.dp)
            .background(DropDownMenuBG, RoundedCornerShape(15.dp))
    ) {

        Text(
            text = selectedOptionText,
            fontFamily = font_archivo,
            fontWeight = FontWeight.Medium,
            color = LighterBlack,
            textAlign = TextAlign.Start,
            fontSize = 13.sp,
            modifier = Modifier
                .padding(start = 20.dp, top = 13.5.dp, bottom = 13.5.dp, end = 47.dp)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                ){
                    Text(text = selectionOption)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MonthDropDownBox() {
    val options = listOf("01","02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier
            .padding(start = 11.dp, end = 11.dp)
            .background(DropDownMenuBG, RoundedCornerShape(15.dp))
    ) {

        Text(
            text = selectedOptionText,
            fontFamily = font_archivo,
            fontWeight = FontWeight.Medium,
            color = CardFieldText,
            textAlign = TextAlign.Start,
            fontSize = 13.sp,
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp, bottom = 20.dp, end = 47.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                ){
                    Text(text = selectionOption)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun YearDropDownBox() {
    val options = listOf("24","25", "26", "27", "28", "29", "30", "31", "32", "33")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier
            .padding(start = 11.dp, end = 11.dp)
            .background(DropDownMenuBG, RoundedCornerShape(15.dp))
    ) {

        Text(
            text = selectedOptionText,
            fontFamily = font_archivo,
            fontWeight = FontWeight.Medium,
            color = CardFieldText,
            textAlign = TextAlign.Start,
            fontSize = 13.sp,
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp, bottom = 20.dp, end = 47.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                ){
                    Text(text = selectionOption)
                }
            }
        }
    }
}

@Composable
fun ConfirmOrderButton(openHomeScreen: (homeScreenState: HomeScreenState) -> Unit)
{

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        modifier = Modifier
            .fillMaxWidth(), onClick = {
            openHomeScreen(HomeScreenState.ORDER_CONFIRMATION_VIEW)
            //  model.enrollInPromotions(context, "PromoName")
        },
        colors = ButtonDefaults.buttonColors(VibrantPurple40),
        shape = RoundedCornerShape(100.dp)

    ) {
        Text(
            text = "Confirm Order",
            fontFamily = font_archivo,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(top = 13.dp, bottom = 13.dp)
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

}