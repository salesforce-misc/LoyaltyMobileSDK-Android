package com.salesforce.loyalty.mobile.myntorewards.views.myreferrals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple80
import com.salesforce.loyalty.mobile.myntorewards.views.components.CommonText
import com.salesforce.loyalty.mobile.myntorewards.views.components.ImageComponent
import com.salesforce.loyalty.mobile.myntorewards.views.components.SecondaryButton

@Composable
fun ReferralCard(referralsInfo: List<Pair<Int, String>>) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(VibrantPurple40, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        val (referNowButton, headerText, sentView, star1, star2, star3, star4, star5, star6) = createRefs()

        CommonText(
            text = stringResource(R.string.my_referral_card_header),
            fontSize = 12.sp,
            color = VibrantPurple80,
            modifier = Modifier
                .constrainAs(headerText) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(horizontal = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .constrainAs(sentView) {
                    top.linkTo(headerText.bottom)
                    start.linkTo(parent.start)
                }
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(referralsInfo) {
                CategoryCount(
                    header = stringResource(id = it.first),
                    value = it.second,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        SecondaryButton(
            onClick = { },
            textContent = stringResource(id = R.string.referral_card_button_text),
            modifier = Modifier
                .constrainAs(referNowButton) {
                    width = Dimension.fillToConstraints
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(sentView.bottom)
                }
                .padding(horizontal = 8.dp),
        )

        ImageComponent(drawableId = R.drawable.ic_star_referral_filled, contentDescription = null, modifier = Modifier
            .constrainAs(star1) {
                end.linkTo(parent.end)
            }
            .size(12.dp))

        ImageComponent(drawableId = R.drawable.ic_star_referrals, contentDescription = null, modifier = Modifier
            .constrainAs(star2) {
                end.linkTo(parent.end, margin = 8.dp)
                top.linkTo(sentView.top, margin = 16.dp)
            }
            .size(20.dp))

        ImageComponent(drawableId = R.drawable.ic_star_referral_filled, contentDescription = null, modifier = Modifier
            .constrainAs(star3) {
                end.linkTo(parent.end, margin = 32.dp)
                top.linkTo(headerText.top, margin = 8.dp)
            }
            .size(16.dp))

        ImageComponent(drawableId = R.drawable.ic_star_referrals, contentDescription = null, modifier = Modifier
            .constrainAs(star4) {
                start.linkTo(parent.start)
                top.linkTo(sentView.top, margin = 24.dp)
            }
            .size(20.dp))

        ImageComponent(drawableId = R.drawable.ic_star_referral_filled, contentDescription = null, modifier = Modifier
            .constrainAs(star5) {
                start.linkTo(star4.end, margin = 30.dp)
                bottom.linkTo(star4.bottom)
            }
            .size(12.dp))

        ImageComponent(drawableId = R.drawable.ic_star_referral_filled, contentDescription = null, modifier = Modifier
            .constrainAs(star6) {
                start.linkTo(parent.start, margin = 8.dp)
                bottom.linkTo(sentView.bottom)
            }
            .size(16.dp))
    }
}

@Composable
fun CategoryCount(header: String, value: String, modifier: Modifier) {
    Column (modifier = modifier){
        CommonText(
            text = header,
            fontSize = 12.sp,
            color = VibrantPurple80,
            fontWeight = FontWeight.Bold
        )

        CommonText(
            text = value,
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun ReferralCardPreview() {
    ReferralCard(listOf(Pair(R.string.my_referral_sent_label, "100"),
        Pair(R.string.my_referrals_accepted_label, "18"),
        Pair(R.string.my_referrals_vouchers_earned_label, "18"),
        Pair(R.string.my_referrals_points_earned_label, "18")))
}