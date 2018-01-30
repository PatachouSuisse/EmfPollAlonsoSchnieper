package com.emfpoll.emfpoll.beans;

/**
 * Created by SchnieperN on 29.01.2018.
 */

public class Vote {

    private Choice choice;
    private String visitorid;

    public Vote() {}

    public Vote(Choice choice, String visitorid) {
        this.choice = choice;
        this.visitorid = visitorid;
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public String getVisitorid() {
        return visitorid;
    }

    public void setVisitorid(String visitorid) {
        this.visitorid = visitorid;
    }

}
