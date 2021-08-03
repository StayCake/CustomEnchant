package com.koisv.customenchants.commands

import com.koisv.customenchants.RangeHarvest
import com.koisv.customenchants.RangeSoil
import io.github.monun.kommand.node.LiteralNode
import org.bukkit.Material

object Enchant {
    fun register(builder: LiteralNode) {
        builder.requires { playerOrNull != null && hasPermission(4,"admin.showenchant") }
        builder.executes {
            val p = player
            if (p.inventory.itemInMainHand.type != Material.AIR) {
                p.inventory.itemInMainHand.apply {
                    addEnchantment(RangeSoil,2)
                    addEnchantment(RangeHarvest, 2)
                }
            }
        }
    }
}