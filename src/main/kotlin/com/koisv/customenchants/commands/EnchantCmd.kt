package com.koisv.customenchants.commands

import com.koisv.customenchants.enchants.RangeHarvest
import com.koisv.customenchants.enchants.RangeSoil
import io.github.monun.kommand.node.LiteralNode
import org.bukkit.Material

object EnchantCmd {
    fun register(builder: LiteralNode) {
        builder.requires { playerOrNull != null && hasPermission(4,"admin.showenchant") }
        builder.executes {
            val p = player
            if (p.inventory.itemInMainHand.type != Material.AIR) {
                RangeSoil().apply(p.inventory.itemInMainHand,RangeSoil().key,3)
                RangeHarvest().apply(p.inventory.itemInMainHand,RangeHarvest().key,3)
            }
        }
    }
}