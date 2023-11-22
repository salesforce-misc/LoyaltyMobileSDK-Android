package com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.GameViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.Wheel
import kotlinx.coroutines.launch

data class GameNameIDDataModel(
    val game: String,
    val gameID: String
)

@Composable
fun SpinWheelLandingPage(navController: NavHostController, gameViewModel: GameViewModelInterface) {

    var wheelValuesLoaded by remember { mutableStateOf(false) }
    val games by gameViewModel.gamesLiveData.observeAsState()

    Box(contentAlignment = Alignment.TopCenter) {
        val gamesList = remember {
            mutableListOf<GameNameIDDataModel>()
        }
        val colourList = remember {
            mutableListOf<Color>()
        }

        val coroutineScope = rememberCoroutineScope()

        gameViewModel.getGames(true)

        LaunchedEffect(true) {
            coroutineScope.launch {

                games?.let {
                    val gamesDate = it.gameDefinitions.get(0).gameRewards
                    gamesDate.let { gameReward ->
                        for (item in gameReward) {
                            item.name?.let { name -> gamesList.add(GameNameIDDataModel(name, item.gameRewardId))}
                            colourList.add(Color(("#" + item.segColor).toColorInt()))
                        }
                        wheelValuesLoaded = true
                    }
                }
            }
        }
        if (wheelValuesLoaded) {
            Wheel(navController, gameViewModel, gamesList, colourList)
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
