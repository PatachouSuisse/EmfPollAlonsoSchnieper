package com.emfpoll.emfpoll;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.emfpoll.emfpoll.beans.Choice;
import com.emfpoll.emfpoll.beans.Question;
import com.emfpoll.emfpoll.beans.Survey;
import com.emfpoll.emfpoll.tasks.CreateSurveyTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class NewActivity extends AppCompatActivity {

    private static final String LOG_TAG =
            HomeActivity.class.getSimpleName();
    private Button buttonGoHome;
    private Button buttonCreatePoll;
    private LinearLayout layoutPoll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        initButtonNew();
        initGeneratorOfField();
        Log.d(LOG_TAG, "============================ NewPoll loaded");
    }


    //Charge les boutons de l'ihm de création de sondage
    private void initButtonNew() {
        buttonGoHome = findViewById(R.id.goHomeNew);
        buttonGoHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Start NewActivity.class
                Intent myIntent = new Intent(NewActivity.this,
                        HomeActivity.class);
                startActivity(myIntent);
            }
        });

        buttonCreatePoll = findViewById(R.id.createPoll);
        buttonCreatePoll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                boolean canInsertQuestions = true;
                ArrayList<Question> questions = new ArrayList<>();
                QuestionLoop: for(int i = 0; i < layoutPoll.getChildCount() - 1; i++) {
                    LinearLayout layoutQuestion = (LinearLayout) layoutPoll.getChildAt(i);
                    LinearLayout layoutQuestionName = (LinearLayout) layoutQuestion.getChildAt(0);
                    String title = ((EditText) layoutQuestionName.getChildAt(2)).getText().toString();
                    if(title.equals("")) {
                        canInsertQuestions = false;
                        break QuestionLoop;
                    }
                    LinearLayout layoutAnswers = (LinearLayout) layoutQuestion.getChildAt(1);
                    ArrayList<Choice> choices = new ArrayList<>();
                    for(int j = 0; j < layoutAnswers.getChildCount(); j++) {
                        String text = ((EditText) layoutAnswers.getChildAt(j)).getText().toString();
                        if(text.equals("")) {
                            if(j != layoutAnswers.getChildCount() - 1) {
                                canInsertQuestions = false;
                                break QuestionLoop;
                            }
                        } else {
                            choices.add(new Choice(text, null, null));
                        }
                    }
                    if(choices.size() < 2) {
                        canInsertQuestions = false;
                        break QuestionLoop;
                    }
                    LinearLayout multipleLayout = (LinearLayout) layoutQuestion.getChildAt(2);
                    boolean multiple = ((CheckBox) multipleLayout.getChildAt(1)).isChecked();
                    questions.add(new Question(choices, title, multiple, null));
                }
                EditText pollname = findViewById(R.id.pollname);
                String name = pollname.getText().toString();
                if(canInsertQuestions && !name.equals("")) {
                    String androidId = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    Survey survey = new Survey(questions, name, new Date(), null, androidId);
                    Integer fkSurvey = null;
                    try {
                        fkSurvey = new CreateSurveyTask().execute(survey).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if(fkSurvey != null) {
                        // Start NewActivity.class
                        Intent myIntent = new Intent(NewActivity.this, AnswerActivity.class);
                        myIntent.putExtra("pk_survey", fkSurvey);
                        startActivity(myIntent);
                    } else {
                        //TODO toast fail
                        Log.e(LOG_TAG, "Le poll n'a pas pu être inséré");
                    }
                } else {
                    //TODO toast pas complet
                    Log.e(LOG_TAG, "Il y a des données manquantes");
                }
            }
        });
    }

    /**
     * Cette méthode permet de créer 2 champs vide pour les réponses.
     */
    private void initGeneratorOfField() {
        layoutPoll = findViewById(R.id.layoutpoll);
        addQuestionField();
        addAddQuestionButton();
    }

    private void addQuestionField() {
        LinearLayout questionLayout = new LinearLayout(NewActivity.this);
        questionLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout questionNameLayout = new LinearLayout(NewActivity.this);
        Space spaceBefore = new Space(NewActivity.this);
        TextView questionTextView = new TextView(NewActivity.this);
        questionTextView.setText("Question :");
        EditText questionName = new EditText(NewActivity.this);
        questionName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        Space spaceAfter = new Space(NewActivity.this);
        questionNameLayout.addView(spaceBefore);
        questionNameLayout.addView(questionTextView);
        questionNameLayout.addView(questionName);
        questionNameLayout.addView(spaceAfter);
        LinearLayout answerLayout = new LinearLayout(NewActivity.this);
        answerLayout.setOrientation(LinearLayout.VERTICAL);
        addAnswerField(answerLayout);
        LinearLayout multipleLayout = new LinearLayout(NewActivity.this);
        TextView multipleTextView = new TextView(NewActivity.this);
        multipleTextView.setText("Choix multiple");
        multipleLayout.addView(multipleTextView);
        CheckBox multipleCheckBox = new CheckBox(NewActivity.this);
        multipleLayout.addView(multipleCheckBox);
        questionLayout.addView(questionNameLayout);
        questionLayout.addView(answerLayout);
        questionLayout.addView(multipleLayout);
        layoutPoll.addView(questionLayout);
    }

    private void addAddQuestionButton() {
        final LinearLayout questionLayout = new LinearLayout(NewActivity.this);
        questionLayout.setOrientation(LinearLayout.VERTICAL);
        Button addQuestionButton = new Button(NewActivity.this);
        addQuestionButton.setText("Ajouter question");
        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestionField();
                //enlever layout avec bouton
                layoutPoll.removeViewAt(layoutPoll.getChildCount() - 2);
                addAddQuestionButton();
            }
        });
        questionLayout.addView(addQuestionButton);
        layoutPoll.addView(questionLayout);
    }

    /**
     * Cette méthode permet de créer les champs réponse de façon dynamique
     */
    private EditText addAnswerField(final LinearLayout ll) {
        //création du edit
        final EditText answerDynamic = new EditText(NewActivity.this);
        answerDynamic.setWidth(48);
        answerDynamic.setHeight(48);
        answerDynamic.setEms(10);
        answerDynamic.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        answerDynamic.setTextSize(14);
        answerDynamic.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //teste si enter a été pressé et si le choix n'est pas vide
                //TODO fixer si dernier de la question et il existe plusieurs questions
                if ((event == null || actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)
                        && !v.getText().toString().equals("")) {
                    answerDynamic.setOnEditorActionListener(null);
                    if (ll.getChildAt(ll.getChildCount() - 1).equals(answerDynamic)) {
                        addAnswerField(ll).requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });
        ll.addView(answerDynamic);
        Log.d(LOG_TAG, "============================ NewPoll addAnswerField");
        return answerDynamic;
    }
}

