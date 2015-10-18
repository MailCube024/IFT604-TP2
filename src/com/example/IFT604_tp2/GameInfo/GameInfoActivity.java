package com.example.IFT604_tp2.GameInfo;

import HockeyLive.Client.Communication.ClientSocket;
import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.Goal;
import HockeyLive.Common.Models.Penalty;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.IFT604_tp2.IntentKeys;
import com.example.IFT604_tp2.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michaël on 10/17/2015.
 */
public class GameInfoActivity extends Activity {
    private Intent currentIntent;
    private Game game;

    private ArrayAdapter<Penalty> hostPenaltyAdapter;
    private ArrayAdapter<Penalty> visitorPenaltyAdapter;
    private ArrayAdapter<Goal> hostGoalAdapter;
    private ArrayAdapter<Goal> visitorGoalAdapter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.game_summary);

        currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();

        try {
            ClientSocket socket = new ClientSocket(1010);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bundle != null)
            game = (Game) bundle.getSerializable(IntentKeys.SELECTED_GAME);
        else
            game = new Game(0, "Chicago", "Montreal");

        InitializeGameDescription();
        InitializeAdapters();
    }

    private void InitializeGameDescription() {
        ((TextView) findViewById(R.id.lblGameInfoTitle)).setText(game.toString());
        ((TextView) findViewById(R.id.lblHostname)).setText(game.getHost());
        ((TextView) findViewById(R.id.lblVisitorName)).setText(game.getVisitor());
    }

    private void InitializeAdapters() {
        hostPenaltyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        hostGoalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        visitorPenaltyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        visitorGoalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());

        ((ListView) findViewById(R.id.lstHostPenalties)).setAdapter(hostPenaltyAdapter);
        ((ListView) findViewById(R.id.lstHostGoals)).setAdapter(hostPenaltyAdapter);
        ((ListView) findViewById(R.id.lstVisitorPenalties)).setAdapter(visitorPenaltyAdapter);
        ((ListView) findViewById(R.id.lstVisitorGoals)).setAdapter(visitorPenaltyAdapter);
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

    }

    public void onBetButtonClicked(View view) {

    }
}
