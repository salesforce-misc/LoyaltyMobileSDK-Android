package com.salesforce.loyalty.mobile.myntorewards.views.myreferrals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VeryLightPurple
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.ReferralItemState
import com.salesforce.loyalty.mobile.myntorewards.views.components.BodyTextSmall
import com.salesforce.loyalty.mobile.myntorewards.views.components.BodyTextSmallBold
import com.salesforce.loyalty.mobile.myntorewards.views.components.CommonText
import com.salesforce.loyalty.mobile.myntorewards.views.components.ImageComponent

const val TEST_TAG_REFERRALS_LIST = "TEST_TAG_REFERRALS_LIST"
const val TEST_TAG_REFERRALS_LIST_ITEM = "TEST_TAG_REFERRALS_LIST_ITEM"

@Composable
fun ReferralList(itemStates: List<ReferralItemState>) {
    // Group Items based on section name to show items under the respective section header
    val grouped = itemStates.groupBy{ stringResource(id = it.sectionName) }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(VeryLightPurple)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag(TEST_TAG_REFERRALS_LIST)
    ){
        grouped.forEach { (section, sectionPersons) ->
            item {
                CommonText(text = section)
            }

            items(sectionPersons) {
                ReferralsListItem(it.mail, it.duration, it.purchaseStatus)
            }
        }
    }
}

@Composable
fun ReferralsListItem(mail: String, duration: String, purchaseStatus: ReferralStatusType) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
            .testTag(TEST_TAG_REFERRALS_LIST_ITEM)
    ) {
        val (statusIcon, mailView, durationView, statusText) = createRefs()

        ImageComponent(
            drawableId = purchaseStatus.iconId,
            contentDescription = null,
            modifier = Modifier.constrainAs(statusIcon) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }.padding(end = 8.dp)
        )

        BodyTextSmallBold(
            text = mail,
            modifier = Modifier.constrainAs(mailView) {
                start.linkTo(statusIcon.end)
            }
        )

        BodyTextSmall(
            text = duration,
            modifier = Modifier.constrainAs(durationView) {
                start.linkTo(statusIcon.end)
                top.linkTo(mailView.bottom)
            }
        )

        CommonText(
            text = stringResource(id = purchaseStatus.content),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = purchaseStatus.contentColor,
            modifier = Modifier.constrainAs(statusText) {
                end.linkTo(parent.end)
                top.linkTo(durationView.top)
            }
        )
    }
}

@Preview
@Composable
fun ReferralsListPreview() {
    ReferralList(referralItemStates())
}

private fun referralItemStates(): List<ReferralItemState> {
    return listOf(
        ReferralItemState(
            R.string.recent_referrals_section_name,
            "strawberry.sheikh@yahoo.com",
            "2 days ago",
            ReferralStatusType.COMPLETED
        )
    )
}