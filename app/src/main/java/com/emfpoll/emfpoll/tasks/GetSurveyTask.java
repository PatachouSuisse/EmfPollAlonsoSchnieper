package com.emfpoll.emfpoll.tasks;

import android.os.AsyncTask;

import com.emfpoll.emfpoll.beans.Survey;
import com.emfpoll.emfpoll.wrk.WrkDB;

/**
 * Created by SchnieperN on 30.01.2018.
 */

public class GetSurveyTask extends AsyncTask<Integer, Void, Survey> {
    @Override
    protected Survey doInBackground(Integer... integers) {
        return WrkDB.getInstance().getSurvey(integers[0]);
    }
}
