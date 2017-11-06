package io.chazza.unitygen.util;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.chazza.unitygen.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by charliej on 07/05/2017.
 */
public class HolographicUtil {

    public static Hologram createHolo(GenObject go, String expiry){
        Integer interval = Main.getInstance().getConfig().getInt("generator." + go.getTier() + ".time");
        List<String> hologram = ColorUtil.translate(Main.getInstance().getConfig().getStringList("generator."+go.getTier()+".hologram"));
        if(hologram != null){
            Location hologramHeight = go.getLocation().add(0.5D, go.getHologramHeight(), 0.5D);
            Hologram genHologram = HologramsAPI.createHologram(Main.getInstance(), hologramHeight);
            for(String holoLine : hologram){
                if (holoLine.toUpperCase().contains("ICON:")) {
                    Material icon = Material.valueOf(holoLine.replace("ICON: ", ""));
                    genHologram.appendItemLine(new ItemStack(icon));
                }else{
                    genHologram.appendTextLine(ColorUtil.translate(holoLine.replace("%player%", go.getOwnerName())
                        .replace("%type%", go.getTier()).replace("%time%", interval.toString())
                        .replace("%expire%", expiry)));
                }
            }
            go.setHologram(genHologram);
            return genHologram;
        }
        return null;
    }

    public static Hologram createHolo(GenObject go){
        Integer interval = Main.getInstance().getConfig().getInt("generator." + go.getTier() + ".time");
        List<String> hologram = ColorUtil.translate(Main.getInstance().getConfig().getStringList("generator."+go.getTier()+".hologram"));
        if(hologram != null){
            Location hologramHeight = go.getLocation().add(0.5D, go.getHologramHeight(), 0.5D);
            Hologram genHologram = HologramsAPI.createHologram(Main.getInstance(), hologramHeight);
            for(String holoLine : hologram){
                if (holoLine.toUpperCase().contains("ICON:")) {
                    Material icon = Material.valueOf(holoLine.replace("ICON: ", ""));
                    genHologram.appendItemLine(new ItemStack(icon));
                }else{
                    genHologram.appendTextLine(ColorUtil.translate(holoLine.replace("%player%", go.getOwnerName())
                        .replace("%type%", go.getTier()).replace("%time%", interval.toString())));
                }
            }
            go.setHologram(genHologram);
            return genHologram;
        }
        return null;
    }

    public static void update(Hologram holo, String tier, ConfigManager cm, String loc){
        if(holo.isDeleted()) return;

        long diff = cm.getConfig().getLong("generator."+loc+".expire") - System.currentTimeMillis();

        int count = 0;

        for(String lines : Main.getInstance().getConfig().getStringList("generator."+tier+".hologram")){
            if(lines.contains("%expire%")){
                holo.removeLine(count);
                holo.insertTextLine(count, ColorUtil.translate(lines.replace("%expire%", GenUtil.buildCountdown(diff))));
            }
            count++;
        }
    }
}
