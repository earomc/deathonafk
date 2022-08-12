package net.earomc.deathonafk;

import net.earomc.deathonafk.rareplayermoveevent.RarePlayerMoveEventCaller;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeathOnAFK extends JavaPlugin {

    private AfkPlayerHandler afkPlayerHandler;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        RarePlayerMoveEventCaller rarePlayerMoveEventCaller = new RarePlayerMoveEventCaller(this, 20L);
        rarePlayerMoveEventCaller.start();
        this.afkPlayerHandler = new AfkPlayerHandler(this);
    }

    @Override
    public void onDisable() {
    }

    public AfkPlayerHandler getAfkPlayerHandler() {
        return afkPlayerHandler;
    }
}
