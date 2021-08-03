package com.koisv.customenchants

import com.koisv.customenchants.enchants.RangeHarvest
import com.koisv.customenchants.enchants.Rangesoil
import org.bukkit.enchantments.Enchantment
import org.bukkit.plugin.java.JavaPlugin
import java.lang.reflect.Field


var RangeSoil: Rangesoil = Rangesoil(200)
var RangeHarvest: RangeHarvest = RangeHarvest(201)
class Main : JavaPlugin() {

    companion object {
        lateinit var instance: Main
            private set
    }

    override fun onEnable() {

        println(String.format("[%s] - 가동 시작!", description.name))

        server.pluginManager.registerEvents(Events(), this)

        instance = this

        try {
            try {
                val f: Field = Enchantment::class.java.getDeclaredField("acceptingNew")
                f.isAccessible = true
                f.set(null, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                //Enchantment.registerEnchantment(RangeSoil)
                //Enchantment.registerEnchantment(RangeHarvest)
            } catch (e: IllegalArgumentException) {
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        /*kommand {
            register("enchantbook") {
                Enchant.register(this)
            }
            register("showenchant") {
                ShowEnchant.register(this)
            }
            register("testenchant") {
                TestEnchant.register(this)
            }
        }*/
    }

    override fun onDisable() {
        try {
            val f: Field = Enchantment::class.java.getDeclaredField("byId")
            f.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            val byId = f.get(null) as HashMap<Int, Enchantment>
            if (byId.containsKey(200)) byId.remove(200)
            if (byId.containsKey(201)) byId.remove(201)
        } catch (e: Exception) {}
        println(String.format("[%s] - 가동 중지.", description.name))
    }
}