package commands

import hazae41.minecraft.kutils.bukkit.msg
import io.github.monun.kommand.node.LiteralNode
import org.bukkit.Material
import org.bukkit.entity.Player

object ShowEnchant {
    fun register(builder: LiteralNode) {
        builder.requires { playerOrNull != null && hasPermission(4,"admin.enchant") }
        builder.executes {
            val p = player
            if (p.inventory.itemInMainHand.type != Material.AIR) {
                p.msg(p.inventory.itemInMainHand.enchantments.toString())
            }
        }
    }
}