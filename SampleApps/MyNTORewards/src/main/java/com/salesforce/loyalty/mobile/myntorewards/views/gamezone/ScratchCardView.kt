package com.salesforce.loyalty.mobile.myntorewards.views.gamezone


import android.util.Log
import android.view.MotionEvent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.salesforce.loyalty.mobile.MyNTORewards.R
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.ScratchCardPerforationColor
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.TextPurpleLightBG
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.VibrantPurple40
import com.salesforce.loyalty.mobile.myntorewards.ui.theme.font_sf_pro
import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
import com.salesforce.loyalty.mobile.sources.loyaltyModels.MemberBenefit
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class DraggedPath(
    val path: Path,
    val width: Float = 50f
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScratchCardView(loyaltyAPIManager: LoyaltyAPIManager) {
    val overlayImage = ImageBitmap.imageResource(id = R.drawable.overlay_img)
    val baseImage = ImageBitmap.imageResource(id = R.drawable.onboarding_image_2)

    val currentPathState = remember { mutableStateOf(DraggedPath(path = Path())) }
    val movedOffsetState = remember { mutableStateOf<Offset?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScratchCardPerforationColor)
    ) {
        IconButton(
            onClick = {
                movedOffsetState.value = null
                currentPathState.value = DraggedPath(path = Path())
            },
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Icon(
                imageVector = Icons.Default.Clear, contentDescription = "Clear",
                tint = MaterialTheme.colors.onPrimary
            )
        }

        // Scratch Card Implementation
        ScratchingCanvas(
            overlayImage = overlayImage,
            baseImage = baseImage,
            modifier = Modifier.align(Alignment.Center),
            movedOffset = movedOffsetState.value,
            onMovedOffset = { x, y ->
                movedOffsetState.value = Offset(x, y)
            },
            currentPath = currentPathState.value.path,
            currentPathThickness = currentPathState.value.width,
            loyaltyAPIManager = loyaltyAPIManager
        )
    }
}

