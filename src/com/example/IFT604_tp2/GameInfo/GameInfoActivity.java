package com.example.IFT604_tp2.GameInfo;

import HockeyLive.Client.Listeners.GameInfoUpdateListener;
import HockeyLive.Common.Models.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.IFT604_tp2.CommunicationService;
import com.example.IFT604_tp2.IntentKeys;
import com.example.IFT604_tp2.R;
import com.example.IFT604_tp2.Service.HockeyActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michaël on 10/17/2015.
 */
public class GameInfoActivity extends HockeyActivity implements CommunicationService.Callbacks {
    private boolean spinnerShowing;
    private ProgressDialog progress;
    private Intent currentIntent;
    private Game game;

    private ArrayAdapter<Penalty> hostPenaltyAdapter;
    private ArrayAdapter<Penalty> visitorPenaltyAdapter;
    private ArrayAdapter<Goal> hostGoalAdapter;
    private ArrayAdapter<Goal> visitorGoalAdapter;

    //@Override
    protected void onServiceConnected() {
        GameInfoUpdateListener listener = new GameInfoUpdateListener() {
            @Override
            public void UpdateGameInfo(GameInfo info) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateGameInfo(info);
                        if (spinnerShowing) {
                            progress.dismiss();
                            spinnerShowing = false;
                        }
                    }
                });
            }
        };

        boundService.AddGameInfoUpdateListener(listener);
        boundService.RequestGameInfo(game.getGameID());
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.game_summary);

        currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();

        if (bundle != null)
            game = (Game) bundle.getSerializable(IntentKeys.SELECTED_GAME);
        else
            game = new Game(0, "Chicago", "Montreal");

        InitializeGameDescription();
        InitializeAdapters();

        progress = new ProgressDialog(this);
        progress.setTitle("Loading game info");
        progress.setMessage("Please wait...");
        progress.show();
        spinnerShowing = true;
    }

    @Override
    protected void onPause() {
        if (spinnerShowing) progress.dismiss();
    }

    private void InitializeGameDescription() {
        ((TextView) findViewById(R.id.lblGameInfoTitle)).setText(game.toString());
        ((TextView) findViewById(R.id.lblHostname)).setText(game.getHost());
        ((TextView) findViewById(R.id.lblVisitorName)).setText(game.getVisitor());
    }

    private void InitializeAdapters() {
        hostPenaltyAdapter = new ArrayAdapter<>(this, R.layout.left_aligned, new ArrayList<>());
        hostGoalAdapter = new ArrayAdapter<>(this, R.layout.left_aligned, new ArrayList<>());
        visitorPenaltyAdapter = new ArrayAdapter<>(this, R.layout.right_aligned, new ArrayList<>());
        visitorGoalAdapter = new ArrayAdapter<>(this, R.layout.right_aligned, new ArrayList<>());

        ((ListView) findViewById(R.id.lstHostPenalties)).setAdapter(hostPenaltyAdapter);
        ((ListView) findViewById(R.id.lstHostGoals)).setAdapter(hostGoalAdapter);
        ((ListView) findViewById(R.id.lstVisitorPenalties)).setAdapter(visitorPenaltyAdapter);
        ((ListView) findViewById(R.id.lstVisitorGoals)).setAdapter(visitorGoalAdapter);
    }

    public void UpdatePenaltyList(List<Penalty> hostPenalties, List<Penalty> visitorPenalties) {
        hostPenaltyAdapter.clear();
        visitorPenaltyAdapter.clear();

        hostPenaltyAdapter.addAll(hostPenalties);
        visitorPenaltyAdapter.addAll(visitorPenalties);
    }

    public void UpdateGoalList(List<Goal> hostGoals, List<Goal> visitorGoals) {
        hostGoalAdapter.clear();
        visitorGoalAdapter.clear();

        hostGoalAdapter.addAll(hostGoals);
        visitorGoalAdapter.addAll(visitorGoals);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        if (checked) {
            switch (view.getId()) {
                case R.id.optHost:
                    if (checked)
                        //TODO: Set bet to host
                        break;
                case R.id.optVisitor:
                    if (checked)
                        //TODO: Set bet to visitor
                        break;
            }
        }
    }

    public void onRefreshButtonClicked(View view) {
        boundService.RequestGameInfo(game.getGameID());
    }

    public void onBetButtonClicked(View view) {

    }

    @Override
    public void setGameList(List<Game> gameList) {
        // Not interested
    }

    @Override
    public void updateGameInfo(GameInfo info) {
        ((TextView) findViewById(R.id.lblHostGoals)).setText(String.valueOf(info.getHostGoalsTotal()));
        ((TextView) findViewById(R.id.lblVisitorGoals)).setText(String.valueOf(info.getVisitorGoalsTotal()));

        ((TextView) findViewById(R.id.lblPeriod)).setText(String.valueOf(info.getPeriod()));
        ((TextView) findViewById(R.id.lblTimer)).setText(info.getPeriodFormattedChronometer());

        UpdateGoalList(info.getHostGoals(), info.getVisitorGoals());
        UpdatePenaltyList(info.getHostPenalties(), info.getVisitorPenalties());
    }

    @Override
    public void betUpdate(Bet bet) {
        //TODO: Add implementation
    }

    @Override
    public void betConfirmed(boolean state) {
        //TODO: Add implementation
    }
}
