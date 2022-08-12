package net.earomc.deathonafk;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerReturnFromAfkEvent extends PlayerEvent {

    private static final HandlerList handlerList = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public PlayerReturnFromAfkEvent(Player player) {
        super(player);
    }

}
