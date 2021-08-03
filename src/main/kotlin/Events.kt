import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.data.Ageable
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

val hoe = listOf(
    Material.WOODEN_HOE,
    Material.STONE_HOE,
    Material.GOLDEN_HOE,
    Material.IRON_HOE,
    Material.DIAMOND_HOE,
    Material.NETHERITE_HOE,
)

val crop = listOf(
    Material.WHEAT,
    Material.BEETROOTS,
    Material.CARROTS,
    Material.POTATOES,
    Material.SUGAR_CANE,
    Material.SWEET_BERRY_BUSH
)

class Events : Listener {

    @EventHandler
    private fun range(e: BlockPlaceEvent) {
        val p = e.player
        val imh = p.inventory.itemInMainHand
        val omh = p.inventory.itemInOffHand
        val mh = when (imh.type) {
            Material.WOODEN_HOE -> imh
            Material.STONE_HOE -> imh
            Material.GOLDEN_HOE -> imh
            Material.IRON_HOE -> imh
            Material.DIAMOND_HOE -> imh
            Material.NETHERITE_HOE -> imh
            else -> omh
        }
        val block = e.block
        if (mh.containsEnchantment(RangeSoil)) {
            val lvl = mh.getEnchantmentLevel(RangeSoil)
            for (lv in 1..lvl) {
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

    @EventHandler
    private fun harvest(e: BlockBreakEvent) {
        val p = e.player
        val mh = e.player.inventory.itemInMainHand
        val block = e.block
        if (mh.containsEnchantment(RangeHarvest) && hoe.contains(mh.type) && crop.contains(block.type)) {
            val lv = mh.getEnchantmentLevel(RangeHarvest)
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
                                Material.SWEET_BERRY_BUSH -> {
                                    val bd = b.blockData as Ageable
                                    if (bd.age == 2) {
                                        b.drops.forEach{
                                            p.world.dropItemNaturally(b.location,it)
                                        }
                                        bd.age = 0
                                        b.blockData = bd
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
                                Material.BEETROOTS -> {
                                    val bd = b.blockData as Ageable
                                    if (bd.age == 3) {
                                        b.drops.forEach{
                                            p.world.dropItemNaturally(b.location,it)
                                        }
                                        bd.age = 0
                                        b.blockData = bd
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
                                else -> {
                                    val bd = b.blockData as Ageable
                                    if (bd.age == 7) {
                                        b.drops.forEach{
                                            p.world.dropItemNaturally(b.location,it)
                                        }
                                        bd.age = 0
                                        b.blockData = bd
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
        }
    }
}