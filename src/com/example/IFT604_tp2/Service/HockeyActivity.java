package com.example.IFT604_tp2.Service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import com.example.IFT604_tp2.CommunicationService;

/**
 * Michael Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public abstract class HockeyActivity extends Activity {

    protected CommunicationService boundService;
    private boolean isServiceBound = false;

    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            boundService = ((CommunicationService.CommunicationBinder)service).getService();
            boundService.registerClient(HockeyActivity.this);

            HockeyActivity.this.onServiceConnected();
        }

        public void onServiceDisconnected(ComponentName className) {
            boundService = null;
            HockeyActivity.this.onServiceDisconnected();
        }
    };

    protected void onServiceConnected() { }
    protected void onServiceDisconnected() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Start le service.
        doBindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(boundService != null)
            boundService.unregisterClient(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(boundService != null)
            boundService.registerClient(this);
    }

    void doBindService() {
        bindService(new Intent(HockeyActivity.this, CommunicationService.class), connection, Context.BIND_AUTO_CREATE);
        isServiceBound = true;
    }

    void doUnbindService() {
        if (isServiceBound) {
            // Detach our existing connection.
            unbindService(connection);
            isServiceBound = false;
        }
    }
}
