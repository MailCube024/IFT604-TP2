package com.example.IFT604_tp2.GameInfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import com.example.IFT604_tp2.R;

/**
 * Created by Michaël on 10/17/2015.
 */
public class GameInfoActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.match_summary);
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
}