@ExperimentalComposeUiApi
@Composable
fun ScratchingCanvas(
    overlayImage: ImageBitmap,
    baseImage: ImageBitmap,
    modifier: Modifier = Modifier,
    movedOffset: Offset?,
    onMovedOffset: (Float, Float) -> Unit,
    currentPath: Path,
    currentPathThickness: Float,
    loyaltyAPIManager: LoyaltyAPIManager
) {
    val textMeasurer = rememberTextMeasurer()
    val scratchedSize = remember { mutableStateOf<Float?>(0f) }
    var animate by remember { mutableStateOf(false) }
    val stroke = Stroke(
        width = 5f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    val coroutineScope = rememberCoroutineScope()

    var apiCalled by remember {
        mutableStateOf(false)
    }

    val rewardTextMutableLiveData = MutableLiveData<String>()
//    val rewardTextLiveData: LiveData<String> = rewardTextMutableLiveData

    rewardTextMutableLiveData.value = "Loading..."

    val rewardTextValue by rewardTextMutableLiveData.observeAsState()

    AnimatedContent(targetState = animate, modifier = modifier) {
        Box(
            modifier = modifier
                .height(199.dp)
                .width(343.dp)
                .background(VibrantPurple40)
                .drawBehind {
//                drawRoundRect(color = Color.White, style = stroke)
                    /*val borderSize = 4.dp.toPx()
                    drawLine(
                        color = Color.Red,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = borderSize

                    )*/
                    val step = 32.dp
                    val stepPx = step.toPx()
//                val stepsCount = (size.width / stepPx).roundToInt()
//                val actualStep = size.width / stepsCount
//                val dotSize = Size(width = actualStep / 2, height = actualStep / 2)
                    val dotSize = Size(width = 16.dp.toPx(), height = 16.dp.toPx())
                    val spacing = 8.dp.toPx()
                    val stepsCount = (size.width / (dotSize.width + spacing)).roundToInt()
                    val actualStep = dotSize.width + spacing
                    for (i in 1..(stepsCount - 1)) {
                        val rect = Rect(
                            offset = Offset(
                                x = i * actualStep,
                                y = (size.height - (dotSize.height / 2))
                            ),
                            size = dotSize,
                        )
                        /*drawRect(
                            ScratchCardPerforationColor,
                            topLeft = rect.topLeft,
                            size = rect.size,
                        )*/
                        drawOval(
                            color = ScratchCardPerforationColor, topLeft = rect.topLeft,
                            size = rect.size
                        )

                    }
                    for (i in 1..(stepsCount - 1)) {
                        val rect = Rect(
                            offset = Offset(x = i * actualStep, y = (-dotSize.height / 2)),
                            size = dotSize,
                        )
                        /*drawRect(
                            ScratchCardPerforationColor,
                            topLeft = rect.topLeft,
                            size = rect.size,
                        )*/
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
//            .border(width = 20.dp, color = VibrantPurple40, shape = RoundedCornerShape(16.dp))
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                println("CurrentPath/ACTION_DOWN: (${it.x}, ${it.y})")
                                currentPath.moveTo(it.x, it.y)
                            }
                            MotionEvent.ACTION_MOVE -> {
                                println("MovedOffset/ACTION_MOVE: (${it.x}, ${it.y})")
                                onMovedOffset(it.x, it.y)
                            }
                        }
                        true
                    }
            ) {
                val canvasWidth = size.width.toInt()
                val canvasHeight = size.height.toInt()
                val imageSize = IntSize(width = canvasWidth, height = canvasHeight)
                val canvasSize = size.width * size.height
//        var scratchedSize: Float = 0f
                // Overlay Image to be scratched
                drawImage(
                    image = overlayImage,
                    dstSize = imageSize
                )
                Log.d("Canvas", "canvasSize $canvasSize")
                movedOffset?.let {
//            currentPath.addOval(oval = Rect(it, currentPathThickness))
                    val rectangle = Rect(it, currentPathThickness)
                    currentPath.apply {
                        this.addRect(rect = Rect(it, currentPathThickness))
                        /*scratchedSize.value =
                    scratchedSize.value?.plus((rectangle.width * rectangle.height))*/
//                Log.d("Canvas", "scratchedSize ${scratchedSize.value}")
                    }

                    /*drawOval(
                topLeft = it,
                brush = SolidColor(VibrantPurple40),
                style = Stroke(width = currentPathThickness)
            )*/
                }
/*
        if(scratchedSize.value!! >= canvasSize){
            Log.d("Canvas", "scratchedSize is bigger!!!!!!!!!!!!!!!!!!!!")
        }*/
                val rect: Rect? = currentPath?.getBounds()
                rect?.let {
                    val scratched = rect.width * rect.height
                    //First time scratched, call the API
//                    LaunchedEffect(key1 = true ) {
                    if (!apiCalled && (scratched > 0f)) {
                        apiCalled = true
                        coroutineScope.launch {
                            val result = loyaltyAPIManager.getGameReward(true)
                            result.onSuccess {
                                Log.d("Canvas", "API Result SUCCESS: ${it}")
                                val reward: String? =
                                    it?.gameRewards?.get(0)?.description
                                reward?.let {
                                    Log.d("Canvas", "API Result SUCCESS LIVEDATA SET: ${it}")
                                    rewardTextMutableLiveData.postValue(it)
                                    rewardTextMutableLiveData.value = it
                                }
                            }.onFailure {
                                Log.d("Canvas", "API Result FAILURE: ${it}")
                            }
                        }

//                        }

                    }

                    Log.d("Canvas", "Bounds of current path: ${rect.width * rect.height}")

                    if (scratched >= canvasSize) {
                        currentPath.moveTo(size.width, size.height)
                        animate = true
                        currentPath.addRect(
                            rect = Rect(
                                Offset(0f, 0f),
                                (size.width * size.height)
                            )
                        )

                    }
                }
                rewardTextValue?.let {
                    Log.d("Canvas", "rewardTextValue: $rewardTextValue")
                val measuredText =
                    textMeasurer.measure(
                        AnnotatedString(rewardTextValue.toString()),
                        constraints = Constraints.fixed(
                            (size.width).toInt(),
                            (size.height).toInt()
                        ),
                        style = TextStyle(
                            fontSize = 36.sp,
                            color = Color.White,
                            fontFamily = font_sf_pro,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    )
                clipPath(path = currentPath, clipOp = ClipOp.Intersect) {
                    // Base Image after scratching
                    /*drawImage(
                image = baseImage,
                dstSize = imageSize
            )*/
                    drawRect(VibrantPurple40, size = imageSize.toSize())
                    drawText(measuredText, topLeft = Offset(x = 0.dp.toPx(), y = 30.dp.toPx()))
                }
            }

            }
        }
    }
}