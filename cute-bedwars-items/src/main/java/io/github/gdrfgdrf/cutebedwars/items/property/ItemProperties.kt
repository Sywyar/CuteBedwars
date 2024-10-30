package io.github.gdrfgdrf.cutebedwars.items.property

import com.github.yitter.idgen.YitIdHelper
import de.tr7zw.nbtapi.NBT
import io.github.gdrfgdrf.cutebedwars.abstracts.items.IItemProperties
import io.github.gdrfgdrf.cutebedwars.abstracts.utils.ICustomList
import io.github.gdrfgdrf.cutebedwars.abstracts.utils.customList
import io.github.gdrfgdrf.cuteframework.locale.LanguageString
import io.github.gdrfgdrf.multimodulemediator.annotation.ServiceImpl
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

@ServiceImpl("item_properties")
class ItemProperties : IItemProperties {
    override var material: Material? = null
    override var name: (() -> LanguageString)? = null

    override val lores: ICustomList<String> = customList()
    override val flags: ICustomList<ItemFlag> = customList()

    override var onClick: ((PlayerInteractEvent) -> Unit)? = null
    override var onLeftClick: ((PlayerInteractEvent) -> Unit)? = null
    override var onRightClick: ((PlayerInteractEvent) -> Unit)? = null

    override var unbreakable: Boolean = false
    override var droppable: Boolean = true
    override var movable: Boolean = true
    override val canPlaceOn: ICustomList<Material> = customList()
    override val canDestroy: ICustomList<Material> = customList()

    override fun check() {
        if (material == null) {
            throw IllegalArgumentException("material is required")
        }
    }

    override fun copy(): IItemProperties {
        val properties = ItemProperties()
        properties.material = this.material
        properties.name = this.name

        properties.lores.add(*this.lores.list.toTypedArray())
        properties.flags.add(*this.flags.list.toTypedArray())

        properties.onClick = this.onClick
        properties.onLeftClick = this.onLeftClick
        properties.onRightClick = this.onRightClick

        properties.unbreakable = this.unbreakable
        properties.droppable = this.droppable
        properties.canPlaceOn.add(*this.canPlaceOn.list.toTypedArray())
        properties.canDestroy.add(*this.canDestroy.list.toTypedArray())
        return properties
    }

    override fun applyTo(itemStack: ItemStack) {
        check()

        val itemMeta = itemStack.itemMeta

        if (name != null) {
            itemMeta.displayName = name!!().get().string
        } else {
            itemMeta.displayName = ""
        }

        itemMeta.lore = arrayListOf()
        itemMeta.lore.addAll(lores.list)
        itemMeta.itemFlags.clear()
        itemMeta.itemFlags.addAll(flags.list)

        itemMeta.isUnbreakable = unbreakable

        itemStack.itemMeta = itemMeta

        NBT.modify(itemStack) { operableNbt ->
            operableNbt.setLong("cube-bedwars-item", YitIdHelper.nextId())

            val canPlaceOnList = operableNbt.getStringList("CanPlaceOn")
            canPlaceOnList.clear()

            canPlaceOn.list.forEach { canPlaceOnMaterial ->
                canPlaceOnList.add("minecraft:${canPlaceOnMaterial.name.lowercase()}")
            }

            val canDestroyList = operableNbt.getStringList("CanDestroy")
            canDestroyList.clear()

            canDestroy.list.forEach { canDestroyMaterial ->
                canDestroyList.add("minecraft:${canDestroyMaterial.name.lowercase()}")
            }
        }
    }
}