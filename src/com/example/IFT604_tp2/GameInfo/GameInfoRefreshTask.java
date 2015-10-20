package com.example.IFT604_tp2.GameInfo;

/**
 * Created by Michaël on 10/16/2015.
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
