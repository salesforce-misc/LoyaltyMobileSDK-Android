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
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.POPUP_ROUNDED_CORNER_SIZE
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.formatPromotionDate
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_CLOSE_POPUP_PROMOTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_DETAIL_HEADING
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_EXPIRATION_DATE
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMO_DESCRIPTION
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMO_NAME
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMO_POPUP
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
        onDismissRequest = {closePopup() },
        properties = PopupProperties(focusable = true, dismissOnBackPress = true, dismissOnClickOutside = false),
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


    val memberEligibilityCategory = results.memberEligibilityCategory
    val promotionEnrollmentRqr = results.promotionEnrollmentRqr
    val membershipPromoEnrollmentState by promotionViewModel.promEnrollmentStatusLiveData.observeAsState()
    val description = results.description ?: ""
    val endDate = results.endDate ?: ""

    Box() {
        var isInProgress by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .background(Color.White, RoundedCornerShape(POPUP_ROUNDED_CORNER_SIZE))
                .verticalScroll(
                    rememberScrollState()
                )
                .testTag(TEST_TAG_PROMO_POPUP),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        )

        {


            val context: Context = LocalContext.current
            Column{
            Box()
            {
                PromotionPopupImageBox(results.promotionImageUrl)
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
                            }
                            .testTag(TEST_TAG_CLOSE_POPUP_PROMOTION)
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
                        .fillMaxWidth()
                        .testTag(TEST_TAG_PROMO_NAME)
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
                    .fillMaxWidth()
                    .testTag(TEST_TAG_DETAIL_HEADING)
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
                    .fillMaxWidth()
                    .testTag(TEST_TAG_PROMO_DESCRIPTION)
            )



            Spacer(modifier = Modifier.height(24.dp))

                EndDateText(endDate)

            }

            if (membershipPromoEnrollmentState == PromotionEnrollmentUpdateState.PROMOTION_ENROLLMENTUPDATE_SUCCESS) {
                isInProgress = false
                promotionViewModel.resetPromEnrollmentStatusDefault()
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
                promotionViewModel.resetPromEnrollmentStatusDefault()

            }

            Column {
                results.promotionName?.let {
                    if (memberEligibilityCategory == MEMBER_ELIGIBILITY_CATEGORY_NOT_ENROLLED) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(0.7f), onClick = {
                                isInProgress = true
                                promotionViewModel.enrollInPromotions(context, it)

                            },
                            colors = ButtonDefaults.buttonColors(VibrantPurple40),
                            shape = RoundedCornerShape(100.dp)

                        ) {
                            OptInText()
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
                                OptOutText()
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
            if (AppConstants.CHECKOUT_PROMOTION_NAME == promotionName) {
                navCheckOutFlowController.currentBackStackEntry?.savedStateHandle?.apply {
                    set(AppConstants.PROMOTION_NAME, promotionName)
                }
                navCheckOutFlowController.navigate(CheckOutFlowScreen.OrderDetailScreen.route)
            }
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


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PromotionPopupImageBox(url:String?)
{
    Box() {
        Image(
            painter = painterResource(id = R.drawable.promotion_card_placeholder),
            contentDescription = stringResource(R.string.cd_onboard_screen_bottom_fade),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)),
            contentScale = ContentScale.Crop
        )

        GlideImage(
            model = url,
            contentDescription = "promotion popup image",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)),

            contentScale = ContentScale.Crop
        ) {
            it.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        }
    }

}

@Composable
fun OptOutText()
{
    Text(
        text = stringResource(id = R.string.label_opt_out),
        fontFamily = font_sf_pro,
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = White,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(top = 3.dp, bottom = 3.dp)
    )
}
@Composable
fun OptInText()
{
    Text(
        text = stringResource(id = R.string.label_opt_in),
        fontFamily = font_sf_pro,
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = White,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(top = 3.dp, bottom = 3.dp)
    )
}


@Composable
fun EndDateText(endDate:String)
{
    val context: Context = LocalContext.current
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
                    append(formatPromotionDate(endDate, context))
                }
            },
            fontFamily = font_sf_pro,
            color = Color.Black,
            modifier = Modifier
                .padding(start = 16.dp)
                .testTag(TEST_TAG_EXPIRATION_DATE),
            textAlign = TextAlign.Start,
            fontSize = 12.sp
        )
    }
}
