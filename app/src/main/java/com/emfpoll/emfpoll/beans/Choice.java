package com.emfpoll.emfpoll.beans;

/**
 * Created by SchnieperN on 29.01.2018.
 */

public class Choice {

    private int pkChoice;
    private String text;
    private boolean multiple;
    private Question question;

    public Choice() {}

    public Choice(int pkChoice) {
        this.pkChoice = pkChoice;
    }

    public Choice(int pkChoiceString, String text, boolean multiple, Question question) {
        this.pkChoice = pkChoice;
        this.text = text;
        this.multiple = multiple;
        this.question = question;
    }

    public int getPkChoice() {
        return pkChoice;
    }

    public void setPkChoice(int pkChoice) {
        this.pkChoice = pkChoice;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}
