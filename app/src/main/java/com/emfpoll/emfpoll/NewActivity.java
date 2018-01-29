package com.emfpoll.emfpoll;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


public class NewActivity extends AppCompatActivity {
    private int a = 0;
    private static final String LOG_TAG =
            HomeActivity.class.getSimpleName();
    Button buttonGoHome;
    Button buttonCreatePoll;

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
        buttonGoHome = findViewById(R.id.createPoll);
        buttonGoHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Start NewActivity.class
                Intent myIntent = new Intent(NewActivity.this,
                        HomeActivity.class);
                startActivity(myIntent);
            }
        });

        buttonCreatePoll = findViewById(R.id.goHomeNew);
        buttonCreatePoll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Start NewActivity.class
                Intent myIntent = new Intent(NewActivity.this,
                        HomeActivity.class);
                startActivity(myIntent);
            }
        });
    }

    /**
     * Cette méthode permet de créer 2 champs vide pour les réponses.
     */
    private void initGeneratorOfField() {
        for (int i = 0; i < 2; i++) {
            addAnswerField();
        }
    }

    /**
     * Cette méthode permet de créer les champs réponse de façon dynamique
     */
    private void addAnswerField() {
        final LinearLayout linearParent = (LinearLayout) findViewById(R.id.layoutnew);

        //création du edit
        final EditText answerDynamic = new EditText(NewActivity.this);
        answerDynamic.setId((int) a);
        answerDynamic.setWidth(48);
        answerDynamic.setHeight(48);
        answerDynamic.setEms(10);
        answerDynamic.setHint("Réponse " + a);
        answerDynamic.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        answerDynamic.setTextSize(14);
        a++;
        linearParent.addView(answerDynamic);
        answerDynamic.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                if (count == 0)
                    addAnswerField();
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
        Log.d(LOG_TAG, "============================ NewPoll addAnswerField with id : " + answerDynamic.getId());
    }
}

