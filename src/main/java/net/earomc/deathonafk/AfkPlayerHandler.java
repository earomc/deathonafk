package net.earomc.deathonafk;

import net.earomc.deathonafk.util.timer.ConditionedTimer;
import net.earomc.deathonafk.rareplayermoveevent.RarePlayerMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AfkPlayerHandler implements Listener {
    private final FileConfiguration configuration;
    private final Plugin plugin;
    private final Set<Player> afkPlayers;
    private final HashMap<Player, Integer> playerToSecondsLastMovedMap;
    private final ConditionedTimer afkPlayersUpdater;

    public AfkPlayerHandler(Plugin plugin) {
        this.plugin = plugin;
        this.configuration = plugin.getConfig();
        this.afkPlayers = new HashSet<>();
        this.playerToSecondsLastMovedMap = new HashMap<>();
        this.afkPlayersUpdater = new ConditionedTimer(plugin, 0, 20, () -> Bukkit.getOnlinePlayers().size() <= 0);
        this.afkPlayersUpdater.runEveryTick(() -> Bukkit.getOnlinePlayers().forEach(player -> {
            if (!isAfk(player)) {
                int newSecondsLastMoved = increaseSecondsLastMoved(player);
                //player.sendMessage("Seconds last moved: " + newSecondsLastMoved);
                if (newSecondsLastMoved >= configuration.getInt("seconds-before-afk")) {
                    setAfk(player, true);
                }
            }
            //Bukkit.broadcastMessage(player.getName() + " -> lastMoved -> " + getSecondsLastMoved(player) + " seconds.");
        }));
    }

    public boolean isAfk(Player player) {
        return afkPlayers.contains(player);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        playerToSecondsLastMovedMap.put(event.getPlayer(), 0);
        //Bukkit.broadcastMessage("Amount of players: " + Bukkit.getOnlinePlayers().size());
        if (afkPlayersUpdater.isRunning()) return;
        if (Bukkit.getOnlinePlayers().size() == 1) {
            afkPlayersUpdater.start();
        }
    }

    private int increaseSecondsLastMoved(Player player) {
        int newSecondsLastMoved = getSecondsLastMoved(player) + 1;
        playerToSecondsLastMovedMap.put(player, newSecondsLastMoved);
        return newSecondsLastMoved;
    }

    private void setAfk(Player player, boolean b) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (b) {
            afkPlayers.add(player);
            pluginManager.callEvent(new PlayerGoAfkEvent(player));
        } else {
            afkPlayers.remove(player);
            pluginManager.callEvent(new PlayerReturnFromAfkEvent(player));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        afkPlayers.remove(player);
        playerToSecondsLastMovedMap.remove(player);
    }

    public int getSecondsLastMoved(Player player) {
        return playerToSecondsLastMovedMap.get(player);
    }

    @EventHandler
    public void onPlayerGoAfk(PlayerGoAfkEvent event) {
        if (!event.isCancelled()) {
            event.getPlayer().setHealth(0);
        }
    }

    @EventHandler
    public void onPlayerComeBack(PlayerReturnFromAfkEvent event) {

    }

    @EventHandler
    public void onPlayerMove(RarePlayerMoveEvent event) {
        //Bukkit.broadcastMessage("RarePlayerMoveEvent called for player " + event.getPlayer().getName());
        Player player = event.getPlayer();
        if (isAfk(player)) {
            setAfk(player, false);
        }
        playerToSecondsLastMovedMap.put(player, 0);

    }
}
