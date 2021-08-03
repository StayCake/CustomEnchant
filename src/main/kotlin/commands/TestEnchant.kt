package commands

import io.github.monun.kommand.node.LiteralNode
import org.bukkit.Material
import org.bukkit.block.data.Ageable

object TestEnchant {
    fun register(builder: LiteralNode) {
        builder.requires { playerOrNull != null && hasPermission(4,"admin.test") }
        builder.executes {
            val p = player
            val b = p.getTargetBlock(3)
            if (
                b?.type == Material.WHEAT
                || b?.type == Material.POTATOES
                || b?.type == Material.CARROTS
                || b?.type == Material.BEETROOTS
            ){
                val bd = b.blockData as Ageable
                if (bd.age == 7) {
                    bd.age = 0
                    b.blockData = bd
                    b.drops.forEach {
                        p.world.dropItemNaturally(b.location,it)
                    }
                }
            }
        }
    }
}