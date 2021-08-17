package com.koisv.customenchants

import com.koisv.customenchants.enchants.RangeHarvest
import com.koisv.customenchants.enchants.RangeSoil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

class Events : Listener {

    @EventHandler
    private fun soil(e: BlockPlaceEvent) {
        RangeSoil().work(e)
    }

    @EventHandler
    private fun harvest(e: BlockBreakEvent) {
        RangeHarvest().work(e)
    }
}