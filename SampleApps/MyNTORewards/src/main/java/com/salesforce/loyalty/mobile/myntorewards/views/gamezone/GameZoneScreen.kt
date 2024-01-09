package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.*
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.TAB_ACTIVE_GAMES
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.TAB_EXPIRED_GAMES
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants.Companion.TAB_PLAYED_GAMES
import com.salesforce.loyalty.mobile.myntorewards.utilities.Common
import com.salesforce.loyalty.mobile.myntorewards.utilities.TestTags.Companion.TEST_TAG_GAME_ZONE_SCREEN
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.GameViewModel
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GamesViewState
import com.salesforce.loyalty.mobile.myntorewards.views.components.EmptyView
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.CheckOutFlowScreen
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.GameZoneTabs
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GameZoneScreen(navController: NavHostController, gameViewModel: GameViewModel) {
    val games by gameViewModel.gamesLiveData.observeAsState()
    val gameViewState by gameViewModel.gamesViewState.observeAsState()
    var isInProgress by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()
    fun refresh() = refreshScope.launch {
        gameViewModel.getGames(context, mock = false)
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)
    LaunchedEffect(true) {
        gameViewModel.getGames(context, mock = false)
    }

    var openBottomsheet by remember { mutableStateOf(false) }
    var playedGameRewardType by remember { mutableStateOf("") }
    var playedGameRewardValue by remember { mutableStateOf("") }

    val bottomSheetScaffoldState = androidx.compose.material.rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed,
            confirmValueChange = {
                // Prevent collapsing by swipe down gesture
                it != BottomSheetValue.Collapsed
            })
    )

    val coroutineScope = rememberCoroutineScope()
    val openBottomSheet = {
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }

    if (openBottomsheet) {
        openBottomSheet()
    }
    val closeBottomSheet = {
        //showBottomBar(true)
        //blurBG = AppConstants.NO_BLUR_BG // need confirmation if background blur is needed
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
    }
    androidx.compose.material.BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,

        sheetContent = {
            Spacer(modifier = Modifier.height(1.dp))
            PlayedGamePopup(playedGameRewardType, playedGameRewardValue, textButtonClicked = {
                navController.navigate(
                    CheckOutFlowScreen.VoucherFullScreen.route
                )
            },
                crossButtonClicked = {
                    openBottomsheet = false
                    closeBottomSheet()
                }, backClicked = {
                    openBottomsheet = false
                    closeBottomSheet()
                }
            )
        },
        sheetShape = RoundedCornerShape(
            AppConstants.POPUP_ROUNDED_CORNER_SIZE,
            AppConstants.POPUP_ROUNDED_CORNER_SIZE,
            0.dp,
            0.dp
        ),
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .background(TextPurpleLightBG)
                .fillMaxSize()
                .testTag(TEST_TAG_GAME_ZONE_SCREEN)
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
                        .pullRefresh(state)
                        .background(VeryLightPurple)
                ) {
                    var selectedTab by remember { mutableStateOf(0) }
                    Row(modifier = Modifier.background(Color.White)) {

                        val tabItems =
                            listOf(
                                GameZoneTabs.ActiveGames,
                                GameZoneTabs.PlayedGames,
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
                                it.participantGameRewards[0].status == ParticipantRewardStatus.YET_TO_REWARD.status
                            }
                            val playedGames = games?.gameDefinitions?.filter {
                                ( it.participantGameRewards[0].status == ParticipantRewardStatus.REWARDED.status ||  it.participantGameRewards[0].status == ParticipantRewardStatus.NO_REWARD.status ) && it.participantGameRewards[0].gameRewardId != null
                            }


                            if(playedGames!=null  && selectedTab== TAB_PLAYED_GAMES) {

                                if(playedGames.isNotEmpty()){
                                    Text(
                                        text = stringResource(id = R.string.text_played_90_days),
                                        fontFamily = font_sf_pro,
                                        textAlign = TextAlign.Start,
                                        fontSize = 12.sp,
                                        color = LightBlack,
                                        modifier = Modifier
                                            .padding(top = 10.dp, start = 16.dp)
                                    )
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        modifier = Modifier
                                            .background(VeryLightPurple)
                                            .padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                                bottom = 16.dp,
                                                top = 10.dp
                                            )
                                            .fillMaxWidth()
                                            .fillMaxHeight(),
                                        verticalArrangement = Arrangement.spacedBy(16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        items(playedGames.size) { playedGameItem ->
                                            val playedGame = playedGames[playedGameItem]
                                            val gameType =
                                                if (GameType.SPIN_A_WHEEL.gameType == playedGame.type) {
                                                    GameType.SPIN_A_WHEEL
                                                } else {
                                                    GameType.SCRATCH_CARD
                                                }

                                           val gameReward= playedGame.gameRewards.filter{
                                                it.gameRewardId==playedGame.participantGameRewards[0].gameRewardId
                                            }.get(0).name.toString()

                                            GameView(
                                                true,
                                                gamePlayingStatus = stringResource(id = R.string.text_played),
                                                title = playedGame.name ?: "",
                                                gameType,
                                                gameReward
                                            ) {
                                                openBottomsheet = true
                                               playedGame.gameRewards.filter{
                                                   it.gameRewardId==playedGame.participantGameRewards[0].gameRewardId
                                               }[0].let {
                                                   playedGameRewardType= it.rewardType.toString()
                                                   playedGameRewardValue= it.rewardValue.toString()
                                              }
                                            }
                                        }
                                    }
                                }
                                    else {
                                        ShowEmptyView(selectedTab = selectedTab)
                                    }
                            }
                            if (notPlayedGames != null) {
                                val (activeGames, expiredGames) = notPlayedGames.partition {
                                    it.participantGameRewards[0].expirationDate.let { expDate ->
                                        (expDate == null) || (expDate?.let { date -> Common.isGameExpired(date) } == false)
                                    }
                                }

                                when (selectedTab) {
                                    TAB_ACTIVE_GAMES -> {
                                        if (activeGames.isNotEmpty()) {
                                            LazyVerticalGrid(
                                                columns = GridCells.Fixed(2),
                                                modifier = Modifier
                                                    .background(VeryLightPurple)
                                                    .padding(
                                                        start = 16.dp,
                                                        end = 16.dp,
                                                        bottom = 16.dp,
                                                        top = 10.dp
                                                    )
                                                    .fillMaxWidth()
                                                    .fillMaxHeight(),
                                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                                            ) {
                                                items(activeGames.size) { activeItem ->
                                                    val activeGame = activeGames[activeItem]
                                                    val expirationDate =
                                                        activeGame.participantGameRewards[0].expirationDate
                                                    val expiryDetail =
                                                        Common.formatGameDateTime(
                                                            expirationDate,
                                                            context
                                                        )
                                                    val gameType =
                                                        if (GameType.SPIN_A_WHEEL.gameType == activeGame.type) {
                                                            GameType.SPIN_A_WHEEL
                                                        } else {
                                                            GameType.SCRATCH_CARD
                                                        }
                                                    GameView(
                                                        false,
                                                        gamePlayingStatus = expiryDetail,
                                                        title = activeGame.name ?: "",
                                                        gameType
                                                    ) {
                                                        val gamePartRewardId = activeGame.participantGameRewards.firstOrNull()?.gameParticipantRewardId

                                                        if (gameType == GameType.SPIN_A_WHEEL) {
                                                            navController.navigate(
                                                                MoreScreens.SpinWheelScreen.route + "?gameParticipantRewardId=$gamePartRewardId"
                                                            )
                                                        } else {
                                                            navController.navigate(MoreScreens.ScratchCardScreen.route + "?gameParticipantRewardId=$gamePartRewardId")
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

                                            Text(
                                                text = stringResource(id = R.string.text_expired_90_days),
                                                fontFamily = font_sf_pro,
                                                textAlign = TextAlign.Start,
                                                fontSize = 12.sp,
                                                color = LightBlack,
                                                modifier = Modifier
                                                    .padding(top = 10.dp, start = 16.dp)
                                            )
                                            LazyVerticalGrid(
                                                columns = GridCells.Fixed(2),
                                                modifier = Modifier
                                                    .background(VeryLightPurple)
                                                    .padding(
                                                        start = 16.dp,
                                                        end = 16.dp,
                                                        bottom = 16.dp,
                                                        top = 10.dp
                                                    )
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
                                                        gamePlayingStatus = stringResource(id = R.string.game_expired),
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
                            ShowEmptyView(selectedTab = selectedTab)
                            if (isInProgress) {
                                isInProgress = false

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
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize(0.1f)
                    )
                }
            }
            PullRefreshIndicator(refreshing, state)
        }
    }

}

@Composable
fun ShowEmptyView(selectedTab: Int){
    when (selectedTab) {
        TAB_ACTIVE_GAMES -> {
            EmptyView(
                header = stringResource(id = R.string.label_empty_active_games)
            )
        }
        TAB_EXPIRED_GAMES -> {
            EmptyView(
                header = stringResource(id = R.string.label_empty_expired_games)
            )
        }
        TAB_PLAYED_GAMES -> {
            EmptyView(
                header = stringResource(id = R.string.label_empty_played_games)
            )
        }
    }
}