package com.koisv.customenchants

import com.koisv.customenchants.commands.Enchant
import com.koisv.customenchants.commands.ShowEnchant
import com.koisv.customenchants.commands.TestEnchant
import com.koisv.customenchants.enchants.RangeHarvest
import com.koisv.customenchants.enchants.Rangesoil
import io.github.monun.kommand.kommand
import org.bukkit.enchantments.Enchantment
import org.bukkit.plugin.java.JavaPlugin
import java.lang.reflect.Field
import java.util.logging.Level

var RangeSoil: Rangesoil = Rangesoil(200)
var RangeHarvest: RangeHarvest = RangeHarvest(201)

class Main : JavaPlugin() {

    companion object {
        lateinit var instance: Main
        private set
    }

    override fun onEnable() {

        saveDefaultConfig()

        println(String.format("[%s] - 가동 시작!", description.name))

        server.pluginManager.registerEvents(Events(), this)

        instance = this

        if ( !config.getBoolean("emergency-mode") ) {
            try {
                try {
                    val f: Field = Enchantment::class.java.getDeclaredField("acceptingNew")
                    f.isAccessible = true
                    f.set(null, true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    Enchantment.registerEnchantment(RangeSoil)
                    Enchantment.registerEnchantment(RangeHarvest)
                } catch (e: IllegalArgumentException) {
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            server.logger.log(Level.WARNING,String.format("[%s] - 경고 | 비상모드가 활성화 되었습니다!", description.name))
            server.logger.log(Level.WARNING,String.format("[%s] - 경고 | 이는 모든 인첸트를 등록하지 않으며,", description.name))
            server.logger.log(Level.WARNING,String.format("[%s] - 경고 | 모든 커스텀 인첸트가 UnSafe 등록처리 됩니다!", description.name))
            server.logger.log(Level.WARNING,String.format("[%s] - 경고 | 테스트용 구문이므로 사용하지 않는다면 config.yml에서 꺼주시기 바랍니다.", description.name))
        }

        kommand {
            register("enchantbook") {
                Enchant.register(this)
            }
            register("showenchant") {
                ShowEnchant.register(this)
            }
            register("testenchant") {
                TestEnchant.register(this)
            }
        }
    }

    override fun onDisable() {
        saveConfig()
        if ( !config.getBoolean("emergency-mode") ) {
            try {
                val f: Field = Enchantment::class.java.getDeclaredField("byId")
                f.isAccessible = true
                @Suppress("UNCHECKED_CAST")
                val byId = f.get(null) as HashMap<Int, Enchantment>
                if (byId.containsKey(200)) byId.remove(200)
                if (byId.containsKey(201)) byId.remove(201)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        println(String.format("[%s] - 가동 중지.", description.name))
    }
}