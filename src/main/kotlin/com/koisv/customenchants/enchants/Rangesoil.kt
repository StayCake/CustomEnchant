package com.koisv.customenchants.enchants

import com.koisv.customenchants.Main
import io.papermc.paper.enchantments.EnchantmentRarity
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack


class Rangesoil(id: Int) : Enchantment(NamespacedKey(Main.instance,id.toString())) {

    override fun getKey(): NamespacedKey {
        return NamespacedKey(Main.instance,200.toString())
    }

    override fun getName(): String {
        return "Rangesoil"
    }

    override fun getMaxLevel(): Int {
        return 3
    }

    override fun getStartLevel(): Int {
        return 1
    }

    override fun isTreasure(): Boolean {
        return false
    }

    override fun isCursed(): Boolean {
        return false
    }

    override fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.WOODEN_HOE -> true
            Material.STONE_HOE -> true
            Material.GOLDEN_HOE -> true
            Material.IRON_HOE -> true
            Material.DIAMOND_HOE -> true
            Material.NETHERITE_HOE -> true
            Material.ENCHANTED_BOOK -> true
            else -> false
        }
    }

    override fun displayName(level: Int): Component {
        return Component.text().content("범위 경작 Lv.$level").build()
    }

    override fun isTradeable(): Boolean {
        return false
    }

    override fun isDiscoverable(): Boolean {
        return false
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.COMMON
    }

    override fun getDamageIncrease(level: Int, entityCategory: EntityCategory): Float {
        return 0F
    }

    override fun getItemTarget(): EnchantmentTarget {
        return EnchantmentTarget.TOOL
    }

    override fun getActiveSlots(): MutableSet<EquipmentSlot> {
        return mutableSetOf(EquipmentSlot.HAND,EquipmentSlot.OFF_HAND)
    }
}