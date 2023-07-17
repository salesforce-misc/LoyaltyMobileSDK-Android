package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptsList(navController: NavHostController) {

    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
            .background(TextPurpleLightBG)
    )  {

        Spacer(modifier = Modifier.height(50.dp))
        Image(
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = "receipt_back_button",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                SearchBar(onSearch = {}, Modifier.weight(0.8f))

                Button(
                    modifier = Modifier
                        .weight(0.25f), onClick = {

                    },
                    colors = ButtonDefaults.buttonColors(VibrantPurple40),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.label_new_receipt),
                        fontFamily = font_sf_pro,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 3.dp, bottom = 3.dp)
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(20) {
                    ReceiptItem()
                }
            }
        }
    }
}

@Composable
fun ReceiptItem(){
    var openReceiptDetail by remember { mutableStateOf(false) }
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable {
                openReceiptDetail = true
            }
    ) {
        Column(modifier = Modifier.weight(0.7f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Receipt Number",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
            )
            Text(
                text = "13-07-2023",
                fontFamily = font_sf_pro,
                color = Color.Black,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
            )

        }
        Column(modifier = Modifier.weight(0.3f), verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.End) {
            Text(
                text = "INR 32392",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.End,
                fontSize = 13.sp,
                modifier = Modifier
            )
            Text(
                text = "434 Points",
                color = Color.Black,
                textAlign = TextAlign.End,
                fontSize = 13.sp,
                modifier = Modifier
            )
        }
    }
    if (openReceiptDetail) {
        ReceiptDetail(closePopup = {
            openReceiptDetail = false
        })
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(onSearch: (String) -> Unit, modifier: Modifier) {
    var text by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(stringResource(id = R.string.receipt_search_text), color = TextDarkGray) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = SearchIconColor) },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch(text)
            // Hide the keyboard after submitting the search
            keyboardController?.hide()
            //or hide keyboard
            focusManager.clearFocus()

        }),
        shape = RoundedCornerShape(size = 12.dp),
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = SearchFieldColor,
            unfocusedContainerColor = SearchFieldColor
        )
    )
}

@Preview
@Composable
fun PreviewReceiptScreen(){
    val navController = rememberNavController()
    ReceiptsList(navController)
}