package com.salesforce.loyalty.mobile.myntorewards.views.components.animation

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.res.ResourcesCompat
import com.salesforce.loyalty.mobile.MyNTORewards.R
import java.util.concurrent.TimeUnit

@Preview(showSystemUi = true)
@Composable
fun ConfettiAnimationView(
    modifier: Modifier = Modifier.fillMaxSize(),
    drawableIds: List<Int>? = null
) {
    lateinit var partySystems: List<PartySystem>

    /**
     * Particles to draw
     */
    val particles = remember { mutableStateOf(emptyList<Particle>()) }

    /**
     * Latest stored frameTimeMilliseconds
     */
    val frameTime = remember { mutableStateOf(0L) }

    /**
     * Area in which the particles are being drawn
     */
    val drawArea = remember { mutableStateOf(Rect()) }

    val drawables = drawableIdToDrawables(drawableIds = drawableIds)
    LaunchedEffect(Unit) {
        partySystems = confettiDrawables(drawables).map { PartySystem(it) }
        var running =true
        while (running) {
            withFrameMillis { frameMs ->
                // Calculate time between frames, fallback to 0 when previous frame doesn't exist
                val deltaMs = if (frameTime.value > 0) (frameMs - frameTime.value) else 0
                frameTime.value = frameMs

                particles.value = partySystems.map { particleSystem ->
                    val totalTimeRunning = System.currentTimeMillis() - particleSystem.createdAt
                    // Do not start particleSystem yet if totalTimeRunning is below delay
                    if (totalTimeRunning < particleSystem.party.delay) return@map listOf()
                    particleSystem.render(deltaMs.div(1000f), drawArea.value)
                }.flatten()
            }

            Handler(Looper.getMainLooper()).postDelayed({
                running= false
            }, 5000)
        }
    }

    Canvas(
        modifier = modifier
            .onGloballyPositioned {
                drawArea.value = Rect(0, 0, it.size.width, it.size.height)
            },
        onDraw = {
            particles.value.forEach { particle ->
                withTransform({
                    rotate(
                        degrees = particle.rotation,
                        pivot = Offset(
                            x = particle.location.x + (particle.width / 2),
                            y = particle.location.y + (particle.width / 2)
                        )
                    )
                    scale(
                        scaleX = particle.scaleX,
                        scaleY = 1f,
                        pivot = Offset(particle.location.x + (particle.width / 2), particle.location.y)
                    )
                }) {
                    particle.shape.draw(this, particle)
                }
            }
        }
    )
}

fun DrawableShape.draw(drawScope: DrawScope, particle: Particle) {
    drawScope.drawIntoCanvas {
        if (tint) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                drawable.colorFilter = BlendModeColorFilter(particle.alphaColor, BlendMode.SRC_IN)
            } else {
                drawable.setColorFilter(particle.alphaColor, PorterDuff.Mode.SRC_IN)
            }
        } else if (applyAlpha) {
            drawable.alpha = particle.alpha
        }

        val size = particle.width
        val height = (size * heightRatio).toInt()
        val top = ((size - height) / 2f).toInt()

        val x = particle.location.y.toInt()
        val y = particle.location.x.toInt()
        drawable.setBounds(y, top + x, size.toInt() + y, top + height + x)
        drawable.draw(it.nativeCanvas)
    }
}

fun confettiDrawables(drawables: List<Drawable>? = null): List<Party> {
    val drawableShapes = drawables?.map { DrawableShape(it) } ?: emptyList()
    return listOf(
        Party(
            speed = 25f,
            maxSpeed = 0f,
            damping = 0.95f,
            angle = 90,
            spread = 360,
            colors = listOf(0x03DAC5, 0xDD7A01, 0xCA9b8C, 0xFCC003, 0xBB86FC, 0x7526E3, 0x2E844A, 0xfce18a, 0xE28787, 0xDD7A01, 0xff726d, 0xf4306d, 0xb48def),
            emitter = EmitterConfig(duration = 2, TimeUnit.SECONDS).perSecond(800),
            position = Position.Relative(0.5, 0.03),
            shapes = drawableShapes,
            size = listOf(Size.SMALL, Size.MEDIUM, Size.LARGE),
            timeToLive = 3000
        )
    )
}

@Composable
fun drawableIdToDrawables(drawableIds: List<Int>? = null): List<Drawable> {
    val drawableIds = drawableIds ?: listOf(R.drawable.particle_1, R.drawable.particle_2, R.drawable.particle_3, R.drawable.particle_3)
    val resources = LocalContext.current.resources
    return drawableIds.map { ResourcesCompat.getDrawable(resources, it, null)!! }
}