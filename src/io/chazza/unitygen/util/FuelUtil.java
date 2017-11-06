package io.chazza.unitygen.util;

import io.chazza.unitygen.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by charliej on 21/05/2017.
 */
public class FuelUtil {

    public static ItemStack createFuel(String tier, int amount){
        FileConfiguration instance = Main.getFileManager().getFuelFile();

        String genName = ColorUtil
            .translate(instance.getString("fuel." + tier + ".item.name"));
        Material itemType = Material
            .valueOf(instance.getString("fuel." + tier + ".item.item"));
        Integer addTime = instance.getInt("fuel." + tier + ".setting.add-time");
        ItemStack is = new ItemStack(itemType);
        ItemMeta im = is.getItemMeta();
        int data = instance.getInt("fuel." + tier + ".item.data");

        List<String> l = ColorUtil
            .translate(instance.getStringList("fuel." + tier + ".item.lore"));
        List<String> lore = new ArrayList<String>();

        for (String lores : l) {
            lores = lores.replace("%tier%", tier).replace("%time%", addTime+"");
            lore.add(lores);
        }
        im.setDisplayName(genName);
        im.setLore(lore);
        is.setItemMeta(im);
        is.setDurability(Short.valueOf(data+""));

        is.setAmount(amount);
        return is;
    }
    public static String getCaseGenName(String tier) {
        for (String fuels : Main.getFileManager().getFuelFile().getConfigurationSection("fuel").getKeys(false)) {
            if (fuels.equalsIgnoreCase(tier))
                return fuels;
        }
        return null;
    }

    public final static String getItemStackTier(ItemStack item) {
        if(item==null) return null;
        ItemMeta meta = item.getItemMeta();
        ItemStack is;
        for (String parent :Main.getFileManager().getFuelFile().getConfigurationSection("fuel").getKeys(false)) {
            String name = ColorUtil.translate(Main.getFileManager().getFuelFile().getString("fuel." + parent + ".item.name").replace("%tier%", parent));
            Material mat = Material.valueOf(Main.getFileManager().getFuelFile().getString("fuel." + parent + ".item.item"));

            ArrayList<String> lore = new ArrayList<>();
            for(String l : Main.getFileManager().getFuelFile().getStringList("fuel."+parent+".item.lore")) {
                lore.add(ColorUtil.translate(l).replace("%time%",
                    String.valueOf(Main.getFileManager().getFuelFile().getInt("fuel." + parent + ".setting.add-time"))));
            }
            is = new ItemStack(mat);
            is.setAmount(item.getAmount());
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(name);
            im.setLore(lore);
            is.setItemMeta(im);
            if(is.isSimilar(item)) return parent;
        }
        return null;
    }
}
