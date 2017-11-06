package io.chazza.unitygen.event.custom;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UGFuelEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private Player player;
    private String generator;
    private Location l;
    private Integer addedTime;

    public UGFuelEvent(Player player, String generator, Location l, Integer addedTime){
        this.player = player;
        this.generator = generator;
        this.l = l;
        this.addedTime = addedTime;
    }
    
    public Player getPlayer(){
        return player;
    }

    public String getGenerator(){
        return generator;
    }
    public Location getLocation(){
        return l;
    }
    public Integer getTimeAdded(){return addedTime;}

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
