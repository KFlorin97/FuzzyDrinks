package com.example.drinkinggame_playstoreversion;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.example.drinkinggame_playstoreversion.StandardModeInsertPlayers.standardModePlayers;

public class StandardModeActivity extends AppCompatActivity
{
    int i=0;
    int one=0;
    int playerIndex=0;
    Button previousChallengeButton, nextChallengeButton, nextChallengeDenialButton;
    TextView challengeView;
    StandardModeDatabase database;
    Cursor cursor;
    String aux;
    int secondPlayerIndex ;
    List<String> challenges = new ArrayList<>();
    private AdView mAdView;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_mode);

        //Banner Ad ===============================================================================

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-7963930731441749/3726573279");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Interstitial Ad
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-7963930731441749/8518545584");
        AdRequest interstitialAdRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(interstitialAdRequest);

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed()
            {
                interstitialAd.loadAd(new AdRequest.Builder().build());
                database.DropDatabase();
                startActivity(new Intent(StandardModeActivity.this, StandardModeLeaderboardActivty.class));

            }
        });

        challengeView = (TextView)findViewById(R.id.challengeView);
        nextChallengeButton = (Button)findViewById(R.id.nextChallengeButton);
        previousChallengeButton = (Button)findViewById(R.id.previousChallengeButton);
        nextChallengeDenialButton = (Button)findViewById(R.id.nextChallengeButtonDenial);
        database = new StandardModeDatabase(this);
        cursor = database.getAllData();
        Random random = new Random();
        if(cursor.getCount() == 0)
        {
            database.instantiateDatabase();
            cursor = database.getAllData();
        }

        while(cursor.moveToNext())
        {
            if(cursor.getString(1).startsWith("give") || cursor.getString(1).startsWith("drink")|| cursor.getString(1).startsWith("if")||
                    cursor.getString(1).startsWith("do")|| cursor.getString(1).startsWith("pick")
                    || cursor.getString(1).startsWith("tell") || cursor.getString(1).startsWith("choose")||
                    cursor.getString(1).startsWith("play")|| cursor.getString(1).startsWith("make")|| cursor.getString(1).startsWith("kiss")
                    || cursor.getString(1).startsWith("keep")|| cursor.getString(1).startsWith("rate")|| cursor.getString(1).startsWith("share"))
            {
                if(cursor.getString(1).endsWith("with") || cursor.getString(1).endsWith("kiss")|| cursor.getString(1).endsWith("against"))
                {
                    secondPlayerIndex = random.nextInt(standardModePlayers.size());
                    while(secondPlayerIndex == playerIndex)
                    {
                        secondPlayerIndex = random.nextInt(standardModePlayers.size());
                    }
                    challenges.add(standardModePlayers.get(playerIndex)+ ", " + cursor.getString(1) + " " + (standardModePlayers.get(secondPlayerIndex)));
                }
                else
                {
                    if(cursor.getString(1).contains("asdfe"))
                    {
                        aux = cursor.getString(1).replace("asdfe",standardModePlayers.get(secondPlayerIndex));
                        challenges.add(standardModePlayers.get(playerIndex) + ", " + aux);

                    }
                    else
                    {
                        challenges.add(standardModePlayers.get(playerIndex) + ", " + cursor.getString(1));
                    }

                }
                playerIndex++;
                if(playerIndex == standardModePlayers.size())
                {
                    Collections.shuffle(standardModePlayers);
                    playerIndex = 0;
                }
            }
            else
            {
                challenges.add(cursor.getString(1));
            }

        }
        Collections.shuffle(challenges);
        if(one == 0)
        {
            previousChallengeButton.setVisibility(View.INVISIBLE);
        }
        challengeView.setText(challenges.get(i));
        i++;
        viewNextChallenge();
        viewPreviousChallenge();

    }


    private void viewPreviousChallenge()
    {
        previousChallengeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                i=i-2;
                challengeView.setText(challenges.get(i));
                previousChallengeButton.setVisibility(View.GONE);
                i++;
            }
        });
    }



    private void viewNextChallenge()
    {
        nextChallengeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(i< 25)
                {
                    challengeView.setText(challenges.get(i));
                    i++;
                    previousChallengeButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    displayAds();
                }
            }
        });

        nextChallengeDenialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(i< 25)
                {
                    challengeView.setText(challenges.get(i));
                    i++;
                    previousChallengeButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    displayAds();
                }
            }
        });
    }

    public void displayAds()
    {
        if(interstitialAd.isLoaded())
        {
            interstitialAd.show();
        }
        else
        {
            database.DropDatabase();
            Toast.makeText(this, "Ad not loaded yet", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, StandardModeLeaderboardActivty.class));
        }
    }
}
