package com.koisv.customenchants

import com.koisv.customenchants.commands.EnchantCmd
import io.github.monun.kommand.kommand
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    companion object {
        lateinit var instance: Main
        private set
    }

    override fun onEnable() {
        println(String.format("[%s] - 가동 시작!", description.name))

        server.pluginManager.registerEvents(Events(), this)

        instance = this

        kommand {
            register("kc") {
                EnchantCmd.register(this)
            }
        }
    }

    override fun onDisable() {
        println(String.format("[%s] - 가동 중지.", description.name))
    }
}