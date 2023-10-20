package com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.AnimatedSpinWheel
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.DefaultSpinWheelColors
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.DefaultSpinWheelDimensions
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.rememberSpinWheelState
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import kotlinx.coroutines.CoroutineScope
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
        }
    }



}

@Composable
fun Wheel(loyaltyAPIManager:LoyaltyAPIManager, gamesList: MutableList<String>, colourList: MutableList<Color>)
{
    val state = rememberSpinWheelState(loyaltyAPIManager, gamesList.size)
    val scope = rememberCoroutineScope()
    Column {
        val textList by remember { mutableStateOf(gamesList) }
        val frameColor = Color(0xFFFFFFFF)
        val dividerColor = Color.White
        val selectorColor = Color(0xFFFF0000)

       val defaultColours=  DefaultSpinWheelColors(
            frameColor = frameColor,
            dividerColor = dividerColor,
            selectorColor = selectorColor,
            pieColors = colourList
        )
        val defaultDimensions=  DefaultSpinWheelDimensions(
            spinWheelSize = 296.dp,
            frameWidth = 4.dp,
            selectorWidth = 12.dp,
        )
        var coroutineScope: CoroutineScope = rememberCoroutineScope()

        AnimatedSpinWheel(

            state = state,
            size = defaultDimensions.spinWheelSize().value,
            frameWidth = defaultDimensions.frameWidth().value,
            selectorWidth = defaultDimensions.selectorWidth().value,
            pieColors = defaultColours.pieColors().value,
            onClick = { scope.launch { state.animate(coroutineScope) {pieIndex -> } } },
            wheelData=textList
        )
    }
}