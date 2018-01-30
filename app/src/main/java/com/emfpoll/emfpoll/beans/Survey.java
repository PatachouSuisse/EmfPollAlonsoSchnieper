package com.emfpoll.emfpoll.beans;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by SchnieperN on 29.01.2018.
 */

public class Survey {

    private int pkSurvey;
    private ArrayList<Question> questions;
    private String name;
    private Date start;
    private Date end;
    private String creatorid;

    public Survey() {}

    public Survey(int pkSurvey) {
        this.pkSurvey = pkSurvey;
    }

    public Survey(int pkSurvey, ArrayList<Question> questions, String name, Date start, Date end, String creatorid) {
        this.pkSurvey = pkSurvey;
        this.questions = questions;
        this.name = name;
        this.start = start;
        this.end = end;
        this.creatorid = creatorid;
    }

    public int getPkSurvey() {
        return pkSurvey;
    }

    public void setPkSurvey(int pkSurvey) {
        this.pkSurvey = pkSurvey;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(String creatorid) {
        this.creatorid = creatorid;
    }

}
