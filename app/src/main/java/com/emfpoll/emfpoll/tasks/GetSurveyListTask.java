package com.emfpoll.emfpoll.tasks;

import android.os.AsyncTask;

import com.emfpoll.emfpoll.beans.Survey;
import com.emfpoll.emfpoll.wrk.WrkDB;

import java.util.ArrayList;

/**
 * Created by SchnieperN on 30.01.2018.
 */

public class GetSurveyListTask extends AsyncTask<String, Void, ArrayList<Survey>> {

    @Override
    protected ArrayList<Survey> doInBackground(String... strings) {
        return WrkDB.getInstance().getSurveyList(strings[0]);
    }
}
