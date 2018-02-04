package com.emfpoll.emfpoll;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emfpoll.emfpoll.beans.Choice;
import com.emfpoll.emfpoll.beans.Question;
import com.emfpoll.emfpoll.beans.Survey;
import com.emfpoll.emfpoll.tasks.GetSurveyTask;

import java.util.concurrent.ExecutionException;


public class AnswerActivity extends Activity {

    private Survey poll;

    private static final String LOG_TAG =
            AnswerActivity.class.getSimpleName();
    Button buttonGoHome;
    Button buttonDeletePoll;
    Button buttonSharePoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        initSurvey(getIntent().getIntExtra("pk_survey", -1));
        initButtonAnswer();
        Log.d(LOG_TAG, "============================ Answer loaded");
    }

    private void initSurvey(int pk_survey) {
        try {
            Survey poll = new GetSurveyTask().execute(pk_survey).get();
            this.poll = poll;
            TextView questionLabel = findViewById(R.id.questionLabel);
            questionLabel.setText(poll.getName());
            LinearLayout layoutPoll = findViewById(R.id.layoutanswer);
            for(Question question : poll.getQuestions()) {
                LinearLayout layoutQuestion = new LinearLayout(AnswerActivity.this);
                layoutQuestion.setOrientation(LinearLayout.VERTICAL);
                TextView questionText = new TextView(AnswerActivity.this);
                questionText.setWidth(100);
                questionText.setHeight(48);
                questionText.setEms(10);
                questionText.setTextSize(14);
                questionText.setText(question.getTitle());
                questionText.setTypeface(questionText.getTypeface(), Typeface.BOLD);
                layoutQuestion.addView(questionText);
                for(Choice choice : question.getChoices()) {
                    LinearLayout choiceLayout = new LinearLayout(AnswerActivity.this);
                    TextView choiceText = new TextView(AnswerActivity.this);
                    choiceText.setWidth(100);
                    choiceText.setHeight(48);
                    choiceText.setEms(10);
                    choiceText.setTextSize(14);
                    choiceText.setText(choice.getText());
                    TextView choiceVotes = new TextView(AnswerActivity.this);
                    choiceVotes.setText(choice.getVotes().size() + "");
                    choiceLayout.addView(choiceText);
                    choiceLayout.addView(choiceVotes);
                    layoutQuestion.addView(choiceLayout);
                }
                layoutPoll.addView(layoutQuestion);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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

        buttonSharePoll = findViewById(R.id.sharePoll);
        Log.d(LOG_TAG, "============================ start ");
        buttonSharePoll.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String message = "Salut ! J'ai créé un nouveau sondage sur EmfPoll, tu peux participer avec ce code : " + AnswerActivity.this.poll.getPkSurvey();
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
