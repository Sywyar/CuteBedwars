package io.github.gdrfgdrf.cutebedwars.game.editing.change

import io.github.gdrfgdrf.cutebedwars.abstracts.editing.change.AbstractChange
import io.github.gdrfgdrf.cutebedwars.abstracts.editing.change.IChangeClassHolder
import io.github.gdrfgdrf.cutebedwars.game.editing.change.annotation.Change
import io.github.gdrfgdrf.cutebedwars.game.editing.change.data.ChangeData

class ChangeClassHolder<T : AbstractChange<*>>(
    private val clazz: Class<T>,
    type: String
) : IChangeClassHolder<T> {
    private val typeClass = Class.forName(type)

    override fun validateArgsLength(protobuf: Boolean, vararg any: Any): Boolean {
        val change = clazz.getAnnotation(Change::class.java)
        val argsRange = if (protobuf) {
            change.minArgs..change.maxArgsForProtobuf
        } else {
            change.minArgs..change.maxArgs
        }

        return argsRange.contains(any.size)
    }

    override fun validate(any: Any): Boolean {
        val validateMethod = clazz.getMethod("validate")
        val result = validateMethod.invoke(any)
        return result as Boolean
    }

    override fun create(vararg any: Any): T {
        val changeData = ChangeData.of(*any)

        val constructor = clazz.getConstructor(ChangeData::class.java)
        val instance = constructor.newInstance(changeData)

        return instance
    }

    override fun type(): Class<*> = typeClass

    companion object {
        fun <T : AbstractChange<*>> create(clazz: Class<T>, type: String) = ChangeClassHolder(clazz, type)
    }
}