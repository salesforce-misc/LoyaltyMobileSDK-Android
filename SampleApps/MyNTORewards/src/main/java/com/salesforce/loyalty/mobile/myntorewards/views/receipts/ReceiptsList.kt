package com.salesforce.loyalty.mobile.myntorewards.views.receipts

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.salesforce.loyalty.mobile.MyNTORewards.BuildConfig
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.ScanningViewModel
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptsList(navController: NavHostController) {

    var searchText by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    val scanViewModel: ScanningViewModel = viewModel()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    var imageUri by remember {
        mutableStateOf<Uri?>(Uri.EMPTY)
    }
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
            .background(TextPurpleLightBG)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },

        horizontalAlignment = Alignment.CenterHorizontally
    )  {

        Spacer(modifier = Modifier.height(50.dp))

        if(imageUri?.path?.isNotEmpty() == true)
        {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver,imageUri)

            } else {
                imageUri?.let {
                    val source = ImageDecoder
                        .createSource(context.contentResolver,it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }

            }

            bitmap.value?.let {  btm ->
                Image(bitmap = btm.asImageBitmap(),
                    contentDescription =null,
                    modifier = Modifier.size(500.dp))
            }
            Spacer(modifier = Modifier.height(30.dp))

            ProcessButton(navController)
        }
        else if (capturedImageUri.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .padding(16.dp, 8.dp),
                painter = rememberAsyncImagePainter(capturedImageUri),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(30.dp))

            ProcessButton(navController)
        }
        else
        {
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

                var expanded by remember { mutableStateOf(false) }
                Button(
                    modifier = Modifier
                        .weight(0.25f), onClick = {
                        expanded= true
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

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = {  Text("Camera") },
                            trailingIcon ={
                                Icon(imageVector = Icons.Default.Camera, contentDescription = "New Item")
                            },
                            onClick = {
                                val permissionCheckResult =
                                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                    cameraLauncher.launch(uri)
                                } else {
                                    // Request a permission
                                    permissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Gallery") },
                            trailingIcon ={
                                Icon(imageVector = Icons.Default.Image, contentDescription = "New Item")
                            },

                            onClick = {     launcher.launch("image/*") }
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                receiptLists?.let{
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
                        ReceiptItem(filteredList[it])
                    }

                }
            }
        }
        }
    }

}


/*@Composable
fun AppContent() {




}*/
fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}


@Composable
fun ReceiptItem(receipt: ScanningViewModel.Receipt){
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
        Column(modifier = Modifier.weight(0.3f), verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.End) {
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
        placeholder = { Text(stringResource(id = R.string.receipt_search_text), color = TextDarkGray) },
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


@Composable
fun ProcessButton(navController: NavHostController) {

    Button(
        modifier = Modifier
            .width(288.dp), onClick = {
            /*navController.navigate(MoreScreens.ScannedReceiptScreen.route){
                popUpTo(MoreScreens.ReceiptListScreen.route) {
                    inclusive = false
                }
            }*/
            navController.navigate(MoreScreens.ScanningProgressScreen.route){
                popUpTo(MoreScreens.ReceiptListScreen.route) {
                    inclusive = false
                }
            }
        },
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(VibrantPurple40),
        shape = RoundedCornerShape(100.dp)

    ) {
        androidx.compose.material3.Text(
            text = "Process",
            fontFamily = font_sf_pro,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 3.dp, bottom = 3.dp)
        )
    }
}
@Composable
fun RequestContentPermission() {

}

@Preview
@Composable
fun PreviewReceiptScreen(){
    val navController = rememberNavController()
    ReceiptsList(navController)
}