package com.salesforce.loyalty.mobile.myntorewards.views.myprofile.badges

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.MyProfileScreenBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextRed
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VeryLightPurple
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_VIEW_ALL_BADGE
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.BadgeViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.BadgeViewState
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ProfileViewScreen

@Composable
fun BadgeRow(
    navCheckOutFlowController: NavController,
    badgeViewModel: BadgeViewModelInterface,
    blurBG: (Dp) -> Unit
) {


    val programBadges by badgeViewModel.programBadgeLiveData.observeAsState() // collecting livedata as state
    val programMemberBadges by badgeViewModel.programMemberBadgeLiveData.observeAsState() // collecting livedata as state
    val badgeProgramFetchStatus by badgeViewModel.badgeProgramViewState.observeAsState() // collecting livedata as state
    val badgeMemberProgramFetchStatus by badgeViewModel.badgeProgramMemberViewState.observeAsState() // collecting livedata as state
// collecting livedata as state
    val context: Context = LocalContext.current

    var isInProgress by remember { mutableStateOf(false) }
    val programBadgeIDListMap = remember { mutableMapOf<String, String>() }

    LaunchedEffect(true) {
        badgeViewModel.loadLoyaltyProgramMemberBadge(context)
        badgeViewModel.loadLoyaltyProgramBadge(context)
        isInProgress = true
    }

    programMemberBadges?.let {
        for (programMemberBadgesItem in programMemberBadges!!.records) {

            if (!(Common.isEndDateExpired(programMemberBadgesItem.endDate) || programMemberBadgesItem.status == AppConstants.BADGES_EXPIRED)) {
                programBadgeIDListMap[(programMemberBadgesItem.loyaltyProgramBadgeId)] =
                    programMemberBadgesItem.endDate
            }
        }
    }


    var filteredAchievedBadges = programBadges?.records?.filter {
        it.status == AppConstants.BADGES_AVAILABLE &&
                programBadgeIDListMap.contains(it.id)
    }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .background(MyProfileScreenBG)
            .padding(start = 16.dp, end = 16.dp),
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.text_my_badges),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
            )
            Text(
                text = stringResource(id = R.string.view_all),
                fontWeight = FontWeight.Bold,
                color = VibrantPurple40,
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                modifier = Modifier
                    .clickable {
                        navCheckOutFlowController.navigate(ProfileViewScreen.BadgeFullScreen.route)
                    }
                    .testTag(TEST_TAG_VIEW_ALL_BADGE)
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            if(badgeProgramFetchStatus is BadgeViewState.BadgeFetchInProgress ||
                badgeMemberProgramFetchStatus is BadgeViewState.BadgeFetchInProgress) {
                isInProgress = true
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize(0.1f)
                )
            }
            else if(badgeProgramFetchStatus is BadgeViewState.BadgeFetchFailure ||
                badgeMemberProgramFetchStatus is BadgeViewState.BadgeFetchFailure){
                isInProgress = false
                BadgeErrorAndEmptyViewMiniScreen(R.drawable.badge_error_icon, stringResource(id = R.string.label_badge_error), TextRed)

            }
            else{
                isInProgress = false
                if (filteredAchievedBadges?.size == 0) {
                    BadgeErrorAndEmptyViewMiniScreen(R.drawable.empty_badge_icon,
                        stringResource(id =R.string.badge_empty_view_text_achieved ), VibrantPurple40)
                } else {
                    filteredAchievedBadges?.let {
                        LazyRow(
                            modifier = Modifier
                                .background(VeryLightPurple)
                                .width(346.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(filteredAchievedBadges) { filteredBadgeItem ->
                                BadgeView(
                                    filteredBadgeItem,
                                    programBadgeIDListMap[filteredBadgeItem.id]
                                ) {
                                    blurBG(it)
                                }
                            }

                        }
                    }
                }
            }
        }

    }
}