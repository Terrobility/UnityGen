package io.chazza.unitygen.event.custom;

import io.chazza.unitygen.util.GenObject;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UGPlaceEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    
    private boolean cancelled;
    private Player player;
    private GenObject go;
    
    public UGPlaceEvent(Player player, GenObject go){
        this.player = player;
        this.go = go;
    }
    
    public Player getPlayer(){
        return player;
    }
    
    public String getGenerator(){
        return go.getTier();
    }

    public GenObject getObject(){return go;}

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
