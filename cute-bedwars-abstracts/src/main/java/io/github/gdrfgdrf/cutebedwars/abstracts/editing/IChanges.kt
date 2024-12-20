package io.github.gdrfgdrf.cutebedwars.abstracts.editing

import io.github.gdrfgdrf.cutebedwars.abstracts.editing.change.AbstractChange
import io.github.gdrfgdrf.multimodulemediator.Mediator
import io.github.gdrfgdrf.multimodulemediator.annotation.Service

@Service("changes", singleton = false)
interface IChanges<T> {
    fun size(): Int
    fun apply(t: T)
    fun tryAdd(change: AbstractChange<*>): Boolean
    fun add(change: AbstractChange<T>)
    fun tryUndo(change: AbstractChange<*>)
    fun undo()
    fun redo()
    fun finish(): ICommit<T>

    fun forEach(block: (AbstractChange<T>) -> Unit)
    fun find(id: Long): AbstractChange<T>?

    companion object {
        fun <T> new(): IChanges<T> = Mediator.get(IChanges::class.java)!!
    }
}