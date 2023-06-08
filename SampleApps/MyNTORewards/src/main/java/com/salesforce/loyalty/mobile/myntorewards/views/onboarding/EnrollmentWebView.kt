package com.salesforce.loyalty.mobile.myntorewards.views.onboarding

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.forceNetwork.ForceAuthManager
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_archivo_bold
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.BottomSheetType
import com.salesforce.loyalty.mobile.myntorewards.utilities.WebUtility
import com.salesforce.loyalty.mobile.myntorewards.views.PopupHeader

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EnrollmentWebView(
    url: String,
    openPopup: (popupStatus: BottomSheetType) -> Unit,
    closeSheet: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.92f)
            .imePadding()
            .background(Color.White, shape = RoundedCornerShape(16.dp)),
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 17.dp, bottom = 17.dp)
        )

        {
            Text(
                text = stringResource(id = R.string.heading_register),
                fontFamily = font_sf_pro,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(start = 10.dp),
                textAlign = TextAlign.Start,
            )

            val interactionSource = remember { MutableInteractionSource() }
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
            Image(
                painter = painterResource(id = R.drawable.close_popup_icon),
                contentDescription = stringResource(id = R.string.cd_close_popup),
                modifier = Modifier
                    .width(16.dp)
                    .height(16.dp)
                    .clickable(interactionSource = interactionSource, indication = null) {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        closeSheet()
                    },
                contentScale = ContentScale.FillWidth,
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Divider(color = Color.LightGray, thickness = 1.dp)
        }

            loadWebUrl(url = url, openPopup = openPopup)
    }
}

@Composable
fun loadWebUrl(url: String, openPopup: (popupStatus: BottomSheetType) -> Unit) {
    val context = LocalContext.current
    var isInProgress by remember { mutableStateOf(false) }
    val redirectUrl =
        ForceAuthManager.forceAuthManager.getConnectedApp().communityUrl + AppConstants.SELF_REGISTER_REDIRECT_URL_PATH
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
            .background(Color.White),
        contentAlignment = Alignment.TopStart
    ) {
        AndroidView(
            factory = {
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            Log.d(
                                "WebViewSignUp",
                                "URL: ${request?.url} isRedirect: ${request?.isRedirect}"
                            )
                            if (request?.url?.toString().equals(redirectUrl)) {
                                Log.d("WebViewSignUp", "redirectUrl matches!!!")
                                openPopup(BottomSheetType.POPUP_CONGRATULATIONS)
                                return false
                            }
                            return super.shouldOverrideUrlLoading(view, request)
                        }

                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            isInProgress = true
                            super.onPageStarted(view, url, favicon)
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            isInProgress = false
                            super.onPageFinished(view, url)
                        }
                    }
                    settings.javaScriptEnabled = true
                    setLayerType(View.LAYER_TYPE_HARDWARE, null)
                    settings.domStorageEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    setInitialScale(0)
                    clearCache(true)
                    clearHistory()
                    clearFormData()
                    loadUrl(url)
                    WebUtility.clearCookies(context)
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier
                .fillMaxSize()
        )
        if (isInProgress) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(0.1f)
                    .align(Alignment.Center)
            )
        }
    }
}