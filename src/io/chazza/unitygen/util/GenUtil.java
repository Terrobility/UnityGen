package io.chazza.unitygen.util;

import io.chazza.unitygen.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GenUtil {

    public static boolean canItemFit(Inventory inv, ItemStack i) {
      HashMap<Integer, ItemStack>not = inv.addItem(i);
      if(!not.isEmpty()){
           inv.addItem(i);
       }
       return not.isEmpty();
    }
    public static String getCaseGenName(String tier) {
        for (String gens : Main.getInstance().getConfig().getConfigurationSection("generator").getKeys(false)) {
            if (gens.equalsIgnoreCase(tier))
                return gens;
        }
        return null;
    }

    public final static String getItemStackTier(ItemStack item) {
        if(item==null) return null;
        ItemMeta meta = item.getItemMeta();

        for (String parent : Main.getInstance().getConfig().getConfigurationSection("generator").getKeys(false)) {
            String name = ColorUtil.translate(Main.getInstance().getConfig().getString("generator." + parent + ".name").replace("%generator%", parent));

            ArrayList<String> lore = new ArrayList<>();
            for(String l : Main.getInstance().getConfig().getStringList("generator."+parent+".lore")) {
                lore.add(ColorUtil.translate(l).replace("%time%", String.valueOf(Main.getInstance().getConfig().getInt("generator." + parent + ".time"))));
            }

            if(item.hasItemMeta() && meta.hasDisplayName() && meta.hasLore()){
                if(meta.getDisplayName().equals(name)){
                    if(meta.getLore().equals(lore)){
                        return parent;
                    }
                }
            }
        }
        return null;
    }

    public final static boolean isLocAGen(Location loc) {
        File userFiles = new File(Main.getInstance().getDataFolder() + File.separator + "user-data");
        if(!userFiles.exists()) return false;
        for (File file : userFiles.listFiles()) {
            if(!file.getName().contains(".yml")) continue;

            YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
            if(yc.getString("generator."+LocationUtil.strToStrLocation(loc)+".tier") != null){
                return true;

            }
        }
        return false;
    }
    public final static UUID getGenOwnerFromLoc(Location loc) {
        String locStr = LocationUtil.strToStrLocation(loc);
        File[] userFiles = new File(Main.getInstance().getDataFolder() + File.separator + "user-data").listFiles();
        for (File file : userFiles) {
            YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
            if(yc.getConfigurationSection("generator."+locStr) != null){
                UUID strToUUID = UUID.fromString(file.getName().replace(".yml", ""));
                return strToUUID;
            }
        }
        return null;
    }
    public static ItemStack createGeneratorIS(String tier, Integer amount){
        String genName = ColorUtil
            .translate(Main.getInstance().getConfig().getString("generator." + tier + ".name"));
        Material itemType = Material
            .valueOf(Main.getInstance().getConfig().getString("generator." + tier + ".block"));
        Integer interval = Main.getInstance().getConfig().getInt("generator." + tier + ".time");
        ItemStack is = new ItemStack(itemType);
        ItemMeta im = is.getItemMeta();

        List<String> l = ColorUtil
            .translate(Main.getInstance().getConfig().getStringList("generator." + tier + ".lore"));
        List<String> lore = new ArrayList<>();

        for (String lores : l) {
            lores = lores.replace("%time%", interval.toString()).replace("%tier%", tier);
            lore.add(lores);
        }
        im.setDisplayName(genName);
        im.setLore(lore);
        is.setItemMeta(im);

        is.setAmount(amount);
        return is;
    }
    public static void dealItemDrop(Player p, String tier){
        ItemStack is = createGeneratorIS(tier, 1);
        if (Main.getInstance().getConfig().getBoolean("general.generatorItemDrop")) {
            p.getLocation().getWorld().dropItem(p.getLocation(), is);
        } else {
            if (!canItemFit(p.getInventory(), is)) {
                p.getWorld().dropItem(p.getLocation(), is);
            }
        }
    }


    public static String buildCountdown(Long diff){
        String str = "";

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        hours = hours % 24;
        minutes = minutes % 60;
        seconds = seconds % 60;

        if(days > 0){
            if(days == 1) str += days + Main.getFileManager().getFormat("day") + " ";
            else str += days + Main.getFileManager().getFormat("days") + " ";
        }
        if(hours > 0){
            if(hours == 1) str += hours + Main.getFileManager().getFormat("hour") + " ";
            else str += hours + Main.getFileManager().getFormat("hours") + " ";
        }
        if(minutes > 0){
            if(minutes == 1) str += minutes + Main.getFileManager().getFormat("minute");
            else str += minutes + Main.getFileManager().getFormat("minutes") + " ";
        }
        if(seconds > 0){
            if(seconds == 1) str += seconds + Main.getFileManager().getFormat("second") + " ";
            else str += seconds + Main.getFileManager().getFormat("seconds") + " ";
        }

        return str;
    }
}
