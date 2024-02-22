package com.salesforce.loyalty.mobile.myntorewards.views.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.RequestBuilderTransform
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.BLUR_BG
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.NO_BLUR_BG
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.formatPromotionDate
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_PROMO_CARD
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.MyPromotionViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferFriendScreen
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PromotionCard(
    page: Int,
    membershipPromo: List<Results>?,
    navCheckOutFlowController: NavController,
    promotionViewModel: MyPromotionViewModelInterface,
    blurBG: (Dp) -> Unit
) {
    var promoDescription = membershipPromo?.get(page)?.description ?: ""
    var promoName = membershipPromo?.get(page)?.promotionName ?: ""
    var endDate = membershipPromo?.get(page)?.endDate ?: ""
    var promotionEnrollmentRqr = membershipPromo?.get(page)?.promotionEnrollmentRqr ?: false
    var memberEligibilityCategory = membershipPromo?.get(page)?.memberEligibilityCategory ?: ""
    val context: Context = LocalContext.current
    val promotionClickState by promotionViewModel.promotionClickState.observeAsState()
    var currentReferralPromotionPopupState by remember { mutableStateOf(false) }

    var currentPromotionDetailPopupState by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .background(Color.White)
            .testTag(TEST_TAG_PROMO_CARD)
            .clickable {
                if (membershipPromo?.get(page)?.promotionId != null) {
                    /*promotionViewModel.checkIfGivenPromotionIsReferralAndEnrolled(
                        context,
                        membershipPromo.get(page)?.promotionId!!
                    )
                } else {*/
                    currentPromotionDetailPopupState = true
                    blurBG(BLUR_BG)
                }
            }
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(16.dp)
                .height(320.dp)
        ) {

            Box(modifier = Modifier.background(Color.White, RoundedCornerShape(8.dp))) {
                Image(
                    painter = painterResource(R.drawable.promotion_card_placeholder),
                    contentDescription = stringResource(com.salesforce.loyalty.mobile.MyNTORewards.R.string.cd_onboard_screen_bottom_fade),
                    modifier = Modifier
                        .size(289.dp, 154.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                )

                membershipPromo?.get(page)?.promotionImageUrl?.let {
                    GlideImage(
                        model = it,
                        contentDescription = stringResource(id = R.string.content_description_promotion_image),
                        modifier = Modifier
                            .size(289.dp, 154.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop,
                    ){
                        it.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    }
                } ?: Image(
                    painter = painterResource(R.drawable.bg_refer_friend_banner),
                    contentDescription = stringResource(com.salesforce.loyalty.mobile.MyNTORewards.R.string.cd_onboard_screen_bottom_fade),
                    modifier = Modifier
                        .size(289.dp, 154.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = promoName,
                fontWeight = FontWeight.Bold,
                fontFamily = font_sf_pro,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.Start)
                    .width(289.dp),
                textAlign = TextAlign.Start,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = promoDescription,
                fontWeight = FontWeight.Normal,
                fontFamily = font_sf_pro,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.Start)
                    .width(289.dp),
                textAlign = TextAlign.Start,
                fontSize = 12.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (endDate.isNotEmpty()) {

                androidx.compose.material.Text(
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
                        .align(Alignment.Start),
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(44.dp))

            //commenting as ios platform also removed this
            /*   Row(modifier = Modifier.height(45.dp))
               {
                   if(memberEligibilityCategory==MEMBER_ELIGIBILITY_CATEGORY_NOT_ENROLLED) {
                       JoinButtonProm {

                       }
                   }
               }*/


        }
    }

    if (currentPromotionDetailPopupState) {
        membershipPromo?.get(page)?.let {
            val referralViewModel: MyReferralsViewModel = hiltViewModel()
            PromotionDifferentiator(promotionViewModel, navCheckOutFlowController, it, it.promotionId.orEmpty(), referralViewModel) {
                currentPromotionDetailPopupState = false
                blurBG(NO_BLUR_BG)
                referralViewModel.clearState()
            }


            /*PromotionEnrollPopup(
                it,
                closePopup = {
                    currentPromotionDetailPopupState = false
                    blurBG(NO_BLUR_BG)
                    promotionViewModel.hideSheet()
                },
                navCheckOutFlowController,
                promotionViewModel
            )*/
        }
    }

    /*if (currentReferralPromotionPopupState) {
        ShowReferralSheet(isEnrolled = it.isUserEnrolledToReferralPromotion) {
            currentPromotionDetailPopupState = false
            blurBG(NO_BLUR_BG)
        }
    }*/





    /*promotionClickState?.let {
        when(it) {
            is PromotionClickState.PromotionReferralApiStatusInProgress -> ProgressDialogComposable { }
            is PromotionClickState.PromotionStateReferralHideSheet ->  {
                blurBG(NO_BLUR_BG)
            }
            is PromotionClickState.PromotionStateReferral -> {
                ShowReferralSheet(promoCode = it.promotionCode, isEnrolled = it.isUserEnrolledToReferralPromotion) {
                    promotionViewModel.hideSheet()
//                    currentPromotionDetailPopupState = false
//                    blurBG(NO_BLUR_BG)
                }
            }
            is PromotionClickState.PromotionReferralApiStatusFailure -> ErrorPopup(
                it.error ?: stringResource(id = R.string.receipt_scanning_error_desc),
                tryAgainClicked = {  },
                textButtonClicked = {  }
            )

            is PromotionClickState.PromotionStateNonReferral -> {
                currentPromotionDetailPopupState = true
                blurBG(BLUR_BG)
            }
        }
    }*/
}

@Composable
fun ShowReferralSheet(viewModel: MyReferralsViewModel, promotionDetails: Results, closePopup: () -> Unit) {
//    Popup(
//        alignment = Alignment.Center,
//        offset = IntOffset(0, 800),
//        onDismissRequest = { closePopup() },
//        properties = PopupProperties(focusable = true, dismissOnBackPress = true, dismissOnClickOutside = false),
//    ) {
        ReferFriendScreen(viewModel, promotionDetails = promotionDetails, backAction = closePopup) {
            closePopup()
        }
//    }
}

@Composable
fun PromotionEmptyView(descriptionResourceId: Int) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty_view),
            contentDescription = stringResource(id = R.string.label_empty_promotions)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = stringResource(id = R.string.label_empty_promotions),
            fontWeight = FontWeight.Bold,
            fontFamily = font_sf_pro,
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = stringResource(id = descriptionResourceId),
            fontWeight = FontWeight.Normal,
            fontFamily = font_sf_pro,
            color = Color.Black,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}


