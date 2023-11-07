package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.views.home.VoucherView
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.GameZoneTabs
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.PromotionTabs

@Composable
fun GameZoneScreen(navController: NavHostController) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.background(TextPurpleLightBG)
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .background(Color.White)
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = stringResource(id = R.string.header_label_game_zone),
                fontFamily = font_sf_pro,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 3.dp, bottom = 3.dp, start = 16.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VeryLightPurple)
            ) {
                var selectedTab by remember { mutableStateOf(0) }
                Row(modifier = Modifier.background(Color.White)) {

                    val tabItems =
                        listOf(
                            GameZoneTabs.ActiveGames,
                            GameZoneTabs.ExpiredGames
                        )
                    ScrollableTabRow(selectedTabIndex = selectedTab,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
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
                        })
                    {
                        tabItems.forEachIndexed { index, it ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = {
                                    androidx.compose.material3.Text(
                                        text = stringResource(it.tabName),
                                        // Add padding if space between tabs needed
//                                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                                    )
                                },
                                selectedContentColor = VibrantPurple40,
                                unselectedContentColor = TextGray,
                            )
                        }
                    }

                }
                when (selectedTab) {
                    0 -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .background(VeryLightPurple)
                                .padding(16.dp)
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                GameView(
                                    thumbnailId = R.drawable.placeholder_game_thumbnail,
                                    titleId = R.string.game_placeholder_title,
                                    GameType.SPIN_A_WHEEL
                                ){
                                    // TODO: replace this with correct navigation component
                                    navController.navigate(MoreScreens.GameBetterLuckScreen.route)
                                }
                            }
                            item() {
                                GameView(
                                    thumbnailId = R.drawable.placeholder_scratch_card,
                                    titleId = R.string.game_placeholder_scratch_card_title,
                                    GameType.SCRATCH_CARD
                                ){
                                    navController.navigate(MoreScreens.ScratchCardScreen.route)
                                }
                            }
                            item() {
                                GameView(
                                    thumbnailId = R.drawable.placeholder_game_thumbnail,
                                    titleId = R.string.game_placeholder_title,
                                    GameType.SPIN_A_WHEEL
                                ){
                                    // TODO: replace this with correct navigation component
                                    navController.navigate(MoreScreens.GameCongratsScreen.route)
                                }
                            }
                            item {
                                GameView(
                                    thumbnailId = R.drawable.placeholder_scratch_card,
                                    titleId = R.string.game_placeholder_scratch_card_title,
                                    GameType.SCRATCH_CARD
                                ){
                                    navController.navigate(MoreScreens.ScratchCardScreen.route)
                                }
                            }
                        }
                    }
                    1 -> {

                    }
                }
            }
        }
    }
}