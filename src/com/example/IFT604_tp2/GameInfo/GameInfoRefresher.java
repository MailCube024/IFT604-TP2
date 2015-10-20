package com.example.IFT604_tp2.GameInfo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Michael Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class GameInfoRefresher {
    private ScheduledFuture<?> schedule;
    private ScheduledExecutorService executor;
    private int interval;
    private GameInfoActivity activity;

    public GameInfoRefresher(int minutes, GameInfoActivity activity) {
        this.activity = activity;
        interval = minutes;
        executor = Executors.newSingleThreadScheduledExecutor();
        schedule = executor.scheduleWithFixedDelay(new GameInfoRefreshTask(activity), interval, interval, TimeUnit.MINUTES);
    }

    public void Reset() {
        schedule.cancel(true);
        schedule = executor.scheduleWithFixedDelay(new GameInfoRefreshTask(activity), interval, interval, TimeUnit.MINUTES);
    }

    public void Stop() {
        schedule.cancel(true);
        executor.shutdownNow();
    }
}
