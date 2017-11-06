package io.chazza.unitygen.event;

import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.event.custom.UGCommandGenerateEvent;
import io.chazza.unitygen.event.custom.UGItemGenerateEvent;
import io.chazza.unitygen.util.ConfigManager;
import io.chazza.unitygen.util.GenUtil;
import io.chazza.unitygen.util.HolographicUtil;
import io.chazza.unitygen.util.LocationUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * Created by charliej on 07/05/2017.
 */
public class GenerateEvent implements Listener {

    Plugin plugin;
    public GenerateEvent(Main plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onGeneration(UGItemGenerateEvent e){
        Player p = e.getPlayer();
        String tier = e.getGenerator();
        Integer amount = e.getAmount();
        if (!Main.getInstance().getConfig().getString("generator." + tier + ".generatorSound." + ".sound").isEmpty()) {
            Sound sound = Sound.valueOf(Main.getInstance().getConfig().getString("generator." + tier
                + ".generatorSound." + ".sound").toUpperCase());
            Integer volume = Main.getInstance().getConfig().getInt("generator."
                + tier + ".generatorSound." + ".volume");
            Integer pitch = Main.getInstance().getConfig().getInt("generator."
                + tier + ".generatorSound." + ".pitch");
            if(p.isOnline()) {
                p.playSound(p.getLocation(), sound, volume, pitch);
            }
        }
        if(GenUtil.canItemFit(e.getInventory(), e.getItemStack())){
            if(Main.getInstance().getConfig().getBoolean("general.tellPlayerOnGeneration")){
                p.sendMessage(Lang.GENERATED.get().replace("%tier%", tier)
                    .replace("%item%", WordUtils.capitalizeFully(e.getItemStack().getType().name().replace("_", " ")))
                    .replace("%amount%", amount.toString()));
            }
        }else{
            if(Main.getInstance().getConfig().getBoolean("general.tellPlayerWhenFull")){
                p.sendMessage(Lang.GENERATOR_FULL.get().replace("%tier%", tier)
                    .replace("%amount%", amount.toString()));
            }
        }
        ConfigManager cm = ConfigManager.getConfig(e.getPlayer());
        if(Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            HolographicUtil.update(e.getObject().getHologram(), e.getGenerator(), cm, LocationUtil.strToStrLocation(e.getLocation()));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGeneration(UGCommandGenerateEvent e){
        Player p = e.getPlayer();
        String tier = e.getGenerator();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), e.getCommand());

        if (!Main.getInstance().getConfig().getString("generator." + tier + ".generatorSound." + ".sound").isEmpty()) {
            Sound sound = Sound.valueOf(Main.getInstance().getConfig().getString("generator." + tier
                + ".generatorSound." + ".sound").toUpperCase());
            Integer volume = Main.getInstance().getConfig().getInt("generator."
                + tier + ".generatorSound." + ".volume");
            Integer pitch = Main.getInstance().getConfig().getInt("generator."
                + tier + ".generatorSound." + ".pitch");

            p.playSound(p.getLocation(), sound, volume, pitch);
        }

    }
}
