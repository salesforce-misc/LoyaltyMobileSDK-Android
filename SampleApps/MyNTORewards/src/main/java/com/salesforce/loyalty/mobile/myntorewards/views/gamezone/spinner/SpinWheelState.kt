package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.GameViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.INITIAL_ROTATION_DURATION
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.ROTATION_DURATION
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.ROTATION_PER_SECOND
import com.salesforce.loyalty.mobile.myntorewards.views.gamezone.spinner.SpinnerConfiguration.Companion.START_DEGREE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

data class SpinWheelState(
    internal val gameViewModel: GameViewModelInterface,
    internal val pieCount: Int,
    private val durationMillis: Int,
    private val rotationPerSecond: Float,
    private val easing: Easing,
    private val startDegree: Float,
) {
    internal var rotation by mutableStateOf(Animatable(START_DEGREE))
    private var spinAnimationState by mutableStateOf(SpinAnimationState.STOPPED)

    suspend fun animate(
        coroutineScope: CoroutineScope,
        textList: MutableList<String>,
        onFinish: (pieIndex: Int) -> Unit = {}
    ) {
        when(spinAnimationState) {
            SpinAnimationState.STOPPED -> {

                spin(coroutineScope,textList, onFinish = onFinish)
            }
            SpinAnimationState.SPINNING -> {
                reset()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun spin(
        coroutineScope: CoroutineScope,
        textList: MutableList<String>,
        onFinish: (pieIndex: Int) -> Unit = {}
    ) {
        if(spinAnimationState == SpinAnimationState.STOPPED) {

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

        /*    val pieDegree = 360f / pieCount
            val quotient = (360f).toInt() / pieDegree.toInt()
            val resultIndex = pieCount - quotient - 1
            onFinish(resultIndex)*/

            gameViewModel.getGames(true)

            coroutineScope.launch {
                val result = gameViewModel.getGameRewardResult(true)
                result.onSuccess {
                    val reward: String? = it?.gameRewards?.get(0)?.name
                    val stopAtThisDegree = stopAtThisDegree(pieCount, textList, reward)
                    rotation.snapTo(stopAtThisDegree)
                    rotation.animateTo(
                        targetValue = (360f * rotationPerSecond * (durationMillis / 1000)) + (stopAtThisDegree),
                        animationSpec = tween(
                            durationMillis = durationMillis,
                            easing = easing
                        ), 10f
                    )
                }.onFailure {

                }



            }
            spinAnimationState = SpinAnimationState.STOPPED
        }
    }

    suspend fun reset() {
        if(spinAnimationState == SpinAnimationState.SPINNING) {

            rotation.snapTo(startDegree)

            spinAnimationState = SpinAnimationState.STOPPED
        }
    }

    private fun stopAtThisDegree(
        pieCount: Int,
        textList: MutableList<String>,
        reward: String?,
    ): Float {

        val size= pieCount
        val factor = 360/size
        val stopAtWheelIndex= textList.indexOf(reward)
        val returnFactor = 360/(size*2)+  ((size-1)-stopAtWheelIndex) * (360/size)
        Log.d("returnFactor",""+ returnFactor)
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
    durationMillis: Int = ROTATION_DURATION,
    rotationPerSecond: Float = ROTATION_PER_SECOND,
    easing: Easing = CubicBezierEasing(0.16f, 1f, 0.3f, 1f),
    startDegree:Float= START_DEGREE
): SpinWheelState {
    return remember {
        SpinWheelState(
            gameViewModel,
            pieCount,
            durationMillis,
            rotationPerSecond,
            easing,
            startDegree
        )
    }
}