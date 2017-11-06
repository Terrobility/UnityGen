package io.chazza.unitygen.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {

    private Inventory inv;
    private InventoryHolder ih;
    private String guiName;
    private Integer guiSize;
   
    
    public InventoryUtil(Player p, String guiName, Integer size){
        this.ih = p;
        this.guiName = guiName;
        this.guiSize = size;
        inv = Bukkit.createInventory(p, size, guiName);
    }
    
    public void addItem(ItemStack is){
        inv.addItem(is);
    }
    public void removeItem(ItemStack is){
        inv.removeItem(is);
    }
    public Integer getSize(){
        return guiSize;
    }
    public String getName(){
        return ColorUtil.translate(guiName);
    }
    public InventoryHolder getHolder(){
        return ih;
    }
    public Inventory getInventory(){
        return inv;
    }
}
