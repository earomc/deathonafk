package net.earomc.deathonafk.util.timer;

import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

public class CountdownTimer<T> extends ConditionedTimer {

    private int count;
    private int stopAt;

    public CountdownTimer(Plugin plugin, int startValue, int stopAt, long delay1, long delay2, BooleanSupplier stoppingCondition) {
        super(plugin, delay1, delay2, stoppingCondition);
        this.count = startValue;
        this.stopAt = stopAt;
    }

    public CountdownTimer(Plugin plugin, int startValue, long delay1, long delay2, T t, BooleanSupplier stoppingCondition) {
        this(plugin, startValue, 0, delay1, delay2, stoppingCondition);
    }

    public CountdownTimer(Plugin plugin, int startValue, long delay1, long delay2, T t) {
        this(plugin, startValue, 0, delay1, delay2, null);
    }

    public CountdownTimer(Plugin plugin, int startValue, int stopAt, long delay1, long delay2) {
        this(plugin, startValue, stopAt, delay1, delay2, null);
    }

    public CountdownTimer(Plugin plugin, int startValue, long delay1, long delay2) {
        this(plugin, startValue, 0, delay1, delay2, null);
    }

    @Override
    protected void runEveryTick() {
        super.runEveryTick();
        if (count <= stopAt) {
            stop();
        }
        count--;
    }

    public void stopAt(int stopAt) {
        this.stopAt = stopAt;
    }

    public int getCount() {
        return count;
    }

    public int getStopAt() {
        return stopAt;
    }
}
