package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.GameViewModelInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

data class SpinWheelState(
    internal val gameViewModel: GameViewModelInterface,
    internal val pieCount: Int,
    private val durationMillis: Int,
    private val delayMillis: Int,
    private val rotationPerSecond: Float,
    private val easing: Easing,
    private val startDegree: Float,
    private val resultDegree: Float? = null,
    internal val autoSpinDelay: Long? = null,
) {
    internal var rotation by mutableStateOf(Animatable(startDegree))
    private var spinAnimationState by mutableStateOf(SpinAnimationState.STOPPED)

    suspend fun animate(coroutineScope: CoroutineScope, onFinish: (pieIndex: Int) -> Unit = {}) {
        when(spinAnimationState) {
            SpinAnimationState.STOPPED -> {
                spin(coroutineScope, onFinish = onFinish)
            }
            SpinAnimationState.SPINNING -> {
                reset()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun spin(coroutineScope: CoroutineScope, onFinish: (pieIndex: Int) -> Unit = {}) {
        if(spinAnimationState == SpinAnimationState.STOPPED) {

            spinAnimationState = SpinAnimationState.SPINNING

            val randomRotationDegree = generateRandomRotationDegree(pieCount)
          /*  rotation.animateTo(
                targetValue = (360f * 4 * (6000 / 1000)),
                animationSpec = tween(
                    durationMillis = 2000,
                    delayMillis = delayMillis,
                    easing = LinearEasing
                )
            )*/


            coroutineScope.launch {
                rotation.snapTo(resultDegree ?:randomRotationDegree)
                rotation.animateTo(
                    targetValue = (360f * rotationPerSecond * (durationMillis / 1000)),
                    animationSpec = tween(
                        durationMillis = 24000,
                        delayMillis = delayMillis,
                        easing = easing
                    )
                )
            }


            val pieDegree = 360f / pieCount
            val quotient = (resultDegree ?: randomRotationDegree).toInt() / pieDegree.toInt()
            val resultIndex = pieCount - quotient - 1

            onFinish(resultIndex)
            gameViewModel.getGames(true)

            coroutineScope.launch {
                val result = gameViewModel.getGames(true)

                rotation.snapTo(resultDegree ?:randomRotationDegree)

                rotation.animateTo(
                    targetValue = (360f * rotationPerSecond * (durationMillis / 1000)) + (resultDegree ?: randomRotationDegree),
                    animationSpec = tween(
                        durationMillis = durationMillis,
                        delayMillis = delayMillis,
                        easing = easing
                    )
                )
                autoSpinDelay?.let {
                    delay(it)
                    spin(coroutineScope)
                }

//                result.onSuccess {
//                    Logger.d("getGames", "after delay")
//
//
//                }.onFailure {
//                    Log.d("Games", "API Result FAILURE: ${it}")
//                }
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

    private fun generateRandomRotationDegree(pieCount: Int): Float {

        val size= pieCount
        val factor = 360/size
        val stopAtWheelIndex= 2
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
    durationMillis: Int = 5000,
    delayMillis: Int = 0,
    rotationPerSecond: Float = 1f,
    easing: Easing = CubicBezierEasing(0.16f, 1f, 0.3f, 1f),
    startDegree: Float = 0f,
    resultDegree: Float? = null,
    autoSpinDelay: Long? = null
): SpinWheelState {
    return remember {
        SpinWheelState(
            gameViewModel,
            pieCount,
            durationMillis,
            delayMillis,
            rotationPerSecond,
            easing,
            startDegree,
            resultDegree,
            autoSpinDelay
        )
    }
}