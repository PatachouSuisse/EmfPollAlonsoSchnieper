package com.emfpoll.emfpoll;

import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.emfpoll.emfpoll.beans.Choice;
import com.emfpoll.emfpoll.beans.Question;
import com.emfpoll.emfpoll.beans.Survey;
import com.emfpoll.emfpoll.beans.Vote;
import com.emfpoll.emfpoll.tasks.CreateSurveyTask;
import com.emfpoll.emfpoll.tasks.GetSurveyListTask;
import com.emfpoll.emfpoll.tasks.GetSurveyTask;
import com.emfpoll.emfpoll.tasks.VoteTask;
import com.emfpoll.emfpoll.wrk.WrkDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private boolean dev = false;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    Button buttonGoHome;
    Button buttonCreatePoll;
    Button buttonVote;
    Button buttonAnswer;
    Button buttonTestVote;
    Button buttonCreateSurvey;
    Button buttonGetSurvey;
    Button buttonGetSurveyList;

    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;


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
        Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
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
                Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(myIntent);
            }
        });


        buttonCreatePoll = findViewById(R.id.goNewTest);
        buttonCreatePoll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.d(LOG_TAG, "============================ start Create load");
                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this, NewActivity.class);
                startActivity(myIntent);
            }
        });

        buttonVote = findViewById(R.id.goVoteTest);
        buttonVote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.d(LOG_TAG, "============================ start Vote load");
                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this, VoteActivity.class);
                startActivity(myIntent);
            }
        });


        buttonAnswer = findViewById(R.id.goAnswerTest);
        buttonAnswer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.d(LOG_TAG, "============================ start Answer load");
                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this, AnswerActivity.class);
                startActivity(myIntent);
            }
        });

        buttonTestVote = findViewById(R.id.testVote);
        buttonTestVote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                ArrayList<Vote> votes = new ArrayList<>();
                String androidId = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                votes.add(new Vote(new Choice(1), androidId));
                try {
                    System.out.println(new VoteTask().execute(votes).get());
                } catch (InterruptedException e) {
                    Log.e(LOG_TAG, e.getMessage());
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    Log.e(LOG_TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        buttonCreateSurvey = findViewById(R.id.testCreateSurvey);
        buttonCreateSurvey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                WrkDB db = WrkDB.getInstance();
                ArrayList<Choice> choices1 = new ArrayList<>();
                choices1.add(new Choice("Choix 1", null, null));
                choices1.add(new Choice("Choix 2", null, null));
                ArrayList<Choice> choices2 = new ArrayList<>();
                choices2.add(new Choice("Choix 1", null, null));
                ArrayList<Question> questions = new ArrayList<>();
                questions.add(new Question(choices1, "question 1", false, null));
                questions.add(new Question(choices2, "question 2", false, null));
                String androidId = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                Survey survey = new Survey(questions, "Test !", new Date(), null, androidId);
                try {
                    System.out.println(new CreateSurveyTask().execute(survey).get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });


        buttonGetSurvey = findViewById(R.id.testGetSurvey);
        buttonGetSurvey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Survey survey = null;
                try {
                    survey = new GetSurveyTask().execute(6).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println(survey);
                System.out.println(survey.getCreatorid());
                System.out.println(survey.getPkSurvey());
                System.out.println(survey.getStart());
                for (Question q : survey.getQuestions()) {
                    System.out.println("QUESTION");
                    System.out.println(q);
                    System.out.println(q.getPkQuestion());
                    System.out.println(q.isMultiple());
                    for (Choice c : q.getChoices()) {
                        System.out.println("CHOIX");
                        System.out.println(c);
                        System.out.println(c.getPkChoice());
                        for (Vote v : c.getVotes()) {
                            System.out.println("VOTE");
                            System.out.println(v.getVisitorid());
                        }
                    }
                }
            }
        });


        buttonGetSurveyList = findViewById(R.id.testGetSurveyList);
        buttonGetSurveyList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String androidId = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                ArrayList<Survey> surveys = null;
                try {
                    surveys = new GetSurveyListTask().execute(androidId).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println(surveys);
            }
        });

        Log.d(LOG_TAG, "============== End initButtonDev");
    }
}