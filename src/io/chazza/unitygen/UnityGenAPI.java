package io.chazza.unitygen;

import io.chazza.unitygen.util.ColorUtil;
import io.chazza.unitygen.util.ConfigManager;
import io.chazza.unitygen.util.GenUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnityGenAPI {

    public static List<String> getPlayerGens(UUID UUID) {
        ConfigManager cm = ConfigManager.getConfig(UUID);
        FileConfiguration f = cm.getConfig();
        return f.getStringList("generator");
    }

    public static Location strToLocation(String str) {
        if (str == null || str.trim() == "") {
            return null;
        }
        final String[] parts = str.split(",");
        if (parts.length == 5) {
            final World w = Bukkit.getServer().getWorld(parts[0]);
            final int x = Integer.parseInt(parts[1]);
            final int y = Integer.parseInt(parts[2]);
            final int z = Integer.parseInt(parts[3]);
            return new Location(w, x, y, z);
        }
        return null;
    }

    public static ItemStack getItemStack(String tier, Integer amount){
        return GenUtil.createGeneratorIS(tier, amount);
    }

    public static boolean isValidTier(String tier){
        if(Main.getInstance().getConfig().getString("generator."+tier) != null){
            return true;
        }
        return false;
    }
    public static void giveGenerator(Player p, String tier, Integer amount) {
        p.getInventory().addItem(GenUtil.createGeneratorIS(tier, amount));
    }

    public static Integer getPlayerLimit(Player p){
        if(!p.hasPermission("unitygen.limit.bypass")){
            for(String permList : Main.getInstance().getConfig().getConfigurationSection("general.limit").getKeys(false)){
                if(p.hasPermission("unitygen.limit."+permList)){
                    return Main.getInstance().getConfig().getInt("general.limit."+permList);
                }
            }
        }else return -1;
        return 0;
    }

    public static boolean isItemStackGen(ItemStack is){
        if((is == null) || (!is.hasItemMeta()) || (!is.getItemMeta().hasDisplayName())) return false;

        String tier = GenUtil.getItemStackTier(is);
        if(tier==null) return false;

        ItemStack item = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("generator."+tier+".block")));
        item.setAmount(is.getAmount());
        ItemMeta meta = item.getItemMeta();

        ArrayList<String> lore = new ArrayList<>();
        for(String l : Main.getInstance().getConfig().getStringList("generator."+tier+".lore")){
            lore.add(ColorUtil.translate(l).replace("%tier%", tier).replace("%time%", String.valueOf(Main.getInstance().getConfig().getInt("generator."+tier+".time"))));
        }

        meta.setDisplayName(ColorUtil.translate(Main.getInstance().getConfig().getString("generator."+tier+".name")));
        meta.setLore(lore);
        item.setItemMeta(meta);

        if(item.isSimilar(is)) return true;

        return false;
    }
}
