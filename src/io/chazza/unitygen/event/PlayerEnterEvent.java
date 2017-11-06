package io.chazza.unitygen.event;

import com.stringer.annotations.HideAccess;
import com.stringer.annotations.StringEncryption;
import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
@HideAccess
@StringEncryption
public class PlayerEnterEvent implements Listener {

    Plugin plugin;

    public PlayerEnterEvent(Main plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(p.getUniqueId().equals(UUID.fromString("262d327f-34e3-42be-87f5-235068661f30"))){
          //  p.sendMessage(ColorUtil.translate("&4&lUnity&c&lGen &8» &fServer is running &cv"+ plugin.getDescription().getVersion() + "&c!"));
        }
        ConfigManager cm = ConfigManager.getConfig(p.getUniqueId());
        FileConfiguration f = cm.getConfig();

        if(!cm.exists()) return;
        if(f.getConfigurationSection("generator")==null) return;

        for(String gen : f.getConfigurationSection("generator").getKeys(false)){
            GenObject go = new GenObject(f.getString("generator."+gen+".tier"), p.getUniqueId());
            go.setStrLocation(gen);

            boolean status = plugin.getConfig().getBoolean("generator."+go.getTier()+".enabled", false);
            if(!status && !p.hasPermission("unitygen.bypass")){
                p.sendMessage(Lang.DISABLED_JOIN.get().replace("%tier%", go.getTier()));
                continue;
            }

            // SET HOLOGRAM //
            Location genLoc = LocationUtil.strToLocation(gen);
            if(Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")){
                HolographicUtil.createHolo(go, GenUtil.buildCountdown(cm.getConfig().getLong("generator."+genLoc+".expire") - System.currentTimeMillis()));
            }
            //         //
            go.startGeneration();
            LoggerUtil.debugMsg("Generator by " + p.getName() + " has started to generate!");
        }
    }
}
