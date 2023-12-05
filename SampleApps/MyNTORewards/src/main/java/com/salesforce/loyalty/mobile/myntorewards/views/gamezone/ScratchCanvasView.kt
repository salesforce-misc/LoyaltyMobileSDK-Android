package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.ScratchCardPerforationColor
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.blueprint.GameViewModelInterface
import com.salesforce.loyalty.mobile.myntorewards.viewmodels.viewStates.GameRewardViewState
import com.salesforce.loyalty.mobile.myntorewards.views.components.ShowErrorDialog
import com.salesforce.loyalty.mobile.myntorewards.views.navigation.MoreScreens
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@ExperimentalComposeUiApi
@Composable
fun CanvasForScratching(
    overlay: ImageBitmap,
    modifier: Modifier = Modifier,
    cursorMovedOffset: Offset?,
    onCursorMovedOffset: (Float, Float) -> Unit,
    path: Path,
    scratchThickness: Float,
    gameViewModel: GameViewModelInterface,
    navController: NavHostController,
    gameParticipantRewardId: String
) {
    val textMeasurer = rememberTextMeasurer()
    var animate by remember { mutableStateOf(false) }

    var apiCalled by remember {
        mutableStateOf(false)
    }

    var isFirstTime by remember { mutableStateOf(false) }

    var isInProgress by remember { mutableStateOf(false) }

    // Initialized the reward text with a placeholder for now. Will replace it with CX approved text once that is available.
    var rewardTextValue by remember { mutableStateOf("Loading...") }

    val gameRewardViewState by gameViewModel.gameRewardsViewState.observeAsState()

    val rewardLiveDataValue by gameViewModel.rewardLiveData.observeAsState()

    val alpha: Float by animateFloatAsState(targetValue = if (animate) 0.0f else 1f,
        animationSpec = tween(durationMillis = 700, easing = LinearEasing))

    var openAlertDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(199.dp)
            .width(343.dp)
            .background(VibrantPurple40)
            .drawBehind {
                val dotSize = Size(width = 16.dp.toPx(), height = 16.dp.toPx())
                val spacing = 8.dp.toPx()
                val stepsCount = (size.width / (dotSize.width + spacing)).roundToInt()
                val actualStep = dotSize.width + spacing
                // Bottom border for scratch card
                for (i in 1..(stepsCount - 1)) {
                    val rect = Rect(
                        offset = Offset(
                            x = i * actualStep,
                            y = (size.height - (dotSize.height / 2))
                        ),
                        size = dotSize,
                    )
                    drawOval(
                        color = ScratchCardPerforationColor, topLeft = rect.topLeft,
                        size = rect.size
                    )

                }
                // Top border for scratch card
                for (i in 1..(stepsCount - 1)) {
                    val rect = Rect(
                        offset = Offset(x = i * actualStep, y = (-dotSize.height / 2)),
                        size = dotSize,
                    )
                    drawOval(
                        color = ScratchCardPerforationColor, topLeft = rect.topLeft,
                        size = rect.size
                    )
                }
            },
    ) {
        Canvas(
            modifier = modifier
                .width(289.dp)
                .height(115.dp)
                .clipToBounds()
                .clip(RoundedCornerShape(16.dp))
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            path.moveTo(it.x, it.y)
                        }
                        MotionEvent.ACTION_MOVE -> {
                            onCursorMovedOffset(it.x, it.y)
                        }
                    }
                    true
                }
        ) {
            val canvasWidth = size.width.toInt()
            val canvasHeight = size.height.toInt()
            val imageSize = IntSize(width = canvasWidth, height = canvasHeight)
            val canvasSize = size.width * size.height
            // Overlay Image to be scratched
            drawImage(
                image = overlay,
                dstSize = imageSize,
                alpha = alpha
            )
            cursorMovedOffset?.let {
                path.apply {
                    this.addRect(rect = Rect(it, scratchThickness))
                }
            }

            val rect: Rect? = path?.getBounds()
            rect?.let {
                val scratched = rect.width * rect.height
                //First time scratched, call the API
                if (!isFirstTime && (scratched > 0f)) {
                    isFirstTime = true
                }

                if ((scratched >= canvasSize) || animate) {
                    path.moveTo(size.width, size.height)
                    /*path.addRect(
                        rect = Rect(
                            Offset(0f, 0f),
                            (size.width * size.height)
                        )
                    )*/

                }
            }
            val measuredText =
                textMeasurer.measure(
                    AnnotatedString(rewardTextValue),
                    constraints = Constraints.fixed(
                        (size.width).toInt(),
                        (size.height).toInt()
                    ),
                    style = TextStyle(
                        fontSize = 32.sp,
                        color = Color.White,
                        fontFamily = font_sf_pro,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            if (animate) {
                drawText(measuredText, topLeft = Offset(x = 0.dp.toPx(), y = 30.dp.toPx()))
            } else {
                clipPath(path = path, clipOp = ClipOp.Intersect) {
                    drawRect(
                        VibrantPurple40,
                        size = imageSize.toSize()
                    )
                    drawText(
                        measuredText,
                        topLeft = Offset(x = 0.dp.toPx(), y = 30.dp.toPx())
                    )
                }
            }
        }
    }
    if (isFirstTime && !apiCalled) {
        LaunchedEffect(true) {
            apiCalled = true
            gameViewModel.getGameReward(gameParticipantRewardId, false)
        }
    }
    when (gameRewardViewState) {
        GameRewardViewState.GameRewardFetchSuccess -> {
            if (isInProgress) {
                isInProgress = false
                rewardLiveDataValue?.let {
                    val reward: String? =
                        it?.gameRewards?.get(0)?.name
                    if (reward != null) {
                        rewardTextValue = reward
                    }
                }
                animate = true
            }
        }
        GameRewardViewState.GameRewardFetchInProgress -> {
            isInProgress = true
        }
        GameRewardViewState.GameRewardFetchFailure -> {
            if (isInProgress) {
                isInProgress = false
                openAlertDialog = true
            }
        }
        else -> {}
    }
    if (animate) {
        LaunchedEffect(key1 = true) {
            delay(3000)
            rewardLiveDataValue?.let {
                val rewardType = it.gameRewards?.get(0)?.rewardType
                if (RewardType.NO_VOUCHER.rewardType.equals(rewardType)) {
                    navController.navigate(MoreScreens.GameBetterLuckScreen.route)
                } else {
                    navController.navigate(MoreScreens.GameCongratsScreen.route)
                }
            }
        }
    }
    if (openAlertDialog) {
        ShowErrorDialog(
            onDismiss = {
                openAlertDialog = false
                navController.popBackStack()
            },
            title = stringResource(id = R.string.game_error),
            description = stringResource(
                id = R.string.game_error_desc
            ),
            confirmButtonText = stringResource(id = R.string.game_error_dialog_ok),
            confirmButtonClick = {
                openAlertDialog = false
                navController.popBackStack()
            })
    }
}