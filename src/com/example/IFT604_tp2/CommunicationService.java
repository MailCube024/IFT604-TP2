package com.example.IFT604_tp2;

import HockeyLive.Client.Communication.Client;
import HockeyLive.Client.Listeners.GameListUpdateListener;
import HockeyLive.Common.Models.Bet;
import HockeyLive.Common.Models.Game;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benoit on 2015-10-17.
 */
public class CommunicationService extends Service {

    public interface Callbacks{
        void setGameList(List<Game> gameList);
    }

    private final IBinder binder = new CommunicationBinder();
    private Callbacks activity;

    private Client client;

    public class CommunicationBinder extends Binder {
        public CommunicationService getService() {
            return CommunicationService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    InetAddress serverAddress = InetAddress.getByName("192.168.0.130");
                    client = new Client(serverAddress);
                    client.Start();
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

    public void registerClient(Activity activity){
        this.activity = (Callbacks)activity;
    }

    public void unregisterClient(Activity activity){
        if(this.activity.equals(activity))
            this.activity = null;
    }

    public void AddGameListUpdateListener(GameListUpdateListener listener) {
        client.AddGameListUpdateListener(listener);
    }

    public void SendBet(Bet bet) {
        //client.SendBet(bet);
    }

    public void RequestGameList() {
        Thread thread = new Thread(new Runnable(){
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

        //GetGameList();
    }

    public void GetGameList() {
        List<Game> listTest = new ArrayList<>();
        listTest.add(new Game(1, "Montreal", "Ottawa"));
        listTest.add(new Game(2, "New-York", "Pittsburg"));
        listTest.add(new Game(3, "Vancouver", "Calgary"));
        listTest.add(new Game(4, "St-Louis", "San-Jose"));
        listTest.add(new Game(5, "Tampa-Bay", "Florida"));
        listTest.add(new Game(6, "Nashville", "Columbus"));
        listTest.add(new Game(7, "Toronto", "Detroit"));
        listTest.add(new Game(8, "Edmonton", "Arizona"));
        listTest.add(new Game(9, "Carolina", "New-York"));
        listTest.add(new Game(10, "Buffalo", "Chicago"));

        if(activity != null)
            activity.setGameList(listTest);
    }
}
