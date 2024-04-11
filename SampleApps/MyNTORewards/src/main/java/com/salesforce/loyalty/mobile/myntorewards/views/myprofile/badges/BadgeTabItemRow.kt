package com.salesforce.loyalty.mobile.myntorewards.views.myprofile.badges

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyProgramBadgeListRecord
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.LighterBlack
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextDarkGray
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VeryLightPurple
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_BADGE_FULLSCREEN_ITEM
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@Composable

fun BadgeFullScreenTabList(
    filteredBadge: List<LoyaltyProgramBadgeListRecord>?,
    programMemberMap: Map<String, String>,
    blurBG: (Dp) -> Unit
) {

    Column(
        modifier = Modifier
            .background(VeryLightPurple)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        filteredBadge?.let {
            LazyColumn(
                modifier = Modifier
                    .background(VeryLightPurple)
                    .padding(16.dp)
                    .width(346.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(filteredBadge) { filteredBadgeItem ->
                    ListItemBadge(filteredBadgeItem, programMemberMap.get(filteredBadgeItem.id)) {
                        blurBG(it)
                    }
                }
            }
        }
    }

}


@Composable
fun ListItemBadge(badge: LoyaltyProgramBadgeListRecord, date: String?, blurBG: (Dp) -> Unit) {
    Spacer(modifier = Modifier.height(12.dp))
    var badgePopupState by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .testTag(TEST_TAG_BADGE_FULLSCREEN_ITEM)
            .clickable {
                blurBG(AppConstants.BLUR_BG)
                badgePopupState = true
            }
    ) {

        Column(modifier = Modifier.weight(0.15f)) {
            Image(
                painter = painterResource(R.drawable.dummy_badge_icon2),
                contentDescription = stringResource(R.string.cd_badge_icon_full_screen),
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp),
                contentScale = ContentScale.FillWidth
            )
        }
        Column(modifier = Modifier
            .weight(0.7f)
            .padding(start = 10.dp)) {
            Text(
                text = badge.name,
                fontWeight = FontWeight.SemiBold,
                fontFamily = font_sf_pro,
                color = LighterBlack,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
            )
            Text(
                text = badge.description,
                fontFamily = font_sf_pro,
                fontWeight = FontWeight.Normal,
                color = TextDarkGray,
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
            )

        }
        Column(modifier = Modifier
            .weight(0.2f)
            .align(Alignment.Bottom)) {
            date?.let{
                Text(
                    text =  ""+endDateExpiredCountInDaysText(date),
                    fontWeight = FontWeight.Bold,
                    color = TextDarkGray,
                    textAlign = TextAlign.End,
                    fontSize = 8.sp,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    if (badgePopupState) {
        BadgePopup(
            badge,
            date,
            closePopup = {
                badgePopupState = false
                blurBG(AppConstants.NO_BLUR_BG)
            }
        )
    }
}


@Composable
fun endDateExpiredCountInDaysText(endDateString: String): String{
     val currentDate = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val endDate = sdf.parse(endDateString)

    /* adding 24 hour to end date as date coming from server is
     having only date not time. and by default its taking time as 00:00
     hence comparison was getting failed as time of start date was hight than time of end date
     incase of same date*/

        /*  if enddate is today---> 28 March till 12 PM
          and if we not add it calculate 28 march 00:00 AM*/

        if (endDate != null) {
            endDate.time= endDate.time + (24 * 60 * 60 * 1000)
        }
        val diff: Long = (endDate?.getTime() ?:currentDate.time ) - currentDate.getTime()
        val days=  TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)

        val dateText = if (days > 0) {
            stringResource(id = R.string.badge_expires_in_days, days)
        } else if (days < 0) {
            stringResource(id = R.string.badge_expired_days_ago, abs(days.toInt()))
        } else {
            stringResource(id = R.string.badge_expires_today)
        }
        return dateText

}
