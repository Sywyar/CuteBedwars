package io.github.gdrfgdrf.cutebedwars.abstracts.particles

import io.github.gdrfgdrf.multimodulemediator.annotation.KotlinSingleton
import io.github.gdrfgdrf.multimodulemediator.annotation.Service
import org.bukkit.Particle

@Service("particles")
@KotlinSingleton
interface IParticles {
    fun getOrCreate(particle: Particle): IManagedParticle
}