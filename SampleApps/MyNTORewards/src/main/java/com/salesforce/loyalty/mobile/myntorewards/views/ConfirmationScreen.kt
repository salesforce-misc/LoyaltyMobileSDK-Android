package com.salesforce.loyalty.mobile.myntorewards.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.views.components.BodyText
import com.salesforce.loyalty.mobile.myntorewards.views.components.HeaderText
import com.salesforce.loyalty.mobile.myntorewards.views.components.ImageComponent
import com.salesforce.loyalty.mobile.myntorewards.views.components.PrimaryButton

@Composable
fun ConfirmationScreen(
    confirmationScreenUiState: ConfirmationScreenUiState,
    imageDrawableId: Int,
    bannerDrawableId: Int = R.drawable.congratulations,
    buttonClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (image, topBanner, headerText, subText, button) = createRefs()

        val guideLineStart = createGuidelineFromStart(16.dp)
        val guideLineEnd = createGuidelineFromEnd(16.dp)

        PrimaryButton(
            textContent = confirmationScreenUiState.buttonText,
            onClick = buttonClick,
            modifier = Modifier
                .constrainAs(button) {
                    width = Dimension.fillToConstraints
                    bottom.linkTo(parent.bottom, margin = 56.dp)
                    start.linkTo(guideLineStart)
                    end.linkTo(guideLineEnd)
                }
        )

        HeaderText(
            text = confirmationScreenUiState.headerContent,
            modifier = Modifier.constrainAs(headerText) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(guideLineStart)
                end.linkTo(guideLineEnd)
            }
        )

        if (confirmationScreenUiState.bannerVisibility) {
            ImageComponent(
                drawableId = bannerDrawableId,
                confirmationScreenUiState.bannerContentDescription,
                modifier = Modifier
                    .constrainAs(topBanner) {
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth()
            )
        }

        ImageComponent(
            drawableId = imageDrawableId,
            contentDescription = confirmationScreenUiState.imageContentDescription,
            modifier = Modifier
                .size(150.dp)
                .constrainAs(image) {
                    start.linkTo(guideLineStart)
                    end.linkTo(guideLineEnd)
                    bottom.linkTo(headerText.top, margin = 36.dp)
                }
        )

        BodyText(
            text = confirmationScreenUiState.subHeaderContent,
            modifier = Modifier.constrainAs(subText) {
                top.linkTo(headerText.bottom)
                start.linkTo(guideLineStart)
                end.linkTo(guideLineEnd)
            }
        )
    }
}

@Composable
fun BetterLuckScreen(onClick: () -> Unit) {
    val betterLuckScreenState = ConfirmationScreenUiState(
        headerContent = stringResource(id = R.string.better_luck_next_time),
        subHeaderContent = stringResource(id = R.string.better_luck_next_time_sub_header),
        buttonText = stringResource(id = R.string.back_text),
        imageContentDescription = stringResource(id = R.string.better_luck_next_time),
        bannerContentDescription = stringResource(id = R.string.better_luck_next_time_sub_header),
        bannerVisibility = false
    )

    ConfirmationScreen(
        confirmationScreenUiState = betterLuckScreenState,
        imageDrawableId = R.drawable.game_no_voucher_icon,
    ) { onClick }
}

@Composable
fun CongratulationsScreen(onClick: () -> Unit) {
    val congratulationScreenState = ConfirmationScreenUiState(
        headerContent = stringResource(id = R.string.game_zone_congrats_header_content),
        subHeaderContent = stringResource(id = R.string.game_zone_congrats_sub_header_content),
        buttonText = stringResource(id = R.string.back_text),
        imageContentDescription = stringResource(id = R.string.game_zone_congrats_header_content),
        bannerContentDescription = stringResource(id = R.string.game_zone_congrats_header_content),
        bannerVisibility = true
    )

    ConfirmationScreen(
        confirmationScreenUiState = congratulationScreenState,
        imageDrawableId = R.drawable.gift_gamezone,
        bannerDrawableId = R.drawable.congratulations
    ) { onClick }
}

@Preview(showSystemUi = true)
@Composable
fun BetterLuckScreenPreview() {
    BetterLuckScreen {}
}

@Preview(showSystemUi = true)
@Composable
fun CongratulationsScreenPreview() {
    CongratulationsScreen {}
}