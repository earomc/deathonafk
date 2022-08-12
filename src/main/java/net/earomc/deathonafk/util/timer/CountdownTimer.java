package net.earomc.deathonafk.util.timer;

import org.bukkit.plugin.Plugin;

import java.util.function.Predicate;

public class CountdownTimer<T> extends ConditionedTimer {

    private int count;
    private int stopAt;

    public CountdownTimer(Plugin plugin, int startValue, int stopAt, long delay1, long delay2, T t, Predicate<T> stoppingCondition) {
        super(plugin, delay1, delay2, t, stoppingCondition);
        this.count = startValue;
        this.stopAt = stopAt;
    }

    public CountdownTimer(Plugin plugin, int startValue, long delay1, long delay2, T t, Predicate<T> stoppingCondition) {
        this(plugin, startValue, 0, delay1, delay2, t, stoppingCondition);
    }

    public CountdownTimer(Plugin plugin, int startValue, long delay1, long delay2, T t) {
        this(plugin, startValue, 0, delay1, delay2, t, null);
    }

    public CountdownTimer(Plugin plugin, int startValue, int stopAt, long delay1, long delay2) {
        this(plugin, startValue, stopAt, delay1, delay2, null, null);
    }

    public CountdownTimer(Plugin plugin, int startValue, long delay1, long delay2) {
        this(plugin, startValue, 0, delay1, delay2, null, null);
    }

    @Override
    protected void runEveryTick() {
        super.runEveryTick();
        if (count <= stopAt) {
            stop();
        } else {

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
