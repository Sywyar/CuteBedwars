package io.github.gdrfgdrf.cutebedwars.particles

import com.github.fierioziy.particlenativeapi.api.particle.type.ParticleType
import com.github.fierioziy.particlenativeapi.api.particle.type.ParticleTypeMotion
import io.github.gdrfgdrf.cutebedwars.abstracts.enums.IParticleStatuses
import io.github.gdrfgdrf.cutebedwars.abstracts.particles.IManagedParticle
import io.github.gdrfgdrf.cutebedwars.abstracts.particles.IParticleGroup
import io.github.gdrfgdrf.cutebedwars.abstracts.particles.IParticleInfo
import io.github.gdrfgdrf.cutebedwars.abstracts.utils.IStopSignal
import io.github.gdrfgdrf.cutebedwars.abstracts.utils.frequencyTask
import io.github.gdrfgdrf.cutebedwars.abstracts.utils.particle
import io.github.gdrfgdrf.cutebedwars.beans.pojo.common.Coordinate
import org.bukkit.World
import org.bukkit.entity.Player

class ParticleGroup private constructor(
    override val name: String,
    override val parent: IManagedParticle
) : IParticleGroup {
    private var status = IParticleStatuses.valueOf("DEFAULT")
    private val list = arrayListOf<IParticleInfo>()

    override fun status(): IParticleStatuses = status

    private fun check() {
        if (status == IParticleStatuses.valueOf("DISMISSED")) {
            throw IllegalStateException("this particle group is dismissed")
        }
    }

    private fun check2() {
        if (status == IParticleStatuses.valueOf("ACTIVATED")) {
            throw IllegalStateException("this particle group is ACTIVATED, cannot perform this operation")
        }
    }

    override fun add(particleInfo: IParticleInfo) {
        check()
        check2()
        if (particleInfo.particle != parent.particle) {
            throw IllegalArgumentException("wrong particle type")
        }
        list.add(particleInfo)
    }

    override fun add(x: Double, y: Double, z: Double, count: Int) {
        check()
        check2()
        val coordinate = Coordinate()
        coordinate.x = x
        coordinate.y = y
        coordinate.z = z

        val particleInfo = ParticleInfo(parent.particle, coordinate, count)
        list.add(particleInfo)
    }

    override fun remove(particleInfo: IParticleInfo) {
        check()
        check2()
        list.remove(particleInfo)
    }

    override fun removeAt(index: Int) {
        check()
        check2()
        list.removeAt(index)
    }

    override fun removeAt(x: Double, y: Double, z: Double) {
        check()
        check2()
        val particleInfos = list.stream()
            .filter {
                val coordinate = it.coordinate
                return@filter coordinate.x == x && coordinate.y == y && coordinate.z == z
            }
            .toList()
        particleInfos?.let {
            it.forEach { particleInfo ->
                list.remove(particleInfo)
            }
        }
    }

    override fun clear() {
        list.clear()
    }

    override fun spawn(playerCollection: Collection<Player>, frequency: Long, far: Boolean): IStopSignal {
        return frequencyTask(frequency) {
            spawn(playerCollection, far)
        }
    }

    override fun spawn(playerCollection: Collection<Player>, far: Boolean) {
        check()
        status = IParticleStatuses.valueOf("ACTIVATED")

        list.forEach { particleInfo ->
            val particle = parent.particle
            val coordinate = particleInfo.coordinate
            val count = particleInfo.count
            val extra = particleInfo.extra

            if (extra == null) {
                particle<ParticleType>(particle)
                    .packet(far, coordinate.x, coordinate.y, coordinate.z, 0.0, 0.0, 0.0, count)
                    .sendTo(playerCollection)
            } else {
                particle<ParticleType>(particle)
                    .packet(far, coordinate.x, coordinate.y, coordinate.z, 0.0, 0.0, 0.0, extra, count)
                    .sendTo(playerCollection)
            }
        }

        status = IParticleStatuses.valueOf("DEFAULT")
    }

    override fun dismiss() {
        status = IParticleStatuses.valueOf("DISMISSED")

        list.clear()
        parent.remove(name)
    }

    companion object {
        fun create(name: String, managedParticle: ManagedParticle) = ParticleGroup(name, managedParticle)
    }
}