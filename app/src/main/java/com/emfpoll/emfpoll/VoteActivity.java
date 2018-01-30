package com.emfpoll.emfpoll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class VoteActivity extends Activity {
    private static final String LOG_TAG =
            VoteActivity.class.getSimpleName();
    Button buttonGoHome;
    Button buttonSendPoll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        initButtonVote();
        Log.d(LOG_TAG, "============================ vote loaded");
    }

    //Charge les boutons de l'ihm principale HOME
    public void initButtonVote() {
        buttonGoHome = findViewById(R.id.goHomeVote);
        buttonGoHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Start NewActivity.class
                Intent myIntent = new Intent(VoteActivity.this,
                        HomeActivity.class);
                startActivity(myIntent);
            }
        });

        buttonSendPoll = findViewById(R.id.deletePoll);


    }
}
