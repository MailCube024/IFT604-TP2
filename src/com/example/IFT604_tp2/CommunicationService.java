package com.example.IFT604_tp2;

import HockeyLive.Client.Communication.Client;
import HockeyLive.Client.Listeners.GameInfoUpdateListener;
import HockeyLive.Client.Listeners.GameListUpdateListener;
import HockeyLive.Client.Listeners.GoalNotificationListener;
import HockeyLive.Client.Listeners.PenaltyNotificationListener;
import HockeyLive.Common.Models.*;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by Benoit on 2015-10-17.
 */
public class CommunicationService extends Service {

    private final IBinder binder = new CommunicationBinder();
    private Callbacks activity;
    private Client client;

    @Override
    public void onCreate() {
        super.onCreate();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress serverAddress = InetAddress.getByName("192.168.2.17");
                    client = new Client(serverAddress);
                    client.Start();

                    PenaltyNotificationListener penaltyListener = new PenaltyNotificationListener() {
                        @Override
                        public void NewPenalty(Penalty penalty, Side side, Game game) {
                            if(activity == null) {
                                ShowPenalty(penalty, side, game);
                            }
                        }
                    };

                    GoalNotificationListener goalListener = new GoalNotificationListener() {
                        @Override
                        public void NewGoal(Goal goal, Side side, Game game) {
                            if(activity == null) {
                                ShowGoal(goal, side, game);
                            }
                        }
                    };

                    client.AddPenaltyNotificationListener(penaltyListener);
                    client.AddGoalNotificationListener(goalListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("CommunicationService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        client.Close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void registerClient(Activity activity) {
        this.activity = (Callbacks) activity;
    }

    public void unregisterClient(Activity activity) {
        if (this.activity.equals(activity))
            this.activity = null;
    }

    public void AddGameListUpdateListener(GameListUpdateListener listener) {
        client.AddGameListUpdateListener(listener);
    }

    public void AddGameInfoUpdateListener(GameInfoUpdateListener listener) {
        client.AddGameInfoUpdateListener(listener);
    }

    public void SendBet(Bet bet) {
        //client.SendBet(bet);
    }

    public void RequestGameList() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.RequestGameList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public GameInfo GetGameInfo() {
        return new GameInfo(1, 2);
    }

    public void RequestGameInfo(int gameID) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.RequestGameInfo(gameID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public void ShowPenalty(Penalty penalty, Side side, Game game) {
        String message = String.format("New Penalty for %s - %s",
                side == Side.Host ? game.getHost() : game.getVisitor(),
                penalty.getPenaltyHolder());

        ShowToast(message);
    }

    public void ShowGoal(Goal goal, Side side, Game game) {
        String message = String.format("New Goal for %s - %s",
                side == Side.Host ? game.getHost() : game.getVisitor(),
                goal.getGoalHolder());

        ShowToast(message);
    }

    private void ShowToast(String message) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(CommunicationService.this.getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface Callbacks {
        void setGameList(List<Game> gameList);

        void updateGameInfo(GameInfo info);

        void betUpdate(Bet bet);

        void betConfirmed(boolean state);
    }

    public class CommunicationBinder extends Binder {
        public CommunicationService getService() {
            return CommunicationService.this;
        }
    }
}
