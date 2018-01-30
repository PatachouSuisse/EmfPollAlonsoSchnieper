package com.emfpoll.emfpoll.beans;

import java.util.ArrayList;

/**
 * Created by SchnieperN on 29.01.2018.
 */

public class Choice {

    private int pkChoice;
    private String text;
    private Question question;
    private ArrayList<Vote> votes;

    public Choice() {}

    public Choice(int pkChoice) {
        this.pkChoice = pkChoice;
    }

    public Choice(String text, Question question, ArrayList<Vote> votes) {
        this.text = text;
        this.question = question;
        this.votes = votes;
    }

    public Choice(int pkChoice, String text, Question question, ArrayList<Vote> votes) {
        this.pkChoice = pkChoice;
        this.text = text;
        this.question = question;
        this.votes = votes;
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public ArrayList<Vote> getVotes() {
        return votes;
    }

    public void setVotes(ArrayList<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return text;
    }

}
