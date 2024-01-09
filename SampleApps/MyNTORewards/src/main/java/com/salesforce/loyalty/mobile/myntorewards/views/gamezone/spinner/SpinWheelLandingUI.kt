package com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import com.salesforce.gamification.model.GameReward
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.SpinnerDefaultColour
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.GameViewModel
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.Wheel
import kotlinx.coroutines.launch

data class GameNameIDDataModel(
    val game: String,
    val gameID: String?
)

@Composable
fun SpinWheelLandingPage(
    navController: NavHostController,
    gameViewModel: GameViewModel,
    gameParticipantRewardId: String
) {

    var wheelValuesLoaded by remember { mutableStateOf(false) }
    var gameRewards: List<GameReward> = mutableListOf()
    LaunchedEffect(key1 = true) {
        gameRewards =
            gameViewModel.getGameRewardsFromGameParticipantRewardId(gameParticipantRewardId)
    }
    Box(contentAlignment = Alignment.TopCenter) {
        val gamesList = remember {
            mutableListOf<GameNameIDDataModel>()
        }
        val colourList = remember {
            mutableListOf<Color>()
        }

        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(true) {
            coroutineScope.launch {
                if(gameRewards.isNotEmpty())
                {
                    for (reward in gameRewards) {
                        reward.name?.let { name -> gamesList.add(GameNameIDDataModel(name, reward.gameRewardId))}
                        if(reward.segColor?.contains("#") == false){
                            colourList.add(Color(("#" + reward.segColor).toColorInt()))
                        }
                        else{
                            colourList.add(Color(("" + (reward.segColor?:SpinnerDefaultColour)).toColorInt()))
                        }
                    }
                }
                wheelValuesLoaded = true
            }
        }
        if (wheelValuesLoaded) {
            Wheel(navController, gameViewModel, gamesList, colourList, gameParticipantRewardId)
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize(0.1f)
                        .align(Alignment.Center)
                )
            }
        }
    }
}