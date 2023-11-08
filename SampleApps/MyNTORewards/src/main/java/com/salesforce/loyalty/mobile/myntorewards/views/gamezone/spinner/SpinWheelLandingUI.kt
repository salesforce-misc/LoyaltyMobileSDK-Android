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
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.Wheel
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import kotlinx.coroutines.launch

@Composable
fun SpinWheelLandingPage(loyaltyAPIManager: LoyaltyAPIManager) {

    var wheelValuesLoaded by remember { mutableStateOf(false) }
    Box(contentAlignment = Alignment.TopCenter) {
        val gamesList= remember {
            mutableListOf<String>()
        }
        val colourList= remember {
            mutableListOf<Color>()
        }

        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(true) {
            coroutineScope.launch {
                val result = loyaltyAPIManager.getGames(true)
                result.onSuccess { it ->
                    Log.d("Games", "API Result SUCCESS: ${it}")
                    val games = it.gameDefinitions.get(0).gameReward.eligibleRewards
                    games.let {
                        for(item in it)
                        {
                            gamesList.add(item.name)
                            colourList.add(Color(("#"+item.seg_color).toColorInt()))
                        }
                        wheelValuesLoaded= true
                    }
                }.onFailure {
                    Log.d("Games", "API Result FAILURE: ${it}")
                }
            }
        }
        if(wheelValuesLoaded)
        {
            Wheel(loyaltyAPIManager, gamesList, colourList)
        } else{
            Box(modifier = Modifier.fillMaxSize()){
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize(0.1f)
                        .align(Alignment.Center)
                )            }
        }
    }
}
