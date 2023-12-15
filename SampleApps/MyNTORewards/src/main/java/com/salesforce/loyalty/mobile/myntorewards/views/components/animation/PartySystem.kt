package com.salesforce.loyalty.mobile.myntorewards.views.components.animation

import android.content.res.Resources
import android.graphics.Rect

/**
 * PartySystem is responsible for requesting particles from the emitter and updating the particles
 * everytime a new frame is requested.
 * @param party configuration class with instructions on how to create the particles for the Emitter
 * @param createdAt timestamp of when the partySystem is created
 * @param pixelDensity default value taken from resources to measure based on pixelDensity
 */
class PartySystem(
    val party: Party,
    val createdAt: Long = System.currentTimeMillis(),
    pixelDensity: Float = Resources.getSystem().displayMetrics.density
) {
    private var emitter = PartyEmitter(party.emitter, pixelDensity)

    private val activeParticles = mutableListOf<Particle>()

    // Called every frame to create and update the particles state
    // returns a list of particles that are ready to be rendered
    fun render(deltaTime: Float, drawArea: Rect): List<Particle> {
        activeParticles.addAll(emitter.createConfetti(deltaTime, party, drawArea))
        activeParticles.forEach { it.render(deltaTime, drawArea) }
        activeParticles.removeAll { it.isDead() }
        return activeParticles.filter { it.drawParticle }
    }

}