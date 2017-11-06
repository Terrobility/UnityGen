package io.chazza.unitygen.event.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UGCommandGenerateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private Player player;
    private String generator;
    private String cmd;

    public UGCommandGenerateEvent(Player player, String generator, String cmd){

        this.player = player;
        this.generator = generator;
        this.cmd = cmd;
    }

    public Player getPlayer(){
        return player;
    }
    public String getGenerator(){
        return generator;
    }
    public String getCommand(){
        return cmd.replace("%player%", player.getName()).replace("%tier%", generator);
    }

    public String getRawCommand(){
        return cmd;
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
