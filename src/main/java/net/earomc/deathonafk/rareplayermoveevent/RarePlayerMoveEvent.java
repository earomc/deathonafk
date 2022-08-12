package net.earomc.deathonafk.rareplayermoveevent;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author earomc
 * Created on August 12, 2022 | 22:11:23
 * ʕっ•ᴥ•ʔっ
 * An alternative to the PlayerMoveEvent which is called less frequently.
 * Meant to be used if there is a more performance-demanding task that needs to be invoked
 * by a PlayerMoveEvent.
 *
 */

public class RarePlayerMoveEvent extends PlayerMoveEvent {

    private static final HandlerList handlerList = new HandlerList();

    public RarePlayerMoveEvent(@NotNull Player player, @NotNull Location from, @Nullable Location to) {
        super(player, from, to);
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
