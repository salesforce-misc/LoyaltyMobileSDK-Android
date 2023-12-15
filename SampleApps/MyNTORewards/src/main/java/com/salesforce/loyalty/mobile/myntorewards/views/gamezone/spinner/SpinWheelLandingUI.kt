package com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.SpinnerDefaultColour
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.GameViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.Wheel
import com.salesforce.loyalty.mobile.sources.loyaltyModels.GameReward
import kotlinx.coroutines.launch

data class GameNameIDDataModel(
    val game: String,
    val gameID: String?
)

@Composable
fun SpinWheelLandingPage(
    navController: NavHostController,
    gameViewModel: GameViewModelInterface,
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