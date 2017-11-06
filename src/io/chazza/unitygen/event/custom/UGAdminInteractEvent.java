package io.chazza.unitygen.event.custom;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class UGAdminInteractEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private Player player;
    private String generator;
    private UUID owner;
    private Location l;

    public UGAdminInteractEvent(Player player, String generator, UUID owner, Location l){
        this.player = player;
        this.generator = generator;
        this.owner = owner;
        this.l = l;
    }
    
    public Player getPlayer(){
        return player;
    }

    public String getGenerator(){
        return generator;
    }
    public UUID getOwner(){
        return owner;
    }
    public Location getLocation(){
        return l;
    }

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
