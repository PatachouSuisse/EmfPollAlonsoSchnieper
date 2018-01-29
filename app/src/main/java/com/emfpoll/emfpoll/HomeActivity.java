package com.emfpoll.emfpoll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class HomeActivity extends Activity {
    private static final String LOG_TAG =
            HomeActivity.class.getSimpleName();
    Button buttonJoinPoll;
    Button buttonCreatePoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initButtonHome();
        Log.d(LOG_TAG, "============================ Home loaded");
    }

    //Charge les boutons de l'ihm principale HOME
    public void initButtonHome() {
        buttonJoinPoll = findViewById(R.id.voteHome);
        buttonJoinPoll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Start NewActivity.class
                Intent myIntent = new Intent(HomeActivity.this,
                        MainActivity.class);
                startActivity(myIntent);
            }
        });


        buttonCreatePoll = findViewById(R.id.homeNewPoll);
        buttonCreatePoll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Start NewActivity.class
                Intent myIntent = new Intent(HomeActivity.this,
                        NewActivity.class);
                startActivity(myIntent);
            }
        });

    }
}
