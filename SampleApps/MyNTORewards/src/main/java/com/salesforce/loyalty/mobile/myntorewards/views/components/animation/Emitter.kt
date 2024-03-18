package com.salesforce.loyalty.mobile.myntorewards.views.components.animation

import java.util.concurrent.TimeUnit

class EmitterConfig(duration: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS) {
    /** Max time allowed to emit in milliseconds */
    var emittingTime: Long = 0

    /** Amount of time needed for each particle creation in milliseconds */
    var amountPerMs: Float = 0f

    init {
        this.emittingTime = TimeUnit.MILLISECONDS.convert(duration, timeUnit)
    }

    fun max(amount: Int): EmitterConfig {
        this.amountPerMs = (emittingTime / amount) / 1000f
        return this
    }

    fun perSecond(amount: Int): EmitterConfig {
        this.amountPerMs = 1f / amount
        return this
    }
}