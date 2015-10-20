package com.example.IFT604_tp2.GameInfo;

/**
 * Michael Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class GameInfoRefreshTask implements Runnable {
    private final GameInfoActivity activity;

    public GameInfoRefreshTask(GameInfoActivity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        activity.invokeGameInfoRefresh();
    }
}
