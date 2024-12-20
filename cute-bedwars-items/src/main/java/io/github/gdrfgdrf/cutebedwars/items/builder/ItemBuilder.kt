package io.github.gdrfgdrf.cutebedwars.items.builder

import de.tr7zw.changeme.nbtapi.NBT
import de.tr7zw.changeme.nbtapi.iface.ReadWriteItemNBT
import io.github.gdrfgdrf.cutebedwars.abstracts.items.IItem
import io.github.gdrfgdrf.cutebedwars.abstracts.items.IItemBuilder
import io.github.gdrfgdrf.cutebedwars.abstracts.items.IItemProperties
import io.github.gdrfgdrf.cutebedwars.items.Item
import io.github.gdrfgdrf.cutebedwars.items.item.SpecialItem
import io.github.gdrfgdrf.multimodulemediator.annotation.ServiceImpl
import org.bukkit.inventory.ItemStack

@ServiceImpl("item_builder")
class ItemBuilder : IItemBuilder {
    override val properties: IItemProperties = IItemProperties.create {}

    override fun modify(modifier: IItemProperties.() -> Unit): IItemBuilder {
        modifier(properties)
        return this
    }

    override fun build(special: Boolean, nbtModifier: ((ReadWriteItemNBT) -> Unit)?): IItem {
        properties.check()

        val itemStack = ItemStack(properties.material)
        properties.applyTo(itemStack, false)

        if (nbtModifier != null) {
            NBT.modify(itemStack, nbtModifier)
        }

        if (special) {
            val specialItem = SpecialItem(itemStack, properties)
            return specialItem
        }

        val item = Item(itemStack, properties)
        return item
    }
}