package com.emfpoll.emfpoll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emfpoll.emfpoll.beans.Choice;
import com.emfpoll.emfpoll.beans.Question;
import com.emfpoll.emfpoll.beans.Survey;
import com.emfpoll.emfpoll.wrk.WrkDB;

import java.util.ArrayList;


public class VoteActivity extends Activity {
    private static final String LOG_TAG = VoteActivity.class.getSimpleName();
    private WrkDB wrkDb;
    private int a = 0;
    Button buttonGoHome;
    Button buttonSendPoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        wrkDb = WrkDB.getInstance();
        Survey survey = wrkDb.getSurvey(7);
        Log.d(LOG_TAG, "============================ vote for this survey : " + survey);
        //
        if (survey != null) {
            //Le champs ou générer toutes les réponse possibles
            final LinearLayout linearParent = (LinearLayout) findViewById(R.id.layoutnew);
            //création du champs du sondage
            final TextView pollTitle = (TextView) findViewById(R.id.questionLabel);
            pollTitle.setText(survey.getName());
            //Question dynamique
            ArrayList<Question> questions = survey.getQuestions();
            for (Question question : questions) {
                final TextView questionTextView = (TextView) findViewById(R.id.questionLabel);
                questionTextView.setText(question.getTitle());
                linearParent.addView(questionTextView);
                //Choix dynamique
                ArrayList<Choice> choices = question.getChoices();
                for (Choice choice : choices) {
                    final TextView answerDynamic = new EditText(VoteActivity.this);
                    answerDynamic.setId((int) a);
                    answerDynamic.setWidth(48);
                    answerDynamic.setHeight(48);
                    answerDynamic.setEms(10);
                    answerDynamic.setText(choice.getText());
                    answerDynamic.setTextSize(14);
                    a++;
                    linearParent.addView(answerDynamic);
                }
            }
        }
        initButtonVote();
        Log.d(LOG_TAG, "============================ vote loaded");
    }

    //Charge les boutons de l'ihm principale HOME
    public void initButtonVote() {
        buttonGoHome = findViewById(R.id.goHomeVote);
        buttonGoHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Start NewActivity.class
                Intent myIntent = new Intent(VoteActivity.this, HomeActivity.class);
                startActivity(myIntent);
            }
        });

        buttonSendPoll = findViewById(R.id.deletePoll);


    }
}
