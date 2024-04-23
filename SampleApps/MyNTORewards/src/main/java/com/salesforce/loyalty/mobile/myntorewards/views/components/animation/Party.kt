package com.salesforce.loyalty.mobile.myntorewards.views.components.animation

import android.graphics.drawable.Drawable

data class Party(
    val angle: Int = 0,
    val spread: Int = 360,
    val speed: Float = 30f,
    val maxSpeed: Float = 0f,
    val damping: Float = 0.9f,
    val size: List<Size> = listOf(Size.SMALL, Size.MEDIUM, Size.LARGE),
    val colors: List<Int> = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
    val shapes: List<DrawableShape> = emptyList(),
    val timeToLive: Long = 2000,
    val fadeOutEnabled: Boolean = true,
    val position: Position = Position.Relative(0.5, 0.5),
    val delay: Int = 0,
    val rotation: Rotation = Rotation(),
    val emitter: EmitterConfig
)

sealed class Position {
    data class Absolute(val x: Float, val y: Float) : Position() {
        fun between(value: Absolute): Position = between(this, value)
    }

    data class Relative(val x: Double, val y: Double) : Position() {
        fun between(value: Relative): Position = between(this, value)
    }

    internal data class between(val min: Position, val max: Position) : Position()
}

data class Rotation(
    val enabled: Boolean = true,
    val speed: Float = 0.3f,
    val variance: Float = 0.5f,
    val multiplier2D: Float = 8f,
    val multiplier3D: Float = 0f
)

data class Size(val sizeInDp: Int, val mass: Float = 5f, val massVariance: Float = 0.2f) {
    init {
        require(mass != 0F) { "mass=$mass must be != 0" }
    }

    companion object {
        val SMALL: Size = Size(sizeInDp = 12, mass = 6f)
        val MEDIUM: Size = Size(sizeInDp = 8, mass = 8f)
        val LARGE: Size = Size(sizeInDp = 16, mass = 20f)
    }
}

data class Vector(var x: Float = 0f, var y: Float = 0f) {
    fun add(v: Vector) {
        x += v.x
        y += v.y
    }

    fun addScaled(v: Vector, s: Float) {
        x += v.x * s
        y += v.y * s
    }

    fun multi(n: Float) {
        x *= n
        y *= n
    }

    fun div(n: Float) {
        x /= n
        y /= n
    }
}

data class DrawableShape(
    val drawable: Drawable,
    val tint: Boolean = true,
    val applyAlpha: Boolean = true,
) {
    val heightRatio =
        if (drawable.intrinsicHeight == -1 && drawable.intrinsicWidth == -1) {
            // If the drawable has no intrinsic size, fill the available space.
            1f
        } else if (drawable.intrinsicHeight == -1 || drawable.intrinsicWidth == -1) {
            // Currently cannot handle a drawable with only one intrinsic dimension.
            0f
        } else {
            drawable.intrinsicHeight.toFloat() / drawable.intrinsicWidth
        }
}