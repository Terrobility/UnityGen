package io.chazza.unitygen.event;

import io.chazza.unitygen.Main;
import io.chazza.unitygen.UnityGenAPI;
import io.chazza.unitygen.util.ColorUtil;
import io.chazza.unitygen.util.GenUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class GUIClickEvent implements Listener {

    Plugin plugin;

    public GUIClickEvent(Main plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onInvClose(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();

        String guiName = ColorUtil.translate(Main.getFileManager().getMsgFile().getString("gui-message.view.gui-name"));
        
        if(e.getInventory().getName().equals(guiName)){
            p.updateInventory();
        }
    }

    @EventHandler
    public void onGUIClick(InventoryClickEvent e){
        if(e.isCancelled()) return;
        Player p = (Player) e.getWhoClicked();
        String guiName = ColorUtil.translate(Main.getFileManager().getMsgFile().getString("gui-message.view.gui-name"));

       
        if(e.getClickedInventory() != null && e.getClickedInventory().getName().equals(guiName)){
            e.setCancelled(true);
            ItemStack clickedItem = e.getCurrentItem();
            if(clickedItem == null) return;
            if(clickedItem.getType() != Material.AIR){
                if(clickedItem.getItemMeta().hasDisplayName()){
                    String tier = GenUtil.getItemStackTier(clickedItem);
                    if(tier==null) return;

                    if(e.isLeftClick()){
                        UnityGenAPI.giveGenerator(p, tier, 1);
                    }else{
                        boolean isEnabled = plugin.getConfig().getBoolean("generator."+tier+".enabled");
                        if(isEnabled){                            
                            plugin.getConfig().set("generator."+tier+".enabled", false);
                            plugin.saveConfig();
                            isEnabled = false;
                        }else{
                            plugin.getConfig().set("generator."+tier+".enabled", true);
                            plugin.saveConfig();
                            isEnabled = true;
                        }
                        Integer interval = plugin.getConfig().getInt("generator."+tier+".time");
                        List<String> lore =  new ArrayList<>();
                        List<String> genLore = ColorUtil.translate(plugin.getConfig().getStringList("generator."+tier+".lore"));
                        List<String> interfaceLore = ColorUtil.translate(Main.getFileManager().getMsgFile().getStringList("gui-message.view.interface-lore"));

                        for(String newLore : interfaceLore){
                            if(newLore.equals("%gen-lore%")){
                                for(String loreList : genLore){
                                    lore.add(loreList.replace("%time%", interval.toString()).replace("%tier%", String.valueOf(isEnabled)));
                                }
                            }else{
                                plugin.reloadConfig();
                                lore.add(newLore.replace("%tier%", tier).replace("%status%", String.valueOf(isEnabled)));   
                            }
                        }

                        ItemStack is = new ItemStack(clickedItem.getType());
                        ItemMeta im = is.getItemMeta();
                        im.setDisplayName(clickedItem.getItemMeta().getDisplayName());
                        im.setLore(lore);
                        is.setItemMeta(im);
                        e.getClickedInventory().setItem(e.getSlot(), is);
                        p.updateInventory();
                        plugin.reloadConfig();
                    }
                }
            }
        }


    }
}
