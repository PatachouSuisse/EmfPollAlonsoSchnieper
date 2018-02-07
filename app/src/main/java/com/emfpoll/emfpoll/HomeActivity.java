package com.emfpoll.emfpoll;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.emfpoll.emfpoll.beans.Survey;
import com.emfpoll.emfpoll.tasks.GetSurveyListTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class HomeActivity extends Activity {
    private static final String LOG_TAG =
            HomeActivity.class.getSimpleName();
    Button buttonJoinPoll;
    Button buttonCreatePoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initPolls();
        initButtonHome();
        Log.d(LOG_TAG, "============================ Home loaded");
    }

    private void initPolls() {
        String androidId = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            ArrayList<Survey> polls = new GetSurveyListTask().execute(androidId).get();
            TableLayout tl = findViewById(R.id.polls);
            while(tl.getChildCount() > 1) {
                tl.removeViewAt(1);
            }
            for(final Survey poll : polls) {
                TableRow tr = new TableRow(getBaseContext());
                tr.setBackground(new ColorDrawable(getResources().getColor(android.R.color.darker_gray)));
                final int TR_PADDING = 5; //dp
                tr.setPadding(TR_PADDING, TR_PADDING, TR_PADDING, TR_PADDING);
                TextView tv = new TextView(getBaseContext());
                tv.setText(poll.getName());
                tr.addView(tv);
                tr.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(LOG_TAG, "============================ start Answer load");
                        // Start AnswerActivity.class
                        Intent myIntent = new Intent(HomeActivity.this, AnswerActivity.class);
                        myIntent.putExtra("pk_survey", poll.getPkSurvey());
                        startActivity(myIntent, null);
                    }
                });
                tl.addView(tr);
            }
        } catch (InterruptedException e) {
            Toast.makeText(this, "Une erreur c'est produit, InterruptedException :(", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        } catch (ExecutionException e) {
            Toast.makeText(this, "Une erreur c'est produit, ExecutionException :(", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //Charge les boutons de l'ihm principale HOME
    public void initButtonHome() {
        buttonJoinPoll = findViewById(R.id.voteHome);
        buttonJoinPoll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Start NewActivity.class
                Intent myIntent = new Intent(HomeActivity.this,
                        VoteActivity.class);
                EditText codeInputText = findViewById(R.id.codeInputText);
                //TODO catch parse le if suffis ?
                if (!String.valueOf(codeInputText.getText()).equals("")){
                    myIntent.putExtra("pk_survey", Integer.parseInt(String.valueOf(codeInputText.getText())));
                    startActivity(myIntent);
                }

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
