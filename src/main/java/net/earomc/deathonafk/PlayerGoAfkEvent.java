package net.earomc.deathonafk;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerGoAfkEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();
    private boolean cancelled = false;


    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public PlayerGoAfkEvent(Player player) {
        super(player);
    }
}
