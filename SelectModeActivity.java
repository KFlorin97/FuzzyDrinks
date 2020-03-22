package com.example.drinkinggame_playstoreversion;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drinkinggame_playstoreversion.util.util.IabHelper;
import com.example.drinkinggame_playstoreversion.util.util.IabResult;
import com.example.drinkinggame_playstoreversion.util.util.Purchase;

public class SelectModeActivity extends AppCompatActivity  {

    private Button standardButton, ownChallengesButton, playerVsPlayerButton, ownChallengesButtonLocked,
            playerVsPlayerButtonLocked, addChallengeButton, shareButton, likeButton;
    private TextView standardModeTextView, addChallengeTextView, customModeTextView, coupleModeTextView;

    private static final String TAG = "FuzzyDrinks";
    IabHelper mHelper;
    static final String item1 = "testpurchase21";
    static final String item2 = "purchase_own_challenges";
    static Boolean shown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);
        openDialog();

        standardModeTextView = (TextView)findViewById(R.id.textViewStandardMode);
        addChallengeTextView = (TextView)findViewById(R.id.textViewAddChallenge);
        customModeTextView = (TextView)findViewById(R.id.textViewCustomMode);
        coupleModeTextView = (TextView)findViewById(R.id.textViewCoupleMode);

        shareButton = (Button)findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        likeButton = (Button)findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        addChallengeButton = (Button)findViewById(R.id.addChallengeButton);
        addChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        ownChallengesButtonLocked = (Button)findViewById(R.id.ownChallengesButtonLocked);
        playerVsPlayerButtonLocked = (Button)findViewById(R.id.playerVsPlayerButtonLocked);

        playerVsPlayerButtonLocked.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                buyItem1();
            }
        });
        
        ownChallengesButtonLocked.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {

            }
        });

        standardButton = (Button)findViewById(R.id.standardModeButton);
        standardButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openStandardModeActivity();
            }
        });

        ownChallengesButton = (Button)findViewById(R.id.ownChallengesButton);
        ownChallengesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openOwnChallengesActivity();
            }
        });

        playerVsPlayerButton = (Button)findViewById(R.id.playerVsPlayerButton);
        playerVsPlayerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openPlayerVsPlayerActivity();
            }
        });
        Typeface customFont = Typeface.createFromAsset(getAssets(), "font/cartoon.ttf");
        standardModeTextView.setTypeface(customFont);
        addChallengeTextView.setTypeface(customFont);
        customModeTextView.setTypeface(customFont);
        coupleModeTextView.setTypeface(customFont);

        // Purchase in app stuff


        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh5zLZZgWMc3GW4vpbDekEnJhPybCd+pb9u1mL+qHu6Mg5E9Bhr/n5nbheW4guTOhue+5jQDroW52lOv9sXIY3ZNkUZy6VqR1SHb0dE3PR2G5BcLqxxFu21e06nwSPgwpZYiUTvrZ37r2CI88XVwge9PtK+vx4LFuTLyO13iusRwBr2SF2kdAE04spXo7QRrKH/nAgnLkLXh6dQ/Yy1l+aaQuGRGkSEtc4WLGlRSp+JzLUcetL4AzPtkGD0kVpU6LTZiTUn8hQcGNQ8IiIP8g7s/Mh1K/OC+OJiT0n/hKDAjZqvH3wp6YypW1WNNwLJR7t6V+jC9xbsMPH94JYrRVNwIDAQAB";

        mHelper = new IabHelper(this,base64EncodedPublicKey);

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       @Override
                                       public void onIabSetupFinished(IabResult result)
                                       {
                                           if(!result.isSuccess())
                                           {
                                               Log.d(TAG, "In-app Billing setup failed: " +
                                                       result);
                                           }
                                           else
                                           {
                                               Log.d(TAG, "In-app Billing is set up OK");
                                           }
                                       }
                                   });

        //Check if product was bought

    }



    //Purchase in-app stuff


    public void buyItem1() {
        mHelper.launchPurchaseFlow(this, item1, 10001,
                mPurchaseFinishedListener, "mypurchasetoken");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(item1)) {
                playerVsPlayerButtonLocked.setVisibility(View.GONE);
                playerVsPlayerButton.setVisibility(View.VISIBLE);
            }

        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }




    //Extra stuff
    protected void sendEmail()
    {
        String[] TO = {"fuzzydrinks19@gmail.com"};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, TO);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Suggest a challenge");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openDialog()
    {
        if(shown == false)
        {
            StartDialog startDialog = new StartDialog();
            startDialog.show(getSupportFragmentManager(),"start dialog");
            shown = true;
        }
    }

    private void openPlayerVsPlayerActivity()
    {
       Intent intent = new Intent(this, CoupleModeInsertPlayers.class);
       startActivity(intent);
    }

    private void openOwnChallengesActivity()
    {
        Intent intent = new Intent(this, CustomModeInsertChallenges.class);
        startActivity(intent);
    }

    private void openStandardModeActivity()
    {
        Intent intent = new Intent(this, StandardModeInsertPlayers.class);
        startActivity(intent);
    }





}
