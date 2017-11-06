package io.chazza.unitygen.event;

import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.event.custom.UGFuelEvent;
import io.chazza.unitygen.event.custom.UGFuelOtherEvent;
import io.chazza.unitygen.util.ConfigManager;
import io.chazza.unitygen.util.FuelUtil;
import io.chazza.unitygen.util.GenUtil;
import io.chazza.unitygen.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GeneratorFuelEvent implements Listener {

    Plugin plugin;

    public GeneratorFuelEvent(Main plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();

        String fuel = FuelUtil.getItemStackTier(p.getItemInHand());

        if(fuel != null && GenUtil.isLocAGen(b.getLocation())){
            Integer timeAdded = Main.getFileManager().getFuelFile().getInt("fuel."+fuel+".setting.add-time");
            e.setCancelled(true);
            UUID genOwner = GenUtil.getGenOwnerFromLoc(b.getLocation());


            if(!Main.getFileManager().getFuelFile().getBoolean("fuel."+fuel+".setting.enabled")
                && !p.hasPermission("unitygen.bypass")) {
                p.sendMessage(Lang.FUEL_DISABLED.get().replace("%tier%", fuel));
                return;
            }

            if (p.getUniqueId().equals(genOwner)) {
                String tier = LocationUtil.tierFromLocation(b.getLocation(), genOwner);

                if(Main.getInstance().getConfig().getString("generator."+tier+".expire") != null
                    && Main.getInstance().getConfig().getString("generator."+tier+".expire").equals("-1")){
                    p.sendMessage(Lang.CANNOT_USE_FUEL.get());
                    return;
                }

                for(String applicable : Main.getFileManager().getFuelFile().getStringList("fuel."+fuel+".setting.applicable")){
                    if(applicable.equalsIgnoreCase("ALL")) break;
                    if(!tier.equalsIgnoreCase(applicable)){
                        p.sendMessage(Lang.NOT_APPLICABLE.get().replace("%tier%", tier));
                        return;
                    }
                }
                if(Main.getFileManager().getFuelFile().getBoolean("fuel."+fuel+".setting.remove-on-use")) {
                    Integer val = p.getItemInHand().getAmount();
                    if (val > 1) p.getItemInHand().setAmount(val - 1);
                    else p.setItemInHand(new ItemStack(Material.AIR));
                }

                UGFuelEvent fuelEvent = new UGFuelEvent(p, tier, b.getLocation(), timeAdded);
                Bukkit.getPluginManager().callEvent(fuelEvent);
                return;

            }else {
                UUID owner = Bukkit.getOfflinePlayer(genOwner).getUniqueId();
                String tier = LocationUtil.tierFromLocation(b.getLocation(), genOwner);

                if(Main.getInstance().getConfig().getString("generator."+tier+".expire") != null
                    && Main.getInstance().getConfig().getString("generator."+tier+".expire").equals("-1")){
                    p.sendMessage(Lang.CANNOT_USE_FUEL.get());
                    return;
                }

                for(String applicable : Main.getFileManager().getFuelFile().getStringList("fuel."+fuel+".setting.applicable")){
                    if(applicable.equalsIgnoreCase("ALL")) break;
                    if(!tier.equalsIgnoreCase(applicable)){
                        p.sendMessage(Lang.NOT_APPLICABLE.get().replace("%tier%", tier));
                        return;
                    }
                }
                if(Main.getFileManager().getFuelFile().getBoolean("fuel."+fuel+".setting.remove-on-use")) {
                    Integer val = p.getItemInHand().getAmount();
                    if (val > 1) p.getItemInHand().setAmount(val - 1);
                    else p.setItemInHand(new ItemStack(Material.AIR));
                }

                UGFuelOtherEvent fuelOtherEvent = new UGFuelOtherEvent(p, tier, b.getLocation(), owner, timeAdded);
                Bukkit.getPluginManager().callEvent(fuelOtherEvent);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFuelEvent(UGFuelEvent e){
        Player p = e.getPlayer();

        ConfigManager cm = ConfigManager.getConfig(p);

        //File userFile = new File(Main.getInstance().getDataFolder() + File.separator + "user-data", p.getUniqueId() + ".yml");
        //YamlConfiguration yc = YamlConfiguration.loadConfiguration(userFile);

        long diff = cm.getConfig().getLong("generator."+LocationUtil.strToStrLocation(e.getLocation())+".expire") + TimeUnit.SECONDS.toMillis(e.getTimeAdded());
        diff = diff - System.currentTimeMillis();

        p.sendMessage(Lang.USED_FUEL.get().replace("%expire%", GenUtil.buildCountdown(diff)));
        diff = cm.getConfig().getLong("generator."+LocationUtil.strToStrLocation(e.getLocation())+".expire") + TimeUnit.SECONDS.toMillis(e.getTimeAdded());
        cm.getConfig().set("generator."+LocationUtil.strToStrLocation(e.getLocation())+".expire", diff);
        cm.saveConfig();
        cm.reload();
        //
    }

    @EventHandler(ignoreCancelled = true)
    public void onFuelOther(UGFuelOtherEvent e){
        Player p = e.getPlayer();
        Player owner = Bukkit.getOfflinePlayer(e.getOwner()).getPlayer();

        ConfigManager cm = ConfigManager.getConfig(owner);
        long diff = cm.getConfig().getLong("generator."+LocationUtil.strToStrLocation(e.getLocation())+".expire") + TimeUnit.SECONDS.toMillis(e.getTimeAdded());
        diff = diff - System.currentTimeMillis();

        p.sendMessage(Lang.USED_FUEL_OTHER.get().replace("%owner%", owner.getName()).replace("%expire%", GenUtil.buildCountdown(diff)));
        diff = cm.getConfig().getLong("generator."+LocationUtil.strToStrLocation(e.getLocation())+".expire") + TimeUnit.SECONDS.toMillis(e.getTimeAdded());
        cm.getConfig().set("generator."+LocationUtil.strToStrLocation(e.getLocation())+".expire", diff);
        cm.saveConfig();
        cm.reload();
        //
       //
    }
}
