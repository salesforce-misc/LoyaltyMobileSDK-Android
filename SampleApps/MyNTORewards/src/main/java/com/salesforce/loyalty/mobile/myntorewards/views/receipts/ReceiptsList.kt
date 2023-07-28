package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ScanningViewModel
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens

@Composable
fun ReceiptsList(navController: NavHostController) {

    var searchText by rememberSaveable { mutableStateOf("") }
    val scanViewModel: ScanningViewModel = viewModel()
    val focusManager = LocalFocusManager.current
    var blurBG by remember { mutableStateOf(0.dp) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
            .background(TextPurpleLightBG)
            .blur(blurBG)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },

        ) {

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
            var receiptLists = scanViewModel.getReceiptLists()
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                SearchBar(onSearch = {
                    searchText = it
                }, Modifier.weight(0.8f), focusManager)
                Button(
                    modifier = Modifier
                        .weight(0.25f), onClick = {
                        navController.navigate(MoreScreens.CaptureImageScreen.route)
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
                receiptLists?.let {
                    val filteredList = receiptLists.filter {
                        if (searchText.isNotEmpty()) {
                            it.receiptNumber.contains(
                                searchText,
                                ignoreCase = true
                            )
                        } else {
                            true
                        }
                    }
                    items(filteredList.size) {
                        ReceiptItem(filteredList[it]) {
                            blurBG = it
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun ReceiptItem(receipt: ScanningViewModel.Receipt, blurBG: (Dp) -> Unit) {
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
                blurBG(AppConstants.BLUR_BG)
                openReceiptDetail = true
            }
    ) {
        Column(modifier = Modifier.weight(0.7f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = receipt.receiptNumber,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
            )
            Text(
                text = receipt.date,
                fontFamily = font_sf_pro,
                color = Color.Black,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
            )

        }
        Column(
            modifier = Modifier.weight(0.3f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = receipt.price,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.End,
                fontSize = 13.sp,
                modifier = Modifier
            )
            Text(
                text = receipt.points + " Points",
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
            blurBG(AppConstants.NO_BLUR_BG)
        })
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(onSearch: (String) -> Unit, modifier: Modifier, focusManager: FocusManager) {
    var text by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = text,
        onValueChange = {
            text = it
            onSearch(it)
        },
        placeholder = {
            Text(
                stringResource(id = R.string.receipt_search_text),
                color = TextDarkGray
            )
        },
        leadingIcon = {
            Icon(
                Icons.Filled.Search,
                contentDescription = null,
                tint = SearchIconColor
            )
        },
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
fun PreviewReceiptScreen() {
    val navController = rememberNavController()
    ReceiptsList(navController)
}