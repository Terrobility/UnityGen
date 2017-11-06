package io.chazza.unitygen.event;

import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.event.custom.UGAdminInteractEvent;
import io.chazza.unitygen.event.custom.UGFuelEvent;
import io.chazza.unitygen.event.custom.UGFuelOtherEvent;
import io.chazza.unitygen.event.custom.UGInteractEvent;
import io.chazza.unitygen.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class GeneratorInteractEvent implements Listener {

    Plugin plugin;

    public GeneratorInteractEvent(Main plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();

        if(p.isSneaking() && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (GenUtil.isLocAGen(b.getLocation())) {
                for (String allowedItem : plugin.getConfig().getStringList("general.allowed-item")) {
                    Material allowed = Material.valueOf(allowedItem.toUpperCase());
                    if (p.getItemInHand().getType().equals(allowed) || FuelUtil.getItemStackTier(p.getItemInHand()) != null) {
                        return;
                    }
                }

                e.setCancelled(true);
                UUID genOwner = GenUtil.getGenOwnerFromLoc(b.getLocation());
                if (p.getUniqueId().equals(genOwner)) {
                    String tier = LocationUtil.tierFromLocation(b.getLocation(), genOwner);
                    UGInteractEvent interactEvent = new UGInteractEvent(p, tier, b.getLocation());
                    Bukkit.getPluginManager().callEvent(interactEvent);

                }else if(p.hasPermission("unitygen.bypass") || Main.getInstance().getConfig().getString("general.remove-gen")
                    .toUpperCase().equals("EVERYONE")) {
                    UUID owner = Bukkit.getOfflinePlayer(genOwner).getUniqueId();
                    String tier = LocationUtil.tierFromLocation(b.getLocation(), genOwner);

                    UGAdminInteractEvent adminInteractEvent = new UGAdminInteractEvent(p, tier, owner, b.getLocation());
                    Bukkit.getPluginManager().callEvent(adminInteractEvent);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGenInteract(UGInteractEvent e){
        Player p = e.getPlayer();
        String tier = e.getGenerator();

        GenObject go = new GenObject(tier, p.getUniqueId());
        go.setStrLocation(LocationUtil.strToStrLocation(e.getLocation()));

        if(Main.getInstance().getConfig().getString("generator."+tier+".expire") != null && !Main.getInstance().getConfig().getString("generator."+tier+".expire").equals("-1") && !p.hasPermission("unitygen.bypass")){
            p.sendMessage(Lang.EXPIRE_REMOVED.get().replace("%tier%", tier));
            e.setCancelled(true);
            return;
        }
        if(Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")){
            String expire = GenUtil.buildCountdown(ConfigManager.getConfig(p).getConfig().getLong("generator."+go.getStrLocation()+".expire") - System.currentTimeMillis());
            go.setHologram(HolographicUtil.createHolo(go, expire));
            go.removeHologram();
        }
        go.removeLocation();
        e.getLocation().getBlock().setType(Material.AIR);
        GenUtil.dealItemDrop(p, tier);
        p.sendMessage(Lang.REMOVED_GENERATOR.get().replace("%tier%", tier));
    }

    @EventHandler(ignoreCancelled = true)
    public void onGenAdminInteract(UGAdminInteractEvent e){
        Player p = e.getPlayer();
        String tier = e.getGenerator();

        GenObject go = new GenObject(tier, e.getOwner());
        go.setStrLocation(LocationUtil.strToStrLocation(e.getLocation()));
        if(Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")){
            String expire = GenUtil.buildCountdown(ConfigManager.getConfig(p).getConfig().getLong("generator."+go.getStrLocation()+".expire") - System.currentTimeMillis());
            go.setHologram(HolographicUtil.createHolo(go, expire));
            go.removeHologram();
        }
        go.removeLocation();
        e.getLocation().getBlock().setType(Material.AIR);
        GenUtil.dealItemDrop(p, tier);
        p.sendMessage(Lang.ADMIN_REMOVED.get().replace("%tier%", tier).replace("%player%", Bukkit.getOfflinePlayer(e.getOwner()).getName()));
    }
}
