package com.salesforce.loyalty.mobile.myntorewards.views.home

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
import com.salesforce.loyalty.mobile.myntorewards.views.components.bottomSheetShape
import com.salesforce.loyalty.mobile.myntorewards.views.myreferrals.ReferFriendScreen
import com.salesforce.loyalty.mobile.myntorewards.views.offers.PromotionEnrollPopupUI
import com.salesforce.loyalty.mobile.myntorewards.views.receipts.ErrorPopup
import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
import com.salesforce.loyalty.mobile.sources.loyaltyModels.Results

const val LOG_TAG = "PromotionDifferentiator"

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
    val context = LocalContext.current

    LaunchedEffect(Unit) {
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
                    // do nothing
                    Logger.d(LOG_TAG, "MyReferralsFetchSuccess")
                }

                is MyReferralsViewState.MyReferralsFetchFailure -> {
                    // do nothing
                    Logger.d(LOG_TAG, "MyReferralsFetchFailure")
                }

                is MyReferralsViewState.MyReferralsFetchInProgress -> {
                    CircularProgress(
                        modifier = Modifier
                            .fillMaxHeight(0.92f)
                            .fillMaxWidth()
                            .background(Color.White, shape = bottomSheetShape)
                    )
                }

                MyReferralsViewState.MyReferralsPromotionEnrolled -> {
                    ReferFriendScreen(referralViewModel, promotionDetails = results, backAction = closePopup) {
                        closePopup()
                    }
                }

                is MyReferralsViewState.MyReferralsPromotionNotEnrolled -> {
                    ReferFriendScreen(referralViewModel, promotionDetails = results, backAction = closePopup) {
                        closePopup()
                    }
                }

                is MyReferralsViewState.MyReferralsPromotionStatusFailure -> {
                    ErrorPopup(
                        it.errorMessage
                            ?: stringResource(id = R.string.game_error_msg),
                        tryAgainClicked = { referralViewModel.fetchReferralProgramStatus(context) },
                        textButtonClicked = { closePopup() },
                        textButton = stringResource(id = R.string.referral_back_button_text)
                    )
                }

                is MyReferralsViewState.PromotionReferralApiStatusFailure -> {
                    ErrorPopup(
                        it.error ?: stringResource(id = R.string.game_error_msg),
                        tryAgainClicked = { referralViewModel.checkIfGivenPromotionIsReferralAndEnrolled(context, results.promotionId.orEmpty()) },
                        textButtonClicked = { closePopup() },
                        textButton = stringResource(id = R.string.referral_back_button_text)
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
            }
        }
    }
}

