package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.myntorewards.utilities.AppConstants
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.GameViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.GameNameIDDataModel
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.INITIAL_ROTATION_DURATION
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.ROTATION_DURATION
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.ROTATION_PER_SECOND
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.START_DEGREE
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import kotlinx.coroutines.*

data class SpinWheelState(
    internal val gameViewModel: GameViewModelInterface,
    internal val pieCount: Int,
    val gameParticipantRewardId:String,
    private val durationMillis: Int,
    private val rotationPerSecond: Float,
    private val easing: Easing,
    private val startDegree: Float,
) {
    internal var rotation by mutableStateOf(Animatable(START_DEGREE))
    private var spinAnimationState by mutableStateOf(SpinAnimationState.STOPPED)

    suspend fun animate(
        coroutineScope: CoroutineScope,
        rewardList: MutableList<GameNameIDDataModel>,
        navController: NavHostController,
        spinFailure: () -> Unit
    ) {
        when (spinAnimationState) {
            SpinAnimationState.STOPPED -> {

                spin(coroutineScope, rewardList, navController){
                    spinFailure()
                }
            }

            SpinAnimationState.SPINNING -> {
                reset()
            }
        }
    }

    fun spin(
        coroutineScope: CoroutineScope,
        rewardList: MutableList<GameNameIDDataModel>,
        navController: NavHostController,
        spinFailure: () -> Unit
    ) {
        if (spinAnimationState == SpinAnimationState.STOPPED) {

            spinAnimationState = SpinAnimationState.SPINNING
            coroutineScope.launch {
                rotation.snapTo(360f)
                rotation.animateTo(
                    targetValue = (360f * rotationPerSecond * (INITIAL_ROTATION_DURATION / 1000)),
                    animationSpec = tween(
                        durationMillis = INITIAL_ROTATION_DURATION,
                        easing = LinearOutSlowInEasing
                    )
                )
            }

            coroutineScope.launch {
                val result = gameViewModel.getGameRewardResult(gameParticipantRewardId,false)
                result.onSuccess {
                    val reward: String? = it?.gameRewards?.get(0)?.gameRewardId
                    val stopAtThisDegree = stopAtThisDegree(pieCount, rewardList, reward)
                    rotation.snapTo(stopAtThisDegree)
                    rotation.animateTo(
                        targetValue = (360f * rotationPerSecond * (durationMillis / 1000)) + (stopAtThisDegree),
                        animationSpec = tween(
                            durationMillis = durationMillis,
                            easing = easing
                        ), 10f
                    )
                    withContext(Dispatchers.Main) {
                        delay(3000)
                        it?.let {
                            val rewardType = it.gameRewards[0].rewardType
                            val rewardValue = it.gameRewards[0].rewardValue
                            if (RewardType.NO_VOUCHER.rewardType.equals(rewardType)) {
                                navController.navigate(MoreScreens.GameBetterLuckScreen.route)
                            } else {
                                navController.navigate(MoreScreens.GameCongratsScreen.route)

                                navController.currentBackStackEntry?.arguments?.putString(
                                    AppConstants.KEY_CONFIRMARION_SCREEN_REWARD_TYPE,rewardType
                                )
                                navController.currentBackStackEntry?.arguments?.putString(
                                    AppConstants.KEY_CONFIRMARION_SCREEN_REWARD_VALUE,rewardValue
                                )
                            }
                        }
                    }
                }.onFailure {
                    rotation.snapTo(360f)
                    spinFailure()
                }


            }
            spinAnimationState = SpinAnimationState.STOPPED
        }
    }

    suspend fun reset() {
        if (spinAnimationState == SpinAnimationState.SPINNING) {

            rotation.snapTo(startDegree)

            spinAnimationState = SpinAnimationState.STOPPED
        }
    }

    private fun stopAtThisDegree(
        pieCount: Int,
        rewardList: MutableList<GameNameIDDataModel>,
        reward: String?,
    ): Float {
        //360 degree devided by total element will give angel which will be there for every element.
        //for example if 4 elements then each element will have 360/4= 90 degree angle
        val eachSegmentOccupiedAngle = 360 / pieCount
        var stopAtWheelIndex= -1

        for(index in rewardList.indices) {
            if( rewardList[index].gameID==reward) {
                stopAtWheelIndex= index
                break
            }
        }

        //eachSegmentOccupiedAngle/2 will provide middle of segment to stop
        //for example if total 4 elements then each element will hold 90 degree.
        //we want top stop at 1st element (2nd member or array) then we we will need total angle should be
        // 45 degree(middle of element)+ 180 degree
        //45+ ((4-1)-1)*90= =45+ 180 degree rotation

        var returnFactor = 0
        if(stopAtWheelIndex!=-1){
            returnFactor = eachSegmentOccupiedAngle/2 + ((pieCount - 1) - stopAtWheelIndex) * eachSegmentOccupiedAngle
        }
        Log.d("returnFactor", "" + returnFactor)
        return returnFactor.toFloat()
    }
}

enum class SpinAnimationState {
    STOPPED, SPINNING
}

@Composable
fun rememberSpinWheelState(
    gameViewModel: GameViewModelInterface,
    pieCount: Int,
    gameParticipantRewardId: String,
    durationMillis: Int = ROTATION_DURATION,
    rotationPerSecond: Float = ROTATION_PER_SECOND,
    easing: Easing = CubicBezierEasing(0.16f, 1f, 0.3f, 1f),
    startDegree: Float = START_DEGREE

): SpinWheelState {
    return remember {
        SpinWheelState(
            gameViewModel,
            pieCount,
            gameParticipantRewardId,
            durationMillis,
            rotationPerSecond,
            easing,
            startDegree
        )
    }
}