package io.github.gdrfgdrf.cutebedwars.abstracts.items

import org.bukkit.entity.Player

interface IItem {
    val properties: IItemProperties
    fun give(player: Player, amount: Int = 1, slotIndex: Int = -1): ICommonItem
}