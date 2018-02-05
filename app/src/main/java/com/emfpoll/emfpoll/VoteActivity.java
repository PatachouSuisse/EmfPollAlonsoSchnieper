package com.emfpoll.emfpoll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.emfpoll.emfpoll.beans.Choice;
import com.emfpoll.emfpoll.beans.Question;
import com.emfpoll.emfpoll.beans.Survey;
import com.emfpoll.emfpoll.beans.Vote;
import com.emfpoll.emfpoll.components.MyRadioGroup;
import com.emfpoll.emfpoll.exceptions.AlreadyVotedException;
import com.emfpoll.emfpoll.tasks.GetSurveyTask;
import com.emfpoll.emfpoll.tasks.VoteTask;
import com.emfpoll.emfpoll.wrk.WrkDB;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class VoteActivity extends Activity {
    private static final String LOG_TAG = VoteActivity.class.getSimpleName();
    private WrkDB wrkDb;
    private int a = 0;
    Button buttonGoHome;
    Button buttonVote;
    private Survey survey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        wrkDb = WrkDB.getInstance();
        survey = null;
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
                layoutQuestion.setId(question.getPkQuestion());
                final TextView questionTextView = new TextView(VoteActivity.this);
                questionTextView.setText(question.getTitle());
                layoutQuestion.addView(questionTextView);
                //Choix dynamique
                if(question.isMultiple()) {
                    LinearLayout choicesLayout = new LinearLayout(VoteActivity.this);
                    choicesLayout.setOrientation(LinearLayout.VERTICAL);
                    for (Choice choice : question.getChoices()) {
                        LinearLayout choiceLayout = new LinearLayout(VoteActivity.this);
                        TextView answerDynamic = new TextView(VoteActivity.this);
                        answerDynamic.setWidth(48);
                        answerDynamic.setHeight(48);
                        answerDynamic.setEms(10);
                        answerDynamic.setText(choice.getText());
                        answerDynamic.setTextSize(14);
                        choiceLayout.addView(answerDynamic);
                        CheckBox choiceCheckBox = new CheckBox(VoteActivity.this);
                        choiceCheckBox.setId(choice.getPkChoice());
                        choiceLayout.addView(choiceCheckBox);
                        choicesLayout.addView(choiceLayout);
                    }
                    layoutQuestion.addView(choicesLayout);
                } else {
                    MyRadioGroup choicesLayout = new MyRadioGroup(VoteActivity.this);
                    choicesLayout.setOrientation(LinearLayout.VERTICAL);
                    for (Choice choice : question.getChoices()) {
                        LinearLayout choiceLayout = new LinearLayout(VoteActivity.this);
                        TextView answerDynamic = new TextView(VoteActivity.this);
                        answerDynamic.setWidth(48);
                        answerDynamic.setHeight(48);
                        answerDynamic.setEms(10);
                        answerDynamic.setText(choice.getText());
                        answerDynamic.setTextSize(14);
                        choiceLayout.addView(answerDynamic);
                        RadioButton choiceRadioButton = new RadioButton(VoteActivity.this);
                        choiceRadioButton.setId(choice.getPkChoice());
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

    //Charge les boutons de l'ihm vote
    private void initButtonVote() {
        buttonGoHome = findViewById(R.id.goHomeVote);
        buttonGoHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Start NewActivity.class
                Intent myIntent = new Intent(VoteActivity.this, HomeActivity.class);
                startActivity(myIntent);
            }
        });

        buttonVote = findViewById(R.id.vote);
        buttonVote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                ArrayList<Vote> votes = new ArrayList<>();
                ArrayList<Question> questions = survey.getQuestions();
                boolean emptyQuestions = false;
                for (int i = 0; i < questions.size(); i++) {
                    Question q = questions.get(i);
                    String androidId = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    LinearLayout layoutPoll = findViewById(R.id.layoutpoll);
                    LinearLayout layoutQuestion = (LinearLayout) layoutPoll.getChildAt(i);
                    if (q.isMultiple()) {
                        LinearLayout layoutChoices = (LinearLayout) layoutQuestion.getChildAt(1);
                        for (int j = 0; j < layoutQuestion.getChildCount(); j++) {
                            LinearLayout layoutChoice = (LinearLayout) layoutChoices.getChildAt(j);
                            CheckBox checkBoxChoice = (CheckBox) layoutChoice.getChildAt(1);
                            if (checkBoxChoice.isChecked()) {
                                votes.add(new Vote(new Choice(checkBoxChoice.getId(), new Question(layoutQuestion.getId())), androidId));
                            }
                        }
                    } else {
                        MyRadioGroup layoutChoices = (MyRadioGroup) layoutQuestion.getChildAt(1);
                        int checkedRadioButtonId = layoutChoices.getCheckedRadioButtonId();
                        Log.i(LOG_TAG, checkedRadioButtonId + " est choisi");
                        if (checkedRadioButtonId != -1) {
                            votes.add(new Vote(new Choice(checkedRadioButtonId, new Question(layoutQuestion.getId())), androidId));
                        } else {
                            emptyQuestions = true;
                            break;
                        }
                    }
                }
                if (emptyQuestions) {
                    //TODO afficher toast
                } else {
                    Boolean success = false;
                    try {
                        success = new VoteTask().execute(votes).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (success == null) {
                        //TODO afficher toast
                    } else if (success) {
                        // Start AnswerActivity.class
                        Intent myIntent = new Intent(VoteActivity.this, AnswerActivity.class);
                        myIntent.putExtra("pk_survey", survey.getPkSurvey());
                        startActivity(myIntent);
                    } else {
                        Log.w(LOG_TAG, "Les votes n'ont pas pu être insérés !");
                    }
                }
            }
        });
    }
}
