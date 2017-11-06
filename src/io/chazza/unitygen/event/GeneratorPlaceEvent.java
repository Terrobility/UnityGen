package io.chazza.unitygen.event;

import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.UnityGenAPI;
import io.chazza.unitygen.event.custom.UGPlaceEvent;
import io.chazza.unitygen.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class GeneratorPlaceEvent implements Listener {

    Plugin plugin;
    public GeneratorPlaceEvent(Main plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e){
        Block b = e.getBlockPlaced();
        Player p = e.getPlayer();

        ConfigManager cm = ConfigManager.getConfig(p.getUniqueId());
        FileConfiguration fc = cm.getConfig();

        if(UnityGenAPI.isItemStackGen(p.getItemInHand())){

            String tier = GenUtil.getItemStackTier(p.getItemInHand());
            GenObject go = new GenObject(tier, p.getUniqueId(), p.getItemInHand().getType());
            String loc = LocationUtil.strToStrLocation(b.getLocation());
            go.setStrLocation(loc);

            boolean enabled = plugin.getConfig().getBoolean("generator."+go.getTier()+".enabled", false);
            if(!enabled && !p.hasPermission("unitygen.bypass")){
                p.sendMessage(Lang.DISABLED_PLACE.get().replace("%tier%", go.getTier()));
                e.setCancelled(true);
                return;
            }

            if(!Main.getInstance().getConfig().getString("generator."+tier+".permission").isEmpty()){
                if(!p.hasPermission(Main.getInstance().getConfig().getString("generator."+tier+".permission"))
                || !p.hasPermission("unitygen.bypass")){
                    p.sendMessage(Lang.GENERATOR_NO_PERMISSION.get().replace("%permission%", Main.getInstance().getConfig().getString("generator."+tier+".permission")));
                    e.setCancelled(true);
                }
            }

            Integer limit = UnityGenAPI.getPlayerLimit(p);
            Integer placedCount = fc.getStringList("generator").size();
            if(!p.hasPermission("unitygen.limit.bypass")) {
                if (placedCount >= limit) {
                    p.sendMessage(Lang.PLACE_LIMIT.get().replace("%limit%", limit.toString()));
                    e.setCancelled(true);
                    return;
                }
            }
            Material bpos1 = e.getBlockPlaced().getLocation().add(-1, 0, 0).getBlock().getType();
            Material bpos2 = e.getBlockPlaced().getLocation().add(+1, 0, 0).getBlock().getType();
            Material bpos3 = e.getBlockPlaced().getLocation().add(0, 0, -1).getBlock().getType();
            Material bpos4 = e.getBlockPlaced().getLocation().add(0, 0, +1).getBlock().getType();

            if (!plugin.getConfig().getBoolean("general.allowDoubleChest")) {
                if (Arrays.asList(Material.CHEST, Material.TRAPPED_CHEST).contains(bpos1)) {
                    e.setCancelled(true);
                    p.sendMessage(Lang.DOUBLE_CHEST.get());
                    return;
                } else if (Arrays.asList(Material.CHEST, Material.TRAPPED_CHEST).contains(bpos2)) {
                    e.setCancelled(true);
                    p.sendMessage(Lang.DOUBLE_CHEST.get());
                    return;
                } else if (Arrays.asList(Material.CHEST, Material.TRAPPED_CHEST).contains(bpos3)) {
                    e.setCancelled(true);
                    p.sendMessage(Lang.DOUBLE_CHEST.get());
                    return;
                } else if (Arrays.asList(Material.CHEST, Material.TRAPPED_CHEST).contains(bpos4)) {
                    e.setCancelled(true);
                    p.sendMessage(Lang.DOUBLE_CHEST.get());
                    return;
                }
            }
            UGPlaceEvent placeEvent = new UGPlaceEvent(p, go);
            Bukkit.getPluginManager().callEvent(placeEvent);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGeneratorPlace(UGPlaceEvent e){
        Player p = e.getPlayer();
        GenObject go = e.getObject();

        ConfigManager cm = ConfigManager.getConfig(p.getUniqueId());
        FileConfiguration fc = cm.getConfig();


        String alias;
        if((Main.getInstance().getConfig().get("generator."+go.getTier()+".alias") != null)
            && (!Main.getInstance().getConfig().getString("generator."+go.getTier()+".alias").isEmpty())){
            alias = ColorUtil.translate(Main.getInstance().getConfig().getString("generator."+go.getTier()+".alias"));
        }else alias = "";

        p.sendMessage(Lang.PLACED_GENERATOR.get()
            .replace("%alias%", alias)
            .replace("%tier%", go.getTier()));

        Long expire = null;
        if(Main.getInstance().getConfig().getInt("generator."+go.getTier()+".expire") == -1){
            fc.set("generator."+go.getStrLocation()+".expire", -1);
        }else{
            Calendar cl = Calendar.getInstance();
            cl.setTimeInMillis(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(Main.getInstance().getConfig().getInt("generator."+go.getTier()+".expire")));
            expire = cl.getTimeInMillis() - System.currentTimeMillis();

            fc.set("generator."+go.getStrLocation()+".expire", cl.getTimeInMillis());
        }
        fc.set("generator."+go.getStrLocation()+".tier", go.getTier());
        cm.saveConfig();
        cm.reload();

        if(Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")){
            if(expire != null)
                HolographicUtil.createHolo(go, GenUtil.buildCountdown(expire));
            if(expire == null)
                HolographicUtil.createHolo(go);
        }
        // GENERATION CODE //
        //Chest chest = (Chest) go.getLocation().getBlock().getState();
        go.startGeneration();
        //                //
    }
}
