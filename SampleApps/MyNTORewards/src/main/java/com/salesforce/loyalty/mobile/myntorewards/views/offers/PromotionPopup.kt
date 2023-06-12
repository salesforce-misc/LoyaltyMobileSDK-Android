package com.salesforce.loyalty.mobile.myntorewards.views.offers

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.MEMBER_ELIGIBILITY_CATEGORY_ELIGIBLE
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.MEMBER_ELIGIBILITY_CATEGORY_NOT_ENROLLED
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.formatPromotionDate
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.MyPromotionViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.CheckOutFlowScreen
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results


@Composable
fun PromotionEnrollPopup(
    results: Results,
    closePopup: () -> Unit,
    navCheckOutFlowController: NavController,
    promotionViewModel: MyPromotionViewModelInterface
) {
    Popup(
        alignment = Alignment.Center,
        offset = IntOffset(0, 800),
        onDismissRequest = { },
        properties = PopupProperties(focusable = true),
    ) {


        PromotionEnrollPopupUI(
            results,
            closePopup = {
                closePopup()
            },
            navCheckOutFlowController,
            promotionViewModel
        )
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PromotionEnrollPopupUI(
    results: Results,
    closePopup: () -> Unit,
    navCheckOutFlowController: NavController,
    promotionViewModel: MyPromotionViewModelInterface
) {


    var memberEligibilityCategory = results.memberEligibilityCategory
    var promotionEnrollmentRqr = results.promotionEnrollmentRqr

    val description = results.description ?: ""
    var endDate = results.endDate ?: ""

    Box() {
        var isInProgress by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .background(Color.White, RoundedCornerShape(16.dp))
                .verticalScroll(
                    rememberScrollState()
                ).testTag("promotion_popup"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        )

        {


            val membershipPromo by promotionViewModel.membershipPromotionLiveData.observeAsState() // collecting livedata as state
            val context: Context = LocalContext.current
            Column{
            Box()
            {

                Box() {
                    Image(
                        painter = painterResource(id = R.drawable.promotion_card_placeholder),
                        contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    GlideImage(
                        model = results.promotionImageUrl,
                        contentDescription = "promotion popup image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),

                        contentScale = ContentScale.Crop
                    ) {
                        it.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.End

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.close_popup_icon),
                        contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .background(White, CircleShape)
                            .padding(3.dp)
                            .clickable {
                                closePopup()
                            }.testTag("close_button")

                    )
                }

            }



            Spacer(modifier = Modifier.height(24.dp))

            results.promotionName?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .fillMaxWidth().testTag("promo_name")
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.text_details),
                fontWeight = FontWeight.Bold,
                color = TextDarkGray,
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth().testTag("detail_heading")
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = description,
                fontWeight = FontWeight.Normal,
                color = TextGray,
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth().testTag("promo_description")
            )



            Spacer(modifier = Modifier.height(24.dp))

            if (endDate.isNotEmpty()) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle()
                        ) {
                            append(stringResource(id = R.string.prom_screen_expiration_text))
                        }
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append(formatPromotionDate(endDate))
                        }
                    },
                    fontFamily = font_sf_pro,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp).testTag("expiration_date"),
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp

                )
            }
            }

            Column {
                results.promotionName?.let {
                    if (memberEligibilityCategory == MEMBER_ELIGIBILITY_CATEGORY_NOT_ENROLLED) {


                        val model: MyPromotionViewModel = viewModel()
                        val membershipPromoEnrollmentState by model.promEnrollmentStatusLiveData.observeAsState(
                            PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_DEFAULT_EMPTY
                        )
                        if (membershipPromoEnrollmentState == PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_SUCCESS) {
                            isInProgress = false
                            model.resetPromEnrollmentStatusDefault()
                            Toast.makeText(
                                LocalContext.current,
                                "Promotion Enrol/UnEnrol Success",
                                Toast.LENGTH_LONG
                            ).show()

                            closePopup()

                        } //after enrollment state change to failure
                        else if (membershipPromoEnrollmentState == PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_FAILURE) {
                            isInProgress = false
                            Toast.makeText(
                                LocalContext.current,
                                "Promotion Enrol/UnEnrol Failed",
                                Toast.LENGTH_LONG
                            ).show()
                            model.resetPromEnrollmentStatusDefault()

                        }

                        Button(
                            modifier = Modifier
                                .fillMaxWidth(0.7f), onClick = {
                                isInProgress = true
                                model.enrollInPromotions(context, it)

                            },
                            colors = ButtonDefaults.buttonColors(VibrantPurple40),
                            shape = RoundedCornerShape(100.dp)

                        ) {
                            Text(
                                text = stringResource(id = R.string.join_text),
                                fontFamily = font_sf_pro,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                color = White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(top = 3.dp, bottom = 3.dp)
                            )
                        }


                    } else if (memberEligibilityCategory == MEMBER_ELIGIBILITY_CATEGORY_ELIGIBLE && promotionEnrollmentRqr == true) {

                        Row() {
                            val promoName= results.promotionName?:""
                            ShopButton(150.dp, navCheckOutFlowController, promoName)

                            Spacer(modifier = Modifier.width(10.dp))
                            Button(
                                modifier = Modifier.width(150.dp), onClick = {
                                    isInProgress = true
                                    promotionViewModel.unEnrollInPromotions(context, it)
                                },
                                colors = ButtonDefaults.buttonColors(VibrantPurple40),
                                shape = RoundedCornerShape(100.dp)

                            ) {
                                Text(
                                    text = stringResource(id = R.string.text_leave),
                                    fontFamily = font_sf_pro,
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp,
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(top = 3.dp, bottom = 3.dp)
                                )
                            }

                        }
                    } else {
                        val promoName= results.promotionName?:""
                        ShopButton(300.dp, navCheckOutFlowController,promoName)

                    }

                }
                Spacer(modifier = Modifier.height(40.dp))

            }


        }

        if (isInProgress) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(0.1f)
                    .align(Alignment.Center)
            )
        }

    }


}


@Composable
fun ShopButton(width: Dp, navCheckOutFlowController: NavController, promotionName: String) {

    Button(
        modifier = Modifier
            .width(width), onClick = {
            navCheckOutFlowController.currentBackStackEntry?.savedStateHandle?.apply {
                set(AppConstants.PROMOTION_NAME,promotionName )
            }
            navCheckOutFlowController.navigate(CheckOutFlowScreen.OrderDetailScreen.route)
        },
        colors = ButtonDefaults.buttonColors(VibrantPurple40),
        shape = RoundedCornerShape(100.dp)

    ) {
        Text(
            text = stringResource(id = R.string.text_shop),
            fontFamily = font_sf_pro,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 3.dp, bottom = 3.dp)
        )
    }
}