package com.example.drinkinggame_playstoreversion;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

public class SelectModeActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    private Button standardButton, ownChallengesButton, playerVsPlayerButton, ownChallengesButtonLocked,
            playerVsPlayerButtonLocked, addChallengeButton, shareButton, likeButton;
    private TextView standardModeTextView, addChallengeTextView, customModeTextView, coupleModeTextView;

    private static final String TAG = "FuzzyDrinks";

    static final String playervsplayerMode = "playervsplayer";
    static final String customMode = "custommode";
    static Boolean shown = false;
    BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);
        openDialog();

        bp = new BillingProcessor(this,null,this);

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
                String shareBody = "https://play.google.com/store/apps/details?id=com.kiss.drinkinggame_playstoreversion";
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
                bp.purchase(SelectModeActivity.this, playervsplayerMode);
            }
        });
        
        ownChallengesButtonLocked.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                bp.purchase(SelectModeActivity.this,customMode);
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
        TransactionDetails transactionDetailsPlayervsPlayer = bp.getPurchaseTransactionDetails(playervsplayerMode);

        if(transactionDetailsPlayervsPlayer != null)
        {
            playerVsPlayerButtonLocked.setVisibility(View.GONE);
            playerVsPlayerButton.setVisibility(View.VISIBLE);
        }
        else
        {
            playerVsPlayerButtonLocked.setVisibility(View.VISIBLE);
            playerVsPlayerButton.setVisibility(View.GONE);
        }

        TransactionDetails transactionDetailsOwnChallenges = bp.getPurchaseTransactionDetails(customMode);

        if(transactionDetailsOwnChallenges != null)
        {
            ownChallengesButtonLocked.setVisibility(View.GONE);
            ownChallengesButton.setVisibility(View.VISIBLE);
        }
        else
        {
            ownChallengesButtonLocked.setVisibility(View.VISIBLE);
            ownChallengesButton.setVisibility(View.GONE);
        }
    }

    //Extra stuff ==================================================================================
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
            Dialog startDialog = new Dialog();
            startDialog.show(getSupportFragmentManager(),"start dialog");
            shown = true;
        }
    }

    //Game - modes =================================================================================

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


    // Billing =====================================================================================
    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        finish();
        startActivity(getIntent());
        if (productId == playervsplayerMode) {
            Toast.makeText(this, "The couple mode was already bought", Toast.LENGTH_SHORT).show();
            playerVsPlayerButtonLocked.setVisibility(View.GONE);
            playerVsPlayerButton.setVisibility(View.VISIBLE);
        } else if (productId == customMode) {
            Toast.makeText(this, "The custom mode was already bought", Toast.LENGTH_SHORT).show();
            ownChallengesButtonLocked.setVisibility(View.GONE);
            ownChallengesButton.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error)
    {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingInitialized()
    {

    }


    //Overrides
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(!bp.handleActivityResult(requestCode,resultCode,data))
        {
            super.onActivityResult(requestCode, resultCode ,data);
        }
    }

    @Override
    public void onDestroy()
    {
        if(bp!=null)
        {
            bp.release();
        }
        super.onDestroy();
    }

}
