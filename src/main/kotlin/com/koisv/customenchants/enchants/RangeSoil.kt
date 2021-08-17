package com.koisv.customenchants.enchants

import com.koisv.customenchants.Utils
import com.koisv.customenchants.Utils.Misc.Companion.hoe
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType

class RangeSoil : KoiEnchant {

    override val name: String
        get() = "범위 경작"
    override val id: Int
        get() = 0
    override val key: NamespacedKey
        get() = NamespacedKey.minecraft("koienchant.rangesoil")
    override val maxLevel: Int
        get() = 3
    override val minLevel: Int
        get() = 1
    override val canEnchant: List<Material>
        get() = hoe

    override fun conflictWith(key: NamespacedKey): Boolean {
        return false
    }

    override fun apply(item: ItemStack, key: NamespacedKey, level: Int): Boolean {
        return if (level in minLevel..maxLevel && canEnchant.contains(item.type)) {
            item.apply {
                itemMeta = itemMeta.apply {
                    val pdc = persistentDataContainer
                    pdc.set(key, PersistentDataType.INTEGER, level)
                }
                if (!Utils.Enchant.loreUpdate(this,name,displayName(level))) {
                    if (lore() != null) {
                        val flore = lore()
                        flore?.add(Component.text(RangeSoil().displayName(level)))
                        lore(flore)
                    } else {
                        lore(
                            listOf(Component.text(RangeSoil().displayName(level)))
                        )
                    }
                }
            }
            true
        } else false
    }

    companion object fun work(e: BlockPlaceEvent) {
        val p = e.player
        val imh = p.inventory.itemInMainHand
        val omh = p.inventory.itemInOffHand
        val mh = if (hoe.contains(omh.type)) omh else imh
        if (mh.itemMeta == null) return
        val pdc = mh.itemMeta.persistentDataContainer
        val block = e.block
        if (pdc.has(RangeSoil().key, PersistentDataType.INTEGER)) {
            val enchantLV = pdc.get(RangeSoil().key, PersistentDataType.INTEGER) as Int
            for (lv in 1..enchantLV) {
                val stx = block.x - lv
                val stz = block.z - lv
                val edx = block.x + lv
                val edz = block.z + lv
                for (x in stx..edx) {
                    for (z in stz..edz) {
                        val tb = p.world.getBlockAt(x,block.y,z)
                        val ub = p.world.getBlockAt(x,block.y + 1,z)
                        if (ub.type == Material.AIR && (tb.type == Material.DIRT || tb.type == Material.GRASS_BLOCK)) {
                            tb.setType(Material.FARMLAND,true)
                            val unbreaking = mh.getEnchantmentLevel(Enchantment.DURABILITY)
                            if (Math.random() * 100 <= 100/(unbreaking + 1)) {
                                mh.itemMeta = mh.itemMeta.apply {
                                    if (this is Damageable) {
                                        damage += 1
                                        if (mh.type.maxDurability <= damage) {
                                            p.inventory.setItemInMainHand(ItemStack(Material.AIR))
                                            p.playSound(p.location, Sound.ENTITY_ITEM_BREAK,1F,1F)
                                            return
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}