package net.earomc.deathonafk.rareplayermoveevent;

import net.earomc.deathonafk.util.timer.ConditionedTimer;
import net.earomc.deathonafk.util.timer.Timer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author earomc
 * Created on August 12, 2022 | 22:15:14
 * ʕっ•ᴥ•ʔっ
 */

public class RarePlayerMoveEventCaller implements Timer {

    private final Plugin plugin;
    private final ConditionedTimer timer;
    private final Map<Player, SimpleLocation> playerToLastLocationMap = new HashMap<>();

    public RarePlayerMoveEventCaller(Plugin plugin, long checkInterval) {
        this.plugin = plugin;
        this.timer = new ConditionedTimer(plugin, checkInterval, checkInterval, () -> !isRunning());
        this.timer.runEveryTick(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                SimpleLocation from = playerToLastLocationMap.get(player);
                SimpleLocation to = new SimpleLocation(player);
                if (!to.equals(from)) {
                    //player has moved since last check.
                    playerToLastLocationMap.put(player, to);
                    World world = player.getWorld();
                    Bukkit.getPluginManager().callEvent(new RarePlayerMoveEvent(player, from == null ? to.toBukkit(world) : from.toBukkit(world), to.toBukkit(world)));
                }
            }
        });
    }

    private record SimpleLocation(double x, double y, double z, float yaw, float pitch) {
        public SimpleLocation(Location location) {
            this(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        public SimpleLocation(Player player) {
            this(player.getLocation());
        }

        @NotNull
        public Location toBukkit(@Nullable World world) {
            return new Location(world, x, y, z, yaw, pitch);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SimpleLocation that = (SimpleLocation) o;
            if (Double.compare(that.x, x) != 0) return false;
            if (Double.compare(that.y, y) != 0) return false;
            if (Double.compare(that.z, z) != 0) return false;
            if (Float.compare(that.yaw, yaw) != 0) return false;
            return Float.compare(that.pitch, pitch) == 0;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(x);
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(y);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(z);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            result = 31 * result + (yaw != +0.0f ? Float.floatToIntBits(yaw) : 0);
            result = 31 * result + (pitch != +0.0f ? Float.floatToIntBits(pitch) : 0);
            return result;
        }
    }

    @Override
    public boolean start() {
        return timer.start();
    }

    @Override
    public boolean stop() {
        return timer.stop();
    }

    @Override
    public boolean isRunning() {
        return this.plugin.isEnabled();
    }
}
