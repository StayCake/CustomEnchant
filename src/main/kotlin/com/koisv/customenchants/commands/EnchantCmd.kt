package com.koisv.customenchants.commands

import com.koisv.customenchants.enchants.RangeHarvest
import com.koisv.customenchants.enchants.RangeSoil
import hazae41.minecraft.kutils.bukkit.msg
import io.github.monun.kommand.KommandArgument.Companion.string
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.LiteralNode
import org.bukkit.Material
import org.bukkit.NamespacedKey

object EnchantCmd {
    fun register(builder: LiteralNode) {
        builder.then("data" to string()) {
            then("level" to int()) {
                requires { playerOrNull != null && hasPermission(4,"admin.showenchant") }
                executes { ctx ->
                    val data : String by ctx
                    val level : Int by ctx
                    when (NamespacedKey.minecraft(data)) {
                        RangeSoil().key -> {
                            RangeSoil().apply(player.inventory.itemInMainHand,level)
                        }
                        RangeHarvest().key -> {
                            RangeHarvest().apply(player.inventory.itemInMainHand,level)
                        }
                    }
                }
            }
        }
        builder.then("show") {
            requires { playerOrNull != null && hasPermission(4,"admin.enchant") }
            executes {
                val p = player
                if (p.inventory.itemInMainHand.type != Material.AIR) {
                    p.inventory.itemInMainHand.itemMeta.persistentDataContainer.keys.forEach {
                        p.msg(it.toString())
                    }
                }
            }
        }
    }
}