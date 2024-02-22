package com.salesforce.loyalty.mobile.myntorewards.views.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.MyReferralsViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.MyPromotionViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.MyReferralsViewState
import com.salesforce.loyalty.mobile.myntorewards.views.components.CircularProgress
import com.salesforce.loyalty.mobile.myntorewards.views.components.ProgressDialogComposable
import com.salesforce.loyalty.mobile.myntorewards.views.components.bottomSheetShape
import com.salesforce.loyalty.mobile.myntorewards.views.offers.PromotionEnrollPopup
import com.salesforce.loyalty.mobile.myntorewards.views.offers.PromotionEnrollPopupUI
import com.salesforce.loyalty.mobile.myntorewards.views.receipts.ErrorPopup
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results

@Composable
fun PromotionDifferentiator(
    promotionViewModel: MyPromotionViewModelInterface,
    navCheckOutFlowController: NavController,
    results: Results,
    promotionId: String,
    referralViewModel: MyReferralsViewModel,
    closePopup: () -> Unit
) {
    val viewState by referralViewModel.uiState.observeAsState(null)
    val promotionClickState by referralViewModel.promotionClickState.observeAsState()
    val context = LocalContext.current
    Log.e("PromotionDifferentiator", "inside")

    LaunchedEffect(Unit) {
        Log.e("LaunchedEffect", "inside")
        referralViewModel.checkIfGivenPromotionIsInCache(context, promotionId)
    }
    Popup(
        alignment = Alignment.Center,
        offset = IntOffset(0, 800),
        onDismissRequest = { closePopup() },
        properties = PopupProperties(focusable = true, dismissOnBackPress = true, dismissOnClickOutside = false),
    ) {
        viewState?.let {
            when (it) {
                is MyReferralsViewState.MyReferralsFetchSuccess -> {
                    Log.e("PromotionDifferentiator", "MyReferralsFetchSuccess")

                }

                is MyReferralsViewState.MyReferralsFetchFailure -> {
                    Log.e("PromotionDifferentiator", "MyReferralsFetchFailure")

                }

                is MyReferralsViewState.MyReferralsFetchInProgress -> {
                    Log.e("PromotionDifferentiator", "MyReferralsFetchInProgress")
                    /*Popup(
                        alignment = Alignment.Center,
                        offset = IntOffset(0, 800),
                        onDismissRequest = { closePopup() },
                        properties = PopupProperties(
                            focusable = true,
                            dismissOnBackPress = true,
                            dismissOnClickOutside = false
                        ),
                    ) {*/
                        CircularProgress(
                            modifier = Modifier
                                .fillMaxHeight(0.92f)
                                .fillMaxWidth()
                            .background(Color.White, shape = bottomSheetShape)
                        )
//                    }
//                ProgressDialogComposable { }
                }

                MyReferralsViewState.MyReferralsPromotionEnrolled -> {
                    Log.e("PromotionDifferentiator", "MyReferralsPromotionEnrolled")

                    ShowReferralSheet(referralViewModel, results) {
                        closePopup()
//                    promotionViewModel.hideSheet()
//                    currentPromotionDetailPopupState = false
//                    blurBG(NO_BLUR_BG)
                    }
                }

                is MyReferralsViewState.MyReferralsPromotionNotEnrolled -> {
                    Log.e("PromotionDifferentiator", "MyReferralsPromotionNotEnrolled")

                    ShowReferralSheet(referralViewModel, results) {
                        closePopup()
//                    promotionViewModel.hideSheet()
//                    currentPromotionDetailPopupState = false
//                    blurBG(NO_BLUR_BG)
                    }
                }

                is MyReferralsViewState.MyReferralsPromotionStatusFailure -> {
                    Log.e("PromotionDifferentiator", "MyReferralsPromotionStatusFailure")

                    ErrorPopup(
                        it.errorMessage
                            ?: stringResource(id = R.string.receipt_scanning_error_desc),
                        tryAgainClicked = { referralViewModel.fetchReferralProgramStatus(context) },
                        textButtonClicked = { }
                    )
                }

                is MyReferralsViewState.PromotionReferralApiStatusFailure -> {
                    Log.e("PromotionDifferentiator", "PromotionReferralApiStatusFailure")

                    ErrorPopup(
                        it.error ?: stringResource(id = R.string.receipt_scanning_error_desc),
                        tryAgainClicked = { referralViewModel.checkIfGivenPromotionIsReferralAndEnrolled(context, results.promotionId.orEmpty()) },
                        textButtonClicked = { }
                    )
                }

                MyReferralsViewState.PromotionStateNonReferral -> {
                    PromotionEnrollPopupUI(
                        results,
                        closePopup = {
                            closePopup()
                        },
                        navCheckOutFlowController,
                        promotionViewModel
                    )
                }

                is MyReferralsViewState.PromotionStateReferral -> referralViewModel.fetchReferralProgramStatus(
                    context
                )
            }
        }
    }

    /*promotionClickState?.let {
        when(it) {
            is PromotionClickState.PromotionReferralApiStatusInProgress -> {
                Log.e("PromotionDifferentiator", "PromotionReferralApiStatusInProgress")

                ProgressDialogComposable { }
            }
            is PromotionClickState.PromotionStateReferralHideSheet ->  {
                Log.e("PromotionDifferentiator", "PromotionStateReferralHideSheet")

                closePopup()
            }
            is PromotionClickState.PromotionStateReferral -> {
                Log.e("PromotionDifferentiator", "PromotionStateReferral")

                referralViewModel.fetchReferralProgramStatus(context)

                *//*ShowReferralSheet(promoCode = it.promotionCode, isEnrolled = it.isUserEnrolledToReferralPromotion) {
                    closePopup()
//                    promotionViewModel.hideSheet()
//                    currentPromotionDetailPopupState = false
//                    blurBG(NO_BLUR_BG)
                }*//*
            }
            is PromotionClickState.PromotionReferralApiStatusFailure -> {
                Log.e("PromotionDifferentiator", "PromotionReferralApiStatusFailure")

                ErrorPopup(
                    it.error ?: stringResource(id = R.string.receipt_scanning_error_desc),
                    tryAgainClicked = {  },
                    textButtonClicked = {  }
                )
            }

            is PromotionClickState.PromotionStateNonReferral -> {
                Log.e("PromotionDifferentiator", "PromotionStateNonReferral")

                PromotionEnrollPopup(
                    results,
                    closePopup = {
                        closePopup()
                    },
                    navCheckOutFlowController,
                    promotionViewModel
                )
            }
        }
    }*/
}

