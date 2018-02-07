package com.emfpoll.emfpoll;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emfpoll.emfpoll.beans.Choice;
import com.emfpoll.emfpoll.beans.Question;
import com.emfpoll.emfpoll.beans.Survey;
import com.emfpoll.emfpoll.tasks.GetSurveyTask;

import java.util.concurrent.ExecutionException;


public class AnswerActivity extends Activity {

    private Survey poll;

    private static final String LOG_TAG = AnswerActivity.class.getSimpleName();
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

    @Override
    protected void onResume() {
        super.onResume();
        String id = null;
        Log.d(LOG_TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++ id : " + id + " " + (getIntent() != null) + " " + (getIntent().getData()));
        if (getIntent() != null && getIntent().getData() != null && getIntent().getData().getQueryParameter("id") != null) {
            id = getIntent().getData().getQueryParameter("id");
            Log.d(LOG_TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++ id : " + id);
        }
        if (id != null && !id.equals("")) {
            initSurvey(Integer.parseInt(id));
            Log.d(LOG_TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++ id : " + id);
        } else {
            initSurvey(getIntent().getIntExtra("pk_survey", -1));
            Log.d(LOG_TAG, "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ id : " + id);
        }
    }

    private void initSurvey(int pk_survey) {
        try {
            Survey poll = new GetSurveyTask().execute(pk_survey).get();
            this.poll = poll;
            TextView questionLabel = findViewById(R.id.questionLabel);
            if (poll != null) {
                questionLabel.setText(poll.getName());
                LinearLayout layoutPoll = findViewById(R.id.layoutanswer);
                layoutPoll.removeAllViews();
                for (Question question : poll.getQuestions()) {
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
                    for (Choice choice : question.getChoices()) {
                        LinearLayout choiceLayout = new LinearLayout(AnswerActivity.this);
                        TextView choiceText = new TextView(AnswerActivity.this);
                        choiceText.setEms(10);
                        choiceText.setTextSize(14);
                        choiceText.setText(choice.getText());
                        View space = new View(AnswerActivity.this);
                        space.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                        TextView choiceVotes = new TextView(AnswerActivity.this);
                        choiceVotes.setText(choice.getVotes().size() + "");
                        choiceLayout.addView(choiceText);
                        choiceLayout.addView(space);
                        choiceLayout.addView(choiceVotes);
                        layoutQuestion.addView(choiceLayout);
                    }
                    layoutPoll.addView(layoutQuestion);
                }
            } else {
                Toast.makeText(this, "Une erreur c'est produit, le sondage est vide :(", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, "ID null");
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
    public void initButtonAnswer() {
        buttonGoHome = findViewById(R.id.goHomeAnswer);
        buttonGoHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.d(LOG_TAG, "============================ start buttonGoHome load");
                // Start NewActivity.class
                Intent myIntent = new Intent(AnswerActivity.this, HomeActivity.class);
                startActivity(myIntent);
            }
        });

        buttonSharePoll = findViewById(R.id.sharePoll);
        Log.d(LOG_TAG, "============================ start ");
        buttonSharePoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Salut ! J'ai créé un nouveau sondage sur EmfPoll (http://emfpoll/quickvote), tu peux participer avec ce code : " + AnswerActivity.this.poll.getPkSurvey();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(sharingIntent, "Partagez votre sondage !"));
            }
        });
    }
}
