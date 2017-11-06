package io.chazza.unitygen.event.custom;

import io.chazza.unitygen.util.GenObject;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class UGItemGenerateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private Player player;
    private String generator;
    private Material material;
    private Integer amount;
    private Integer data;
    private ItemStack is;
    private Location location;
    private Inventory inv;
    private GenObject go;

    public UGItemGenerateEvent(Player player, String generator, ItemStack is, Location loc, Inventory inv, GenObject go){

        this.player = player;
        this.generator = generator;
        this.is = is;
        this.material = is.getType();
        this.data = Integer.valueOf(is.getDurability());
        this.amount = is.getAmount();
        this.location = loc;
        this.inv = inv;
        this.go = go;
    }

    public Player getPlayer(){
        return player;
    }
    public String getGenerator(){
        return generator;
    } 
    public Material getMaterial(){
        return material;
    } 
    public Integer getData(){
        return data;
    }
    public Integer getAmount(){
        return amount;
    }

    public boolean setItemStack(ItemStack is){
        this.is = is;
        return true;
    }
    public GenObject getObject(){
        return go;
    }
    public ItemStack getItemStack(){return is;}
    public Location getLocation(){return location;}
    public Inventory getInventory(){return inv;}

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
