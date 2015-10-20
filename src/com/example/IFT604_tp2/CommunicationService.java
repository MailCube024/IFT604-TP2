package com.example.IFT604_tp2;

import HockeyLive.Client.Communication.Client;
import HockeyLive.Client.Listeners.*;
import HockeyLive.Common.Models.*;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.net.InetAddress;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Benoit on 2015-10-17.
 */
public class CommunicationService extends Service {

    private final IBinder binder = new CommunicationBinder();
    private Callbacks activity;
    private Client client;

    private HashMap<Integer, Double> betGains = new HashMap<>();
    private List<Bet> betsReceived = new ArrayList<>();
    private double totalAmountGained = 0.0;

    @Override
    public void onCreate() {
        super.onCreate();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress serverAddress = InetAddress.getByName("192.168.56.2");
                    client = new Client(serverAddress);
                    client.Start();

                    PenaltyNotificationListener penaltyListener = new PenaltyNotificationListener() {
                        @Override
                        public void NewPenalty(Penalty penalty, Side side, Game game) {
                            if (activity == null) {
                                ShowPenalty(penalty, side, game);
                            }
                        }
                    };

                    GoalNotificationListener goalListener = new GoalNotificationListener() {
                        @Override
                        public void NewGoal(Goal goal, Side side, Game game) {
                            if (activity == null) {
                                ShowGoal(goal, side, game);
                            }
                        }
                    };

                    client.AddPenaltyNotificationListener(penaltyListener);
                    client.AddGoalNotificationListener(goalListener);

                    client.AddBetConfirmationListener(new BetConfirmationListener() {
                        @Override
                        public void IsBetConfirmed(boolean betConfirmation) {
                            ShowBetReception(betConfirmation);
                        }
                    });

                    client.AddBetUpdateListener(new BetUpdateListener() {
                        @Override
                        public void BetUpdate(Bet bet) {
                            if (!betsReceived.contains(bet)) {
                                betsReceived.add(bet);
                                Double oldGain = betGains.containsKey(bet.getGameID()) ? betGains.get(bet.getGameID()) : 0.0;
                                totalAmountGained += oldGain + bet.getAmountGained();
                                betGains.put(bet.getGameID(), oldGain + bet.getAmountGained());
                                ShowBetResultForGame(bet);
                                ShowTotalBetGains(totalAmountGained);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void ShowTotalBetGains(double totalAmountGained) {
        String message = String.format("Total gain up till now :%s",
                NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalAmountGained));
        ShowToast(message);
    }

    private void ShowBetResultForGame(Bet bet) {
        String message = String.format("Gain for bet on %s : %s", bet.getBetOn(),
                NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(bet.getAmountGained()));
        ShowToast(message);
    }

    private void ShowBetReception(boolean betConfirmation) {
        if (betConfirmation)
            ShowToast("Bet received - good luck!");
        else
            ShowToast("Bet was not registered");
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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.SendBet(bet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
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
                Toast.makeText(CommunicationService.this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface Callbacks {
        void setGameList(List<Game> gameList);

        void updateGameInfo(GameInfo info);
    }

    public class CommunicationBinder extends Binder {
        public CommunicationService getService() {
            return CommunicationService.this;
        }
    }
}
