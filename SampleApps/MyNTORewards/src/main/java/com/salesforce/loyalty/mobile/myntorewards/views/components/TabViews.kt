package com.salesforce.loyalty.mobile.myntorewards.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextGray
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.ReferralTabs
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.TabBaseItem

const val TEST_TAG_TAB_VIEW = "TEST_TAG_TAB_VIEW"

@Composable
fun CustomScrollableTab(
    tabItems: List<TabBaseItem>,
    selectedTab: Int,
    updateTab: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTab,
        modifier = Modifier.fillMaxWidth().background(Color.White),
        containerColor = Color.White,
        divider = {},
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTab])
                    .background(Color.White),
                height = 2.dp,
                color = VibrantPurple40
            )
        }
    ) {
        tabItems.forEachIndexed { index, it ->
            Tab(
                selected = selectedTab == index,
                onClick = { updateTab(index) },
                text = { Text(text = stringResource(it.tabName)) },
                selectedContentColor = VibrantPurple40,
                unselectedContentColor = TextGray,
            )
        }
    }
}

@Preview
@Composable
fun CustomScrollableTabPreview() {
    CustomScrollableTab(
        listOf(ReferralTabs.Success, ReferralTabs.InProgress), 0, {}
    )
}