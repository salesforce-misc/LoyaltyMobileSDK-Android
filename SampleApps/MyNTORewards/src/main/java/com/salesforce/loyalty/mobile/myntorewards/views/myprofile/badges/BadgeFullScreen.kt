package com.salesforce.loyalty.mobile.myntorewards.views.myprofile.badges

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.badge.models.LoyaltyProgramBadgeListRecord
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.BADGES_ACHIEVED
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.BADGES_AVAILABLE
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.BADGES_EXPIRED
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.badgeEmptyViewMsgDescription
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.badgeEmptyViewMsgHeader
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common.Companion.isEndDateExpired
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_BADGE_TABS_ROW
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_VIEW_ALL_BADGE_FULL_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.BadgeViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.BadgeViewState
import com.salesforce.loyalty.mobile.myntorewards.views.components.CustomScrollableTab
import com.salesforce.loyalty.mobile.myntorewards.views.components.ErrorOrEmptyView
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.BadgeTabs
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BadgeFullScreen(
    navCheckOutFlowController: NavController,
    badgeViewModel: BadgeViewModelInterface
) {
    val TAB_ACHIEVED = 0
    val TAB_AVAILABLE = 1
    val TAB_EXPIRED = 2

    var refreshing by remember { mutableStateOf(false) }
    var blurBG by remember { mutableStateOf(0.dp) }
    val refreshScope = rememberCoroutineScope()
    val programBadgeIDListExpiredMap = remember { mutableMapOf<String, String>() }
    val programBadgeIDListMap = remember { mutableMapOf<String, String>() }
    var badgePopupState by remember { mutableStateOf(false) }
    var filteredBadges: List<LoyaltyProgramBadgeListRecord>? =
        remember { listOf() }
    var isInProgress by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val dateTimeFormatter = DateTimeFormatter.ofPattern(AppConstants.PROMOTION_DATE_API_FORMAT)

    val context: Context = LocalContext.current

    fun refresh() = refreshScope.launch {
        badgeViewModel.getCahchedProgramBadge(context, true)
        badgeViewModel.getCahchedProgramMemberBadge(context, true)
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)

    val programBadges by badgeViewModel.programBadgeLiveData.observeAsState() // collecting livedata as state
    val programMemberBadges by badgeViewModel.programMemberBadgeLiveData.observeAsState() // collecting livedata as state
    val badgeProgramFetchStatus by badgeViewModel.badgeProgramViewState.observeAsState() // collecting livedata as state
    val badgeMemberProgramFetchStatus by badgeViewModel.badgeProgramMemberViewState.observeAsState() // collecting livedata as state

    LaunchedEffect(true) {
        badgeViewModel.getCahchedProgramMemberBadge(context)
        badgeViewModel.getCahchedProgramBadge(context)
    }

    LaunchedEffect(true) {

        programMemberBadges?.let {
            for (programMemberBadgesItem in programMemberBadges!!.records) {

                if ( programMemberBadgesItem.status == BADGES_EXPIRED) {
                    programBadgeIDListExpiredMap[programMemberBadgesItem.loyaltyProgramBadgeId] =
                        programMemberBadgesItem.endDate
                } else {
                    programBadgeIDListMap[programMemberBadgesItem.loyaltyProgramBadgeId] =
                        programMemberBadgesItem.endDate
                }
            }
        }
    }

    Box(contentAlignment = Alignment.TopCenter) {

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .background(Color.White)
                .pullRefresh(state)
                .blur(blurBG)
                .testTag(TEST_TAG_VIEW_ALL_BADGE_FULL_SCREEN)
        )
        {

            BadgeFullScreenHeader {
                navCheckOutFlowController.popBackStack()
            }
            var selectedTab by remember { mutableStateOf(0) }

            Row(
                modifier = Modifier
                    .background(Color.White)
                    .testTag(TEST_TAG_BADGE_TABS_ROW)
            ) {

                val tabItems =
                    listOf(
                        BadgeTabs.TabAchieved,
                        BadgeTabs.TabAvailable,
                        BadgeTabs.TabExpired
                    )

                CustomScrollableTab(tabItems, selectedTab) {
                    selectedTab = it
                }
            }

            if (badgeProgramFetchStatus is BadgeViewState.BadgeFetchInProgress ||
                badgeMemberProgramFetchStatus is BadgeViewState.BadgeFetchInProgress
            ) {
                isInProgress = true
            } else if (badgeProgramFetchStatus is BadgeViewState.BadgeFetchFailure ||
                badgeMemberProgramFetchStatus is BadgeViewState.BadgeFetchFailure
            ) {
                isInProgress = false
                isError = true
            } else {
                isInProgress = false
                isError = false
            }

            if (isInProgress) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize(0.1f)
                    )
                }

            } else if (isError) {
                ErrorOrEmptyView(viewImageID = R.drawable.ic_astronaut, header= stringResource(id = R.string.label_badge_error))
            } else {

                Box {

                    Box {
                        when (selectedTab) {
                            TAB_ACHIEVED -> {
                                filteredBadges = programBadges?.records?.filter {
                                    it.status == BADGES_AVAILABLE &&
                                            programBadgeIDListMap.contains(it.id)
                                }
                                val programMemberShortedMapAchieved: Map<String, String> = programBadgeIDListMap.toSortedMap(compareBy<String> { LocalDate.parse(programBadgeIDListMap[it], dateTimeFormatter) })
                                val sortedFilteredList= mutableListOf<LoyaltyProgramBadgeListRecord>()

                               /* We have to iterate again in a list to match the Ids thats why its O(2^n) complexity.
                                There an scope of improvement in logic but for that API logic needs also to be correct
                                there is other possibility of code improvement */
                                for(programMemberShortedMapItem in programMemberShortedMapAchieved){
                                    if (filteredBadges != null) {
                                        for(filteredBadgeItem in filteredBadges!!){
                                            if(filteredBadgeItem.id== programMemberShortedMapItem.key){
                                                sortedFilteredList.add(filteredBadgeItem)
                                            }
                                        }
                                    }
                                }

                                BadgeFullScreenTabList(BADGES_ACHIEVED, sortedFilteredList, programBadgeIDListMap) {
                                    badgePopupState = true
                                    blurBG = it
                                }
                            }

                            TAB_AVAILABLE -> {
                                filteredBadges = programBadges?.records?.filter {
                                    it.status == BADGES_AVAILABLE &&
                                            !programBadgeIDListMap.contains(it.id) && !programBadgeIDListExpiredMap.contains(
                                        it.id
                                    )
                                }

                                BadgeFullScreenTabList(BADGES_AVAILABLE, filteredBadges, programBadgeIDListMap) {
                                    badgePopupState = true
                                    blurBG = it
                                }
                            }

                            TAB_EXPIRED -> {
                                filteredBadges = programBadges?.records?.filter {
                                    it.status == BADGES_EXPIRED ||
                                            programBadgeIDListExpiredMap.contains(it.id)

                                }
                                // badge is being decided as expired only basis on status only not basis of end date.
                                //If badge job has not run and status is active badge will still being considered as actice,

                                val programMemberShortedMapExpired: Map<String, String> = programBadgeIDListExpiredMap.toSortedMap(compareBy<String> { LocalDate.parse(programBadgeIDListExpiredMap[it], dateTimeFormatter) }.reversed())

                                /* We have to iterate again in a list to match the Ids thats why its O(2^n) complexity.
                                There an scope of improvement in logic but for that API logic needs also to be correct
                                there is other possibility of code improvement */

                                val sortedFilteredList= mutableListOf<LoyaltyProgramBadgeListRecord>()
                                for(programMemberShortedMapItem in programMemberShortedMapExpired){
                                    if (filteredBadges != null) {
                                        for(filteredBadgeItem in filteredBadges!!){
                                            if(filteredBadgeItem.id== programMemberShortedMapItem.key){
                                                sortedFilteredList.add(filteredBadgeItem)
                                            }
                                        }
                                    }
                                }

                                BadgeFullScreenTabList(
                                    BADGES_EXPIRED,
                                    sortedFilteredList,
                                    programBadgeIDListExpiredMap
                                ) {
                                    badgePopupState = true
                                    blurBG = it
                                }
                            }
                        }
                    }
                    Box {
                        if (filteredBadges?.isEmpty() == true) {
                            ErrorOrEmptyView(header=stringResource(id = badgeEmptyViewMsgHeader(selectedTab)),
                                description = stringResource(id = badgeEmptyViewMsgDescription(selectedTab)))
                        }
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        PullRefreshIndicator(refreshing, state)
    }
}