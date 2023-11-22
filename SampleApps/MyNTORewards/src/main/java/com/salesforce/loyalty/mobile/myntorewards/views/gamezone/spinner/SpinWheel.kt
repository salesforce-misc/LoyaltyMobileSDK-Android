package com.salesforce.loyalty.mobile.myntorewards.views.gamezone

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

interface SpinWheelColors {
    @Composable
    fun frameColor(): State<Color>
    @Composable
    fun dividerColor(): State<Color>
    @Composable
    fun selectorColor(): State<Color>
    @Composable
    fun pieColors(): State<List<Color>>
}

@Immutable
public class DefaultSpinWheelColors(
    private val frameColor: Color,
    private val dividerColor: Color,
    private val selectorColor: Color,
    private val pieColors: List<Color>
) : SpinWheelColors {

    @Composable
    override fun frameColor(): State<Color> {
        return rememberUpdatedState(frameColor)
    }

    @Composable
    override fun dividerColor(): State<Color> {
        return rememberUpdatedState(dividerColor)
    }

    @Composable
    override fun selectorColor(): State<Color> {
        return rememberUpdatedState(selectorColor)
    }

    @Composable
    override fun pieColors(): State<List<Color>> {
        return rememberUpdatedState(pieColors)
    }
}

interface SpinWheelDimensions {
    @Composable
    fun spinWheelSize(): State<Dp>
    @Composable
    fun frameWidth(): State<Dp>
    @Composable
    fun selectorWidth(): State<Dp>
}

@Immutable
class DefaultSpinWheelDimensions(
    private val spinWheelSize: Dp,
    private val frameWidth: Dp,
    private val selectorWidth: Dp
) : SpinWheelDimensions {

    @Composable
    override fun spinWheelSize(): State<Dp> {
        return rememberUpdatedState(spinWheelSize)
    }

    @Composable
    override fun frameWidth(): State<Dp> {
        return rememberUpdatedState(frameWidth)
    }

    @Composable
    override fun selectorWidth(): State<Dp> {
        return rememberUpdatedState(selectorWidth)
    }
}