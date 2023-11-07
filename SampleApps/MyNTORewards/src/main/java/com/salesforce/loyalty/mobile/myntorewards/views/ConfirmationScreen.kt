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

/**
 * Common composable screen for Better Luck and Congratulations screen as most of the look and feel is same
 */
@Composable
fun ConfirmationScreen(
    headerContent: String,
    subHeaderContent: String,
    buttonText: String,
    imageContentDescription: String,
    bannerContentDescription: String,
    bannerVisibility: Boolean = false,
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
            textContent = buttonText,
            onClick = { buttonClick() },
            modifier = Modifier.constrainAs(button) {
                width = Dimension.fillToConstraints
                bottom.linkTo(parent.bottom, margin = 56.dp)
                start.linkTo(guideLineStart)
                end.linkTo(guideLineEnd)
            }
        )

        HeaderText(
            text = headerContent,
            modifier = Modifier.constrainAs(headerText) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(guideLineStart)
                end.linkTo(guideLineEnd)
            }
        )

        if (bannerVisibility) {
            ImageComponent(
                drawableId = bannerDrawableId,
                bannerContentDescription,
                modifier = Modifier.constrainAs(topBanner) {
                    top.linkTo(parent.top)
                }.fillMaxWidth()
            )
        }

        ImageComponent(
            drawableId = imageDrawableId,
            contentDescription = imageContentDescription,
            modifier = Modifier
                .size(150.dp)
                .constrainAs(image) {
                    start.linkTo(guideLineStart)
                    end.linkTo(guideLineEnd)
                    bottom.linkTo(headerText.top, margin = 36.dp)
                }
        )

        BodyText(
            text = subHeaderContent,
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
    ConfirmationScreen(
        headerContent = stringResource(id = R.string.better_luck_next_time),
        subHeaderContent = stringResource(id = R.string.better_luck_next_time_sub_header),
        buttonText = stringResource(id = R.string.back_text),
        imageContentDescription = stringResource(id = R.string.better_luck_next_time),
        bannerContentDescription = stringResource(id = R.string.better_luck_next_time_sub_header),
        imageDrawableId = R.drawable.game_no_voucher_icon,
    ) { onClick() }
}

@Composable
fun CongratulationsScreen(offerPercent: String, onClick: () -> Unit) {
    ConfirmationScreen(
        headerContent = stringResource(id = R.string.game_zone_congrats_header_content),
        subHeaderContent = stringResource(id = R.string.game_zone_congrats_sub_header_content, offerPercent),
        buttonText = stringResource(id = R.string.back_text),
        imageContentDescription = stringResource(id = R.string.game_zone_congrats_header_content),
        bannerContentDescription = stringResource(id = R.string.game_zone_congrats_header_content),
        bannerVisibility = true,
        imageDrawableId = R.drawable.gift_gamezone,
        bannerDrawableId = R.drawable.congratulations
    ) { onClick() }
}

@Preview(showSystemUi = true)
@Composable
fun BetterLuckScreenPreview() {
    BetterLuckScreen {}
}

@Preview(showSystemUi = true)
@Composable
fun CongratulationsScreenPreview() {
    CongratulationsScreen("20%") {}
}