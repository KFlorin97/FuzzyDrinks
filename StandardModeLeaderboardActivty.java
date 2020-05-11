package com.example.drinkinggame_playstoreversion;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import static com.example.drinkinggame_playstoreversion.StandardModeInsertPlayers.standardModePlayers;

public class StandardModeLeaderboardActivty extends AppCompatActivity {

    TextView winnerView, ultimateChallengeView;
    Button goHomeButton;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_mode_leaderboard_activty);
        // End - Game ==============================================================================

        Random random = new Random();
        int playInderx = random.nextInt(standardModePlayers.size());
        winnerView = (TextView)findViewById(R.id.textViewStandardModeWinner);
        ultimateChallengeView = (TextView)findViewById(R.id.textViewUltimateChallenge);
        goHomeButton = (Button)findViewById(R.id.homeButton);
        goHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });

        Typeface customFont = Typeface.createFromAsset(getAssets(),"font/cartoon.ttf");
        winnerView.setText(standardModePlayers.get(playInderx));
        winnerView.setTypeface(customFont);
        ultimateChallengeView.setTypeface(customFont);
    }

    private void goHome()
    {
        Intent intent = new Intent(this,SelectModeActivity.class);
        startActivity(intent);
    }
}
