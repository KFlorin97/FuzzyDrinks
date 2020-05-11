package com.example.drinkinggame_playstoreversion;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class StandardModeInsertPlayers extends AppCompatActivity
{
    Button buttonAddPlayer, buttonRemovePlayer, buttonViewAllPlayers, playButton;
    EditText playerName;
    TextView textViewPlayerName;
    public static List<String> standardModePlayers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_mode_insert_players);
        buttonAddPlayer = (Button)findViewById(R.id.buttonAddPlayer);
        textViewPlayerName = (TextView)findViewById(R.id.textViewPlayerName);
        playButton = (Button)findViewById(R.id.buttonPlay);
        buttonRemovePlayer = (Button)findViewById(R.id.buttonRemovePlayer);
        playerName = (EditText)findViewById(R.id.editTextPlayerName);
        buttonViewAllPlayers = (Button)findViewById(R.id.buttonViewAllPlayers);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "font/cartoon.ttf");
        textViewPlayerName.setTypeface(customFont);
        playerName.setTypeface(customFont);

        AddPlayer();
        DeletePlayer();
        ViewAllPlayers();
        playButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                playStandard();
            }
        });
    }

    private void playStandard()
    {
        if(standardModePlayers.size() <2)
        {
            Toast.makeText(StandardModeInsertPlayers.this, "Please add at least 2 players",
                    Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(this, StandardModeActivity.class);
            startActivity(intent);
        }
    }

    public void AddPlayer()
    {
        buttonAddPlayer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!playerName.getText().toString().matches(""))
                {
                    if(standardModePlayers.contains(playerName.getText().toString()))
                    {
                        Toast.makeText(StandardModeInsertPlayers.this, "Player already exists",
                                Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        standardModePlayers.add(playerName.getText().toString());
                        Toast.makeText(StandardModeInsertPlayers.this, "Player was added",
                                Toast.LENGTH_LONG).show();
                        playerName.setText("");
                    }
                }
                else
                {
                    Toast.makeText(StandardModeInsertPlayers.this, "Please insert a name",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void DeletePlayer()
    {
        buttonRemovePlayer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!playerName.getText().toString().matches(""))
                {
                    if(standardModePlayers.remove(playerName.getText().toString()))
                    {
                        Toast.makeText(StandardModeInsertPlayers.this, "Player removed",
                                Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(StandardModeInsertPlayers.this, "Player doesn't exist",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(StandardModeInsertPlayers.this, "Please insert a name",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void ViewAllPlayers()
    {
        buttonViewAllPlayers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int i=0;
                StringBuffer buffer = new StringBuffer();
                while(i < standardModePlayers.size())
                {
                    buffer.append("Name :"+ standardModePlayers.get(i)+"\n");
                    i++;
                }
                if(standardModePlayers.size() == 0)
                {
                    showMessage("Take care", "No players were inserted");
                }
                else
                {
                    showMessage("Players", buffer.toString());
                }
            }
        });
    }

    public void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
}

