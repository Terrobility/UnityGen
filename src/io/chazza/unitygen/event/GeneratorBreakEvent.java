package io.chazza.unitygen.event;

import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.util.GenUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class GeneratorBreakEvent implements Listener {

    Plugin plugin;

    public GeneratorBreakEvent(Main plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onGeneratorBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        Block b = e.getBlock();

        if(GenUtil.isLocAGen(b.getLocation())){
            UUID genOwner = GenUtil.getGenOwnerFromLoc(b.getLocation());
            String PlayerUsername = Bukkit.getOfflinePlayer(genOwner).getName();


            if(p.getUniqueId().equals(genOwner)){
                p.sendMessage(Lang.BREAK_GENERATOR.get());
                e.setCancelled(true);
            }else{
                p.sendMessage(Lang.OWNED_GENERATOR.get().replace("%player%", PlayerUsername));
                e.setCancelled(true); 
            }
        }
    }

}
