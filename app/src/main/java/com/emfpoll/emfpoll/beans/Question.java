package com.emfpoll.emfpoll.beans;

import java.util.ArrayList;

/**
 * Created by SchnieperN on 29.01.2018.
 */

public class Question {

    private int pkQuestion;
    private ArrayList<Choice> choices;
    private String title;
    private boolean multiple;
    private Survey survey;

    public Question() {}

    public Question(int pkQuestion) {
        this.pkQuestion = pkQuestion;
    }

    public Question(ArrayList<Choice> choices, String title, boolean multiple, Survey survey) {
        this.choices = choices;
        this.title = title;
        this.multiple = multiple;
        this.survey = survey;
    }

    public Question(int pkQuestion, ArrayList<Choice> choices, String title, boolean multiple, Survey survey) {
        this.pkQuestion = pkQuestion;
        this.choices = choices;
        this.title = title;
        this.multiple = multiple;
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

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    @Override
    public String toString() {
        return title;
    }

}
