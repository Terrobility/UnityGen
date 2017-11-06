package io.chazza.unitygen.event;

import io.chazza.unitygen.Main;
import io.chazza.unitygen.util.GenUtil;
import io.chazza.unitygen.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

public class HopperEvents implements Listener {

    Plugin plugin;

    public HopperEvents(Main plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        if(e.getBlockPlaced().getType().equals(Material.HOPPER)){
            if (!plugin.getConfig().getBoolean("general.allowHopper")) {
                if (LocationUtil.isGenNearby(e.getBlockPlaced().getLocation())) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    /**
     * UPDATE POST
     * 1.8 Support Re-Added!
     * > Hopper Event works in 1.8, items won't transfer
     * New UGCommandGenerateEvent
     * UGItemGenerateEvent changed to UGItemGenerateEvent
     * Fixed issue with Generator Placement
     */

    @EventHandler(ignoreCancelled = true)
    public void onMove(InventoryMoveItemEvent e) {
        InventoryHolder ih = e.getSource().getHolder();
        if(ih instanceof Chest){
            Chest ch = (Chest) ih;

            if(GenUtil.isLocAGen(ch.getLocation())) {
                if (!plugin.getConfig().getBoolean("general.allowHopper")) {
                    e.setCancelled(true);
                }
            }
        }

    }
}
