package com.koisv.customenchants.enchants

import com.koisv.customenchants.Utils
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

interface KoiEnchant {
    val name: String
    val id: Int
    val key: NamespacedKey
    val maxLevel: Int
    val minLevel: Int
    val canEnchant: List<Material>
    fun work(event: Any)
    fun conflictWith(key: NamespacedKey): Boolean
    fun apply(item: ItemStack, level: Int) : Boolean
    fun displayName(level: Int): String {
        return "§7$name ${Utils.Misc.rNum(level)}"
    }
}