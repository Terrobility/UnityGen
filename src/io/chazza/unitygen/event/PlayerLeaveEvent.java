package io.chazza.unitygen.event;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.util.LoggerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class PlayerLeaveEvent implements Listener {

    Plugin plugin;
    public PlayerLeaveEvent(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        if(Main.getInstance().getActiveGens().containsKey(e.getPlayer().getUniqueId())) {
            for (Iterator<Map.Entry<UUID,BukkitTask>> it = Main.getInstance().getActiveGens().entrySet().iterator(); it.hasNext();) {
                Map.Entry<UUID,BukkitTask> e1 = it.next();

                LoggerUtil.debugMsg("HashMap: (Key) " + e1.getKey());
                LoggerUtil.debugMsg("HashMap: (Value) " + e1.getValue());
                LoggerUtil.debugMsg("Quit (Player): " + e.getPlayer().getUniqueId());

                if(e1.getKey().equals(e.getPlayer().getUniqueId())){
                    LoggerUtil.debugMsg("Found player " + e.getPlayer().getUniqueId()+"!");
                    Bukkit.getScheduler().cancelTask(e1.getValue().getTaskId());
                    it.remove();
                    Main.getInstance().getActiveGens().remove(e1.getValue(), e1.getKey());
                }else LoggerUtil.debugMsg("Could not find active generator for " + e.getPlayer().getName());
            }


            boolean isHolographicDisplaysEnabled = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");

            if (isHolographicDisplaysEnabled) {
                for (Hologram hologram : HologramsAPI.getHolograms(plugin)) {
                    Location hologramLoc = hologram.getLocation();

                    if (Main.getFileManager().doesUserContainHologram(e.getPlayer().getUniqueId(),
                            hologramLoc.toString())) {
                        hologram.delete();
                        Main.getFileManager().removeGenHologram(e.getPlayer().getUniqueId(), hologram);
                    }
                }
            }
        }else LoggerUtil.debugMsg("No active generator for " + e.getPlayer().getName() + "!");
    }
}