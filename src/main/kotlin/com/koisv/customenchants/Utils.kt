package com.koisv.customenchants

import com.koisv.customenchants.enchants.RangeHarvest
import com.koisv.customenchants.enchants.RangeSoil
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType
import java.util.*
import java.util.stream.Collectors

class Utils {
    class Misc {
        companion object {
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

            internal enum class Roman(val value: Int) {
                I(1), IV(4), V(5), IX(9), X(10), XL(40), L(50), XC(90), C(100), CD(400), D(500), CM(900), M(1000);

                companion object {
                    val reverseSortedValues: List<Roman>
                        get() = Arrays.stream(values())
                            .sorted(Comparator.comparing { e: Roman -> e.value }.reversed())
                            .collect(Collectors.toList())
                }
            }
            fun rNum(number: Int): String {
                var nums = number
                require(!(nums <= 0 || nums > 4000)) { "$nums is not in range (0,4000]" }
                val romanNumerals: List<Roman> = Roman.reverseSortedValues
                var i = 0
                val sb = StringBuilder()
                while (nums > 0 && i < romanNumerals.size) {
                    val currentSymbol: Roman = romanNumerals[i]
                    if (currentSymbol.value <= nums) {
                        sb.append(currentSymbol.name)
                        nums -= currentSymbol.value
                    } else {
                        i++
                    }
                }
                return sb.toString()
            }
            fun itemUse(item: ItemStack,p: Player) {
                val unbreaking = item.getEnchantmentLevel(Enchantment.DURABILITY)
                if (Math.random() * 100 <= 100/(unbreaking + 1) && p.gameMode != GameMode.CREATIVE) {
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
    }

    class Enchant {
        companion object {
            private val enchantKeys = listOf(
                RangeSoil().key,
                RangeHarvest().key
            )

            fun has(item: ItemStack) : Boolean {
                val pdc = item.itemMeta.persistentDataContainer
                enchantKeys.forEach {
                    if (pdc.has(it, PersistentDataType.INTEGER)) return true
                }
                return false
            }

            fun has(item: ItemStack, key: NamespacedKey) : Boolean {
                val pdc = item.itemMeta.persistentDataContainer
                return pdc.has(key, PersistentDataType.INTEGER)
            }

            fun get(item: ItemStack, key: NamespacedKey) : Int {
                val pdc = item.itemMeta.persistentDataContainer
                return pdc.get(key, PersistentDataType.INTEGER) ?: -1
            }

            fun loreUpdate(item: ItemStack, name: String, display: String) : Boolean{
                var final = false
                item.apply {
                    itemMeta = itemMeta.apply {
                        val flore = lore()
                        flore?.forEach {
                            val tComp = it as TextComponent
                            val tText = tComp.content()
                            val tName = tText.contains(name)
                            if (tName) {
                                val index = flore.indexOf(it)
                                flore[index] = Component.text(display)
                                final = !final
                            }
                        }
                        lore(flore)
                    }
                }
                return final
            }
        }
    }
}