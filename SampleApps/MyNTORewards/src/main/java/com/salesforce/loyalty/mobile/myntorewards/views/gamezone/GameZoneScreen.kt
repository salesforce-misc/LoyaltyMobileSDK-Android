package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.KEY_GAME_PARTICIPANT_REWARD_ID
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.TAB_ACTIVE_GAMES
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.TAB_EXPIRED_GAMES
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.GameViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GamesViewState
import com.salesforce.loyalty.mobile.myntorewards.views.components.EmptyView
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.GameZoneTabs
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import java.io.Serializable

@Composable
fun GameZoneScreen(navController: NavHostController, gameViewModel: GameViewModelInterface) {
    val games by gameViewModel.gamesLiveData.observeAsState()
    val gameViewState by gameViewModel.gamesViewState.observeAsState()
    var isInProgress by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(true) {
        gameViewModel.getGames(context, false)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(TextPurpleLightBG)
            .fillMaxSize()
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
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 16.sp,
                                        fontFamily = font_sf_pro,
                                        // Add padding if space between tabs needed
                                        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                                    )
                                },
                                selectedContentColor = VibrantPurple40,
                                unselectedContentColor = TextGray,
                            )
                        }
                    }

                }
                when (gameViewState) {
                    GamesViewState.GamesFetchSuccess -> {
                        isInProgress = false
                        val notPlayedGames = games?.gameDefinitions?.filter {
                            it.participantGameRewards[0].gameRewardId == null
                        }
                        if (notPlayedGames != null) {
                            val (activeGames, expiredGames) = notPlayedGames.partition {
                                it.participantGameRewards[0].expirationDate.let { expDate ->
                                    expDate?.let { date -> Common.isGameExpired(date) } == false
                                }
                            }

                            when (selectedTab) {
                                TAB_ACTIVE_GAMES -> {
                                    if (activeGames.isNotEmpty()) {
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
                                            items(activeGames.size) { activeItem ->
                                                val activeGame = activeGames[activeItem]
                                                val expirationDate =
                                                    activeGame.participantGameRewards[0].expirationDate
                                                val expiryDetail = expirationDate?.let {
                                                    Common.formatGameDateTime(
                                                        it,
                                                        context
                                                    )
                                                }
                                                val gameType =
                                                    if (GameType.SPIN_A_WHEEL.gameType == activeGame.type) {
                                                        GameType.SPIN_A_WHEEL
                                                    } else {
                                                        GameType.SCRATCH_CARD
                                                    }
                                                GameView(
                                                    false,
                                                    expiryDetail = expiryDetail,
                                                    title = activeGame.name ?: "",
                                                    gameType
                                                ) {
                                                    navController.currentBackStackEntry?.arguments?.putString(
                                                        KEY_GAME_PARTICIPANT_REWARD_ID,
                                                        activeGame.participantGameRewards[0].gameParticipantRewardId
                                                    )
                                                    if (gameType == GameType.SPIN_A_WHEEL) {
                                                        val gamesData =
                                                            activeGame.gameRewards as ArrayList
                                                        navController.currentBackStackEntry?.arguments?.putParcelableArrayList(
                                                            AppConstants.KEY_GAME_REWARD,
                                                            gamesData
                                                        )

                                                        navController.navigate(MoreScreens.SpinWheelScreen.route)
                                                    } else {
                                                        navController.navigate(MoreScreens.ScratchCardScreen.route)
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        ShowEmptyView(selectedTab = selectedTab)
                                    }
                                }
                                TAB_EXPIRED_GAMES -> {
                                    if (expiredGames.isNotEmpty()) {
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
                                            items(expiredGames.size) { expiredItem ->
                                                val activeGame = expiredGames[expiredItem]
                                                val gameType =
                                                    if (GameType.SPIN_A_WHEEL.gameType == activeGame.type) {
                                                        GameType.SPIN_A_WHEEL
                                                    } else {
                                                        GameType.SCRATCH_CARD
                                                    }
                                                GameView(
                                                    true,
                                                    expiryDetail = stringResource(id = R.string.game_expired),
                                                    title = activeGame.name ?: "",
                                                    gameType
                                                ) {}
                                            }
                                        }
                                    } else {
                                        ShowEmptyView(selectedTab = selectedTab)
                                    }
                                }
                            }
                        } else {
                            ShowEmptyView(selectedTab = selectedTab)
                        }
                    }
                    GamesViewState.GamesFetchFailure -> {
                        if (isInProgress) {
                            isInProgress = false
                            ShowEmptyView(selectedTab = selectedTab)
                        }
                    }
                    GamesViewState.GamesFetchInProgress -> {
                        isInProgress = true
                    }

                    else -> {}
                }
            }
        }

        if (isInProgress) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(0.1f)
            )
        }
    }
}

@Composable
fun ShowEmptyView(selectedTab: Int){
    when (selectedTab) {
        TAB_ACTIVE_GAMES -> {
            EmptyView(
                header = stringResource(id = R.string.label_no_games),
                description = stringResource(
                    id = R.string.label_empty_active_games
                )
            )
        }
        TAB_EXPIRED_GAMES -> {
            EmptyView(
                header = stringResource(id = R.string.label_no_games),
                description = stringResource(
                    id = R.string.label_empty_expired_games
                )
            )
        }
    }
}