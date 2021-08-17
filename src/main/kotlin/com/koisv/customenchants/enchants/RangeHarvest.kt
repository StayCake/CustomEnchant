package com.koisv.customenchants.enchants

import com.koisv.customenchants.Utils
import com.koisv.customenchants.Utils.Misc.Companion.crop
import com.koisv.customenchants.Utils.Misc.Companion.hoe
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.block.data.Ageable
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType

class RangeHarvest : KoiEnchant {

    override val name: String
        get() = "범위 수확"
    override val id: Int
        get() = 1
    override val key: NamespacedKey
        get() = NamespacedKey.minecraft("koienchant.rangeharvest")
    override val maxLevel: Int
        get() = 3
    override val minLevel: Int
        get() = 1
    override val canEnchant: List<Material>
        get() = hoe

    override fun conflictWith(key: NamespacedKey): Boolean {
        return false
    }

    override fun apply(item: ItemStack, level: Int): Boolean {
        return if (level in minLevel..maxLevel && canEnchant.contains(item.type)) {
            item.apply {
                itemMeta = itemMeta.apply {
                    val pdc = persistentDataContainer
                    pdc.set(key, PersistentDataType.INTEGER, level)
                }
                if (!Utils.Enchant.loreUpdate(this,name,displayName(level))) {
                    if (lore() != null) {
                        val flore = lore()
                        flore?.add(Component.text(RangeHarvest().displayName(level)))
                        lore(flore)
                    } else {
                        lore(
                            listOf(Component.text(RangeHarvest().displayName(level)))
                        )
                    }
                }
            }
            true
        } else false
    }

    companion object fun work(e: BlockBreakEvent) {
        val p = e.player
        val mh = e.player.inventory.itemInMainHand
        if (mh.itemMeta == null) return
        val pdc = mh.itemMeta.persistentDataContainer
        val block = e.block
        if (
            pdc.has(RangeHarvest().key, PersistentDataType.INTEGER)
            && hoe.contains(mh.type)
            && crop.contains(block.type)
        ) {
            val lv = pdc.get(RangeHarvest().key, PersistentDataType.INTEGER) as Int
            for (lvl in 1..lv) {
                val stx = block.x - lv
                val stz = block.z - lv
                val edx = block.x + lv
                val edz = block.z + lv
                for (x in stx..edx) {
                    for (z in stz..edz) {
                        val b = p.world.getBlockAt(x,block.y,z)
                        val ub = p.world.getBlockAt(x,block.y+1,z)
                        if (crop.contains(b.type)) {
                            e.isCancelled = true
                            when (b.type) {
                                Material.SUGAR_CANE -> {
                                    ub.breakNaturally()
                                    itemUse(mh,p)
                                }
                                Material.SWEET_BERRY_BUSH -> {
                                    val bd = b.blockData as Ageable
                                    if (bd.age == 2) {
                                        dropOut(b,bd,p,mh)
                                    }
                                }
                                Material.BEETROOTS -> {
                                    val bd = b.blockData as Ageable
                                    if (bd.age == 3) {
                                        dropOut(b,bd,p,mh)
                                    }
                                }
                                else -> {
                                    val bd = b.blockData as Ageable
                                    if (bd.age == 7) {
                                        dropOut(b,bd,p,mh)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun dropOut(block: Block, data: Ageable, p: Player, item: ItemStack) {
        block.drops.forEach{
            p.world.dropItemNaturally(block.location,it)
        }
        data.age = 0
        block.blockData = data
        itemUse(item,p)
    }

    private fun itemUse(item: ItemStack,p: Player) {
        val unbreaking = item.getEnchantmentLevel(Enchantment.DURABILITY)
        if (Math.random() * 100 <= 100/(unbreaking + 1)) {
            item.itemMeta = item.itemMeta.apply {
                if (this is Damageable) {
                    damage += 1
                    if (item.type.maxDurability <= damage) {
                        p.inventory.setItemInMainHand(ItemStack(Material.AIR))
                        p.playSound(
                            Sound.sound(
                                Key.key("entity.item.break"),
                                Sound.Source.MASTER,1F,1F
                            )
                        )
                        return
                    }
                }
            }
        }
    }
}