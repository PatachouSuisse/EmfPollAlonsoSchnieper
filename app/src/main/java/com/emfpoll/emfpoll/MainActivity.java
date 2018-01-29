package com.emfpoll.emfpoll;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private boolean dev = true;
    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();

    Button buttonGoHome;
    Button buttonCreatePoll;
    Button buttonVote;
    Button buttonAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "============================ Main loaded");
        if (dev) {
            //Application de débug
            devApp();
        } else {
            //Application Prod
            prodApp();
        }
    }


    //Init states
    private void devApp() {
        setContentView(R.layout.activity_main);
        initButtonDev();
    }

    private void prodApp() {
        Intent myIntent = new Intent(MainActivity.this,
                HomeActivity.class);
        startActivity(myIntent);
    }

    //Charge les bouton de l'ihm main, dev.
    private void initButtonDev() {
        Log.d(LOG_TAG, "============== Start initButtonDev");
        buttonGoHome = findViewById(R.id.goHomeTest);
        buttonGoHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.d(LOG_TAG, "============================ start Home load");
                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        HomeActivity.class);
                startActivity(myIntent);
            }
        });


        buttonCreatePoll = findViewById(R.id.goNewTest);
        buttonCreatePoll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.d(LOG_TAG, "============================ start Create load");
                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        NewActivity.class);
                startActivity(myIntent);
            }
        });

        buttonVote = findViewById(R.id.goVoteTest);
        buttonVote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.d(LOG_TAG, "============================ start Vote load");
                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        VoteActivity.class);
                startActivity(myIntent);
            }
        });


        buttonAnswer = findViewById(R.id.goAnswerTest);
        buttonAnswer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.d(LOG_TAG, "============================ start Answer load");
                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        AnswerActivity.class);
                startActivity(myIntent);
            }
        });
        Log.d(LOG_TAG, "============== End initButtonDev");
    }
}