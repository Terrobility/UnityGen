package io.chazza.unitygen.event;

import io.chazza.unitygen.Main;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;

public class ChunkEvent implements Listener {

    Plugin plugin;

    public ChunkEvent(Main plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent e){
        for(Location loc : Main.getInstance().getActiveChunks()){
            Chunk lck = loc.getChunk();
            if(e.getChunk().equals(lck)){
                e.setCancelled(true);
            }
        }
    }
}
