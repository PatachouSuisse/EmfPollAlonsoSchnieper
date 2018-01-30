package com.emfpoll.emfpoll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class AnswerActivity extends Activity {
    private static final String LOG_TAG =
            AnswerActivity.class.getSimpleName();
    Button buttonGoHome;
    Button buttonDeletePoll;
    Button buttonSharePoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        initButtonAnswer();
        Log.d(LOG_TAG, "============================ Answer loaded");
    }

    //Charge les boutons de l'ihm principale HOME
    public void initButtonAnswer() {
        buttonGoHome = findViewById(R.id.goHomeAnswer);
        buttonGoHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.d(LOG_TAG, "============================ start buttonGoHome load");
                // Start NewActivity.class
                Intent myIntent = new Intent(AnswerActivity.this,
                        HomeActivity.class);
                startActivity(myIntent);
            }
        });

        buttonDeletePoll = findViewById(R.id.deletePoll);


        buttonSharePoll = findViewById(R.id.sharePoll);
        Log.d(LOG_TAG, "============================ start ");
        buttonSharePoll.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   String message = "Salut ! J'ai créé un nouveau sondage sur EmfPoll, tu peux participer avec ce code : ";
                                                   Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                                   sharingIntent.setType("text/plain");
                                                   sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n");
                                                   sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                                                   startActivity(Intent.createChooser(sharingIntent, "Partager votre sondage !"));
                                               }
                                           }
        );


    }
}
