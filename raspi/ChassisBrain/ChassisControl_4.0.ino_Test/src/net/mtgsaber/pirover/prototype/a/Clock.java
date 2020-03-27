package net.mtgsaber.pirover.prototype.a;

import java.util.ArrayList;

/**
 * Author: Andrew Arnold (4/25/2018)
 */
public class Clock implements Runnable {
    public interface Tickable {
        void tick();
    }

    public final long waitTime;

    private boolean on;
    private final ArrayList<Tickable> tickables;

    public Clock(long waitTime) {
        this.waitTime = waitTime;
        tickables = new ArrayList<>();
    }

    @Override
    public void run() {
        while (on) {
            long startTime = System.currentTimeMillis();
            long nextCycleTime = startTime + waitTime;

            synchronized (this) {
                for (Tickable tickable : tickables)
                    tickable.tick();
            }

            while (System.currentTimeMillis() < nextCycleTime && on) ;
        }
    }

    public void addTickable(Tickable tickable) {
        synchronized (this) {
            tickables.add(tickable);
        }
    }

    public void removeTickable(Tickable tickable) {
        if (tickables.contains(tickable))
            synchronized (this) {
                tickables.remove(tickable);
            }
    }

    public void start() {
        on = true;
    }
    public void stop() {
        on = false;
    }
    public boolean isOn() {
        return on;
    }
}
