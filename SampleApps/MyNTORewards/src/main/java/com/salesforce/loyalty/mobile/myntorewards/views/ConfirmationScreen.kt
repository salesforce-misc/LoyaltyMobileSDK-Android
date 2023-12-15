package com.salesforce.loyalty.mobile.myntorewards.views

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.views.components.BodyText
import com.salesforce.loyalty.mobile.myntorewards.views.components.HeaderText
import com.salesforce.loyalty.mobile.myntorewards.views.components.HtmlText
import com.salesforce.loyalty.mobile.myntorewards.views.components.ImageComponent
import com.salesforce.loyalty.mobile.myntorewards.views.components.PrimaryButton
import com.salesforce.loyalty.mobile.myntorewards.views.components.animation.ConfettiAnimationView

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
    requireAnimation: Boolean = false,
    imageDrawableId: Int,
    bannerDrawableId: Int = R.drawable.congratulations,
    buttonClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (image, topBanner, lineImage, headerText, subText, button) = createRefs()

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

        AnimatedView(
            requireAnimation,
            bannerDrawableId,
            bannerContentDescription,
            topBanner,
            lineImage,
            image
        )

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

        HtmlText(
            text = subHeaderContent,
            size = 16f,
            textGravity = Gravity.CENTER,
            modifier = Modifier.constrainAs(subText) {
                top.linkTo(headerText.bottom)
                start.linkTo(guideLineStart)
                end.linkTo(guideLineEnd)
            }.padding(horizontal = 32.dp)
        )
    }
}

@Composable
private fun ConstraintLayoutScope.AnimatedView(
    requireAnimation: Boolean,
    bannerDrawableId: Int,
    bannerContentDescription: String,
    topBanner: ConstrainedLayoutReference,
    lineImage: ConstrainedLayoutReference,
    image: ConstrainedLayoutReference
) {
    if (requireAnimation) {
        ImageComponent(
            drawableId = bannerDrawableId,
            bannerContentDescription,
            modifier = Modifier.Companion.constrainAs(topBanner) {
                top.linkTo(parent.top)
            }.fillMaxWidth()
        )
        ConfettiAnimationView()
    } else {
        ImageComponent(
            drawableId = R.drawable.game_better_luck_line,
            contentDescription = null,
            modifier = Modifier.Companion.constrainAs(lineImage) {
                start.linkTo(image.start)
                end.linkTo(image.end)
                top.linkTo(image.top)
                bottom.linkTo(image.bottom)
            }
        )
    }
}

@Composable
fun BetterLuckScreen(onClick: () -> Unit) {
    ConfirmationScreen(
        headerContent = stringResource(id = R.string.better_luck_next_time),
        subHeaderContent = stringResource(id = R.string.better_luck_next_time_sub_header),
        buttonText = stringResource(id = R.string.play_more_text),
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
        buttonText = stringResource(id = R.string.play_more_text),
        imageContentDescription = stringResource(id = R.string.game_zone_congrats_header_content),
        bannerContentDescription = stringResource(id = R.string.game_zone_congrats_header_content),
        requireAnimation = true,
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