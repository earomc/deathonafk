package net.earomc.deathonafk.util.timer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

public class ConditionedTimer implements Timer {

    @NotNull
    private final Plugin plugin;
    private BukkitTask task;
    protected long delay1, delay2;
    protected boolean isRunning = false;
    @Nullable
    protected BooleanSupplier stoppingCondition;
    @Nullable
    protected Runnable onStopRunnable;
    @Nullable
    protected Runnable onStartRunnable;
    @Nullable
    private Runnable everyTickConsumer;


    public ConditionedTimer(@NotNull Plugin plugin, long delay1, long delay2, @Nullable BooleanSupplier stoppingCondition) {
        this.plugin = plugin;
        this.delay1 = delay1;
        this.delay2 = delay2;
        this.stoppingCondition = stoppingCondition;
    }

    @Override
    public boolean start() {
        if (!isRunning) {
            isRunning = true;
            if (onStartRunnable != null) {
                onStartRunnable.run();
            }
            task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                runEveryTick();
                if (stoppingCondition != null) {
                    if (stoppingCondition.getAsBoolean()) {
                        task.cancel();
                    }
                }
            }, delay1, delay2);
            return true;
        }
        return false;
    }

    public void setStoppingCondition(@Nullable BooleanSupplier stoppingCondition) {
        this.stoppingCondition = stoppingCondition;
    }

    /**
     * @return Returns true if the timer was stopped.
     */
    public final boolean stop() {
        if (cancel()) {
            if (onStopRunnable != null) {
                onStopRunnable.run();
            }
            return true;
        }
        return false;
    }

    /**
     * Just cancels the task and does not run the onStopConsumer
     */

    public final boolean cancel() {
        if (isRunning) {
            isRunning = false;
            task.cancel();
            return true;
        }
        return false;
    }

    public final boolean isRunning() {
        return isRunning;
    }

    public void onStop(Runnable runnable) {
        this.onStopRunnable = runnable;
    }

    public void onStart(Runnable runnable) {
        this.onStartRunnable = runnable;
    }

    public void runEveryTick(Runnable runEveryTick) {
        this.everyTickConsumer = runEveryTick;
    }

    protected void runEveryTick() {
        if (everyTickConsumer != null) {
            everyTickConsumer.run();
        }
    }

}
