package com.emfpoll.emfpoll.beans;

import java.util.ArrayList;

/**
 * Created by SchnieperN on 29.01.2018.
 */

public class Question {

    private int pkQuestion;
    private ArrayList<Choice> choices;
    private String title;
    private Survey survey;

    public Question() {}

    public Question(int pkQuestion) {
        this.pkQuestion = pkQuestion;
    }

    public Question(int pkQuestion, ArrayList<Choice> choices, String title, Survey survey) {
        this.pkQuestion = pkQuestion;
        this.choices = choices;
        this.title = title;
        this.survey = survey;
    }

    public int getPkQuestion() {
        return pkQuestion;
    }

    public void setPkQuestion(int pkQuestion) {
        this.pkQuestion = pkQuestion;
    }

    public ArrayList<Choice> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<Choice> choices) {
        this.choices = choices;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

}
