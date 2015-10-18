package com.example.IFT604_tp2;

import HockeyLive.Common.Models.Game;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.IFT604_tp2.GameInfo.GameInfoActivity;

import java.util.ArrayList;
import java.util.List;


public class MatchListActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list);

        //Start le service.

        ListView matchList = (ListView) findViewById(R.id.matchList);

        //Request pour la liste de match.
        //R�ception de la liste de match.

        /**Valeur de test pour la liste de match**/
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
        /******************************************/

        ArrayAdapter<Game> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTest);
        matchList.setAdapter(adapter);

        matchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Ouverture de la nouvelle activit� avec GameID de choisi.
                Game selectedGame = (Game) parent.getItemAtPosition(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("SelectedGame", selectedGame);

                Intent intent = new Intent(getBaseContext(), GameInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


}
