package com.example.IFT604_tp2;

import HockeyLive.Client.Listeners.GameListUpdateListener;
import HockeyLive.Common.Models.Game;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.IFT604_tp2.GameInfo.GameInfoActivity;
import com.example.IFT604_tp2.Service.HockeyActivity;

import java.util.List;

public class MatchListActivity extends HockeyActivity implements CommunicationService.Callbacks {

    @Override
    protected void onServiceConnected() {
        //Request pour la liste de match.
        boundService.RequestGameList();

        GameListUpdateListener listener = new GameListUpdateListener() {
            @Override
            public void UpdateGameList(List<Game> games) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setGameList(games);
                    }
                });
            }
        };

        boundService.AddGameListUpdateListener(listener);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list);

        ListView matchList = (ListView) findViewById(R.id.matchList);

        matchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Ouverture de la nouvelle activité avec GameID de choisi.
                Game selectedGame = (Game) parent.getItemAtPosition(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("SelectedGame", selectedGame);

                Intent intent = new Intent(getBaseContext(), GameInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void setGameList(List<Game> gameList) {
        ArrayAdapter<Game> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gameList);

        ListView matchList = (ListView) findViewById(R.id.matchList);
        matchList.setAdapter(adapter);
    }
}
