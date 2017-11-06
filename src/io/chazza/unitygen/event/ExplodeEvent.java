package io.chazza.unitygen.event;

import io.chazza.unitygen.Main;
import io.chazza.unitygen.util.GenUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;
import java.util.List;

public class ExplodeEvent implements Listener {

    Plugin plugin;

    public ExplodeEvent(Main plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent e) {
        if(plugin.getConfig().getBoolean("general.useGenProtection")) {
            List<Block> destroyed = e.blockList();
            Iterator<Block> it = destroyed.iterator();
            e.setCancelled(true);
            while (it.hasNext()) {
                Block block = it.next();
                Location loc = block.getLocation();
                if (!GenUtil.isLocAGen(loc)) {
                    block.setType(Material.AIR);
                }
            }
        }
    }
}
