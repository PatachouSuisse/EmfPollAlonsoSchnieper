package com.emfpoll.emfpoll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emfpoll.emfpoll.beans.Choice;
import com.emfpoll.emfpoll.beans.Question;
import com.emfpoll.emfpoll.beans.Survey;
import com.emfpoll.emfpoll.tasks.GetSurveyTask;
import com.emfpoll.emfpoll.wrk.WrkDB;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


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
        Survey survey = null;
        try {
            survey = new GetSurveyTask().execute(getIntent().getIntExtra("pk_survey", -1)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "============================ vote for this survey : " + survey);
        //
        if (survey != null) {
            //Le champs ou générer toutes les réponse possibles
            final LinearLayout linearParent = findViewById(R.id.layoutpoll);
            //création du champs du sondage
            final TextView pollTitle = (TextView) findViewById(R.id.questionLabel);
            pollTitle.setText(survey.getName());
            //Question dynamique
            ArrayList<Question> questions = survey.getQuestions();
            for (Question question : questions) {
                LinearLayout layoutQuestion = new LinearLayout(VoteActivity.this);
                layoutQuestion.setOrientation(LinearLayout.VERTICAL);
                final TextView questionTextView = new TextView(VoteActivity.this);
                questionTextView.setText(question.getTitle());
                layoutQuestion.addView(questionTextView);
                //Choix dynamique
                ArrayList<Choice> choices = question.getChoices();
                //TODO utiliser un layout pour les choix ou prendre le premier enfant comme titre ?
                if(question.isMultiple()) {
                    //TODO aligner question et checkbox
                    LinearLayout choicesLayout = new LinearLayout(VoteActivity.this);
                    LinearLayout choiceLayout = new LinearLayout(VoteActivity.this);
                    choiceLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout checkLayout = new LinearLayout(VoteActivity.this);
                    checkLayout.setOrientation(LinearLayout.VERTICAL);
                    for (Choice choice : choices) {
                        TextView answerDynamic = new TextView(VoteActivity.this);
                        answerDynamic.setWidth(48);
                        answerDynamic.setHeight(48);
                        answerDynamic.setEms(10);
                        answerDynamic.setText(choice.getText());
                        answerDynamic.setTextSize(14);
                        choiceLayout.addView(answerDynamic);
                        CheckBox choiceCheckBox = new CheckBox(VoteActivity.this);
                        checkLayout.addView(choiceCheckBox);
                    }
                    choicesLayout.addView(choiceLayout);
                    choicesLayout.addView(checkLayout);
                    layoutQuestion.addView(choicesLayout);
                } else {
                    /*
                    TODO RADIOBUTTON NE FONCTIONNE PAS
                     nécessaire de créer son propre RadioButton
                     (il est nécessaire que les RadioButtons soient les enfants directs d'un RadioGroup)
                    */
                    RadioGroup choicesLayout = new RadioGroup(VoteActivity.this);
                    for (Choice choice : choices) {
                        LinearLayout choiceLayout = new LinearLayout(VoteActivity.this);
                        TextView answerDynamic = new TextView(VoteActivity.this);
                        answerDynamic.setWidth(48);
                        answerDynamic.setHeight(48);
                        answerDynamic.setEms(10);
                        answerDynamic.setText(choice.getText());
                        answerDynamic.setTextSize(14);
                        choiceLayout.addView(answerDynamic);
                        RadioButton choiceRadioButton = new RadioButton(VoteActivity.this);
                        choiceLayout.addView(choiceRadioButton);
                        choicesLayout.addView(choiceLayout);
                    }
                    layoutQuestion.addView(choicesLayout);
                }
                linearParent.addView(layoutQuestion);
            }
            initButtonVote();
            Log.d(LOG_TAG, "============================ vote loaded");
            Toast.makeText(this, "Vote chargé", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Une erreur c'est produit, le sondage est vide :(", Toast.LENGTH_LONG).show();
        }
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
    }
}
